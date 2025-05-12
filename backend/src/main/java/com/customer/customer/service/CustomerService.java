package com.customer.customer.service;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.customer.customer.dto.BulkUploadResponse;
import com.customer.customer.dto.CustomerDto;
import com.customer.customer.entity.Customer;


public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customerDto);
    CustomerDto updateCustomer(Long id, CustomerDto customerDto);
    CustomerDto getCustomerById(Long id);
    void deleteCustomer(Long id);
    List<CustomerDto> getAllCustomers();
    BulkUploadResponse bulkUpload(MultipartFile file);
    Customer saveCustomer(Customer customer);

}
