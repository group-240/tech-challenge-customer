package com.fiap.techchallenge.adapters.controllers;

import com.fiap.techchallenge.application.usecases.CustomerUseCase;
import com.fiap.techchallenge.domain.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Controller Tests")
class CustomerControllerTest {

    @Mock
    private CustomerUseCase customerUseCase;

    @InjectMocks
    private CustomerController customerController;

    private static final String VALID_CPF = "11144477735";
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_EMAIL = "joao.silva@example.com";
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        mockCustomer = Customer.builder()
                .id(CUSTOMER_ID)
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .cpf(VALID_CPF)
                .build();
    }

    @Nested
    @DisplayName("Register Customer Tests")
    class RegisterCustomerTests {

        @Test
        @DisplayName("Should register customer successfully")
        void shouldRegisterCustomerSuccessfully() {
            // Arrange
            when(customerUseCase.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF))
                    .thenReturn(mockCustomer);

            // Act
            Customer result = customerController.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF);

            // Assert
            assertNotNull(result);
            assertEquals(CUSTOMER_ID, result.getId());
            assertEquals(VALID_NAME, result.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.getEmail());
            assertEquals(VALID_CPF, result.getCpf());

            verify(customerUseCase).registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF);
        }

        @Test
        @DisplayName("Should delegate to use case when registering customer")
        void shouldDelegateToUseCaseWhenRegisteringCustomer() {
            // Arrange
            when(customerUseCase.registerCustomer(anyString(), anyString(), anyString()))
                    .thenReturn(mockCustomer);

            // Act
            customerController.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF);

            // Assert
            verify(customerUseCase, times(1)).registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF);
        }
    }

    @Nested
    @DisplayName("Find Customer By CPF Tests")
    class FindCustomerByCpfTests {

        @Test
        @DisplayName("Should find customer by CPF successfully")
        void shouldFindCustomerByCpfSuccessfully() {
            // Arrange
            when(customerUseCase.findCustomerByCpf(VALID_CPF))
                    .thenReturn(Optional.of(mockCustomer));

            // Act
            Optional<Customer> result = customerController.findCustomerByCpf(VALID_CPF);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(CUSTOMER_ID, result.get().getId());
            assertEquals(VALID_NAME, result.get().getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.get().getEmail());
            assertEquals(VALID_CPF, result.get().getCpf());

            verify(customerUseCase).findCustomerByCpf(VALID_CPF);
        }

        @Test
        @DisplayName("Should return empty when customer not found by CPF")
        void shouldReturnEmptyWhenCustomerNotFoundByCpf() {
            // Arrange
            when(customerUseCase.findCustomerByCpf(VALID_CPF))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Customer> result = customerController.findCustomerByCpf(VALID_CPF);

            // Assert
            assertFalse(result.isPresent());
            verify(customerUseCase).findCustomerByCpf(VALID_CPF);
        }

        @Test
        @DisplayName("Should delegate to use case when finding customer by CPF")
        void shouldDelegateToUseCaseWhenFindingCustomerByCpf() {
            // Arrange
            when(customerUseCase.findCustomerByCpf(anyString()))
                    .thenReturn(Optional.of(mockCustomer));

            // Act
            customerController.findCustomerByCpf(VALID_CPF);

            // Assert
            verify(customerUseCase, times(1)).findCustomerByCpf(VALID_CPF);
        }
    }

    @Nested
    @DisplayName("Find Customer By ID Tests")
    class FindCustomerByIdTests {

        @Test
        @DisplayName("Should find customer by ID successfully")
        void shouldFindCustomerByIdSuccessfully() {
            // Arrange
            when(customerUseCase.findCustomerById(CUSTOMER_ID))
                    .thenReturn(Optional.of(mockCustomer));

            // Act
            Optional<Customer> result = customerController.findCustomerById(CUSTOMER_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(CUSTOMER_ID, result.get().getId());
            assertEquals(VALID_NAME, result.get().getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.get().getEmail());
            assertEquals(VALID_CPF, result.get().getCpf());

            verify(customerUseCase).findCustomerById(CUSTOMER_ID);
        }

        @Test
        @DisplayName("Should return empty when customer not found by ID")
        void shouldReturnEmptyWhenCustomerNotFoundById() {
            // Arrange
            UUID randomId = UUID.randomUUID();
            when(customerUseCase.findCustomerById(randomId))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Customer> result = customerController.findCustomerById(randomId);

            // Assert
            assertFalse(result.isPresent());
            verify(customerUseCase).findCustomerById(randomId);
        }

        @Test
        @DisplayName("Should delegate to use case when finding customer by ID")
        void shouldDelegateToUseCaseWhenFindingCustomerById() {
            // Arrange
            when(customerUseCase.findCustomerById(any(UUID.class)))
                    .thenReturn(Optional.of(mockCustomer));

            // Act
            customerController.findCustomerById(CUSTOMER_ID);

            // Assert
            verify(customerUseCase, times(1)).findCustomerById(CUSTOMER_ID);
        }
    }

    @Nested
    @DisplayName("Find All Customers Tests")
    class FindAllCustomersTests {

        @Test
        @DisplayName("Should find all customers successfully")
        void shouldFindAllCustomersSuccessfully() {
            // Arrange
            Customer customer1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 1")
                    .email("customer1@example.com")
                    .cpf("11144477735")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 2")
                    .email("customer2@example.com")
                    .cpf("52998224725")
                    .build();

            List<Customer> mockCustomers = Arrays.asList(customer1, customer2);
            when(customerUseCase.findCustomerAll()).thenReturn(mockCustomers);

            // Act
            List<Customer> result = customerController.findAllCustomers();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(customer1.getId(), result.get(0).getId());
            assertEquals(customer2.getId(), result.get(1).getId());

            verify(customerUseCase).findCustomerAll();
        }

        @Test
        @DisplayName("Should return empty list when no customers found")
        void shouldReturnEmptyListWhenNoCustomersFound() {
            // Arrange
            when(customerUseCase.findCustomerAll()).thenReturn(List.of());

            // Act
            List<Customer> result = customerController.findAllCustomers();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(customerUseCase).findCustomerAll();
        }

        @Test
        @DisplayName("Should delegate to use case when finding all customers")
        void shouldDelegateToUseCaseWhenFindingAllCustomers() {
            // Arrange
            when(customerUseCase.findCustomerAll()).thenReturn(List.of(mockCustomer));

            // Act
            customerController.findAllCustomers();

            // Assert
            verify(customerUseCase, times(1)).findCustomerAll();
        }
    }
}

