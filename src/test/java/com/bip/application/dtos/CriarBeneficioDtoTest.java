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
 * Testes para CriarBeneficioDto
 * Valida construtores, validações Bean Validation, getters, setters e toString
 */
@DisplayName("CriarBeneficioDto Tests")
class CriarBeneficioDtoTest {

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
            CriarBeneficioDto dto = new CriarBeneficioDto();

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getNome()).isNull();
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getValorInicial()).isNull();
        }

        @Test
        @DisplayName("Deve criar DTO com construtor completo")
        void deveCriarDtoComConstrutorCompleto() {
            // Given
            String nome = "Benefício VR";
            String descricao = "Voucher Refeição mensal";
            BigDecimal valorInicial = new BigDecimal("500.00");

            // When
            CriarBeneficioDto dto = new CriarBeneficioDto(nome, descricao, valorInicial);

            // Then
            assertThat(dto.getNome()).isEqualTo(nome);
            assertThat(dto.getDescricao()).isEqualTo(descricao);
            assertThat(dto.getValorInicial()).isEqualTo(valorInicial);
        }

        @Test
        @DisplayName("Deve criar DTO com valores nulos no construtor completo")
        void deveCriarDtoComValoresNulosNoConstrutorCompleto() {
            // When
            CriarBeneficioDto dto = new CriarBeneficioDto(null, null, null);

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getNome()).isNull();
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getValorInicial()).isNull();
        }
    }

    @Nested
    @DisplayName("Validações Bean Validation")
    class ValidacoesBeanValidationTests {

        @Test
        @DisplayName("Deve passar na validação com dados válidos")
        void devePassarNaValidacaoComDadosValidos() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Benefício VR",
                "Voucher Refeição mensal",
                new BigDecimal("500.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar na validação com nome nulo")
        void deveFalharNaValidacaoComNomeNulo() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                null,
                "Descrição válida",
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Nome é obrigatório");
        }

        @Test
        @DisplayName("Deve falhar na validação com nome vazio")
        void deveFalharNaValidacaoComNomeVazio() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "",
                "Descrição válida",
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(2); // @NotBlank e @Size
            assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsAnyOf("Nome é obrigatório", "Nome deve ter entre 3 e 100 caracteres");
        }

        @Test
        @DisplayName("Deve falhar na validação com nome muito curto")
        void deveFalharNaValidacaoComNomeMuitoCurto() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "AB",
                "Descrição válida",
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Nome deve ter entre 3 e 100 caracteres");
        }

        @Test
        @DisplayName("Deve falhar na validação com nome muito longo")
        void deveFalharNaValidacaoComNomeMuitoLongo() {
            // Given
            String nomeLongo = "A".repeat(101);
            CriarBeneficioDto dto = new CriarBeneficioDto(
                nomeLongo,
                "Descrição válida",
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Nome deve ter entre 3 e 100 caracteres");
        }

        @Test
        @DisplayName("Deve falhar na validação com descrição muito longa")
        void deveFalharNaValidacaoComDescricaoMuitoLonga() {
            // Given
            String descricaoLonga = "A".repeat(501);
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Nome Válido",
                descricaoLonga,
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Descrição não pode exceder 500 caracteres");
        }

        @Test
        @DisplayName("Deve falhar na validação com valor inicial nulo")
        void deveFalharNaValidacaoComValorInicialNulo() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Nome Válido",
                "Descrição válida",
                null
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Valor inicial é obrigatório");
        }

        @Test
        @DisplayName("Deve falhar na validação com valor inicial negativo")
        void deveFalharNaValidacaoComValorInicialNegativo() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Nome Válido",
                "Descrição válida",
                new BigDecimal("-10.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Valor inicial deve ser positivo ou zero");
        }

        @ParameterizedTest
        @ValueSource(strings = {"0", "0.00", "100.50", "999999.99"})
        @DisplayName("Deve passar na validação com valores iniciais válidos")
        void devePassarNaValidacaoComValoresIniciaisValidos(String valor) {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Nome Válido",
                "Descrição válida",
                new BigDecimal(valor)
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve validar com descrição nula")
        void deveValidarComDescricaoNula() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Nome Válido",
                null,
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty(); // Descrição é opcional
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve definir e obter nome corretamente")
        void deveDefinirEObterNomeCorretamente() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto();
            String nome = "Benefício VT";

            // When
            dto.setNome(nome);

            // Then
            assertThat(dto.getNome()).isEqualTo(nome);
        }

        @Test
        @DisplayName("Deve definir e obter descrição corretamente")
        void deveDefinirEObterDescricaoCorretamente() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto();
            String descricao = "Voucher Transporte";

            // When
            dto.setDescricao(descricao);

            // Then
            assertThat(dto.getDescricao()).isEqualTo(descricao);
        }

        @Test
        @DisplayName("Deve definir e obter valor inicial corretamente")
        void deveDefinirEObterValorInicialCorretamente() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto();
            BigDecimal valorInicial = new BigDecimal("250.75");

            // When
            dto.setValorInicial(valorInicial);

            // Then
            assertThat(dto.getValorInicial()).isEqualTo(valorInicial);
        }

        @Test
        @DisplayName("Deve aceitar valores nulos nos setters")
        void deveAceitarValoresNulosNosSetters() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto("Nome", "Desc", BigDecimal.TEN);

            // When
            dto.setNome(null);
            dto.setDescricao(null);
            dto.setValorInicial(null);

            // Then
            assertThat(dto.getNome()).isNull();
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getValorInicial()).isNull();
        }
    }

    @Nested
    @DisplayName("Serialização")
    class SerializacaoTests {

        @Test
        @DisplayName("Deve implementar Serializable")
        void deveImplementarSerializable() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto();

            // Then
            assertThat(dto).isInstanceOf(Serializable.class);
        }

        @Test
        @DisplayName("Deve ter serialVersionUID definido")
        void deveTerSerialVersionUIDDefinido() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto();

            // When & Then - Verifica se a classe é serializável
            assertThat(dto).isInstanceOf(Serializable.class);
            
            // Verifica se a classe tem o campo serialVersionUID através de reflexão
            try {
                var field = CriarBeneficioDto.class.getDeclaredField("serialVersionUID");
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
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "VR Mensal",
                "Benefício de alimentação",
                new BigDecimal("600.00")
            );

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString)
                .contains("CriarBeneficioDto{")
                .contains("nome='VR Mensal'")
                .contains("valorInicial=600.00")
                .contains("}");
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto();

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString)
                .contains("CriarBeneficioDto{")
                .contains("nome='null'")
                .contains("valorInicial=null")
                .contains("}");
        }

        @ParameterizedTest
        @MethodSource("provideDtoVariants")
        @DisplayName("Deve gerar toString para diferentes variações de dados")
        void deveGerarToStringParaDiferentesVariacoesDeDados(CriarBeneficioDto dto, String expectedPart) {
            // When
            String toString = dto.toString();

            // Then
            assertThat(toString).contains(expectedPart);
        }

        static Stream<Arguments> provideDtoVariants() {
            return Stream.of(
                Arguments.of(
                    new CriarBeneficioDto("Nome Longo Teste", null, new BigDecimal("0.01")),
                    "nome='Nome Longo Teste'"
                ),
                Arguments.of(
                    new CriarBeneficioDto("", "", BigDecimal.ZERO),
                    "valorInicial=0"
                ),
                Arguments.of(
                    new CriarBeneficioDto("Test", "Desc", new BigDecimal("999999.99")),
                    "valorInicial=999999.99"
                )
            );
        }
    }

    @Nested
    @DisplayName("Casos Extremos")
    class CasosExtremosTests {

        @Test
        @DisplayName("Deve lidar com valores muito grandes")
        void deveLidarComValoresMuitoGrandes() {
            // Given
            BigDecimal valorGrande = new BigDecimal("999999999999999999.99");
            CriarBeneficioDto dto = new CriarBeneficioDto();

            // When
            dto.setValorInicial(valorGrande);

            // Then
            assertThat(dto.getValorInicial()).isEqualTo(valorGrande);
        }

        @Test
        @DisplayName("Deve lidar com strings nos limites")
        void deveLidarComStringsNosLimites() {
            // Given
            String nomeMinimo = "ABC"; // 3 caracteres
            String nomeMaximo = "A".repeat(100); // 100 caracteres
            String descricaoMaxima = "B".repeat(500); // 500 caracteres
            
            CriarBeneficioDto dto1 = new CriarBeneficioDto(nomeMinimo, "", BigDecimal.ONE);
            CriarBeneficioDto dto2 = new CriarBeneficioDto(nomeMaximo, descricaoMaxima, BigDecimal.TEN);

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations1 = validator.validate(dto1);
            Set<ConstraintViolation<CriarBeneficioDto>> violations2 = validator.validate(dto2);

            // Then
            assertThat(violations1).isEmpty();
            assertThat(violations2).isEmpty();
        }

        @Test
        @DisplayName("Deve validar caracteres especiais em strings")
        void deveValidarCaracteresEspeciaisEmStrings() {
            // Given
            String nomeEspecial = "Açaí & Café ® 123";
            String descricaoEspecial = "Benefício com çãrãctéres €$peci@is 中文";
            CriarBeneficioDto dto = new CriarBeneficioDto(
                nomeEspecial, 
                descricaoEspecial, 
                new BigDecimal("100.00")
            );

            // When
            Set<ConstraintViolation<CriarBeneficioDto>> violations = validator.validate(dto);

            // Then
            assertThat(violations).isEmpty();
            assertThat(dto.getNome()).isEqualTo(nomeEspecial);
            assertThat(dto.getDescricao()).isEqualTo(descricaoEspecial);
        }
    }
}