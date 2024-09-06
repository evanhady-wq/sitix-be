package com.sitix.model.service.Impl;

import com.sitix.exceptions.ResourceNotFoundException;
import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.CustomerResponse;
import com.sitix.model.entity.Customer;
import com.sitix.model.entity.User;
import com.sitix.model.service.CustomerService;
import com.sitix.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    //CREATOR AUTHORITY

    public void createCustomer(CustomerRequest customerRequest) {
       Customer customer = Customer.builder()
               .name(customerRequest.getName())
               .phone(customerRequest.getPhone())
               .user(customerRequest.getUser())
               .build();

        customerRepository.saveAndFlush(customer);
    }

    public CustomerResponse EditCustomerProfile(CustomerRequest customerRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());

        customer.setName(customerRequest.getName());
        customer.setPhone(customerRequest.getPhone());

        customerRepository.saveAndFlush(customer);
        return convertToResponse(customer);
    }

    //SoftDeleted
    public void deleteAccount() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        customer.setIsDeleted(true);

        customerRepository.saveAndFlush(customer);
    }

    public CustomerResponse viewCustomerProfile() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerRepository.findByUserId(loggedInUser.getId());
        return convertToResponse(customer) ;
    }

    //ADMIN AUTHORITY
    public CustomerResponse getById(String id) {
        return convertToResponse(findCustomerById(id));
    }

    public List<CustomerResponse> viewAllCustomer() {
        return customerRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public void deleteCustomer(String id) {
        Customer customer = findCustomerById(id);
        if(customer.getIsDeleted().equals(true)){
            customerRepository.delete(findCustomerById(id));
        } else throw new ResourceNotFoundException("Customer still Active");
    }

    public Customer findCustomerById(String id){
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer Not Found"));
    }

    private CustomerResponse convertToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .isDeleted(customer.getIsDeleted())
                .build();
    }
}