package com.bip.application.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AtualizarBeneficioDto Tests")
class AtualizarBeneficioDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar com construtor padrão")
        void deveCriarComConstrutorPadrao() {
            // Act
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto();

            // Assert
            assertThat(dto).isNotNull();
            assertThat(dto.getNome()).isNull();
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getValorInicial()).isNull();
        }

        @Test
        @DisplayName("Deve criar com construtor completo")
        void deveCriarComConstrutorCompleto() {
            // Arrange
            String nome = "Vale Alimentação";
            String descricao = "Benefício de alimentação";
            BigDecimal valor = new BigDecimal("500.00");

            // Act
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(nome, descricao, valor);

            // Assert
            assertThat(dto).isNotNull();
            assertThat(dto.getNome()).isEqualTo(nome);
            assertThat(dto.getDescricao()).isEqualTo(descricao);
            assertThat(dto.getValorInicial()).isEqualTo(valor);
        }
    }

    @Nested
    @DisplayName("Getters and Setters Tests")
    class GettersAndSettersTests {

        @Test
        @DisplayName("Deve definir e obter nome")
        void deveDefinirEObterNome() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto();
            String nome = "Vale Refeição";

            // Act
            dto.setNome(nome);

            // Assert
            assertThat(dto.getNome()).isEqualTo(nome);
        }

        @Test
        @DisplayName("Deve definir e obter descrição")
        void deveDefinirEObterDescricao() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto();
            String descricao = "Benefício de refeição para funcionários";

            // Act
            dto.setDescricao(descricao);

            // Assert
            assertThat(dto.getDescricao()).isEqualTo(descricao);
        }

        @Test
        @DisplayName("Deve definir e obter valor inicial")
        void deveDefinirEObterValorInicial() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto();
            BigDecimal valor = new BigDecimal("350.00");

            // Act
            dto.setValorInicial(valor);

            // Assert
            assertThat(dto.getValorInicial()).isEqualTo(valor);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Deve ser válido com dados corretos")
        void deveSerValidoComDadosCorretos() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                "Vale Alimentação",
                "Benefício de alimentação",
                new BigDecimal("400.00")
            );

            // Act
            Set<ConstraintViolation<AtualizarBeneficioDto>> violations = validator.validate(dto);

            // Assert
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve ser inválido com nome nulo")
        void deveSerInvalidoComNomeNulo() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                null,
                "Descrição válida",
                new BigDecimal("300.00")
            );

            // Act
            Set<ConstraintViolation<AtualizarBeneficioDto>> violations = validator.validate(dto);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Nome é obrigatório");
        }

        @Test
        @DisplayName("Deve ser inválido com nome vazio")
        void deveSerInvalidoComNomeVazio() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                "",
                "Descrição válida",
                new BigDecimal("300.00")
            );

            // Act
            Set<ConstraintViolation<AtualizarBeneficioDto>> violations = validator.validate(dto);

            // Assert
            assertThat(violations).hasSize(2);
            assertThat(violations.stream().map(ConstraintViolation::getMessage))
                .containsExactlyInAnyOrder(
                    "Nome é obrigatório",
                    "Nome deve ter entre 3 e 100 caracteres"
                );
        }

        @Test
        @DisplayName("Deve ser inválido com valor inicial negativo")
        void deveSerInvalidoComValorInicialNegativo() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                "Vale Alimentação",
                "Descrição válida",
                new BigDecimal("-100.00")
            );

            // Act
            Set<ConstraintViolation<AtualizarBeneficioDto>> violations = validator.validate(dto);

            // Assert
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Valor inicial deve ser maior que zero");
        }

        @Test
        @DisplayName("Deve ser válido com valor inicial zero para atualização")
        void deveSerValidoComValorInicialZero() {
            // Arrange - Para atualização, zero não deveria ser permitido segundo a regra de negócio
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                "Vale Alimentação",
                "Descrição válida",
                BigDecimal.ZERO
            );

            // Act
            Set<ConstraintViolation<AtualizarBeneficioDto>> violations = validator.validate(dto);

            // Assert - Zero deve ser inválido (deve ser maior que zero)
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Valor inicial deve ser maior que zero");
        }

        @Test
        @DisplayName("Deve ser válido com valor inicial nulo")
        void deveSerValidoComValorInicialNulo() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                "Vale Alimentação",
                "Descrição válida",
                null
            );

            // Act
            Set<ConstraintViolation<AtualizarBeneficioDto>> violations = validator.validate(dto);

            // Assert
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Deve gerar toString corretamente")
        void deveGerarToStringCorretamente() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto(
                "Vale Alimentação",
                "Benefício de alimentação",
                new BigDecimal("500.00")
            );

            // Act
            String toString = dto.toString();

            // Assert
            assertThat(toString).isNotNull();
            assertThat(toString).contains("AtualizarBeneficioDto");
            assertThat(toString).contains("Vale Alimentação");
            assertThat(toString).contains("Benefício de alimentação");
            assertThat(toString).contains("500.00");
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Arrange
            AtualizarBeneficioDto dto = new AtualizarBeneficioDto();

            // Act
            String toString = dto.toString();

            // Assert
            assertThat(toString).isNotNull();
            assertThat(toString).contains("AtualizarBeneficioDto");
        }
    }
}