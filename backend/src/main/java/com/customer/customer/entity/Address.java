package com.customer.customer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String addressLine1;
    private String addressLine2;

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")  // Join column to associate address with customer
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    // public void setCustomer(Customer customer) {
    
    //     throw new UnsupportedOperationException("Unimplemented method 'setCustomer'");
    // }
    // public Object getCity() {
       
    //     throw new UnsupportedOperationException("Unimplemented method 'getCity'");
    // }
    // public Object getCountry() {
      
    //     throw new UnsupportedOperationException("Unimplemented method 'getCountry'");
    // }
    // public void setCountry(Country country) {
  
    //     throw new UnsupportedOperationException("Unimplemented method 'setCountry'");
    // }
    // public void setCity(City city) {
     
    //     throw new UnsupportedOperationException("Unimplemented method 'setCity'");
    // }

    
}
