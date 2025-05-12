package com.customer.customer.dto;

import lombok.Data;

@Data

public class AddressDto {

    private String addressLine1;
    private String addressLine2;
    private Long cityId;
    private Long countryId;

}
