package com.fiap.techchallenge.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Invalid CPF Exception Tests")
class InvalidCpfExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Arrange
            String message = "CPF cannot be null";

            // Act
            InvalidCpfException exception = new InvalidCpfException(message);

            // Assert
            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with CPF validation message")
        void shouldCreateExceptionWithCpfValidationMessage() {
            // Arrange
            String message = "CPF must contain exactly 11 digits";

            // Act
            InvalidCpfException exception = new InvalidCpfException(message);

            // Assert
            assertEquals(message, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            // Arrange
            String message = "Invalid CPF";

            // Act & Assert
            assertThrows(InvalidCpfException.class, () -> {
                throw new InvalidCpfException(message);
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveExceptionMessageWhenThrown() {
            // Arrange
            String message = "Invalid CPF checksum";

            // Act & Assert
            InvalidCpfException thrown = assertThrows(InvalidCpfException.class, () -> {
                throw new InvalidCpfException(message);
            });

            assertEquals(message, thrown.getMessage());
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Arrange & Act
            InvalidCpfException exception = new InvalidCpfException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("CPF Validation Messages Tests")
    class CpfValidationMessagesTests {

        @Test
        @DisplayName("Should handle null CPF message")
        void shouldHandleNullCpfMessage() {
            // Arrange
            String message = "CPF cannot be null";

            // Act
            InvalidCpfException exception = new InvalidCpfException(message);

            // Assert
            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("Should handle invalid length message")
        void shouldHandleInvalidLengthMessage() {
            // Arrange
            String message = "CPF must contain exactly 11 digits";

            // Act
            InvalidCpfException exception = new InvalidCpfException(message);

            // Assert
            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("Should handle invalid checksum message")
        void shouldHandleInvalidChecksumMessage() {
            // Arrange
            String message = "Invalid CPF checksum";

            // Act
            InvalidCpfException exception = new InvalidCpfException(message);

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
            String expectedMessage = "CPF validation failed";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new InvalidCpfException(expectedMessage);
            } catch (InvalidCpfException e) {
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
            String expectedMessage = "Invalid CPF format";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new InvalidCpfException(expectedMessage);
            } catch (RuntimeException e) {
                exceptionCaught = true;
                assertTrue(e instanceof InvalidCpfException);
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }
    }
}

