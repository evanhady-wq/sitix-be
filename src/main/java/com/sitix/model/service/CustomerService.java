package com.sitix.model.service;

import com.sitix.model.dto.request.CustomerRequest;
import com.sitix.model.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    void createCustomer(CustomerRequest customerRequest);
    CustomerResponse viewCustomerProfile();
    CustomerResponse EditCustomerProfile(CustomerRequest customerRequest);
    void deleteAccount();
    CustomerResponse getById(String id);
    List<CustomerResponse> viewAllCustomer();
    void deleteCustomer(String id);

}
