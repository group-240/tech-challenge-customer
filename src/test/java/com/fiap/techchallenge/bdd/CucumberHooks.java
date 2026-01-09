package com.fiap.techchallenge.bdd;

import com.fiap.techchallenge.external.datasource.repositories.CustomerJpaRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CucumberHooks {

    @Autowired(required = false)
    private CustomerJpaRepository customerJpaRepository;

    @Before
    public void setUp() {
        // Limpa o banco de dados antes de cada cenário para garantir isolamento
        if (customerJpaRepository != null) {
            customerJpaRepository.deleteAll();
        }
    }
}

