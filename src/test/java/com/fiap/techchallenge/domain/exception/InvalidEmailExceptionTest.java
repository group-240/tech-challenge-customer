package com.fiap.techchallenge.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Invalid Email Exception Tests")
class InvalidEmailExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Arrange
            String message = "Invalid email format";

            // Act
            InvalidEmailException exception = new InvalidEmailException(message);

            // Assert
            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with specific email error")
        void shouldCreateExceptionWithSpecificEmailError() {
            // Arrange
            String email = "invalid-email";
            String message = "Invalid email format: " + email;

            // Act
            InvalidEmailException exception = new InvalidEmailException(message);

            // Assert
            assertEquals(message, exception.getMessage());
            assertTrue(exception.getMessage().contains(email));
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            // Arrange
            String message = "Invalid email";

            // Act & Assert
            assertThrows(InvalidEmailException.class, () -> {
                throw new InvalidEmailException(message);
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveExceptionMessageWhenThrown() {
            // Arrange
            String message = "Invalid email format: test@";

            // Act & Assert
            InvalidEmailException thrown = assertThrows(InvalidEmailException.class, () -> {
                throw new InvalidEmailException(message);
            });

            assertEquals(message, thrown.getMessage());
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Arrange & Act
            InvalidEmailException exception = new InvalidEmailException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Email Validation Messages Tests")
    class EmailValidationMessagesTests {

        @Test
        @DisplayName("Should handle missing @ symbol message")
        void shouldHandleMissingAtSymbolMessage() {
            // Arrange
            String message = "Invalid email format: test.example.com";

            // Act
            InvalidEmailException exception = new InvalidEmailException(message);

            // Assert
            assertEquals(message, exception.getMessage());
            assertTrue(exception.getMessage().contains("Invalid email format"));
        }

        @Test
        @DisplayName("Should handle missing domain message")
        void shouldHandleMissingDomainMessage() {
            // Arrange
            String message = "Invalid email format: test@";

            // Act
            InvalidEmailException exception = new InvalidEmailException(message);

            // Assert
            assertEquals(message, exception.getMessage());
            assertTrue(exception.getMessage().contains("test@"));
        }

        @Test
        @DisplayName("Should handle invalid format message")
        void shouldHandleInvalidFormatMessage() {
            // Arrange
            String message = "Invalid email format: @example.com";

            // Act
            InvalidEmailException exception = new InvalidEmailException(message);

            // Assert
            assertEquals(message, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Catch Block Tests")
    class CatchBlockTests {

        @Test
        @DisplayName("Should work with try-catch blocks")
        void shouldWorkWithTryCatchBlocks() {
            // Arrange
            String expectedMessage = "Email validation failed";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new InvalidEmailException(expectedMessage);
            } catch (InvalidEmailException e) {
                exceptionCaught = true;
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }

        @Test
        @DisplayName("Should be catchable as RuntimeException")
        void shouldBeCatchableAsRuntimeException() {
            // Arrange
            String expectedMessage = "Invalid email address";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new InvalidEmailException(expectedMessage);
            } catch (RuntimeException e) {
                exceptionCaught = true;
                assertTrue(e instanceof InvalidEmailException);
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }
    }
}

