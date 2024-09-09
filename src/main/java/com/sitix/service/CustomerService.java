package com.sitix.service;

import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    void createCustomer(CustomerRequest customerRequest);
    CustomerResponse viewCustomerProfile();
    CustomerResponse EditCustomerProfile(CustomerRequest customerRequest);
    void deleteAccount(String id);
    CustomerResponse getById(String id);
    List<CustomerResponse> viewAllCustomer();
    void deleteCustomer(String id);

}
