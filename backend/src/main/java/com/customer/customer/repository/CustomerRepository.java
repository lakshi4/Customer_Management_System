package com.customer.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import com.customer.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNic(String nic);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.mobileNumbers LEFT JOIN FETCH c.addresses WHERE c.id = :id")

    Optional<Customer> findByIdWithDetails(@Param("id") Long id);

   
    
}
