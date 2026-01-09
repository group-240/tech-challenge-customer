package com.fiap.techchallenge.application.usecases.mappers;

import com.fiap.techchallenge.domain.entities.Customer;
import com.fiap.techchallenge.external.datasource.entities.CustomerJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Mapper Tests")
class CustomerMapperTest {

    private static final String VALID_CPF = "11144477735";
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_EMAIL = "joao.silva@example.com";
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    @Nested
    @DisplayName("To JPA Entity Tests")
    class ToJpaEntityTests {

        @Test
        @DisplayName("Should convert domain entity to JPA entity successfully")
        void shouldConvertDomainEntityToJpaEntitySuccessfully() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerJpaEntity jpaEntity = CustomerMapper.toJpaEntity(customer);

            // Assert
            assertNotNull(jpaEntity);
            assertEquals(CUSTOMER_ID, jpaEntity.getId());
            assertEquals(VALID_NAME, jpaEntity.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), jpaEntity.getEmail());
            assertEquals(VALID_CPF, jpaEntity.getCpf());
        }

        @Test
        @DisplayName("Should return null when domain entity is null")
        void shouldReturnNullWhenDomainEntityIsNull() {
            // Arrange & Act
            CustomerJpaEntity jpaEntity = CustomerMapper.toJpaEntity(null);

            // Assert
            assertNull(jpaEntity);
        }

        @Test
        @DisplayName("Should convert domain entity with empty email")
        void shouldConvertDomainEntityWithEmptyEmail() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email("")
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerJpaEntity jpaEntity = CustomerMapper.toJpaEntity(customer);

            // Assert
            assertNotNull(jpaEntity);
            assertEquals("", jpaEntity.getEmail());
        }

        @Test
        @DisplayName("Should preserve all customer data when converting to JPA entity")
        void shouldPreserveAllCustomerDataWhenConvertingToJpaEntity() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerJpaEntity jpaEntity = CustomerMapper.toJpaEntity(customer);

            // Assert
            assertEquals(customer.getId(), jpaEntity.getId());
            assertEquals(customer.getName(), jpaEntity.getName());
            assertEquals(customer.getEmail(), jpaEntity.getEmail());
            assertEquals(customer.getCpf(), jpaEntity.getCpf());
        }
    }

    @Nested
    @DisplayName("To Domain Entity Tests")
    class ToDomainEntityTests {

        @Test
        @DisplayName("Should convert JPA entity to domain entity successfully")
        void shouldConvertJpaEntityToDomainEntitySuccessfully() {
            // Arrange
            CustomerJpaEntity jpaEntity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    VALID_NAME,
                    VALID_EMAIL,
                    VALID_CPF
            );

            // Act
            Customer customer = CustomerMapper.toDomainEntity(jpaEntity);

            // Assert
            assertNotNull(customer);
            assertEquals(CUSTOMER_ID, customer.getId());
            assertEquals(VALID_NAME, customer.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), customer.getEmail());
            assertEquals(VALID_CPF, customer.getCpf());
        }

        @Test
        @DisplayName("Should return null when JPA entity is null")
        void shouldReturnNullWhenJpaEntityIsNull() {
            // Arrange & Act
            Customer customer = CustomerMapper.toDomainEntity(null);

            // Assert
            assertNull(customer);
        }

        @Test
        @DisplayName("Should convert JPA entity with empty email")
        void shouldConvertJpaEntityWithEmptyEmail() {
            // Arrange
            CustomerJpaEntity jpaEntity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    VALID_NAME,
                    "",
                    VALID_CPF
            );

            // Act
            Customer customer = CustomerMapper.toDomainEntity(jpaEntity);

            // Assert
            assertNotNull(customer);
            assertEquals("", customer.getEmail());
        }

        @Test
        @DisplayName("Should preserve all JPA entity data when converting to domain entity")
        void shouldPreserveAllJpaEntityDataWhenConvertingToDomainEntity() {
            // Arrange
            CustomerJpaEntity jpaEntity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    VALID_NAME,
                    VALID_EMAIL,
                    VALID_CPF
            );

            // Act
            Customer customer = CustomerMapper.toDomainEntity(jpaEntity);

            // Assert
            assertEquals(jpaEntity.getId(), customer.getId());
            assertEquals(jpaEntity.getName(), customer.getName());
            assertEquals(jpaEntity.getEmail().toLowerCase(), customer.getEmail());
            assertEquals(jpaEntity.getCpf(), customer.getCpf());
        }
    }

    @Nested
    @DisplayName("Bidirectional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Should maintain data integrity in bidirectional conversion")
        void shouldMaintainDataIntegrityInBidirectionalConversion() {
            // Arrange
            Customer originalCustomer = Customer.builder()
                    .id(CUSTOMER_ID)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act
            CustomerJpaEntity jpaEntity = CustomerMapper.toJpaEntity(originalCustomer);
            Customer convertedCustomer = CustomerMapper.toDomainEntity(jpaEntity);

            // Assert
            assertEquals(originalCustomer.getId(), convertedCustomer.getId());
            assertEquals(originalCustomer.getName(), convertedCustomer.getName());
            assertEquals(originalCustomer.getEmail(), convertedCustomer.getEmail());
            assertEquals(originalCustomer.getCpf(), convertedCustomer.getCpf());
        }

        @Test
        @DisplayName("Should handle multiple conversions correctly")
        void shouldHandleMultipleConversionsCorrectly() {
            // Arrange
            CustomerJpaEntity originalJpaEntity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    VALID_NAME,
                    VALID_EMAIL,
                    VALID_CPF
            );

            // Act
            Customer customer = CustomerMapper.toDomainEntity(originalJpaEntity);
            CustomerJpaEntity convertedJpaEntity = CustomerMapper.toJpaEntity(customer);

            // Assert
            assertEquals(originalJpaEntity.getId(), convertedJpaEntity.getId());
            assertEquals(originalJpaEntity.getName(), convertedJpaEntity.getName());
            assertEquals(originalJpaEntity.getEmail().toLowerCase(), convertedJpaEntity.getEmail());
            assertEquals(originalJpaEntity.getCpf(), convertedJpaEntity.getCpf());
        }
    }
}

