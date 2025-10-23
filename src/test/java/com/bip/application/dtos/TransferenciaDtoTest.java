package com.bip.application.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para TransferenciaDto
 * Valida construtores, validações Bean Validation, getters, setters e toString
 */
@DisplayName("TransferenciaDto Tests")
class TransferenciaDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Nested
    @DisplayName("Construtores")
    class ConstrutoresTests {

        @Test
        @DisplayName("Deve criar DTO com construtor padrão")
        void deveCriarDtoComConstrutorPadrao() {
            // When
            TransferenciaDto dto = new TransferenciaDto();

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getBeneficioOrigemId()).isNull();
            assertThat(dto.getBeneficioDestinoId()).isNull();
            assertThat(dto.getValor()).isNull();
            assertThat(dto.getDescricao()).isNull();
        }

        @Test
        @DisplayName("Deve criar DTO com construtor completo")
        void deveCriarDtoComConstrutorCompleto() {
            // Given
            Long origemId = 1L;
            Long destinoId = 2L;
            BigDecimal valor = new BigDecimal("100.00");
            String descricao = "Transferência teste";

            // When
            TransferenciaDto dto = new TransferenciaDto(origemId, destinoId, valor, descricao);

            // Then
            assertThat(dto.getBeneficioOrigemId()).isEqualTo(origemId);
            assertThat(dto.getBeneficioDestinoId()).isEqualTo(destinoId);
            assertThat(dto.getValor()).isEqualTo(valor);
            assertThat(dto.getDescricao()).isEqualTo(descricao);
        }

        @Test
        @DisplayName("Deve criar DTO com valores nulos no construtor completo")
        void deveCriarDtoComValoresNulosNoConstrutorCompleto() {
            // When
            TransferenciaDto dto = new TransferenciaDto(null, null, null, null);

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getBeneficioOrigemId()).isNull();
            assertThat(dto.getBeneficioDestinoId()).isNull();
            assertThat(dto.getValor()).isNull();
            assertThat(dto.getDescricao()).isNull();
        }
    }

    @Nested
    @DisplayName("Validações Bean Validation")
    class ValidacoesBeanValidationTests {

        @Test
        @DisplayName("Deve passar na validação com dados válidos")
        void devePassarNaValidacaoComDadosValidos() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal("100.00"),
                "Transferência de benefício"
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar na validação com beneficio origem ID nulo")
        void deveFalharNaValidacaoComBeneficioOrigemIdNulo() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                null,
                2L,
                new BigDecimal("100.00"),
                "Transferência"
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID do benefício de origem é obrigatório");
        }

        @Test
        @DisplayName("Deve falhar na validação com beneficio origem ID zero ou negativo")
        void deveFalharNaValidacaoComBeneficioOrigemIdZeroOuNegativo() {
            // Given
            TransferenciaDto dto1 = new TransferenciaDto(0L, 2L, new BigDecimal("100.00"), "Transferência");
            TransferenciaDto dto2 = new TransferenciaDto(-1L, 2L, new BigDecimal("100.00"), "Transferência");

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations1 = validator.validate(dto1);
            Set<ConstraintViolation<TransferenciaDto>> violations2 = validator.validate(dto2);

            // Then
            assertThat(violations1).hasSize(1);
            assertThat(violations1.iterator().next().getMessage())
                .isEqualTo("ID do benefício de origem deve ser positivo");
            
            assertThat(violations2).hasSize(1);
            assertThat(violations2.iterator().next().getMessage())
                .isEqualTo("ID do benefício de origem deve ser positivo");
        }

        @Test
        @DisplayName("Deve falhar na validação com beneficio destino ID nulo")
        void deveFalharNaValidacaoComBeneficioDestinoIdNulo() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                null,
                new BigDecimal("100.00"),
                "Transferência"
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("ID do benefício de destino é obrigatório");
        }

        @Test
        @DisplayName("Deve falhar na validação com beneficio destino ID zero ou negativo")
        void deveFalharNaValidacaoComBeneficioDestinoIdZeroOuNegativo() {
            // Given
            TransferenciaDto dto1 = new TransferenciaDto(1L, 0L, new BigDecimal("100.00"), "Transferência");
            TransferenciaDto dto2 = new TransferenciaDto(1L, -1L, new BigDecimal("100.00"), "Transferência");

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations1 = validator.validate(dto1);
            Set<ConstraintViolation<TransferenciaDto>> violations2 = validator.validate(dto2);

            // Then
            assertThat(violations1).hasSize(1);
            assertThat(violations1.iterator().next().getMessage())
                .isEqualTo("ID do benefício de destino deve ser positivo");
            
            assertThat(violations2).hasSize(1);
            assertThat(violations2.iterator().next().getMessage())
                .isEqualTo("ID do benefício de destino deve ser positivo");
        }

        @Test
        @DisplayName("Deve falhar na validação com valor nulo")
        void deveFalharNaValidacaoComValorNulo() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                null,
                "Transferência"
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Valor da transferência é obrigatório");
        }

        @Test
        @DisplayName("Deve falhar na validação com valor zero ou negativo")
        void deveFalharNaValidacaoComValorZeroOuNegativo() {
            // Given
            TransferenciaDto dto1 = new TransferenciaDto(1L, 2L, BigDecimal.ZERO, "Transferência");
            TransferenciaDto dto2 = new TransferenciaDto(1L, 2L, new BigDecimal("-10.00"), "Transferência");

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations1 = validator.validate(dto1);
            Set<ConstraintViolation<TransferenciaDto>> violations2 = validator.validate(dto2);

            // Then
            assertThat(violations1).hasSize(1);
            assertThat(violations1.iterator().next().getMessage())
                .isEqualTo("Valor da transferência deve ser maior que zero");
            
            assertThat(violations2).hasSize(1);
            assertThat(violations2.iterator().next().getMessage())
                .isEqualTo("Valor da transferência deve ser maior que zero");
        }

        @Test
        @DisplayName("Deve falhar na validação com descrição muito longa")
        void deveFalharNaValidacaoComDescricaoMuitoLonga() {
            // Given
            String descricaoLonga = "A".repeat(501);
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal("100.00"),
                descricaoLonga
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Descrição não pode exceder 500 caracteres");
        }

        @ParameterizedTest
        @ValueSource(strings = {"0.01", "100.50", "999999.99"})
        @DisplayName("Deve passar na validação com valores válidos")
        void devePassarNaValidacaoComValoresValidos(String valor) {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal(valor),
                "Transferência válida"
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve validar com descrição nula")
        void deveValidarComDescricaoNula() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal("100.00"),
                null
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty(); // Descrição é opcional
        }

        @Test
        @DisplayName("Deve validar múltiplas violações simultaneamente")
        void deveValidarMultiplasViolacoesSimultaneamente() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                null,
                -1L,
                new BigDecimal("-100.00"),
                "A".repeat(501)
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(4);
            assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                    "ID do benefício de origem é obrigatório",
                    "ID do benefício de destino deve ser positivo",
                    "Valor da transferência deve ser maior que zero",
                    "Descrição não pode exceder 500 caracteres"
                );
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve definir e obter beneficio origem ID corretamente")
        void deveDefinirEObterBeneficioOrigemIdCorretamente() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();
            Long origemId = 42L;

            // When
            dto.setBeneficioOrigemId(origemId);

            // Then
            assertThat(dto.getBeneficioOrigemId()).isEqualTo(origemId);
        }

        @Test
        @DisplayName("Deve definir e obter beneficio destino ID corretamente")
        void deveDefinirEObterBeneficioDestinoIdCorretamente() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();
            Long destinoId = 84L;

            // When
            dto.setBeneficioDestinoId(destinoId);

            // Then
            assertThat(dto.getBeneficioDestinoId()).isEqualTo(destinoId);
        }

        @Test
        @DisplayName("Deve definir e obter valor corretamente")
        void deveDefinirEObterValorCorretamente() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();
            BigDecimal valor = new BigDecimal("250.75");

            // When
            dto.setValor(valor);

            // Then
            assertThat(dto.getValor()).isEqualTo(valor);
        }

        @Test
        @DisplayName("Deve definir e obter descrição corretamente")
        void deveDefinirEObterDescricaoCorretamente() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();
            String descricao = "Transferência entre benefícios";

            // When
            dto.setDescricao(descricao);

            // Then
            assertThat(dto.getDescricao()).isEqualTo(descricao);
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(1L, 2L, BigDecimal.TEN, "Desc");

            // When
            dto.setBeneficioOrigemId(null);
            dto.setBeneficioDestinoId(null);
            dto.setValor(null);
            dto.setDescricao(null);

            // Then
            assertThat(dto.getBeneficioOrigemId()).isNull();
            assertThat(dto.getBeneficioDestinoId()).isNull();
            assertThat(dto.getValor()).isNull();
            assertThat(dto.getDescricao()).isNull();
        }
    }

    @Nested
    @DisplayName("Serialização")
    class SerializacaoTests {

        @Test
        @DisplayName("Deve implementar Serializable")
        void deveImplementarSerializable() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();

            // Then
            assertThat(dto).isInstanceOf(Serializable.class);
        }

        @Test
        @DisplayName("Deve ter serialVersionUID definido")
        void deveTerSerialVersionUIDDefinido() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();

            // When & Then - Verifica se a classe é serializável
            assertThat(dto).isInstanceOf(Serializable.class);
            
            // Verifica se a classe tem o campo serialVersionUID através de reflexão
            try {
                var field = TransferenciaDto.class.getDeclaredField("serialVersionUID");
                field.setAccessible(true);
                long serialVersionUID = (Long) field.get(null);
                assertThat(serialVersionUID).isEqualTo(1L);
            } catch (Exception e) {
                throw new AssertionError("serialVersionUID deve estar definido", e);
            }
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve gerar toString com todos os campos")
        void deveGerarToStringComTodosCampos() {
            // Given
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal("150.00"),
                "Transferência mensal"
            );

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString)
                .contains("TransferenciaDto{")
                .contains("origem=1")
                .contains("destino=2")
                .contains("valor=150.00")
                .contains("}");
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Given
            TransferenciaDto dto = new TransferenciaDto();

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString)
                .contains("TransferenciaDto{")
                .contains("origem=null")
                .contains("destino=null")
                .contains("valor=null")
                .contains("}");
        }

        @ParameterizedTest
        @MethodSource("provideDtoVariants")
        @DisplayName("Deve gerar toString para diferentes variações de dados")
        void deveGerarToStringParaDiferentesVariacoesDeDados(TransferenciaDto dto, String expectedPart) {
            // When
            String toString = dto.toString();

            // Then
            assertThat(toString).contains(expectedPart);
        }

        static Stream<Arguments> provideDtoVariants() {
            return Stream.of(
                Arguments.of(
                    new TransferenciaDto(999L, 888L, new BigDecimal("0.01"), "Desc"),
                    "origem=999"
                ),
                Arguments.of(
                    new TransferenciaDto(1L, 2L, BigDecimal.ZERO, ""),
                    "destino=2"
                ),
                Arguments.of(
                    new TransferenciaDto(10L, 20L, new BigDecimal("999999.99"), null),
                    "valor=999999.99"
                )
            );
        }
    }

    @Nested
    @DisplayName("Casos Extremos")
    class CasosExtremosTests {

        @Test
        @DisplayName("Deve lidar com IDs muito grandes")
        void deveLidarComIdsMuitoGrandes() {
            // Given
            Long idGrande = Long.MAX_VALUE;
            TransferenciaDto dto = new TransferenciaDto();

            // When
            dto.setBeneficioOrigemId(idGrande);
            dto.setBeneficioDestinoId(idGrande - 1);

            // Then
            assertThat(dto.getBeneficioOrigemId()).isEqualTo(idGrande);
            assertThat(dto.getBeneficioDestinoId()).isEqualTo(idGrande - 1);
        }

        @Test
        @DisplayName("Deve lidar com valores muito grandes")
        void deveLidarComValoresMuitoGrandes() {
            // Given
            BigDecimal valorGrande = new BigDecimal("999999999999999999.99");
            TransferenciaDto dto = new TransferenciaDto();

            // When
            dto.setValor(valorGrande);

            // Then
            assertThat(dto.getValor()).isEqualTo(valorGrande);
        }

        @Test
        @DisplayName("Deve lidar com descrição no limite")
        void deveLidarComDescricaoNoLimite() {
            // Given
            String descricaoLimite = "A".repeat(500); // Exatamente 500 caracteres
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal("100.00"),
                descricaoLimite
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
            assertThat(dto.getDescricao()).hasSize(500);
        }

        @Test
        @DisplayName("Deve validar caracteres especiais na descrição")
        void deveValidarCaracteresEspeciaisNaDescricao() {
            // Given
            String descricaoEspecial = "Transferência com çãrãctéres €$peci@is 中文 🎉";
            TransferenciaDto dto = new TransferenciaDto(
                1L,
                2L,
                new BigDecimal("100.00"),
                descricaoEspecial
            );

            // When
            Set<ConstraintViolation<TransferenciaDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
            assertThat(dto.getDescricao()).isEqualTo(descricaoEspecial);
        }
    }
}