package com.customer.customer.entity;

//import org.springframework.data.annotation.Id;
import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.Table;


@Entity
@Data
@Table(name = "mobile_numbers")

public class MobileNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="customer_id")
    private Customer customer;

   

     // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return number;
    }

    public void setMobileNumber(String mobileNumber) {
        this.number = mobileNumber;
    }


    // public void setCustomer(Customer customer) {
       
    //     throw new UnsupportedOperationException("Unimplemented method 'setCustomer'");
    // }

}
