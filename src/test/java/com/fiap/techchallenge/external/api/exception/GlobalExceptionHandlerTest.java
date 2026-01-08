package com.fiap.techchallenge.external.api.exception;

import com.fiap.techchallenge.domain.exception.DomainException;
import com.fiap.techchallenge.domain.exception.InvalidCpfException;
import com.fiap.techchallenge.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("Domain Exception Tests")
    class DomainExceptionTests {

        @Test
        @DisplayName("Should handle DomainException and return BAD_REQUEST")
        void shouldHandleDomainExceptionAndReturnBadRequest() {
            // Arrange
            String errorMessage = "Domain validation error";
            DomainException exception = new DomainException(errorMessage);

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleDomainException(exception);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertNotNull(body);
            assertEquals(errorMessage, body.get("error"));
            assertEquals(400, body.get("status"));
            assertNotNull(body.get("timestamp"));
        }

        @Test
        @DisplayName("Should include timestamp in error response")
        void shouldIncludeTimestampInErrorResponse() {
            // Arrange
            DomainException exception = new DomainException("Test error");

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleDomainException(exception);

            // Assert
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertTrue(body.containsKey("timestamp"));
        }

        @Test
        @DisplayName("Should handle DomainException with cause")
        void shouldHandleDomainExceptionWithCause() {
            // Arrange
            Throwable cause = new RuntimeException("Root cause");
            DomainException exception = new DomainException("Error with cause", cause);

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleDomainException(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertEquals("Error with cause", body.get("error"));
        }
    }

    @Nested
    @DisplayName("Invalid CPF Exception Tests")
    class InvalidCpfExceptionTests {

        @Test
        @DisplayName("Should handle InvalidCpfException and return BAD_REQUEST")
        void shouldHandleInvalidCpfExceptionAndReturnBadRequest() {
            // Arrange
            String errorMessage = "CPF cannot be null";
            InvalidCpfException exception = new InvalidCpfException(errorMessage);

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleInvalidCpfException(exception);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertNotNull(body);
            assertEquals(errorMessage, body.get("error"));
            assertEquals(400, body.get("status"));
            assertNotNull(body.get("timestamp"));
        }

        @Test
        @DisplayName("Should handle CPF validation error messages")
        void shouldHandleCpfValidationErrorMessages() {
            // Arrange
            String[] errorMessages = {
                    "CPF cannot be null",
                    "CPF must contain exactly 11 digits",
                    "Invalid CPF checksum"
            };

            for (String message : errorMessages) {
                InvalidCpfException exception = new InvalidCpfException(message);

                // Act
                ResponseEntity<Object> response = exceptionHandler.handleInvalidCpfException(exception);

                // Assert
                Map<String, Object> body = (Map<String, Object>) response.getBody();
                assertEquals(message, body.get("error"));
            }
        }
    }

    @Nested
    @DisplayName("Invalid Email Exception Tests")
    class InvalidEmailExceptionTests {

        @Test
        @DisplayName("Should handle InvalidEmailException and return BAD_REQUEST")
        void shouldHandleInvalidEmailExceptionAndReturnBadRequest() {
            // Arrange
            String errorMessage = "Invalid email format: test@";
            InvalidEmailException exception = new InvalidEmailException(errorMessage);

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleInvalidEmailException(exception);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertNotNull(body);
            assertEquals(errorMessage, body.get("error"));
            assertEquals(400, body.get("status"));
            assertNotNull(body.get("timestamp"));
        }

        @Test
        @DisplayName("Should handle email validation error messages")
        void shouldHandleEmailValidationErrorMessages() {
            // Arrange
            String errorMessage = "Invalid email format: invalid-email";
            InvalidEmailException exception = new InvalidEmailException(errorMessage);

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleInvalidEmailException(exception);

            // Assert
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertEquals(errorMessage, body.get("error"));
            assertTrue(body.get("error").toString().contains("Invalid email format"));
        }
    }

    @Nested
    @DisplayName("Response Structure Tests")
    class ResponseStructureTests {

        @Test
        @DisplayName("Should include all required fields in error response")
        void shouldIncludeAllRequiredFieldsInErrorResponse() {
            // Arrange
            DomainException exception = new DomainException("Test error");

            // Act
            ResponseEntity<Object> response = exceptionHandler.handleDomainException(exception);

            // Assert
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            assertTrue(body.containsKey("timestamp"));
            assertTrue(body.containsKey("status"));
            assertTrue(body.containsKey("error"));
        }

        @Test
        @DisplayName("Should have consistent response format across all exceptions")
        void shouldHaveConsistentResponseFormatAcrossAllExceptions() {
            // Arrange
            DomainException domainEx = new DomainException("Domain error");
            InvalidCpfException cpfEx = new InvalidCpfException("CPF error");
            InvalidEmailException emailEx = new InvalidEmailException("Email error");

            // Act
            ResponseEntity<Object> domainResponse = exceptionHandler.handleDomainException(domainEx);
            ResponseEntity<Object> cpfResponse = exceptionHandler.handleInvalidCpfException(cpfEx);
            ResponseEntity<Object> emailResponse = exceptionHandler.handleInvalidEmailException(emailEx);

            // Assert
            Map<String, Object> domainBody = (Map<String, Object>) domainResponse.getBody();
            Map<String, Object> cpfBody = (Map<String, Object>) cpfResponse.getBody();
            Map<String, Object> emailBody = (Map<String, Object>) emailResponse.getBody();

            // All responses should have the same structure
            assertEquals(domainBody.keySet(), cpfBody.keySet());
            assertEquals(cpfBody.keySet(), emailBody.keySet());
        }
    }
}

