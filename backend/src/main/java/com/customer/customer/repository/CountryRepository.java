package com.customer.customer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customer.customer.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
    
     Optional<Country> findByName(String name);

}
