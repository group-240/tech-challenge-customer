package com.fiap.techchallenge.application.usecases;

import com.fiap.techchallenge.domain.entities.Customer;
import com.fiap.techchallenge.domain.exception.DomainException;
import com.fiap.techchallenge.domain.exception.NotFoundException;
import com.fiap.techchallenge.domain.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer UseCase Implementation Tests")
class CustomerUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerUseCaseImpl customerUseCase;

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
            when(customerRepository.existsByCpf(VALID_CPF)).thenReturn(false);
            when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

            // Act
            Customer result = customerUseCase.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF);

            // Assert
            assertNotNull(result);
            assertEquals(VALID_NAME, result.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.getEmail());
            assertEquals(VALID_CPF, result.getCpf());

            verify(customerRepository).existsByCpf(VALID_CPF);
            verify(customerRepository).save(any(Customer.class));
        }

        @Test
        @DisplayName("Should register customer with empty email")
        void shouldRegisterCustomerWithEmptyEmail() {
            // Arrange
            Customer customerWithoutEmail = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email("")
                    .cpf(VALID_CPF)
                    .build();

            when(customerRepository.existsByCpf(VALID_CPF)).thenReturn(false);
            when(customerRepository.save(any(Customer.class))).thenReturn(customerWithoutEmail);

            // Act
            Customer result = customerUseCase.registerCustomer(VALID_NAME, "", VALID_CPF);

            // Assert
            assertNotNull(result);
            assertEquals("", result.getEmail());
            verify(customerRepository).save(any(Customer.class));
        }

        @Test
        @DisplayName("Should capture customer data when registering")
        void shouldCaptureCustomerDataWhenRegistering() {
            // Arrange
            when(customerRepository.existsByCpf(VALID_CPF)).thenReturn(false);
            when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

            ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

            // Act
            customerUseCase.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF);

            // Assert
            verify(customerRepository).save(customerCaptor.capture());
            Customer captured = customerCaptor.getValue();
            assertNotNull(captured.getId());
            assertEquals(VALID_NAME, captured.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), captured.getEmail());
            assertEquals(VALID_CPF, captured.getCpf());
        }

        @Test
        @DisplayName("Should throw exception when CPF already exists")
        void shouldThrowExceptionWhenCpfAlreadyExists() {
            // Arrange
            when(customerRepository.existsByCpf(VALID_CPF)).thenReturn(true);

            // Act & Assert
            DomainException exception = assertThrows(DomainException.class, () ->
                    customerUseCase.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF)
            );

            assertEquals("Customer with CPF " + VALID_CPF + " already exists", exception.getMessage());
            verify(customerRepository).existsByCpf(VALID_CPF);
            verify(customerRepository, never()).save(any(Customer.class));
        }

        @Test
        @DisplayName("Should generate unique ID for each customer")
        void shouldGenerateUniqueIdForEachCustomer() {
            // Arrange
            when(customerRepository.existsByCpf(anyString())).thenReturn(false);
            when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            Customer customer1 = customerUseCase.registerCustomer(VALID_NAME, VALID_EMAIL, "11144477735");
            Customer customer2 = customerUseCase.registerCustomer(VALID_NAME, VALID_EMAIL, "52998224725");

            // Assert
            assertNotNull(customer1.getId());
            assertNotNull(customer2.getId());
            assertNotEquals(customer1.getId(), customer2.getId());
        }
    }

    @Nested
    @DisplayName("Find Customer By CPF Tests")
    class FindCustomerByCpfTests {

        @Test
        @DisplayName("Should find customer by CPF successfully")
        void shouldFindCustomerByCpfSuccessfully() {
            // Arrange
            when(customerRepository.findByCpf(VALID_CPF)).thenReturn(Optional.of(mockCustomer));

            // Act
            Optional<Customer> result = customerUseCase.findCustomerByCpf(VALID_CPF);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(CUSTOMER_ID, result.get().getId());
            assertEquals(VALID_NAME, result.get().getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.get().getEmail());
            assertEquals(VALID_CPF, result.get().getCpf());

            verify(customerRepository).findByCpf(VALID_CPF);
        }

        @Test
        @DisplayName("Should throw NotFoundException when customer not found by CPF")
        void shouldThrowNotFoundExceptionWhenCustomerNotFoundByCpf() {
            // Arrange
            when(customerRepository.findByCpf(VALID_CPF)).thenReturn(Optional.empty());

            // Act & Assert
            NotFoundException exception = assertThrows(NotFoundException.class, () ->
                    customerUseCase.findCustomerByCpf(VALID_CPF)
            );

            assertEquals("Record not found", exception.getMessage());
            verify(customerRepository).findByCpf(VALID_CPF);
        }
    }

    @Nested
    @DisplayName("Find Customer By ID Tests")
    class FindCustomerByIdTests {

        @Test
        @DisplayName("Should find customer by ID successfully")
        void shouldFindCustomerByIdSuccessfully() {
            // Arrange
            when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(mockCustomer));

            // Act
            Optional<Customer> result = customerUseCase.findCustomerById(CUSTOMER_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(CUSTOMER_ID, result.get().getId());
            assertEquals(VALID_NAME, result.get().getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.get().getEmail());
            assertEquals(VALID_CPF, result.get().getCpf());

            verify(customerRepository).findById(CUSTOMER_ID);
        }

        @Test
        @DisplayName("Should throw NotFoundException when customer not found by ID")
        void shouldThrowNotFoundExceptionWhenCustomerNotFoundById() {
            // Arrange
            UUID randomId = UUID.randomUUID();
            when(customerRepository.findById(randomId)).thenReturn(Optional.empty());

            // Act & Assert
            NotFoundException exception = assertThrows(NotFoundException.class, () ->
                    customerUseCase.findCustomerById(randomId)
            );

            assertEquals("Record not found", exception.getMessage());
            verify(customerRepository).findById(randomId);
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
            when(customerRepository.findAll()).thenReturn(mockCustomers);

            // Act
            List<Customer> result = customerUseCase.findCustomerAll();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(customer1.getId(), result.get(0).getId());
            assertEquals(customer2.getId(), result.get(1).getId());

            verify(customerRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no customers found")
        void shouldReturnEmptyListWhenNoCustomersFound() {
            // Arrange
            when(customerRepository.findAll()).thenReturn(List.of());

            // Act
            List<Customer> result = customerUseCase.findCustomerAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(customerRepository).findAll();
        }

        @Test
        @DisplayName("Should return all customers with correct data")
        void shouldReturnAllCustomersWithCorrectData() {
            // Arrange
            List<Customer> mockCustomers = Arrays.asList(mockCustomer);
            when(customerRepository.findAll()).thenReturn(mockCustomers);

            // Act
            List<Customer> result = customerUseCase.findCustomerAll();

            // Assert
            assertEquals(1, result.size());
            Customer customer = result.get(0);
            assertEquals(CUSTOMER_ID, customer.getId());
            assertEquals(VALID_NAME, customer.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), customer.getEmail());
            assertEquals(VALID_CPF, customer.getCpf());
        }
    }
}

