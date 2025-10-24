package com.bip.presentation.controllers;

import com.bip.application.dtos.AtualizarBeneficioDto;
import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.application.usecases.BeneficioUseCase;
import com.bip.presentation.utils.ErrorResponseBuilder;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("BeneficioController")
class BeneficioControllerTest {
    
    @Mock
    private BeneficioUseCase beneficioUseCase;
    
    @Mock
    private ErrorResponseBuilder errorResponseBuilder;
    
    @InjectMocks
    private BeneficioController controller;
    
    private BeneficioDto beneficioDtoValido;
    private CriarBeneficioDto criarBeneficioDtoValido;
    private AtualizarBeneficioDto atualizarBeneficioDtoValido;
    
    @BeforeEach
    void setUp() {
        beneficioDtoValido = new BeneficioDto(
            1L,
            "Benefício Teste",
            "Descrição teste",
            BigDecimal.valueOf(1000),
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        criarBeneficioDtoValido = new CriarBeneficioDto(
            "Novo Benefício",
            "Nova descrição",
            BigDecimal.valueOf(500)
        );
        
        atualizarBeneficioDtoValido = new AtualizarBeneficioDto(
            "Benefício Atualizado",
            "Descrição atualizada",
            BigDecimal.valueOf(800)
        );
        
        // Configurar mocks do ErrorResponseBuilder para retornar mapas corretos
        when(errorResponseBuilder.buildSuccessResponse()).thenReturn(
            Response.ok(Map.of("mensagem", "Operação realizada com sucesso")).build()
        );
        when(errorResponseBuilder.buildSuccessResponse(any())).thenAnswer(invocation -> {
            Object argument = invocation.getArgument(0);
            return Response.ok(argument).build();
        });
        when(errorResponseBuilder.buildBadRequestError(any(Exception.class))).thenReturn(
            Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("erro", "Requisição inválida"))
                .build()
        );
        when(errorResponseBuilder.buildNotFoundError(any(Exception.class))).thenReturn(
            Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("erro", "Recurso não encontrado"))
                .build()
        );
        when(errorResponseBuilder.buildInternalServerError(any(Exception.class))).thenReturn(
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("erro", "Erro interno do servidor"))
                .build()
        );
    }
    
    @Nested
    @DisplayName("GET /status")
    class GetStatusTests {
        
        @Test
        @DisplayName("Deve retornar status da API com sucesso")
        void shouldReturnApiStatus() {
            // Act
            Response response = controller.getStatus();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> status = (Map<String, Object>) response.getEntity();
            
            assertThat(status).containsKey("status");
            assertThat(status).containsKey("timestamp");
            assertThat(status).containsKey("message");
            assertThat(status).containsKey("version");
            assertThat(status).containsKey("endpoints");
            
            assertThat(status.get("status")).isEqualTo("API funcionando");
            assertThat(status.get("version")).isEqualTo("3.0.0-Clean-Architecture");
        }
    }
    
    @Nested
    @DisplayName("GET /beneficios")
    class ListarTodosTests {
        
        @Test
        @DisplayName("Deve listar todos os benefícios com sucesso")
        void shouldListAllBeneficios() {
            // Arrange
            List<BeneficioDto> beneficios = Arrays.asList(beneficioDtoValido);
            when(beneficioUseCase.listarTodos()).thenReturn(beneficios);
            
            // Act
            Response response = controller.listarTodos();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isEqualTo(beneficios);
            verify(beneficioUseCase).listarTodos();
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            when(beneficioUseCase.listarTodos()).thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.listarTodos();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).listarTodos();
        }
    }
    
    @Nested
    @DisplayName("GET /beneficios/ativos")
    class ListarAtivosTests {
        
        @Test
        @DisplayName("Deve listar benefícios ativos com sucesso")
        void shouldListActiveBeneficios() {
            // Arrange
            List<BeneficioDto> beneficios = Arrays.asList(beneficioDtoValido);
            when(beneficioUseCase.listarAtivos()).thenReturn(beneficios);
            
            // Act
            Response response = controller.listarAtivos();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isEqualTo(beneficios);
            verify(beneficioUseCase).listarAtivos();
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            when(beneficioUseCase.listarAtivos()).thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.listarAtivos();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).listarAtivos();
        }
    }
    
    @Nested
    @DisplayName("GET /beneficios/{id}")
    class BuscarPorIdTests {
        
        @Test
        @DisplayName("Deve buscar benefício por ID com sucesso")
        void shouldFindBeneficioById() {
            // Arrange
            Long id = 1L;
            when(beneficioUseCase.buscarPorId(id)).thenReturn(Optional.of(beneficioDtoValido));
            
            // Act
            Response response = controller.buscarPorId(id);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isEqualTo(beneficioDtoValido);
            verify(beneficioUseCase).buscarPorId(id);
        }
        
        @Test
        @DisplayName("Deve retornar 404 quando benefício não encontrado")
        void shouldReturn404WhenBeneficioNotFound() {
            // Arrange
            Long id = 999L;
            when(beneficioUseCase.buscarPorId(id)).thenReturn(Optional.empty());
            
            // Act
            Response response = controller.buscarPorId(id);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(404);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Benefício não encontrado");
            assertThat(erro.get("id")).isEqualTo(id);
            verify(beneficioUseCase).buscarPorId(id);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando ID é nulo")
        void shouldReturn400WhenIdIsNull() {
            // Act
            Response response = controller.buscarPorId(null);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("ID deve ser um número positivo");
            verify(beneficioUseCase, never()).buscarPorId(any());
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando ID é negativo")
        void shouldReturn400WhenIdIsNegative() {
            // Act
            Response response = controller.buscarPorId(-1L);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("ID deve ser um número positivo");
            verify(beneficioUseCase, never()).buscarPorId(any());
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            Long id = 1L;
            when(beneficioUseCase.buscarPorId(id)).thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.buscarPorId(id);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).buscarPorId(id);
        }
    }
    
    @Nested
    @DisplayName("POST /beneficios")
    class CriarTests {
        
        @Test
        @DisplayName("Deve criar benefício com sucesso")
        void shouldCreateBeneficio() {
            // Arrange
            when(beneficioUseCase.criar(criarBeneficioDtoValido)).thenReturn(beneficioDtoValido);
            
            // Act
            Response response = controller.criar(criarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(201);
            assertThat(response.getEntity()).isEqualTo(beneficioDtoValido);
            verify(beneficioUseCase).criar(criarBeneficioDtoValido);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando dados são inválidos")
        void shouldReturn400WhenDataIsInvalid() {
            // Arrange
            when(beneficioUseCase.criar(criarBeneficioDtoValido))
                .thenThrow(new IllegalArgumentException("Nome é obrigatório"));
            
            // Act
            Response response = controller.criar(criarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("detalhes")).isEqualTo("Nome é obrigatório");
            verify(beneficioUseCase).criar(criarBeneficioDtoValido);
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            when(beneficioUseCase.criar(criarBeneficioDtoValido))
                .thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.criar(criarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).criar(criarBeneficioDtoValido);
        }
    }
    
    @Nested
    @DisplayName("PUT /beneficios/{id}")
    class AtualizarTests {
        
        @Test
        @DisplayName("Deve atualizar benefício com sucesso")
        void shouldUpdateBeneficio() {
            // Arrange
            Long id = 1L;
            when(beneficioUseCase.atualizar(id, atualizarBeneficioDtoValido)).thenReturn(beneficioDtoValido);
            
            // Act
            Response response = controller.atualizar(id, atualizarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isEqualTo(beneficioDtoValido);
            verify(beneficioUseCase).atualizar(id, atualizarBeneficioDtoValido);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando ID é inválido")
        void shouldReturn400WhenIdIsInvalid() {
            // Act
            Response response = controller.atualizar(0L, atualizarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("ID deve ser um número positivo");
            verify(beneficioUseCase, never()).atualizar(any(), any());
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando dados são inválidos")
        void shouldReturn400WhenDataIsInvalid() {
            // Arrange
            Long id = 1L;
            when(beneficioUseCase.atualizar(id, atualizarBeneficioDtoValido))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));
            
            // Act
            Response response = controller.atualizar(id, atualizarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Dados inválidos");
            assertThat(erro.get("detalhes")).isEqualTo("Dados inválidos");
            verify(beneficioUseCase).atualizar(id, atualizarBeneficioDtoValido);
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            Long id = 1L;
            when(beneficioUseCase.atualizar(id, atualizarBeneficioDtoValido))
                .thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.atualizar(id, atualizarBeneficioDtoValido);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).atualizar(id, atualizarBeneficioDtoValido);
        }
    }
    
    @Nested
    @DisplayName("DELETE /beneficios/{id}")
    class RemoverTests {
        
        @Test
        @DisplayName("Deve remover benefício com sucesso")
        void shouldRemoveBeneficio() {
            // Arrange
            Long id = 1L;
            doNothing().when(beneficioUseCase).remover(id);
            
            // Act
            Response response = controller.remover(id);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> sucesso = (Map<String, Object>) response.getEntity();
            
            assertThat(sucesso.get("mensagem")).isEqualTo("Benefício removido com sucesso");
            assertThat(sucesso.get("id")).isEqualTo(id);
            verify(beneficioUseCase).remover(id);
        }
        
        @Test
        @DisplayName("Deve retornar 400 quando ID é inválido")
        void shouldReturn400WhenIdIsInvalid() {
            // Act
            Response response = controller.remover(-1L);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(400);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("ID deve ser um número positivo");
            verify(beneficioUseCase, never()).remover(any());
        }
        
        @Test
        @DisplayName("Deve retornar 404 quando benefício não encontrado")
        void shouldReturn404WhenBeneficioNotFound() {
            // Arrange
            Long id = 999L;
            doThrow(new IllegalArgumentException("Benefício não encontrado"))
                .when(beneficioUseCase).remover(id);
            
            // Act
            Response response = controller.remover(id);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(404);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Benefício não encontrado");
            assertThat(erro.get("detalhes")).isEqualTo("Benefício não encontrado");
            verify(beneficioUseCase).remover(id);
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            Long id = 1L;
            doThrow(new RuntimeException("Erro interno")).when(beneficioUseCase).remover(id);
            
            // Act
            Response response = controller.remover(id);
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).remover(id);
        }
    }
    
    @Nested
    @DisplayName("GET /beneficios/estatisticas")
    class EstatisticasTests {
        
        @Test
        @DisplayName("Deve retornar estatísticas com sucesso")
        void shouldReturnStatistics() {
            // Arrange
            long totalAtivos = 5L;
            BigDecimal somaValores = BigDecimal.valueOf(5000);
            
            when(beneficioUseCase.contarAtivos()).thenReturn(totalAtivos);
            when(beneficioUseCase.somarValoresAtivos()).thenReturn(somaValores);
            
            // Act
            Response response = controller.estatisticas();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> estatisticas = (Map<String, Object>) response.getEntity();
            
            assertThat(estatisticas.get("totalBeneficiosAtivos")).isEqualTo(totalAtivos);
            assertThat(estatisticas.get("somaTotalValores")).isEqualTo(somaValores);
            assertThat(estatisticas).containsKey("timestamp");
            
            verify(beneficioUseCase).contarAtivos();
            verify(beneficioUseCase).somarValoresAtivos();
        }
        
        @Test
        @DisplayName("Deve retornar erro 500 quando use case lança exceção")
        void shouldReturn500WhenUseCaseThrowsException() {
            // Arrange
            when(beneficioUseCase.contarAtivos()).thenThrow(new RuntimeException("Erro interno"));
            
            // Act
            Response response = controller.estatisticas();
            
            // Assert
            assertThat(response.getStatus()).isEqualTo(500);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> erro = (Map<String, Object>) response.getEntity();
            
            assertThat(erro.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(erro.get("detalhes")).isEqualTo("Erro interno");
            verify(beneficioUseCase).contarAtivos();
        }
    }
}