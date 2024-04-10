package com.taxinow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class TaxiNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiNowApplication.class, args);
    }

}
