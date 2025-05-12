package com.customer.customer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = "nic"))

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime dateOfBirth;

    @Column(nullable = false, unique = true)
    private String nic;

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<MobileNumber> mobileNumbers = new ArrayList<>();

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Address> address = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(name = "customer_familyMember",
            joinColumns = @jakarta.persistence.JoinColumn(name = "customer_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "familyMember_id"))
    private List<Customer> familyMembers = new ArrayList<>();

    public void addMobileNumber(MobileNumber mobileNumber) {
        mobileNumbers.add(mobileNumber);
        mobileNumber.setCustomer(this);
    }

    public void addAddress(Address address) {
        this.address.add(address);
        address.setCustomer(this);
    }

    public void addFamilyMember(Customer familyMember) {
        familyMembers.add(familyMember);
     
    }

    public void setDateOfBirth(LocalDate localDate) {
        throw new UnsupportedOperationException("Unimplemented method 'setDateOfBirth'");
    }

    public void setDateOfBirth(String dateOfBirth2) {
        throw new UnsupportedOperationException("Unimplemented method 'setDateOfBirth'");
    }   

}
