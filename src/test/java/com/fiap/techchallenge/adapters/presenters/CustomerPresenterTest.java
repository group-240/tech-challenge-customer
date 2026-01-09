package com.fiap.techchallenge.adapters.presenters;

import com.fiap.techchallenge.domain.entities.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Presenter Tests")
class CustomerPresenterTest {

    private static final String VALID_CPF = "11144477735";
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_EMAIL = "joao.silva@example.com";
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    @Nested
    @DisplayName("To Response DTO Tests")
    class ToResponseDTOTests {

        @Test
        @DisplayName("Should convert customer to response DTO successfully")
        void shouldConvertCustomerToResponseDTOSuccessfully() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerPresenter.CustomerResponseDTO dto = CustomerPresenter.toResponseDTO(customer);

            // Assert
            assertNotNull(dto);
            assertEquals(CUSTOMER_ID, dto.getId());
            assertEquals(VALID_NAME, dto.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), dto.getEmail());
            assertEquals(VALID_CPF, dto.getCpf());
        }

        @Test
        @DisplayName("Should preserve all customer data in DTO")
        void shouldPreserveAllCustomerDataInDTO() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerPresenter.CustomerResponseDTO dto = CustomerPresenter.toResponseDTO(customer);

            // Assert
            assertEquals(customer.getId(), dto.getId());
            assertEquals(customer.getName(), dto.getName());
            assertEquals(customer.getEmail(), dto.getEmail());
            assertEquals(customer.getCpf(), dto.getCpf());
        }

        @Test
        @DisplayName("Should convert customer with empty email")
        void shouldConvertCustomerWithEmptyEmail() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email("")
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerPresenter.CustomerResponseDTO dto = CustomerPresenter.toResponseDTO(customer);

            // Assert
            assertNotNull(dto);
            assertEquals("", dto.getEmail());
        }
    }

    @Nested
    @DisplayName("To Response DTO List Tests")
    class ToResponseDTOListTests {

        @Test
        @DisplayName("Should convert list of customers to DTOs successfully")
        void shouldConvertListOfCustomersToDTOsSuccessfully() {
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

            // Act
            List<CustomerPresenter.CustomerResponseDTO> dtos = CustomerPresenter.toResponseDTOList(customers);

            // Assert
            assertNotNull(dtos);
            assertEquals(2, dtos.size());
            assertEquals(customer1.getId(), dtos.get(0).getId());
            assertEquals(customer2.getId(), dtos.get(1).getId());
        }

        @Test
        @DisplayName("Should return empty list when customer list is empty")
        void shouldReturnEmptyListWhenCustomerListIsEmpty() {
            // Arrange
            List<Customer> customers = List.of();

            // Act
            List<CustomerPresenter.CustomerResponseDTO> dtos = CustomerPresenter.toResponseDTOList(customers);

            // Assert
            assertNotNull(dtos);
            assertTrue(dtos.isEmpty());
        }

        @Test
        @DisplayName("Should preserve order when converting list")
        void shouldPreserveOrderWhenConvertingList() {
            // Arrange
            Customer customer1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("First Customer")
                    .email("first@example.com")
                    .cpf("11144477735")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Second Customer")
                    .email("second@example.com")
                    .cpf("52998224725")
                    .build();

            List<Customer> customers = Arrays.asList(customer1, customer2);

            // Act
            List<CustomerPresenter.CustomerResponseDTO> dtos = CustomerPresenter.toResponseDTOList(customers);

            // Assert
            assertEquals("First Customer", dtos.get(0).getName());
            assertEquals("Second Customer", dtos.get(1).getName());
        }

        @Test
        @DisplayName("Should convert all customer fields correctly in list")
        void shouldConvertAllCustomerFieldsCorrectlyInList() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            List<Customer> customers = List.of(customer);

            // Act
            List<CustomerPresenter.CustomerResponseDTO> dtos = CustomerPresenter.toResponseDTOList(customers);

            // Assert
            assertEquals(1, dtos.size());
            CustomerPresenter.CustomerResponseDTO dto = dtos.get(0);
            assertEquals(CUSTOMER_ID, dto.getId());
            assertEquals(VALID_NAME, dto.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), dto.getEmail());
            assertEquals(VALID_CPF, dto.getCpf());
        }
    }

    @Nested
    @DisplayName("Customer Response DTO Tests")
    class CustomerResponseDTOTests {

        @Test
        @DisplayName("Should create DTO with default constructor")
        void shouldCreateDTOWithDefaultConstructor() {
            // Act
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO();

            // Assert
            assertNotNull(dto);
            assertNull(dto.getId());
            assertNull(dto.getName());
            assertNull(dto.getEmail());
            assertNull(dto.getCpf());
        }

        @Test
        @DisplayName("Should create DTO with parameterized constructor")
        void shouldCreateDTOWithParameterizedConstructor() {
            // Act
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO(
                    CUSTOMER_ID, VALID_NAME, VALID_EMAIL, VALID_CPF
            );

            // Assert
            assertEquals(CUSTOMER_ID, dto.getId());
            assertEquals(VALID_NAME, dto.getName());
            assertEquals(VALID_EMAIL, dto.getEmail());
            assertEquals(VALID_CPF, dto.getCpf());
        }

        @Test
        @DisplayName("Should set and get ID correctly")
        void shouldSetAndGetIdCorrectly() {
            // Arrange
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO();

            // Act
            dto.setId(CUSTOMER_ID);

            // Assert
            assertEquals(CUSTOMER_ID, dto.getId());
        }

        @Test
        @DisplayName("Should set and get name correctly")
        void shouldSetAndGetNameCorrectly() {
            // Arrange
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO();

            // Act
            dto.setName(VALID_NAME);

            // Assert
            assertEquals(VALID_NAME, dto.getName());
        }

        @Test
        @DisplayName("Should set and get email correctly")
        void shouldSetAndGetEmailCorrectly() {
            // Arrange
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO();

            // Act
            dto.setEmail(VALID_EMAIL);

            // Assert
            assertEquals(VALID_EMAIL, dto.getEmail());
        }

        @Test
        @DisplayName("Should set and get CPF correctly")
        void shouldSetAndGetCpfCorrectly() {
            // Arrange
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO();

            // Act
            dto.setCpf(VALID_CPF);

            // Assert
            assertEquals(VALID_CPF, dto.getCpf());
        }

        @Test
        @DisplayName("Should handle all setters and getters")
        void shouldHandleAllSettersAndGetters() {
            // Arrange
            CustomerPresenter.CustomerResponseDTO dto = new CustomerPresenter.CustomerResponseDTO();

            // Act
            dto.setId(CUSTOMER_ID);
            dto.setName(VALID_NAME);
            dto.setEmail(VALID_EMAIL);
            dto.setCpf(VALID_CPF);

            // Assert
            assertEquals(CUSTOMER_ID, dto.getId());
            assertEquals(VALID_NAME, dto.getName());
            assertEquals(VALID_EMAIL, dto.getEmail());
            assertEquals(VALID_CPF, dto.getCpf());
        }
    }
}

