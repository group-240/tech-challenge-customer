package com.fiap.techchallenge.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain Exception Tests")
class DomainExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Arrange
            String message = "Domain error occurred";

            // Act
            DomainException exception = new DomainException(message);

            // Assert
            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            // Arrange
            String message = "Domain error with cause";
            Throwable cause = new RuntimeException("Root cause");

            // Act
            DomainException exception = new DomainException(message, cause);

            // Assert
            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void shouldBeThrowable() {
            // Arrange
            String message = "Test error";

            // Act & Assert
            assertThrows(DomainException.class, () -> {
                throw new DomainException(message);
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveExceptionMessageWhenThrown() {
            // Arrange
            String message = "Custom domain error";

            // Act & Assert
            DomainException thrown = assertThrows(DomainException.class, () -> {
                throw new DomainException(message);
            });

            assertEquals(message, thrown.getMessage());
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Arrange & Act
            DomainException exception = new DomainException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should support exception chaining")
        void shouldSupportExceptionChaining() {
            // Arrange
            Throwable rootCause = new IllegalArgumentException("Root cause");
            Throwable intermediateCause = new DomainException("Intermediate", rootCause);

            // Act
            DomainException exception = new DomainException("Top level", intermediateCause);

            // Assert
            assertEquals("Top level", exception.getMessage());
            assertEquals(intermediateCause, exception.getCause());
            assertEquals(rootCause, exception.getCause().getCause());
        }

        @Test
        @DisplayName("Should work with try-catch blocks")
        void shouldWorkWithTryCatchBlocks() {
            // Arrange
            String expectedMessage = "Expected error";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new DomainException(expectedMessage);
            } catch (DomainException e) {
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
            String expectedMessage = "Runtime error";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new DomainException(expectedMessage);
            } catch (RuntimeException e) {
                exceptionCaught = true;
                assertTrue(e instanceof DomainException);
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }
    }
}

