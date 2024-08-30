package com.sitix.model.service.Impl;

import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.TransactionDetailRequest;
import com.sitix.model.dto.request.TransactionRequest;
import com.sitix.model.dto.response.TicketResponse;
import com.sitix.model.dto.response.TransactionDetailResponse;
import com.sitix.model.dto.response.TransactionResponse;
import com.sitix.model.entity.*;
import com.sitix.model.service.TransactionService;
import com.sitix.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final TicketRepository ticketRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private TransactionResponse generateTransactionResponse(Transaction transaction) {
        List<TransactionDetailResponse> transactionDetailResponseList = new ArrayList<>();
        List<TransactionDetail> transactionDetailList = transaction.getTransactionDetails();
        for (int i = 0; i < transactionDetailList.size(); i++) {
            TransactionDetailResponse transactionDetailResponse = TransactionDetailResponse.builder()
                    .id(transactionDetailList.get(i).getId())
                    .quantity(transactionDetailList.get(i).getQuantity())
                    .transactionId(transactionDetailList.get(i).getTransaction().getId())
                    .ticketCategoryName(transactionDetailList.get(i).getTicketCategory().getName())
                    .eventName(transactionDetailList.get(i).getTicketCategory().getEvent().getName())
                    .build();
            transactionDetailResponseList.add(transactionDetailResponse);
        }
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .customerId(transaction.getCustomer().getId())
                .status(transaction.getStatus())
                .transactionDetails(transactionDetailResponseList)
                .paidAt(transaction.getPaidAt())
                .build();
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedIn.getId());
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .build();

        transactionRepository.save(transaction);

        List<TransactionDetailRequest> transactionDetailRequests = transactionRequest.getTransactionDetailRequests();
        List<TransactionDetail> transactionDetailList = new ArrayList<>();

        double totalAmount = 0;

        for (TransactionDetailRequest transactionDetailRequest : transactionDetailRequests) {
            TicketCategory ticketCategory = ticketCategoryRepository.findById(transactionDetailRequest.getTicketCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket Category Not Found"));

            if (!ticketCategory.hasAvailableTickets(transactionDetailRequest.getQuantity())) {
                throw new ResourceNotFoundException("No available tickets for the selected category");
            }

            TransactionDetail transactionDetail = TransactionDetail.builder()
                    .transaction(transaction)
                    .quantity(transactionDetailRequest.getQuantity())
                    .ticketCategory(ticketCategory)
                    .build();
            transactionDetailList.add(transactionDetail);

            transactionDetailRepository.save(transactionDetail);
            ticketCategory.reduceQuota(transactionDetail.getQuantity());
            ticketCategoryRepository.save(ticketCategory);

            totalAmount += transactionDetailRequest.getQuantity() * ticketCategory.getPrice();
        }

        transaction.setTransactionDetails(transactionDetailList);
        transactionRepository.save(transaction);

        String paymentUrl = payTransaction(transaction.getId(), totalAmount, transactionDetailList);
        TransactionResponse transactionResponse = generateTransactionResponse(transaction);
        transactionResponse.setPaymentUrl(paymentUrl);

        return transactionResponse;
    }

    public String payTransaction(String id, Double totalPayment, List<TransactionDetail> details) {
        String uniqueOrderId = details.get(0).getTransaction().getId();
        String url = "https://app.sandbox.midtrans.com/snap/v1/transactions";
        String midtransServerKey = "SB-Mid-server-5WH6ehzAiCWvPQV1DYJp59g9";
        Map<String, Object> params = new HashMap<>();

        Map<String, Object> transactionDetails = new HashMap<>();
        params.put("transaction_details", transactionDetails);
        transactionDetails.put("order_id", uniqueOrderId);
        transactionDetails.put("gross_amount", totalPayment);

        List<Map<String, Object>> itemDetails = new ArrayList<>();
        for (TransactionDetail detail : details) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", detail.getId());
            item.put("price", detail.getTicketCategory().getPrice());
            item.put("quantity", detail.getQuantity());
            item.put("name", detail.getTicketCategory().getEvent().getName() +" " + detail.getTicketCategory().getName() + " Ticket");
            itemDetails.add(item);
        }
        params.put("item_details", itemDetails);

        Map<String, Object> creditCard = new HashMap<>();
        params.put("credit_card", creditCard);
        creditCard.put("secure", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((midtransServerKey + ":").getBytes()));

        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST,
                new HttpEntity<>(params, headers), Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("redirect_url")) {
            return (String) responseBody.get("redirect_url");
        } else {
            throw new RuntimeException("Failed to create transaction: " + responseBody);
        }
    }


    public void setTransactionStatus(Map<String, Object> notification){
        String orderId = (String) notification.get("order_id");
        String transactionStatus = (String) notification.get("transaction_status");

        Transaction transaction = transactionRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + orderId));

        if ("capture".equals(transactionStatus) || "settlement".equals(transactionStatus)) {
            transaction.setStatus(Transaction.Status.PAID);

            List<TransactionDetail> transactionDetails = transactionDetailRepository.findByTransactionId(transaction.getId());

            for(TransactionDetail transactionDetail : transactionDetails){
                Ticket ticket = Ticket.builder()
                        .transaction(transaction)
                        .event(transactionDetail.getTicketCategory().getEvent())
                        .ticketCategory(transactionDetail.getTicketCategory())
                        .build();

                ticketRepository.saveAndFlush(ticket);
            }


        } else if ("deny".equals(transactionStatus) || "cancel".equals(transactionStatus) || "expire".equals(transactionStatus)) {
            transaction.setStatus(Transaction.Status.CANCELLED);

            for (TransactionDetail detail : transaction.getTransactionDetails()) {
                TicketCategory ticketCategory = detail.getTicketCategory();
                ticketCategory.increaseQuota(detail.getQuantity());
                ticketCategoryRepository.saveAndFlush(ticketCategory);
            }
        }

        transactionRepository.saveAndFlush(transaction);
    }

    public List<TransactionResponse> viewMyTransaction (){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        return transactionRepository.findTransactionByCustomerId(customer.getId()).stream().map(this::generateTransactionResponse).toList();

    }

    public List<TicketResponse> viewTicket (){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found for user id: " + loggedInUser.getId());
        }

        List<Transaction> transactions = transactionRepository.findTransactionByCustomerId(customer.getId());

        List<Ticket> listTicket = new ArrayList<>();
        for (Transaction transaction : transactions) {
            List<Ticket> ticketList = ticketRepository.findByTransactionId(transaction.getId());
            listTicket.addAll(ticketList);
        }
        return listTicket.stream().map(this::generateTicketResponse).toList();

    }

    private TicketResponse generateTicketResponse(Ticket ticket){
        return TicketResponse.builder()
                .id(ticket.getId())
                .transactionId(ticket.getTransaction().getId())
                .eventName(ticket.getEvent().getName())
                .ticketCategory(ticket.getTicketCategory().getName())
                .build();
    }

}
