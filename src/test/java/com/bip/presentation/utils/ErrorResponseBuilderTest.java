package com.bip.presentation.utils;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ErrorResponseBuilder")
class ErrorResponseBuilderTest {

    private ErrorResponseBuilder errorResponseBuilder;

    @BeforeEach
    void setUp() {
        errorResponseBuilder = new ErrorResponseBuilder();
    }

    @Nested
    @DisplayName("buildInternalServerError")
    class BuildInternalServerErrorTests {

        @Test
        @DisplayName("Deve criar resposta de erro interno 500 com mensagem da exceção")
        void shouldBuildInternalServerErrorWithExceptionMessage() {
            // Arrange
            Exception exception = new RuntimeException("Erro de banco de dados");

            // Act
            Response response = errorResponseBuilder.buildInternalServerError(exception);

            // Assert
            assertThat(response.getStatus()).isEqualTo(500);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity).containsKey("erro");
            assertThat(entity).containsKey("detalhes");
            assertThat(entity.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(entity.get("detalhes")).isEqualTo("Erro de banco de dados");
        }

        @Test
        @DisplayName("Deve criar resposta de erro interno 500 com mensagem nula")
        void shouldBuildInternalServerErrorWithNullMessage() {
            // Arrange
            Exception exception = new RuntimeException((String) null);

            // Act
            Response response = errorResponseBuilder.buildInternalServerError(exception);

            // Assert
            assertThat(response.getStatus()).isEqualTo(500);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity.get("erro")).isEqualTo("Erro interno do servidor");
            assertThat(entity.get("detalhes")).isEqualTo("Detalhes não disponíveis");
        }
    }

    @Nested
    @DisplayName("buildBadRequestError")
    class BuildBadRequestErrorTests {

        @Test
        @DisplayName("Deve criar resposta de erro 400 com mensagem da exceção")
        void shouldBuildBadRequestErrorWithExceptionMessage() {
            // Arrange
            Exception exception = new IllegalArgumentException("Campo obrigatório não informado");

            // Act
            Response response = errorResponseBuilder.buildBadRequestError(exception);

            // Assert
            assertThat(response.getStatus()).isEqualTo(400);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity).containsKey("erro");
            assertThat(entity).containsKey("detalhes");
            assertThat(entity.get("erro")).isEqualTo("Requisição inválida");
            assertThat(entity.get("detalhes")).isEqualTo("Campo obrigatório não informado");
        }

        @Test
        @DisplayName("Deve criar resposta de erro 400 com mensagem vazia")
        void shouldBuildBadRequestErrorWithEmptyMessage() {
            // Arrange
            Exception exception = new IllegalArgumentException("");

            // Act
            Response response = errorResponseBuilder.buildBadRequestError(exception);

            // Assert
            assertThat(response.getStatus()).isEqualTo(400);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity.get("erro")).isEqualTo("Requisição inválida");
            assertThat(entity.get("detalhes")).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("buildNotFoundError")
    class BuildNotFoundErrorTests {

        @Test
        @DisplayName("Deve criar resposta de erro 404 com mensagem string")
        void shouldBuildNotFoundErrorWithStringMessage() {
            // Arrange
            String message = "Usuário não encontrado";

            // Act
            Response response = errorResponseBuilder.buildNotFoundError(message);

            // Assert
            assertThat(response.getStatus()).isEqualTo(404);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity).containsKey("erro");
            assertThat(entity).containsKey("detalhes");
            assertThat(entity.get("erro")).isEqualTo("Recurso não encontrado");
            assertThat(entity.get("detalhes")).isEqualTo("Usuário não encontrado");
        }

        @Test
        @DisplayName("Deve criar resposta de erro 404 com exceção")
        void shouldBuildNotFoundErrorWithException() {
            // Arrange
            Exception exception = new IllegalArgumentException("Produto não encontrado");

            // Act
            Response response = errorResponseBuilder.buildNotFoundError(exception);

            // Assert
            assertThat(response.getStatus()).isEqualTo(404);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity.get("erro")).isEqualTo("Recurso não encontrado");
            assertThat(entity.get("detalhes")).isEqualTo("Produto não encontrado");
        }

        @Test
        @DisplayName("Deve criar resposta de erro 404 com mensagem nula")
        void shouldBuildNotFoundErrorWithNullMessage() {
            // Act
            Response response = errorResponseBuilder.buildNotFoundError((String) null);

            // Assert
            assertThat(response.getStatus()).isEqualTo(404);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity.get("erro")).isEqualTo("Recurso não encontrado");
            assertThat(entity.get("detalhes")).isEqualTo("Detalhes não disponíveis");
        }
    }

    @Nested
    @DisplayName("buildErrorResponse")
    class BuildErrorResponseTests {

        @Test
        @DisplayName("Deve criar resposta de erro personalizada")
        void shouldBuildCustomErrorResponse() {
            // Arrange
            Response.Status status = Response.Status.CONFLICT;
            String errorMessage = "Conflito de dados";
            String details = "Email já existe no sistema";

            // Act
            Response response = errorResponseBuilder.buildErrorResponse(status, errorMessage, details);

            // Assert
            assertThat(response.getStatus()).isEqualTo(409);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity).containsKey("erro");
            assertThat(entity).containsKey("detalhes");
            assertThat(entity.get("erro")).isEqualTo("Conflito de dados");
            assertThat(entity.get("detalhes")).isEqualTo("Email já existe no sistema");
        }

        @Test
        @DisplayName("Deve criar resposta de erro com parâmetros nulos")
        void shouldBuildErrorResponseWithNullParameters() {
            // Act
            Response response = errorResponseBuilder.buildErrorResponse(
                Response.Status.BAD_REQUEST, null, null);

            // Assert
            assertThat(response.getStatus()).isEqualTo(400);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity).containsKey("erro");
            assertThat(entity).containsKey("detalhes");
            assertThat(entity.get("erro")).isEqualTo("Erro não especificado");
            assertThat(entity.get("detalhes")).isEqualTo("Detalhes não disponíveis");
        }
    }

    @Nested
    @DisplayName("buildSuccessResponse")
    class BuildSuccessResponseTests {

        @Test
        @DisplayName("Deve criar resposta de sucesso sem entidade")
        void shouldBuildSuccessResponseWithoutEntity() {
            // Act
            Response response = errorResponseBuilder.buildSuccessResponse();

            // Assert
            assertThat(response.getStatus()).isEqualTo(200);

            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            assertThat(entity).containsKey("mensagem");
            assertThat(entity.get("mensagem")).isEqualTo("Operação realizada com sucesso");
        }

        @Test
        @DisplayName("Deve criar resposta de sucesso com entidade")
        void shouldBuildSuccessResponseWithEntity() {
            // Arrange
            String testEntity = "Dados de teste";

            // Act
            Response response = errorResponseBuilder.buildSuccessResponse(testEntity);

            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isEqualTo(testEntity);
        }

        @Test
        @DisplayName("Deve criar resposta de sucesso com entidade nula")
        void shouldBuildSuccessResponseWithNullEntity() {
            // Act
            Response response = errorResponseBuilder.buildSuccessResponse(null);

            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isNull();
        }

        @Test
        @DisplayName("Deve criar resposta de sucesso com Map como entidade")
        void shouldBuildSuccessResponseWithMapEntity() {
            // Arrange
            Map<String, Object> testMap = Map.of(
                "id", 1L,
                "nome", "Teste",
                "ativo", true
            );

            // Act
            Response response = errorResponseBuilder.buildSuccessResponse(testMap);

            // Assert
            assertThat(response.getStatus()).isEqualTo(200);
            assertThat(response.getEntity()).isEqualTo(testMap);
        }
    }

    @Nested
    @DisplayName("Threading Safety")
    class ThreadingSafetyTests {

        @Test
        @DisplayName("Deve ser thread-safe usando ConcurrentHashMap")
        void shouldBeThreadSafeUsingConcurrentHashMap() {
            // Arrange
            Exception exception = new RuntimeException("Teste concorrência");

            // Act
            Response response = errorResponseBuilder.buildInternalServerError(exception);

            // Assert
            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            // Verifica se é uma implementação thread-safe
            assertThat(entity).isInstanceOf(java.util.concurrent.ConcurrentHashMap.class);
        }

        @Test
        @DisplayName("Deve ser thread-safe em buildSuccessResponse")
        void shouldBeThreadSafeInBuildSuccessResponse() {
            // Act
            Response response = errorResponseBuilder.buildSuccessResponse();

            // Assert
            @SuppressWarnings("unchecked")
            Map<String, Object> entity = (Map<String, Object>) response.getEntity();

            // Verifica se é uma implementação thread-safe
            assertThat(entity).isInstanceOf(java.util.concurrent.ConcurrentHashMap.class);
        }
    }
}