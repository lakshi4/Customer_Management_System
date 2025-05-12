package com.customer.customer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customer.customer.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {
    
    Optional<City> findByNameAndCountryId(String name, Long countryId);

    

}
