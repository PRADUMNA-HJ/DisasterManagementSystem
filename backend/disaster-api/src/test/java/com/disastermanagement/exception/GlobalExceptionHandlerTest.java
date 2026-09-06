package com.disastermanagement.exception;

import com.disastermanagement.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 *
 * <p>Each handler method is invoked directly on the handler instance — no Spring context
 * is loaded, keeping these tests fast and focused on response shape and status codes.</p>
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    // ──────────────────────────────────────────────────────
    // ResourceNotFoundException  →  404
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("ResourceNotFoundException (404)")
    class ResourceNotFoundTests {

        @Test
        @DisplayName("Returns 404 with message from simple constructor")
        void simpleMessage() {
            ResourceNotFoundException ex = new ResourceNotFoundException("Shelter not found with id: 42");

            ResponseEntity<ApiResponse<Void>> response = handler.handleResourceNotFoundException(ex);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isSuccess());
            assertEquals("Shelter not found with id: 42", response.getBody().getMessage());
            assertNull(response.getBody().getData());
        }

        @Test
        @DisplayName("Returns 404 with message from three-arg constructor")
        void threeArgConstructor() {
            ResourceNotFoundException ex = new ResourceNotFoundException("User", "id", 7L);

            ResponseEntity<ApiResponse<Void>> response = handler.handleResourceNotFoundException(ex);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("User not found with id: '7'", response.getBody().getMessage());
        }
    }

    // ──────────────────────────────────────────────────────
    // NoResourceFoundException (Spring MVC)  →  404
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("NoResourceFoundException (404)")
    class NoResourceFoundTests {

        @Test
        @DisplayName("Returns 404 with resource path in message")
        void returnsPathInMessage() {
            NoResourceFoundException ex = new NoResourceFoundException(
                    org.springframework.http.HttpMethod.GET, "/api/v1/nonexistent");

            ResponseEntity<ApiResponse<Void>> response = handler.handleNoResourceFoundException(ex);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("/api/v1/nonexistent"));
        }
    }

    // ──────────────────────────────────────────────────────
    // DuplicateResourceException  →  409
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("DuplicateResourceException (409)")
    class DuplicateResourceTests {

        @Test
        @DisplayName("Returns 409 with duplicate message")
        void simpleMessage() {
            DuplicateResourceException ex = new DuplicateResourceException("User already exists with email: 'test@mail.com'");

            ResponseEntity<ApiResponse<Void>> response = handler.handleDuplicateResourceException(ex);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertEquals("User already exists with email: 'test@mail.com'", response.getBody().getMessage());
        }

        @Test
        @DisplayName("Returns 409 with three-arg constructor message")
        void threeArgConstructor() {
            DuplicateResourceException ex = new DuplicateResourceException("User", "email", "dup@mail.com");

            ResponseEntity<ApiResponse<Void>> response = handler.handleDuplicateResourceException(ex);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals("User already exists with email: 'dup@mail.com'", response.getBody().getMessage());
        }
    }

    // ──────────────────────────────────────────────────────
    // BadRequestException / IllegalArgumentException  →  400
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("BadRequestException & IllegalArgumentException (400)")
    class BadRequestTests {

        @Test
        @DisplayName("BadRequestException returns 400")
        void badRequest() {
            BadRequestException ex = new BadRequestException("Invalid severity value");

            ResponseEntity<ApiResponse<Void>> response = handler.handleBadRequestException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertEquals("Invalid severity value", response.getBody().getMessage());
        }

        @Test
        @DisplayName("IllegalArgumentException returns 400")
        void illegalArgument() {
            IllegalArgumentException ex = new IllegalArgumentException("Page index must not be negative");

            ResponseEntity<ApiResponse<Void>> response = handler.handleBadRequestException(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Page index must not be negative", response.getBody().getMessage());
        }
    }

    // ──────────────────────────────────────────────────────
    // MethodArgumentNotValidException (Bean Validation) → 400
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("MethodArgumentNotValidException (400)")
    class ValidationTests {

        @Test
        @DisplayName("Returns 400 with field-level error map in data")
        void validationErrors() throws NoSuchMethodException {
            // Build a BindingResult with two field errors
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
            bindingResult.addError(new FieldError("request", "title", "must not be blank"));
            bindingResult.addError(new FieldError("request", "location", "must not be blank"));

            MethodParameter methodParameter = new MethodParameter(
                    this.getClass().getDeclaredMethod("validationErrors"), -1);
            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

            ResponseEntity<ApiResponse<Map<String, String>>> response = handler.handleValidationExceptions(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertEquals("Validation failed", response.getBody().getMessage());

            Map<String, String> errors = response.getBody().getData();
            assertNotNull(errors);
            assertEquals(2, errors.size());
            assertEquals("must not be blank", errors.get("title"));
            assertEquals("must not be blank", errors.get("location"));
        }
    }

    // ──────────────────────────────────────────────────────
    // MissingServletRequestParameterException  →  400
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("MissingServletRequestParameterException (400)")
    class MissingParamTests {

        @Test
        @DisplayName("Returns 400 with parameter name and type in message")
        void missingParameter() {
            MissingServletRequestParameterException ex =
                    new MissingServletRequestParameterException("severity", "String");

            ResponseEntity<ApiResponse<Void>> response = handler.handleMissingParams(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("severity"));
            assertTrue(response.getBody().getMessage().contains("String"));
        }
    }

    // ──────────────────────────────────────────────────────
    // MethodArgumentTypeMismatchException  →  400
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("MethodArgumentTypeMismatchException (400)")
    class TypeMismatchTests {

        @Test
        @DisplayName("Returns 400 with parameter name and expected type")
        void typeMismatch() {
            MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                    "abc", Long.class, "id", null, new NumberFormatException("For input string: \"abc\""));

            ResponseEntity<ApiResponse<Void>> response = handler.handleTypeMismatch(ex);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("id"));
            assertTrue(response.getBody().getMessage().contains("Long"));
        }
    }

    // ──────────────────────────────────────────────────────
    // DataIntegrityViolationException  →  409
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("DataIntegrityViolationException (409)")
    class DataIntegrityTests {

        @Test
        @DisplayName("Returns 409 with generic safe message (no internals leaked)")
        void dataIntegrity() {
            DataIntegrityViolationException ex = new DataIntegrityViolationException(
                    "could not execute statement", new RuntimeException("Duplicate entry 'admin@test.com'"));

            ResponseEntity<ApiResponse<Void>> response = handler.handleDataIntegrityViolation(ex);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            // Verify message does NOT leak internal SQL details
            assertFalse(response.getBody().getMessage().contains("Duplicate entry"));
            assertTrue(response.getBody().getMessage().contains("data integrity constraint"));
        }
    }

    // ──────────────────────────────────────────────────────
    // BadCredentialsException / UnauthorizedException  →  401
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("Authentication failures (401)")
    class AuthenticationTests {

        @Test
        @DisplayName("BadCredentialsException returns 401")
        void badCredentials() {
            BadCredentialsException ex = new BadCredentialsException("Bad credentials");

            ResponseEntity<ApiResponse<Void>> response = handler.handleBadCredentials(ex);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertEquals("Bad credentials", response.getBody().getMessage());
        }

        @Test
        @DisplayName("UnauthorizedException returns 401")
        void unauthorized() {
            UnauthorizedException ex = new UnauthorizedException("Token expired");

            ResponseEntity<ApiResponse<Void>> response = handler.handleBadCredentials(ex);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Token expired", response.getBody().getMessage());
        }

        @Test
        @DisplayName("Null message falls back to 'Invalid credentials'")
        void nullMessageFallback() {
            BadCredentialsException ex = new BadCredentialsException(null);

            ResponseEntity<ApiResponse<Void>> response = handler.handleBadCredentials(ex);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Invalid credentials", response.getBody().getMessage());
        }
    }

    // ──────────────────────────────────────────────────────
    // AccessDeniedException  →  403
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("AccessDeniedException (403)")
    class AccessDeniedTests {

        @Test
        @DisplayName("Returns 403 with standard access denied message")
        void accessDenied() {
            AccessDeniedException ex = new AccessDeniedException("Access is denied");

            ResponseEntity<ApiResponse<Void>> response = handler.handleAccessDenied(ex);

            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("Access denied"));
        }
    }

    // ──────────────────────────────────────────────────────
    // HttpRequestMethodNotSupportedException  →  405
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("HttpRequestMethodNotSupportedException (405)")
    class MethodNotAllowedTests {

        @Test
        @DisplayName("Returns 405 with the unsupported HTTP method in message")
        void methodNotAllowed() {
            HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("DELETE");

            ResponseEntity<ApiResponse<Void>> response = handler.handleMethodNotAllowed(ex);

            assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("DELETE"));
        }
    }

    // ──────────────────────────────────────────────────────
    // Fallback Exception handler  →  500
    // ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("Fallback Exception handler (500)")
    class FallbackTests {

        @Test
        @DisplayName("Returns 500 with generic safe message for unexpected errors")
        void unexpectedException() {
            Exception ex = new NullPointerException("something broke internally");

            ResponseEntity<ApiResponse<Void>> response = handler.handleAllUncaughtExceptions(ex);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            // Verify internals are NOT leaked
            assertFalse(response.getBody().getMessage().contains("NullPointerException"));
            assertFalse(response.getBody().getMessage().contains("something broke"));
            assertTrue(response.getBody().getMessage().contains("unexpected internal server error"));
        }
    }

    // ──────────────────────────────────────────────────────
    // Cross-cutting: every response has a non-null timestamp
    // ──────────────────────────────────────────────────────
    @Test
    @DisplayName("All error responses include a non-null timestamp")
    void allResponsesHaveTimestamp() {
        ResponseEntity<ApiResponse<Void>> r1 = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("test"));
        ResponseEntity<ApiResponse<Void>> r2 = handler.handleAllUncaughtExceptions(
                new RuntimeException("boom"));

        assertNotNull(r1.getBody().getTimestamp(), "404 response should have timestamp");
        assertNotNull(r2.getBody().getTimestamp(), "500 response should have timestamp");
    }
}
