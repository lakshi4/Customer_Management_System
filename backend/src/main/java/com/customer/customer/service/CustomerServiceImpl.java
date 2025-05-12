package com.customer.customer.service;

import com.customer.customer.dto.*;
import com.customer.customer.dto.BulkUploadResponse;
import com.customer.customer.dto.CustomerDto;
import com.customer.customer.entity.*;
//import com.customer.customer.exception.ResourceNotFoundException;
import com.customer.customer.repository.*;
import com.customer.customer.util.ExcelHelper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Helper;

import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl <excelHelper> implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final ExcelHelper excelHelper;

    private static final int BATCH_SIZE = 1000;
    // create a new customer
    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        validateNIC(customerDto.getNic());
        
        Customer customer = new Customer();
        mapCustomerDTOToEntity(customerDto, customer);
        
        Customer savedCustomer = customerRepository.save(customer);
        return mapCustomerToDTO(savedCustomer);
    }

   
    // Update customer
    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow();
        
        if (!customer.getNic().equals(customerDto.getNic())) {
            validateNIC(customerDto.getNic());
        }
        
        // Clear existing associations
        customer.getMobileNumbers().clear();
        customer.getAddress().clear();
        customer.getFamilyMembers().clear();
        
        mapCustomerDTOToEntity(customerDto, customer);
        
        Customer updatedCustomer = customerRepository.save(customer);
        return mapCustomerToDTO(updatedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdWithDetails(id)
                .orElseThrow();
        return mapCustomerToDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapCustomerToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow();
        customerRepository.delete(customer);
    }

    @Override
    @Transactional
    public BulkUploadResponse bulkUpload(MultipartFile file) {
        try {
            if (!ExcelHelper.hasExcelFormat(file)) {
                throw new IllegalArgumentException("Please upload an excel file");
            }

            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int totalRecords = 0;
        
            int successCount = 0;
         
            int failureCount = 0;
            List<Customer> batchCustomers = new ArrayList<>(BATCH_SIZE);

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                totalRecords++;

                try {
                    Customer customer = mapRowToCustomer(currentRow);
                    batchCustomers.add(customer);

                    if (batchCustomers.size() >= BATCH_SIZE) {
                        customerRepository.saveAll(batchCustomers);
                        successCount += batchCustomers.size();
                        batchCustomers.clear();
                    }
                } catch (Exception e) {
                    failureCount++;
                    // Log error or collect error details
                }
            }

            // Save remaining records in the batch
            if (!batchCustomers.isEmpty()) {
                customerRepository.saveAll(batchCustomers);
                successCount += batchCustomers.size();
            }

            return new BulkUploadResponse();

        } catch (IOException e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage());
        }
    }

    private void validateNIC(String nic) {
        if (customerRepository.findByNic(nic).isPresent()) {
            throw new IllegalArgumentException("Customer with NIC " + nic + " already exists");
        }
    }

    private Customer mapRowToCustomer(Row row) {
        Customer customer = new Customer();
        customer.setName(row.getCell(0).getStringCellValue());
        customer.setDateOfBirth(LocalDate.parse(row.getCell(1).getStringCellValue()));
        customer.setNic(row.getCell(2).getStringCellValue());

        // Process mobile numbers if present
        Cell mobileCell = row.getCell(3);
        if (mobileCell != null) {
            String[] mobiles = mobileCell.getStringCellValue().split(",");
            for (String mobile : mobiles) {
                MobileNumber mobileNumber = new MobileNumber();
                mobileNumber.setNumber(mobile.trim());
                customer.addMobileNumber(mobileNumber);
            }
        }

        return customer;
    }

    private void mapCustomerDTOToEntity(CustomerDto dto, Customer entity) {
        entity.setName(dto.getName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setNic(dto.getNic());

        // Map mobile numbers
        if (dto.getMobileNumbers() != null) {
            for (String number : dto.getMobileNumbers()) {
                MobileNumber mobileNumber = new MobileNumber();
                mobileNumber.setNumber(number);
                entity.addMobileNumber(mobileNumber);
            }
        }

        // Map addresses
        if (dto.getAddresses() != null) {
            for (AddressDto addressDto : dto.getAddress()) {
                Address address = new Address();
                address.setAddressLine1(addressDto.getAddressLine1());
                address.setAddressLine2(addressDto.getAddressLine2());

                if (addressDto.getCountryId() != null) {
                    Country country = countryRepository.findById(addressDto.getCountryId())
                            .orElseThrow();
                    address.setCountry(country);
                }

                if (addressDto.getCityId() != null) {
                    City city = cityRepository.findById(addressDto.getCityId())
                            .orElseThrow();
                    address.setCity(city);
                }

                entity.addAddress(address);
            }
        }

        // Map family members
        if (dto.getFamilyMemberIds() != null) {
            for (Long familyMemberId : dto.getFamilyMemberIds()) {
                Customer familyMember = customerRepository.findById(familyMemberId)
                        .orElseThrow();
                entity.addFamilyMember(familyMember);
            }
        }
    }

    private CustomerDto mapCustomerToDTO(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setNic(customer.getNic());

        // Map mobile numbers
        if (customer.getMobileNumbers() != null) {
            dto.setMobileNumbers(customer.getMobileNumbers().stream()
                    .map(MobileNumber::getNumber)
                    .collect(Collectors.toList()));
        }

        // Map addresses
        if (customer.getAddress() != null) {
            dto.setAddresses(customer.getAddress().stream()
                    .map(address -> {
                        AddressDto addressDto = new AddressDto();
                        addressDto.setAddressLine1(address.getAddressLine1());
                        addressDto.setAddressLine2(address.getAddressLine2());
                        if (address.getCity() != null) {
                            addressDto.setCityId(address.getCity().getId());
                        }
                        if (address.getCountry() != null) {
                            addressDto.setCountryId(address.getCountry().getId());
                        }
                        return addressDto;
                    })
                    .collect(Collectors.toList()));
        }

        // Map family members
        if (customer.getFamilyMembers() != null) {
            dto.setFamilyMemberIds(customer.getFamilyMembers().stream()
                    .map(Customer::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // @Override
    // public CustomerDto createCustomer(CustomerDto customerDto) {
        
    //     throw new UnsupportedOperationException("Unimplemented method 'createCustomer'");
    // }

    // @Override
    // public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
       
    //     throw new UnsupportedOperationException("Unimplemented method 'updateCustomer'");
    // }

    // @Override
    // public CustomerDto getCustomerById(Long id) {
        
    //     throw new UnsupportedOperationException("Unimplemented method 'getCustomerById'");
    // }
}