package com.customer.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.customer.customer.BackendApplication;

@SpringBootTest
@ContextConfiguration(classes = BackendApplication.class)
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
