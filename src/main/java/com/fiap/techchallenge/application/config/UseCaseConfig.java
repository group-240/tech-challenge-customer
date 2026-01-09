package com.fiap.techchallenge.application.config;

import com.fiap.techchallenge.adapters.controllers.*;
import com.fiap.techchallenge.adapters.gateway.*;
import com.fiap.techchallenge.application.usecases.*;
import com.fiap.techchallenge.domain.repositories.*;
import com.fiap.techchallenge.external.datasource.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    // Repository Gateways (implementam as interfaces do domínio)
    @Bean
    public CustomerRepository customerRepository(CustomerJpaRepository customerJpaRepository) {
        return new CustomerRepositoryGateway(customerJpaRepository);
    }

    // Use Cases (aplicação core)
    @Bean
    public CustomerUseCase customerUseCase(CustomerRepository customerRepository) {
        return new CustomerUseCaseImpl(customerRepository);
    }

    // Controllers de orquestração (adapters)
    @Bean
    public CustomerController customerController(CustomerUseCase customerUseCase) {
        return new CustomerController(customerUseCase);
    }

}
