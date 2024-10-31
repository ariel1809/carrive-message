package com.example.carrivemessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.manage.carriveutility.repository"})
@EntityScan("com.manage.carrive")
public class CarriveMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarriveMessageApplication.class, args);
    }

}
