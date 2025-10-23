package com.bip.presentation.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {
    
    private GlobalExceptionHandler handler;
    
    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }
    
    @Nested
    @DisplayName("ConstraintViolationException handling")
    class ConstraintViolationExceptionTests {
        
        @Test
        @DisplayName("Deve tratar ConstraintViolationException com violações")
        void shouldHandleConstraintViolationExceptionWithViolations() {
            // Arrange
            ConstraintViolation<?> violation1 = createMockViolation("nome", "Nome é obrigatório");
            ConstraintViolation<?> violation2 = createMockViolation("valor", "Valor deve ser positivo");
            
            Set<ConstraintViolation<?>> violations = Set.of(violation1, violation2);
            ConstraintViolationException exception = new ConstraintViolationException(violations);
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("tipo")).isEqualTo("VALIDATION_ERROR");
            
            String detalhes = (String) erro.get("detalhes");
            assertThat(detalhes).contains("nome: Nome é obrigatório");
            assertThat(detalhes).contains("valor: Valor deve ser positivo");
        }
        
        @Test
        @DisplayName("Deve tratar ConstraintViolationException sem violações")
        void shouldHandleConstraintViolationExceptionWithoutViolations() {
            // Arrange
            Set<ConstraintViolation<?>> violations = Set.of();
            ConstraintViolationException exception = new ConstraintViolationException(violations);
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("tipo")).isEqualTo("VALIDATION_ERROR");
            assertThat(erro).doesNotContainKey("detalhes");
        }
        
        private ConstraintViolation<?> createMockViolation(String property, String message) {
            ConstraintViolation<?> violation = mock(ConstraintViolation.class);
            Path path = mock(Path.class);
            
            when(path.toString()).thenReturn(property);
            when(violation.getPropertyPath()).thenReturn(path);
            when(violation.getMessage()).thenReturn(message);
            
            return violation;
        }
    }
    
    @Nested
    @DisplayName("IllegalArgumentException handling")
    class IllegalArgumentExceptionTests {
        
        @Test
        @DisplayName("Deve tratar IllegalArgumentException")
        void shouldHandleIllegalArgumentException() {
            // Arrange
            IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("tipo")).isEqualTo("INVALID_ARGUMENT");
            assertThat(erro.get("detalhes")).isEqualTo("Argumento inválido");
        }
        
        @Test
        @DisplayName("Deve tratar IllegalArgumentException com mensagem nula")
        void shouldHandleIllegalArgumentExceptionWithNullMessage() {
            // Arrange
            IllegalArgumentException exception = new IllegalArgumentException((String) null);
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("tipo")).isEqualTo("INVALID_ARGUMENT");
            assertThat(erro.get("detalhes")).isNull();
        }
    }
    
    @Nested
    @DisplayName("IllegalStateException handling")
    class IllegalStateExceptionTests {
        
        @Test
        @DisplayName("Deve tratar IllegalStateException")
        void shouldHandleIllegalStateException() {
            // Arrange
            IllegalStateException exception = new IllegalStateException("Estado inválido");
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Operação não permitida");
            assertThat(erro.get("tipo")).isEqualTo("INVALID_STATE");
            assertThat(erro.get("detalhes")).isEqualTo("Estado inválido");
        }
        
        @Test
        @DisplayName("Deve tratar IllegalStateException com mensagem nula")
        void shouldHandleIllegalStateExceptionWithNullMessage() {
            // Arrange
            IllegalStateException exception = new IllegalStateException((String) null);
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Operação não permitida");
            assertThat(erro.get("tipo")).isEqualTo("INVALID_STATE");
            assertThat(erro.get("detalhes")).isNull();
        }
    }
    
    @Nested
    @DisplayName("Generic Exception handling")
    class GenericExceptionTests {
        
        @Test
        @DisplayName("Deve tratar exceção genérica")
        void shouldHandleGenericException() {
            // Arrange
            RuntimeException exception = new RuntimeException("Erro genérico");
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("tipo")).isEqualTo("INTERNAL_ERROR");
            assertThat(erro.get("detalhes")).isEqualTo("Erro genérico");
            assertThat(erro).containsKey("timestamp");
        }
        
        @Test
        @DisplayName("Deve tratar NullPointerException")
        void shouldHandleNullPointerException() {
            // Arrange
            NullPointerException exception = new NullPointerException("Valor nulo");
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("tipo")).isEqualTo("INTERNAL_ERROR");
            assertThat(erro.get("detalhes")).isEqualTo("Valor nulo");
            assertThat(erro).containsKey("timestamp");
        }
        
        @Test
        @DisplayName("Deve tratar exceção com mensagem nula")
        void shouldHandleExceptionWithNullMessage() {
            // Arrange
            RuntimeException exception = new RuntimeException((String) null);
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("tipo")).isEqualTo("INTERNAL_ERROR");
            assertThat(erro.get("detalhes")).isNull();
            assertThat(erro).containsKey("timestamp");
        }
        
        @Test
        @DisplayName("Deve tratar exceção customizada")
        void shouldHandleCustomException() {
            // Arrange
            CustomException exception = new CustomException("Erro customizado");
            
            // Act
            Response response = handler.toResponse(exception);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("tipo")).isEqualTo("INTERNAL_ERROR");
            assertThat(erro.get("detalhes")).isEqualTo("Erro customizado");
            assertThat(erro).containsKey("timestamp");
        }
    }
    
    // Classe de teste para exceção customizada
    private static class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }
}