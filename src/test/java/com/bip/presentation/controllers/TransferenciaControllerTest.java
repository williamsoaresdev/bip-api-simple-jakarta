package com.bip.presentation.controllers;

import com.bip.application.dtos.TransferenciaDto;
import com.bip.application.usecases.TransferenciaUseCase;
import com.bip.domain.valueobjects.Money;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransferenciaController")
class TransferenciaControllerTest {
    
    @Mock
    private TransferenciaUseCase transferenciaUseCase;
    
    @InjectMocks
    private TransferenciaController controller;
    
    private TransferenciaDto transferenciaDtoValida;
    
    @BeforeEach
    void setUp() {
        transferenciaDtoValida = new TransferenciaDto(
            1L,
            2L,
            BigDecimal.valueOf(100),
            "Transferência teste"
        );
    }
    
    @Nested
    @DisplayName("POST /transferencias")
    class ExecutarTransferenciaTests {
        
        @Test
        @DisplayName("Deve executar transferência com sucesso")
        void shouldExecuteTransferenceSuccessfully() {
            // Arrange
            doNothing().when(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
            
            // Act
            Response response = controller.executarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = (Map<String, Object>) response.getEntity();
            
            assertThat(resultado.get("sucesso")).isEqualTo(true);
            assertThat(resultado.get("mensagem")).isEqualTo("Transferência realizada com sucesso");
            assertThat(resultado.get("origem")).isEqualTo(transferenciaDtoValida.getBeneficioOrigemId());
            assertThat(resultado.get("destino")).isEqualTo(transferenciaDtoValida.getBeneficioDestinoId());
            assertThat(resultado.get("valor")).isEqualTo(transferenciaDtoValida.getValor());
            assertThat(resultado.get("descricao")).isEqualTo(transferenciaDtoValida.getDescricao());
            assertThat(resultado).containsKey("timestamp");
            
            verify(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando dados são inválidos")
        void shouldReturn400WhenDataIsInvalid() {
            // Arrange
            doThrow(new IllegalArgumentException("Benefícios não podem ser iguais"))
                .when(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
            
            // Act
            Response response = controller.executarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("detalhes")).isEqualTo("Benefícios não podem ser iguais");
            verify(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando operação é inválida")
        void shouldReturn400WhenOperationIsInvalid() {
            // Arrange
            doThrow(new IllegalStateException("Saldo insuficiente"))
                .when(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
            
            // Act
            Response response = controller.executarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Operação inválida");
            assertThat(erro.get("detalhes")).isEqualTo("Saldo insuficiente");
            verify(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            doThrow(new RuntimeException("Erro interno"))
                .when(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
            
            // Act
            Response response = controller.executarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(transferenciaUseCase).executarTransferencia(transferenciaDtoValida);
        }
    }
    
    @Nested
    @DisplayName("POST /transferencias/validar")
    class ValidarTransferenciaTests {
        
        @Test
        @DisplayName("Deve validar transferência como válida")
        void shouldValidateTransferenceAsValid() {
            // Arrange
            when(transferenciaUseCase.validarTransferencia(transferenciaDtoValida)).thenReturn(true);
            
            // Act
            Response response = controller.validarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = (Map<String, Object>) response.getEntity();
            
            assertThat(resultado.get("valida")).isEqualTo(true);
            assertThat(resultado.get("origem")).isEqualTo(transferenciaDtoValida.getBeneficioOrigemId());
            assertThat(resultado.get("destino")).isEqualTo(transferenciaDtoValida.getBeneficioDestinoId());
            assertThat(resultado.get("valor")).isEqualTo(transferenciaDtoValida.getValor());
            assertThat(resultado).doesNotContainKey("motivo");
            
            verify(transferenciaUseCase).validarTransferencia(transferenciaDtoValida);
        }
        
        @Test
        @DisplayName("Deve validar transferência como inválida")
        void shouldValidateTransferenceAsInvalid() {
            // Arrange
            when(transferenciaUseCase.validarTransferencia(transferenciaDtoValida)).thenReturn(false);
            
            // Act
            Response response = controller.validarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = (Map<String, Object>) response.getEntity();
            
            assertThat(resultado.get("valida")).isEqualTo(false);
            assertThat(resultado.get("origem")).isEqualTo(transferenciaDtoValida.getBeneficioOrigemId());
            assertThat(resultado.get("destino")).isEqualTo(transferenciaDtoValida.getBeneficioDestinoId());
            assertThat(resultado.get("valor")).isEqualTo(transferenciaDtoValida.getValor());
            assertThat(resultado.get("motivo")).isEqualTo("Transferência não é possível (saldo insuficiente, benefícios inativos ou IDs iguais)");
            
            verify(transferenciaUseCase).validarTransferencia(transferenciaDtoValida);
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            when(transferenciaUseCase.validarTransferencia(transferenciaDtoValida))
                .thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.validarTransferencia(transferenciaDtoValida);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro na validação");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(transferenciaUseCase).validarTransferencia(transferenciaDtoValida);
        }
    }
    
    @Nested
    @DisplayName("GET /transferencias/taxa")
    class CalcularTaxaTests {
        
        @Test
        @DisplayName("Deve calcular taxa com sucesso")
        void shouldCalculateTaxSuccessfully() {
            // Arrange
            BigDecimal valor = BigDecimal.valueOf(1000);
            Money taxa = new Money(BigDecimal.valueOf(50));
            
            when(transferenciaUseCase.calcularTaxa(valor)).thenReturn(taxa);
            
            // Act
            Response response = controller.calcularTaxa(valor);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> resultado = (Map<String, Object>) response.getEntity();
            
            assertThat(resultado.get("valorOriginal")).isEqualTo(valor);
            assertThat(resultado.get("taxa")).isEqualTo(taxa.getValor());
            assertThat(resultado.get("valorComTaxa")).isEqualTo(valor.add(taxa.getValor()));
            
            verify(transferenciaUseCase).calcularTaxa(valor);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando valor é nulo")
        void shouldReturn400WhenValueIsNull() {
            // Act
            Response response = controller.calcularTaxa(null);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Valor deve ser positivo");
            verify(transferenciaUseCase, never()).calcularTaxa(any());
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando valor é zero")
        void shouldReturn400WhenValueIsZero() {
            // Act
            Response response = controller.calcularTaxa(BigDecimal.ZERO);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Valor deve ser positivo");
            verify(transferenciaUseCase, never()).calcularTaxa(any());
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando valor é negativo")
        void shouldReturn400WhenValueIsNegative() {
            // Act
            Response response = controller.calcularTaxa(BigDecimal.valueOf(-100));
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Valor deve ser positivo");
            verify(transferenciaUseCase, never()).calcularTaxa(any());
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            BigDecimal valor = BigDecimal.valueOf(1000);
            when(transferenciaUseCase.calcularTaxa(valor)).thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.calcularTaxa(valor);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro no cálculo da taxa");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(transferenciaUseCase).calcularTaxa(valor);
        }
    }
}