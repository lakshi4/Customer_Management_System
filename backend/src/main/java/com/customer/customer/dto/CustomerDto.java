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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateOfBirth;
    private String nic;
    private List<String> mobileNumbers = new ArrayList<>();
    private List<AddressDto> address = new ArrayList<>(); 
    private List<Long> familyMemberIds = new ArrayList<>();



    
    
}
