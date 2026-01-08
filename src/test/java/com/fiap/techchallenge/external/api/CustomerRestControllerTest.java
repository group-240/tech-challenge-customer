package com.fiap.techchallenge.external.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge.adapters.controllers.CustomerController;
import com.fiap.techchallenge.domain.entities.Customer;
import com.fiap.techchallenge.domain.exception.DomainException;
import com.fiap.techchallenge.domain.exception.InvalidCpfException;
import com.fiap.techchallenge.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerRestController.class)
@DisplayName("Customer REST Controller Tests")
class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    @DisplayName("POST /customers - Register Customer Tests")
    class RegisterCustomerTests {

        @Test
        @DisplayName("Should register customer successfully and return 201")
        void shouldRegisterCustomerSuccessfullyAndReturn201() throws Exception {
            // Arrange
            CustomerRestController.CustomerRequestDTO request = new CustomerRestController.CustomerRequestDTO();
            request.setName(VALID_NAME);
            request.setEmail(VALID_EMAIL);
            request.setCpf(VALID_CPF);

            when(customerController.registerCustomer(VALID_NAME, VALID_EMAIL, VALID_CPF))
                    .thenReturn(mockCustomer);

            // Act & Assert
            mockMvc.perform(post("/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(CUSTOMER_ID.toString()))
                    .andExpect(jsonPath("$.name").value(VALID_NAME))
                    .andExpect(jsonPath("$.email").value(VALID_EMAIL.toLowerCase()))
                    .andExpect(jsonPath("$.cpf").value(VALID_CPF));
        }

        @Test
        @DisplayName("Should return 400 when CPF is invalid")
        void shouldReturn400WhenCpfIsInvalid() throws Exception {
            // Arrange
            CustomerRestController.CustomerRequestDTO request = new CustomerRestController.CustomerRequestDTO();
            request.setName(VALID_NAME);
            request.setEmail(VALID_EMAIL);
            request.setCpf("12345678901");

            when(customerController.registerCustomer(anyString(), anyString(), anyString()))
                    .thenThrow(new InvalidCpfException("Invalid CPF checksum"));

            // Act & Assert
            mockMvc.perform(post("/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Invalid CPF checksum"));
        }

        @Test
        @DisplayName("Should return 400 when email is invalid")
        void shouldReturn400WhenEmailIsInvalid() throws Exception {
            // Arrange
            CustomerRestController.CustomerRequestDTO request = new CustomerRestController.CustomerRequestDTO();
            request.setName(VALID_NAME);
            request.setEmail("invalid-email");
            request.setCpf(VALID_CPF);

            when(customerController.registerCustomer(anyString(), anyString(), anyString()))
                    .thenThrow(new InvalidEmailException("Invalid email format: invalid-email"));

            // Act & Assert
            mockMvc.perform(post("/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should return 400 when customer with CPF already exists")
        void shouldReturn400WhenCustomerWithCpfAlreadyExists() throws Exception {
            // Arrange
            CustomerRestController.CustomerRequestDTO request = new CustomerRestController.CustomerRequestDTO();
            request.setName(VALID_NAME);
            request.setEmail(VALID_EMAIL);
            request.setCpf(VALID_CPF);

            when(customerController.registerCustomer(anyString(), anyString(), anyString()))
                    .thenThrow(new DomainException("Customer with CPF " + VALID_CPF + " already exists"));

            // Act & Assert
            mockMvc.perform(post("/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Customer with CPF " + VALID_CPF + " already exists"));
        }
    }

    @Nested
    @DisplayName("GET /customers/{id} - Find Customer By ID Tests")
    class FindCustomerByIdTests {

        @Test
        @DisplayName("Should find customer by ID and return 200")
        void shouldFindCustomerByIdAndReturn200() throws Exception {
            // Arrange
            when(customerController.findCustomerById(CUSTOMER_ID))
                    .thenReturn(Optional.of(mockCustomer));

            // Act & Assert
            mockMvc.perform(get("/customers/{id}", CUSTOMER_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(CUSTOMER_ID.toString()))
                    .andExpect(jsonPath("$.name").value(VALID_NAME))
                    .andExpect(jsonPath("$.email").value(VALID_EMAIL.toLowerCase()))
                    .andExpect(jsonPath("$.cpf").value(VALID_CPF));
        }

        @Test
        @DisplayName("Should return 404 when customer not found by ID")
        void shouldReturn404WhenCustomerNotFoundById() throws Exception {
            // Arrange
            UUID randomId = UUID.randomUUID();
            when(customerController.findCustomerById(randomId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/customers/{id}", randomId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /customers/cpf/{cpf} - Find Customer By CPF Tests")
    class FindCustomerByCpfTests {

        @Test
        @DisplayName("Should find customer by CPF and return 200")
        void shouldFindCustomerByCpfAndReturn200() throws Exception {
            // Arrange
            when(customerController.findCustomerByCpf(VALID_CPF))
                    .thenReturn(Optional.of(mockCustomer));

            // Act & Assert
            mockMvc.perform(get("/customers/cpf/{cpf}", VALID_CPF)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(CUSTOMER_ID.toString()))
                    .andExpect(jsonPath("$.name").value(VALID_NAME))
                    .andExpect(jsonPath("$.email").value(VALID_EMAIL.toLowerCase()))
                    .andExpect(jsonPath("$.cpf").value(VALID_CPF));
        }

        @Test
        @DisplayName("Should return 404 when customer not found by CPF")
        void shouldReturn404WhenCustomerNotFoundByCpf() throws Exception {
            // Arrange
            when(customerController.findCustomerByCpf(VALID_CPF))
                    .thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/customers/cpf/{cpf}", VALID_CPF)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /customers - Find All Customers Tests")
    class FindAllCustomersTests {

        @Test
        @DisplayName("Should find all customers and return 200")
        void shouldFindAllCustomersAndReturn200() throws Exception {
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

            List<Customer> customers = Arrays.asList(customer1, customer2);
            when(customerController.findAllCustomers()).thenReturn(customers);

            // Act & Assert
            mockMvc.perform(get("/customers")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name").value("Customer 1"))
                    .andExpect(jsonPath("$[1].name").value("Customer 2"));
        }

        @Test
        @DisplayName("Should return empty list when no customers found")
        void shouldReturnEmptyListWhenNoCustomersFound() throws Exception {
            // Arrange
            when(customerController.findAllCustomers()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/customers")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }
}

