package com.fiap.techchallenge.adapters.gateway;

import com.fiap.techchallenge.application.usecases.mappers.CustomerMapper;
import com.fiap.techchallenge.domain.entities.Customer;
import com.fiap.techchallenge.external.datasource.entities.CustomerJpaEntity;
import com.fiap.techchallenge.external.datasource.repositories.CustomerJpaRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Repository Gateway Tests")
class CustomerRepositoryGatewayTest {

    @Mock
    private CustomerJpaRepository customerJpaRepository;

    @InjectMocks
    private CustomerRepositoryGateway customerRepositoryGateway;

    private static final String VALID_CPF = "11144477735";
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_EMAIL = "joao.silva@example.com";
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    private Customer mockCustomer;
    private CustomerJpaEntity mockJpaEntity;

    @BeforeEach
    void setUp() {
        mockCustomer = Customer.builder()
                .id(CUSTOMER_ID)
                .name(VALID_NAME)
                .email(VALID_EMAIL)
                .cpf(VALID_CPF)
                .build();

        mockJpaEntity = new CustomerJpaEntity(
                CUSTOMER_ID,
                VALID_NAME,
                VALID_EMAIL,
                VALID_CPF
        );
    }

    @Nested
    @DisplayName("Save Customer Tests")
    class SaveCustomerTests {

        @Test
        @DisplayName("Should save customer successfully")
        void shouldSaveCustomerSuccessfully() {
            // Arrange
            when(customerJpaRepository.save(any(CustomerJpaEntity.class)))
                    .thenReturn(mockJpaEntity);

            // Act
            Customer result = customerRepositoryGateway.save(mockCustomer);

            // Assert
            assertNotNull(result);
            assertEquals(CUSTOMER_ID, result.getId());
            assertEquals(VALID_NAME, result.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.getEmail());
            assertEquals(VALID_CPF, result.getCpf());

            verify(customerJpaRepository).save(any(CustomerJpaEntity.class));
        }

        @Test
        @DisplayName("Should convert domain entity to JPA entity before saving")
        void shouldConvertDomainEntityToJpaEntityBeforeSaving() {
            // Arrange
            when(customerJpaRepository.save(any(CustomerJpaEntity.class)))
                    .thenReturn(mockJpaEntity);

            // Act
            customerRepositoryGateway.save(mockCustomer);

            // Assert
            verify(customerJpaRepository).save(argThat(jpaEntity ->
                    jpaEntity.getId().equals(CUSTOMER_ID) &&
                    jpaEntity.getName().equals(VALID_NAME) &&
                    jpaEntity.getEmail().equals(VALID_EMAIL.toLowerCase()) &&
                    jpaEntity.getCpf().equals(VALID_CPF)
            ));
        }

        @Test
        @DisplayName("Should convert saved JPA entity back to domain entity")
        void shouldConvertSavedJpaEntityBackToDomainEntity() {
            // Arrange
            CustomerJpaEntity savedEntity = new CustomerJpaEntity(
                    UUID.randomUUID(),
                    "New Name",
                    "new@example.com",
                    "52998224725"
            );
            when(customerJpaRepository.save(any(CustomerJpaEntity.class)))
                    .thenReturn(savedEntity);

            // Act
            Customer result = customerRepositoryGateway.save(mockCustomer);

            // Assert
            assertEquals(savedEntity.getId(), result.getId());
            assertEquals(savedEntity.getName(), result.getName());
            assertEquals(savedEntity.getEmail().toLowerCase(), result.getEmail());
            assertEquals(savedEntity.getCpf(), result.getCpf());
        }
    }

    @Nested
    @DisplayName("Find Customer By ID Tests")
    class FindCustomerByIdTests {

        @Test
        @DisplayName("Should find customer by ID successfully")
        void shouldFindCustomerByIdSuccessfully() {
            // Arrange
            when(customerJpaRepository.findById(CUSTOMER_ID))
                    .thenReturn(Optional.of(mockJpaEntity));

            // Act
            Optional<Customer> result = customerRepositoryGateway.findById(CUSTOMER_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(CUSTOMER_ID, result.get().getId());
            assertEquals(VALID_NAME, result.get().getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.get().getEmail());
            assertEquals(VALID_CPF, result.get().getCpf());

            verify(customerJpaRepository).findById(CUSTOMER_ID);
        }

        @Test
        @DisplayName("Should return empty when customer not found by ID")
        void shouldReturnEmptyWhenCustomerNotFoundById() {
            // Arrange
            UUID randomId = UUID.randomUUID();
            when(customerJpaRepository.findById(randomId))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Customer> result = customerRepositoryGateway.findById(randomId);

            // Assert
            assertFalse(result.isPresent());
            verify(customerJpaRepository).findById(randomId);
        }

        @Test
        @DisplayName("Should convert JPA entity to domain entity when found")
        void shouldConvertJpaEntityToDomainEntityWhenFound() {
            // Arrange
            when(customerJpaRepository.findById(CUSTOMER_ID))
                    .thenReturn(Optional.of(mockJpaEntity));

            // Act
            Optional<Customer> result = customerRepositoryGateway.findById(CUSTOMER_ID);

            // Assert
            assertTrue(result.isPresent());
            Customer customer = result.get();
            assertEquals(mockJpaEntity.getId(), customer.getId());
            assertEquals(mockJpaEntity.getName(), customer.getName());
        }
    }

    @Nested
    @DisplayName("Find Customer By CPF Tests")
    class FindCustomerByCpfTests {

        @Test
        @DisplayName("Should find customer by CPF successfully")
        void shouldFindCustomerByCpfSuccessfully() {
            // Arrange
            when(customerJpaRepository.findByCpf(VALID_CPF))
                    .thenReturn(Optional.of(mockJpaEntity));

            // Act
            Optional<Customer> result = customerRepositoryGateway.findByCpf(VALID_CPF);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(CUSTOMER_ID, result.get().getId());
            assertEquals(VALID_NAME, result.get().getName());
            assertEquals(VALID_EMAIL.toLowerCase(), result.get().getEmail());
            assertEquals(VALID_CPF, result.get().getCpf());

            verify(customerJpaRepository).findByCpf(VALID_CPF);
        }

        @Test
        @DisplayName("Should return empty when customer not found by CPF")
        void shouldReturnEmptyWhenCustomerNotFoundByCpf() {
            // Arrange
            when(customerJpaRepository.findByCpf(VALID_CPF))
                    .thenReturn(Optional.empty());

            // Act
            Optional<Customer> result = customerRepositoryGateway.findByCpf(VALID_CPF);

            // Assert
            assertFalse(result.isPresent());
            verify(customerJpaRepository).findByCpf(VALID_CPF);
        }

        @Test
        @DisplayName("Should convert JPA entity to domain entity when found by CPF")
        void shouldConvertJpaEntityToDomainEntityWhenFoundByCpf() {
            // Arrange
            when(customerJpaRepository.findByCpf(VALID_CPF))
                    .thenReturn(Optional.of(mockJpaEntity));

            // Act
            Optional<Customer> result = customerRepositoryGateway.findByCpf(VALID_CPF);

            // Assert
            assertTrue(result.isPresent());
            Customer customer = result.get();
            assertEquals(mockJpaEntity.getCpf(), customer.getCpf());
        }
    }

    @Nested
    @DisplayName("Exists By CPF Tests")
    class ExistsByCpfTests {

        @Test
        @DisplayName("Should return true when customer exists by CPF")
        void shouldReturnTrueWhenCustomerExistsByCpf() {
            // Arrange
            when(customerJpaRepository.existsByCpf(VALID_CPF))
                    .thenReturn(true);

            // Act
            boolean result = customerRepositoryGateway.existsByCpf(VALID_CPF);

            // Assert
            assertTrue(result);
            verify(customerJpaRepository).existsByCpf(VALID_CPF);
        }

        @Test
        @DisplayName("Should return false when customer does not exist by CPF")
        void shouldReturnFalseWhenCustomerDoesNotExistByCpf() {
            // Arrange
            when(customerJpaRepository.existsByCpf(VALID_CPF))
                    .thenReturn(false);

            // Act
            boolean result = customerRepositoryGateway.existsByCpf(VALID_CPF);

            // Assert
            assertFalse(result);
            verify(customerJpaRepository).existsByCpf(VALID_CPF);
        }
    }

    @Nested
    @DisplayName("Find All Customers Tests")
    class FindAllCustomersTests {

        @Test
        @DisplayName("Should find all customers successfully")
        void shouldFindAllCustomersSuccessfully() {
            // Arrange
            CustomerJpaEntity jpaEntity1 = new CustomerJpaEntity(
                    UUID.randomUUID(),
                    "Customer 1",
                    "customer1@example.com",
                    "11144477735"
            );

            CustomerJpaEntity jpaEntity2 = new CustomerJpaEntity(
                    UUID.randomUUID(),
                    "Customer 2",
                    "customer2@example.com",
                    "52998224725"
            );

            List<CustomerJpaEntity> mockJpaEntities = Arrays.asList(jpaEntity1, jpaEntity2);
            when(customerJpaRepository.findAll()).thenReturn(mockJpaEntities);

            // Act
            List<Customer> result = customerRepositoryGateway.findAll();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(jpaEntity1.getId(), result.get(0).getId());
            assertEquals(jpaEntity2.getId(), result.get(1).getId());

            verify(customerJpaRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no customers found")
        void shouldReturnEmptyListWhenNoCustomersFound() {
            // Arrange
            when(customerJpaRepository.findAll()).thenReturn(List.of());

            // Act
            List<Customer> result = customerRepositoryGateway.findAll();

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(customerJpaRepository).findAll();
        }

        @Test
        @DisplayName("Should convert all JPA entities to domain entities")
        void shouldConvertAllJpaEntitiesToDomainEntities() {
            // Arrange
            List<CustomerJpaEntity> mockJpaEntities = Arrays.asList(mockJpaEntity);
            when(customerJpaRepository.findAll()).thenReturn(mockJpaEntities);

            // Act
            List<Customer> result = customerRepositoryGateway.findAll();

            // Assert
            assertEquals(1, result.size());
            Customer customer = result.get(0);
            assertEquals(mockJpaEntity.getId(), customer.getId());
            assertEquals(mockJpaEntity.getName(), customer.getName());
            assertEquals(mockJpaEntity.getEmail().toLowerCase(), customer.getEmail());
            assertEquals(mockJpaEntity.getCpf(), customer.getCpf());
        }
    }
}

