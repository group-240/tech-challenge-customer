package com.fiap.techchallenge.domain.entities;

import com.fiap.techchallenge.domain.exception.InvalidCpfException;
import com.fiap.techchallenge.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Entity Tests")
class CustomerTest {

    private static final String VALID_CPF = "11144477735"; // CPF válido
    private static final String VALID_NAME = "João da Silva";
    private static final String VALID_EMAIL = "joao.silva@example.com";

    @Nested
    @DisplayName("Customer Creation Tests")
    class CustomerCreationTests {

        @Test
        @DisplayName("Should create customer with valid data")
        void shouldCreateCustomerWithValidData() {
            // Arrange & Act
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Assert
            assertNotNull(customer);
            assertNotNull(customer.getId());
            assertEquals(VALID_NAME, customer.getName());
            assertEquals(VALID_EMAIL.toLowerCase(), customer.getEmail());
            assertEquals(VALID_CPF, customer.getCpf());
        }

        @Test
        @DisplayName("Should create customer with empty email")
        void shouldCreateCustomerWithEmptyEmail() {
            // Arrange & Act
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email("")
                    .cpf(VALID_CPF)
                    .build();

            // Assert
            assertNotNull(customer);
            assertEquals("", customer.getEmail());
        }

        @Test
        @DisplayName("Should create customer with null email")
        void shouldCreateCustomerWithNullEmail() {
            // Arrange & Act
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(null)
                    .cpf(VALID_CPF)
                    .build();

            // Assert
            assertNotNull(customer);
            assertEquals("", customer.getEmail());
        }

        @Test
        @DisplayName("Should normalize email to lowercase")
        void shouldNormalizeEmailToLowercase() {
            // Arrange & Act
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email("JOAO.SILVA@EXAMPLE.COM")
                    .cpf(VALID_CPF)
                    .build();

            // Assert
            assertEquals("joao.silva@example.com", customer.getEmail());
        }

        @Test
        @DisplayName("Should trim name whitespace")
        void shouldTrimNameWhitespace() {
            // Arrange & Act
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("  João da Silva  ")
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Assert
            assertEquals("João da Silva", customer.getName());
        }

        @Test
        @DisplayName("Should accept CPF with formatting characters")
        void shouldAcceptCpfWithFormattingCharacters() {
            // Arrange & Act
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf("111.444.777-35")
                    .build();

            // Assert
            assertEquals(VALID_CPF, customer.getCpf());
        }
    }

    @Nested
    @DisplayName("Customer Validation - Name Tests")
    class NameValidationTests {

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            // Arrange, Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(null)
                            .email(VALID_EMAIL)
                            .cpf(VALID_CPF)
                            .build()
            );

            assertEquals("Name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {
            // Arrange, Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name("")
                            .email(VALID_EMAIL)
                            .cpf(VALID_CPF)
                            .build()
            );

            assertEquals("Name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            // Arrange, Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name("   ")
                            .email(VALID_EMAIL)
                            .cpf(VALID_CPF)
                            .build()
            );

            assertEquals("Name cannot be null or empty", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Customer Validation - Email Tests")
    class EmailValidationTests {

        @Test
        @DisplayName("Should throw exception when email format is invalid")
        void shouldThrowExceptionWhenEmailFormatIsInvalid() {
            // Arrange, Act & Assert
            InvalidEmailException exception = assertThrows(InvalidEmailException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email("invalid-email")
                            .cpf(VALID_CPF)
                            .build()
            );

            assertTrue(exception.getMessage().contains("Invalid email format"));
        }

        @Test
        @DisplayName("Should throw exception when email has no domain")
        void shouldThrowExceptionWhenEmailHasNoDomain() {
            // Arrange, Act & Assert
            assertThrows(InvalidEmailException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email("test@")
                            .cpf(VALID_CPF)
                            .build()
            );
        }

        @Test
        @DisplayName("Should accept valid email formats")
        void shouldAcceptValidEmailFormats() {
            // Arrange
            String[] validEmails = {
                    "test@example.com",
                    "test.name@example.com",
                    "test+tag@example.co.uk",
                    "test_underscore@example.com"
            };

            // Act & Assert
            for (String email : validEmails) {
                assertDoesNotThrow(() ->
                        Customer.builder()
                                .id(UUID.randomUUID())
                                .name(VALID_NAME)
                                .email(email)
                                .cpf(VALID_CPF)
                                .build()
                );
            }
        }
    }

    @Nested
    @DisplayName("Customer Validation - CPF Tests")
    class CpfValidationTests {

        @Test
        @DisplayName("Should throw exception when CPF is null")
        void shouldThrowExceptionWhenCpfIsNull() {
            // Arrange, Act & Assert
            InvalidCpfException exception = assertThrows(InvalidCpfException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email(VALID_EMAIL)
                            .cpf(null)
                            .build()
            );

            assertEquals("CPF cannot be null", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when CPF has less than 11 digits")
        void shouldThrowExceptionWhenCpfHasLessThan11Digits() {
            // Arrange, Act & Assert
            InvalidCpfException exception = assertThrows(InvalidCpfException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email(VALID_EMAIL)
                            .cpf("123456789")
                            .build()
            );

            assertEquals("CPF must contain exactly 11 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when CPF has more than 11 digits")
        void shouldThrowExceptionWhenCpfHasMoreThan11Digits() {
            // Arrange, Act & Assert
            InvalidCpfException exception = assertThrows(InvalidCpfException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email(VALID_EMAIL)
                            .cpf("123456789012")
                            .build()
            );

            assertEquals("CPF must contain exactly 11 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when CPF has all same digits")
        void shouldThrowExceptionWhenCpfHasAllSameDigits() {
            // Arrange, Act & Assert
            InvalidCpfException exception = assertThrows(InvalidCpfException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email(VALID_EMAIL)
                            .cpf("11111111111")
                            .build()
            );

            assertEquals("Invalid CPF checksum", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when CPF checksum is invalid")
        void shouldThrowExceptionWhenCpfChecksumIsInvalid() {
            // Arrange, Act & Assert
            InvalidCpfException exception = assertThrows(InvalidCpfException.class, () ->
                    Customer.builder()
                            .id(UUID.randomUUID())
                            .name(VALID_NAME)
                            .email(VALID_EMAIL)
                            .cpf("12345678901")
                            .build()
            );

            assertEquals("Invalid CPF checksum", exception.getMessage());
        }

        @Test
        @DisplayName("Should accept valid CPF numbers")
        void shouldAcceptValidCpfNumbers() {
            // Arrange
            String[] validCpfs = {
                    "11144477735",  // Valid CPF
                    "52998224725",  // Valid CPF
                    "39053344705"   // Valid CPF
            };

            // Act & Assert
            for (String cpf : validCpfs) {
                assertDoesNotThrow(() ->
                        Customer.builder()
                                .id(UUID.randomUUID())
                                .name(VALID_NAME)
                                .email(VALID_EMAIL)
                                .cpf(cpf)
                                .build()
                );
            }
        }
    }

    @Nested
    @DisplayName("Customer Validation - ID Tests")
    class IdValidationTests {

        @Test
        @DisplayName("Should throw exception when ID is null")
        void shouldThrowExceptionWhenIdIsNull() {
            // Arrange, Act & Assert
            NullPointerException exception = assertThrows(NullPointerException.class, () ->
                    Customer.builder()
                            .id(null)
                            .name(VALID_NAME)
                            .email(VALID_EMAIL)
                            .cpf(VALID_CPF)
                            .build()
            );

            assertEquals("ID cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Customer Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when ID and CPF are the same")
        void shouldBeEqualWhenIdAndCpfAreTheSame() {
            // Arrange
            UUID id = UUID.randomUUID();
            Customer customer1 = Customer.builder()
                    .id(id)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            Customer customer2 = Customer.builder()
                    .id(id)
                    .name("Different Name")
                    .email("different@email.com")
                    .cpf(VALID_CPF)
                    .build();

            // Act & Assert
            assertEquals(customer1, customer2);
            assertEquals(customer1.hashCode(), customer2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ID is different")
        void shouldNotBeEqualWhenIdIsDifferent() {
            // Arrange
            Customer customer1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act & Assert
            assertNotEquals(customer1, customer2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act & Assert
            assertEquals(customer, customer);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act & Assert
            assertNotEquals(null, customer);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Arrange
            Customer customer = Customer.builder()
                    .id(UUID.randomUUID())
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act & Assert
            assertNotEquals(customer, "String");
        }
    }

    @Nested
    @DisplayName("Customer ToString Test")
    class ToStringTests {

        @Test
        @DisplayName("Should return string representation with all fields")
        void shouldReturnStringRepresentationWithAllFields() {
            // Arrange
            UUID id = UUID.randomUUID();
            Customer customer = Customer.builder()
                    .id(id)
                    .name(VALID_NAME)
                    .email(VALID_EMAIL)
                    .cpf(VALID_CPF)
                    .build();

            // Act
            String toString = customer.toString();

            // Assert
            assertNotNull(toString);
            assertTrue(toString.contains(id.toString()));
            assertTrue(toString.contains(VALID_NAME));
            assertTrue(toString.contains(VALID_EMAIL.toLowerCase()));
            assertTrue(toString.contains(VALID_CPF));
        }
    }
}

