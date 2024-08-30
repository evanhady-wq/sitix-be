package com.sitix.controller;

import com.sitix.constant.APIUrl;
import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.CommonResponse;
import com.sitix.model.dto.response.CustomerResponse;
import com.sitix.model.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIUrl.CUSTOMER_API)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    //CUSTOMER AUTHORITY
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<CustomerResponse>> viewMyProfile() {
        CustomerResponse customerResponse = customerService.viewCustomerProfile();
        CommonResponse<CustomerResponse> response = generateCustomerResponse(HttpStatus.OK.value(),"Profile", Optional.of(customerResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/profile")
    public ResponseEntity<CommonResponse<CustomerResponse>> editMyProfile(@Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse update = customerService.
                EditCustomerProfile(customerRequest);
        CommonResponse<CustomerResponse> response = generateCustomerResponse(HttpStatus.OK.value(),"Update Success", Optional.of(update));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/profile")
    public ResponseEntity<CommonResponse<CustomerResponse>> deleteAccount() {
        customerService.deleteAccount();
        CommonResponse<CustomerResponse> response = generateCustomerResponse(HttpStatus.OK.value(),"Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }

    //ADMIN AUTHORITY
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer() {
        List<CustomerResponse> customerResponsesList = customerService.viewAllCustomer();
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>> builder()
                .message("Success Load All Customer")
                .data(Optional.of(customerResponsesList))
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getById(id);
        CommonResponse<CustomerResponse> response = generateCustomerResponse(HttpStatus.OK.value(),"Search Result By ID", Optional.of(customerResponse));
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> deleteAccountCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        CommonResponse<CustomerResponse> response = generateCustomerResponse(HttpStatus.OK.value(),"Delete Success", Optional.empty());
        return ResponseEntity.ok(response);
    }

    private CommonResponse<CustomerResponse> generateCustomerResponse(Integer code, String message, Optional<CustomerResponse> customer) {
        return CommonResponse.<CustomerResponse>builder()
                .statusCode(code)
                .message(message)
                .data(customer)
                .build();
    }
}
