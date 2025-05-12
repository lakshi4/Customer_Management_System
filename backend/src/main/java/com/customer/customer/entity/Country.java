package com.customer.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;   
import lombok.Data;
import org.springframework.data.annotation.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "countries")

public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @OneToMany(mappedBy = "country" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<City> cities = new ArrayList<>();

    public void addCity(City city) {
        cities.add(city);
        city.setCountry(this);
    }

}
