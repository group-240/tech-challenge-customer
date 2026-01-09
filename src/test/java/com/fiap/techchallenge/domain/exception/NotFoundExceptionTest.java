package com.fiap.techchallenge.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Not Found Exception Tests")
class NotFoundExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            // Arrange
            String message = "Record not found";

            // Act
            NotFoundException exception = new NotFoundException(message);

            // Assert
            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            // Arrange
            String message = "Customer not found";
            Throwable cause = new RuntimeException("Database error");

            // Act
            NotFoundException exception = new NotFoundException(message, cause);

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
            String message = "Entity not found";

            // Act & Assert
            assertThrows(NotFoundException.class, () -> {
                throw new NotFoundException(message);
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveExceptionMessageWhenThrown() {
            // Arrange
            String message = "Customer with ID xyz not found";

            // Act & Assert
            NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
                throw new NotFoundException(message);
            });

            assertEquals(message, thrown.getMessage());
        }

        @Test
        @DisplayName("Should be instance of DomainException")
        void shouldBeInstanceOfDomainException() {
            // Arrange & Act
            NotFoundException exception = new NotFoundException("Test");

            // Assert
            assertTrue(exception instanceof DomainException);
        }

        @Test
        @DisplayName("Should be instance of RuntimeException")
        void shouldBeInstanceOfRuntimeException() {
            // Arrange & Act
            NotFoundException exception = new NotFoundException("Test");

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
            Throwable rootCause = new IllegalArgumentException("Invalid ID");
            Throwable intermediateCause = new NotFoundException("Customer not found", rootCause);

            // Act
            NotFoundException exception = new NotFoundException("Resource unavailable", intermediateCause);

            // Assert
            assertEquals("Resource unavailable", exception.getMessage());
            assertEquals(intermediateCause, exception.getCause());
            assertEquals(rootCause, exception.getCause().getCause());
        }

        @Test
        @DisplayName("Should inherit from DomainException properly")
        void shouldInheritFromDomainExceptionProperly() {
            // Arrange
            String message = "Not found error";

            // Act
            NotFoundException exception = new NotFoundException(message);

            // Assert
            assertTrue(exception instanceof DomainException);
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
            String expectedMessage = "Record not found";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new NotFoundException(expectedMessage);
            } catch (NotFoundException e) {
                exceptionCaught = true;
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }

        @Test
        @DisplayName("Should be catchable as DomainException")
        void shouldBeCatchableAsDomainException() {
            // Arrange
            String expectedMessage = "Customer not found";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new NotFoundException(expectedMessage);
            } catch (DomainException e) {
                exceptionCaught = true;
                assertTrue(e instanceof NotFoundException);
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }

        @Test
        @DisplayName("Should be catchable as RuntimeException")
        void shouldBeCatchableAsRuntimeException() {
            // Arrange
            String expectedMessage = "Entity not found";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new NotFoundException(expectedMessage);
            } catch (RuntimeException e) {
                exceptionCaught = true;
                assertTrue(e instanceof NotFoundException);
                assertEquals(expectedMessage, e.getMessage());
            }

            // Assert
            assertTrue(exceptionCaught);
        }

        @Test
        @DisplayName("Should handle exception with cause in catch block")
        void shouldHandleExceptionWithCauseInCatchBlock() {
            // Arrange
            Throwable cause = new RuntimeException("Database connection failed");
            String expectedMessage = "Customer not found";
            boolean exceptionCaught = false;

            // Act
            try {
                throw new NotFoundException(expectedMessage, cause);
            } catch (NotFoundException e) {
                exceptionCaught = true;
                assertEquals(expectedMessage, e.getMessage());
                assertEquals(cause, e.getCause());
            }

            // Assert
            assertTrue(exceptionCaught);
        }
    }
}

