package com.fiap.techchallenge.external.datasource.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer JPA Entity Tests")
class CustomerJpaEntityTest {

    private static final String VALID_CPF = "11144477735";
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_EMAIL = "joao.silva@example.com";
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create entity with default constructor")
        void shouldCreateEntityWithDefaultConstructor() {
            // Act
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Assert
            assertNotNull(entity);
            assertNull(entity.getId());
            assertNull(entity.getName());
            assertNull(entity.getEmail());
            assertNull(entity.getCpf());
        }

        @Test
        @DisplayName("Should create entity with parameterized constructor")
        void shouldCreateEntityWithParameterizedConstructor() {
            // Act
            CustomerJpaEntity entity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    VALID_NAME,
                    VALID_EMAIL,
                    VALID_CPF
            );

            // Assert
            assertNotNull(entity);
            assertEquals(CUSTOMER_ID, entity.getId());
            assertEquals(VALID_NAME, entity.getName());
            assertEquals(VALID_EMAIL, entity.getEmail());
            assertEquals(VALID_CPF, entity.getCpf());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get ID correctly")
        void shouldSetAndGetIdCorrectly() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();
            UUID newId = UUID.randomUUID();

            // Act
            entity.setId(newId);

            // Assert
            assertEquals(newId, entity.getId());
        }

        @Test
        @DisplayName("Should set and get name correctly")
        void shouldSetAndGetNameCorrectly() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Act
            entity.setName(VALID_NAME);

            // Assert
            assertEquals(VALID_NAME, entity.getName());
        }

        @Test
        @DisplayName("Should set and get email correctly")
        void shouldSetAndGetEmailCorrectly() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Act
            entity.setEmail(VALID_EMAIL);

            // Assert
            assertEquals(VALID_EMAIL, entity.getEmail());
        }

        @Test
        @DisplayName("Should set and get CPF correctly")
        void shouldSetAndGetCpfCorrectly() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Act
            entity.setCpf(VALID_CPF);

            // Assert
            assertEquals(VALID_CPF, entity.getCpf());
        }

        @Test
        @DisplayName("Should handle all setters and getters together")
        void shouldHandleAllSettersAndGettersTogether() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Act
            entity.setId(CUSTOMER_ID);
            entity.setName(VALID_NAME);
            entity.setEmail(VALID_EMAIL);
            entity.setCpf(VALID_CPF);

            // Assert
            assertEquals(CUSTOMER_ID, entity.getId());
            assertEquals(VALID_NAME, entity.getName());
            assertEquals(VALID_EMAIL, entity.getEmail());
            assertEquals(VALID_CPF, entity.getCpf());
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should preserve data when using both constructors and setters")
        void shouldPreserveDataWhenUsingBothConstructorsAndSetters() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    "Original Name",
                    "original@email.com",
                    VALID_CPF
            );

            // Act
            entity.setName(VALID_NAME);
            entity.setEmail(VALID_EMAIL);

            // Assert
            assertEquals(CUSTOMER_ID, entity.getId());
            assertEquals(VALID_NAME, entity.getName());
            assertEquals(VALID_EMAIL, entity.getEmail());
            assertEquals(VALID_CPF, entity.getCpf());
        }

        @Test
        @DisplayName("Should handle null values in setters")
        void shouldHandleNullValuesInSetters() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity(
                    CUSTOMER_ID,
                    VALID_NAME,
                    VALID_EMAIL,
                    VALID_CPF
            );

            // Act
            entity.setName(null);
            entity.setEmail(null);
            entity.setCpf(null);

            // Assert
            assertEquals(CUSTOMER_ID, entity.getId());
            assertNull(entity.getName());
            assertNull(entity.getEmail());
            assertNull(entity.getCpf());
        }

        @Test
        @DisplayName("Should accept empty strings")
        void shouldAcceptEmptyStrings() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Act
            entity.setName("");
            entity.setEmail("");
            entity.setCpf("");

            // Assert
            assertEquals("", entity.getName());
            assertEquals("", entity.getEmail());
            assertEquals("", entity.getCpf());
        }
    }

    @Nested
    @DisplayName("Entity State Tests")
    class EntityStateTests {

        @Test
        @DisplayName("Should maintain state after multiple updates")
        void shouldMaintainStateAfterMultipleUpdates() {
            // Arrange
            CustomerJpaEntity entity = new CustomerJpaEntity();

            // Act
            entity.setId(CUSTOMER_ID);
            entity.setName("First Name");
            entity.setName("Second Name");
            entity.setName(VALID_NAME);

            // Assert
            assertEquals(VALID_NAME, entity.getName());
        }

        @Test
        @DisplayName("Should create independent entity instances")
        void shouldCreateIndependentEntityInstances() {
            // Arrange & Act
            CustomerJpaEntity entity1 = new CustomerJpaEntity(
                    UUID.randomUUID(),
                    "Customer 1",
                    "customer1@example.com",
                    "11144477735"
            );

            CustomerJpaEntity entity2 = new CustomerJpaEntity(
                    UUID.randomUUID(),
                    "Customer 2",
                    "customer2@example.com",
                    "52998224725"
            );

            // Modify entity1
            entity1.setName("Modified Customer 1");

            // Assert
            assertEquals("Modified Customer 1", entity1.getName());
            assertEquals("Customer 2", entity2.getName());
        }
    }
}

