package com.customer.customer.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data

public class CustomerDto {

    private Long id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateOfBirth;
    private String nic;
    private List<String> mobileNumbers = new ArrayList<>();
    private List<AddressDto> address = new ArrayList<>(); 
    private List<Long> familyMemberIds = new ArrayList<>();


    public Object getAddresses() {
        
        throw new UnsupportedOperationException("Unimplemented method 'getAddresses'");
    }
    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        
   this.dateOfBirth = dateOfBirth;
    }
    public void setAddresses(List<AddressDto> collect) {

        throw new UnsupportedOperationException("Unimplemented method 'setAddresses'");
    }  
    
    
}
