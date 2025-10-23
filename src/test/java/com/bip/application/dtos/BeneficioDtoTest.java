package com.bip.application.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para BeneficioDto
 * Valida construtores, getters, setters, serialização e toString
 */
@DisplayName("BeneficioDto Tests")
class BeneficioDtoTest {

    @Nested
    @DisplayName("Construtores")
    class ConstrutoresTests {

        @Test
        @DisplayName("Deve criar DTO com construtor padrão")
        void deveCriarDtoComConstrutorPadrao() {
            // When
            BeneficioDto dto = new BeneficioDto();

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isNull();
            assertThat(dto.getNome()).isNull();
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getSaldo()).isNull();
            assertThat(dto.getAtivo()).isNull();
            assertThat(dto.getCriadoEm()).isNull();
            assertThat(dto.getAtualizadoEm()).isNull();
        }

        @Test
        @DisplayName("Deve criar DTO com construtor completo")
        void deveCriarDtoComConstrutorCompleto() {
            // Given
            Long id = 1L;
            String nome = "Benefício Teste";
            String descricao = "Descrição do benefício";
            BigDecimal saldo = new BigDecimal("1000.00");
            Boolean ativo = true;
            LocalDateTime criadoEm = LocalDateTime.now().minusDays(1);
            LocalDateTime atualizadoEm = LocalDateTime.now();

            // When
            BeneficioDto dto = new BeneficioDto(id, nome, descricao, saldo, ativo, criadoEm, atualizadoEm);

            // Then
            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getNome()).isEqualTo(nome);
            assertThat(dto.getDescricao()).isEqualTo(descricao);
            assertThat(dto.getSaldo()).isEqualTo(saldo);
            assertThat(dto.getAtivo()).isEqualTo(ativo);
            assertThat(dto.getCriadoEm()).isEqualTo(criadoEm);
            assertThat(dto.getAtualizadoEm()).isEqualTo(atualizadoEm);
        }

        @Test
        @DisplayName("Deve criar DTO com valores nulos no construtor completo")
        void deveCriarDtoComValoresNulosNoConstrutorCompleto() {
            // When
            BeneficioDto dto = new BeneficioDto(null, null, null, null, null, null, null);

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isNull();
            assertThat(dto.getNome()).isNull();
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getSaldo()).isNull();
            assertThat(dto.getAtivo()).isNull();
            assertThat(dto.getCriadoEm()).isNull();
            assertThat(dto.getAtualizadoEm()).isNull();
        }
    }

    @Nested
    @DisplayName("Getters e Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("Deve definir e obter ID corretamente")
        void deveDefinirEObterIdCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();
            Long id = 42L;

            // When
            dto.setId(id);

            // Then
            assertThat(dto.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Deve definir e obter nome corretamente")
        void deveDefinirEObterNomeCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();
            String nome = "Benefício VR";

            // When
            dto.setNome(nome);

            // Then
            assertThat(dto.getNome()).isEqualTo(nome);
        }

        @Test
        @DisplayName("Deve definir e obter descrição corretamente")
        void deveDefinirEObterDescricaoCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();
            String descricao = "Voucher Refeição mensal";

            // When
            dto.setDescricao(descricao);

            // Then
            assertThat(dto.getDescricao()).isEqualTo(descricao);
        }

        @Test
        @DisplayName("Deve definir e obter saldo corretamente")
        void deveDefinirEObterSaldoCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();
            BigDecimal saldo = new BigDecimal("500.75");

            // When
            dto.setSaldo(saldo);

            // Then
            assertThat(dto.getSaldo()).isEqualTo(saldo);
        }

        @Test
        @DisplayName("Deve definir e obter status ativo corretamente")
        void deveDefinirEObterStatusAtivoCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();

            // When & Then - true
            dto.setAtivo(true);
            assertThat(dto.getAtivo()).isTrue();

            // When & Then - false
            dto.setAtivo(false);
            assertThat(dto.getAtivo()).isFalse();

            // When & Then - null
            dto.setAtivo(null);
            assertThat(dto.getAtivo()).isNull();
        }

        @Test
        @DisplayName("Deve definir e obter data de criação corretamente")
        void deveDefinirEObterDataCriacaoCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();
            LocalDateTime criadoEm = LocalDateTime.of(2023, 1, 15, 10, 30);

            // When
            dto.setCriadoEm(criadoEm);

            // Then
            assertThat(dto.getCriadoEm()).isEqualTo(criadoEm);
        }

        @Test
        @DisplayName("Deve definir e obter data de atualização corretamente")
        void deveDefinirEObterDataAtualizacaoCorretamente() {
            // Given
            BeneficioDto dto = new BeneficioDto();
            LocalDateTime atualizadoEm = LocalDateTime.of(2023, 6, 20, 14, 45);

            // When
            dto.setAtualizadoEm(atualizadoEm);

            // Then
            assertThat(dto.getAtualizadoEm()).isEqualTo(atualizadoEm);
        }
    }

    @Nested
    @DisplayName("Serialização")
    class SerializacaoTests {

        @Test
        @DisplayName("Deve implementar Serializable")
        void deveImplementarSerializable() {
            // Given
            BeneficioDto dto = new BeneficioDto();

            // Then
            assertThat(dto).isInstanceOf(Serializable.class);
        }

        @Test
        @DisplayName("Deve ter serialVersionUID definido")
        void deveTerSerialVersionUIDDefinido() {
            // Given
            BeneficioDto dto = new BeneficioDto();

            // When & Then - Verifica se a classe é serializável
            assertThat(dto).isInstanceOf(Serializable.class);
            
            // Verifica se a classe tem o campo serialVersionUID através de reflexão
            try {
                var field = BeneficioDto.class.getDeclaredField("serialVersionUID");
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
        @DisplayName("Deve gerar toString com todos os campos principais")
        void deveGerarToStringComTodosCamposPrincipais() {
            // Given
            BeneficioDto dto = new BeneficioDto(
                1L, 
                "VR Mensal", 
                "Benefício de alimentação", 
                new BigDecimal("600.00"), 
                true, 
                LocalDateTime.now(), 
                LocalDateTime.now()
            );

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString)
                .contains("BeneficioDto{")
                .contains("id=1")
                .contains("nome='VR Mensal'")
                .contains("saldo=600.00")
                .contains("ativo=true")
                .contains("}");
        }

        @Test
        @DisplayName("Deve gerar toString com valores nulos")
        void deveGerarToStringComValoresNulos() {
            // Given
            BeneficioDto dto = new BeneficioDto();

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString)
                .contains("BeneficioDto{")
                .contains("id=null")
                .contains("nome='null'")
                .contains("saldo=null")
                .contains("ativo=null")
                .contains("}");
        }

        @ParameterizedTest
        @MethodSource("provideDtoVariants")
        @DisplayName("Deve gerar toString para diferentes variações de dados")
        void deveGerarToStringParaDiferentesVariacoesDeDados(BeneficioDto dto, String expectedPart) {
            // When
            String toString = dto.toString();

            // Then
            assertThat(toString).contains(expectedPart);
        }

        static Stream<Arguments> provideDtoVariants() {
            return Stream.of(
                Arguments.of(
                    new BeneficioDto(999L, "Nome Grande", null, new BigDecimal("0.01"), false, null, null),
                    "id=999"
                ),
                Arguments.of(
                    new BeneficioDto(0L, "", "", BigDecimal.ZERO, true, null, null),
                    "nome=''"
                ),
                Arguments.of(
                    new BeneficioDto(-1L, "Test", "Desc", new BigDecimal("-100"), false, null, null),
                    "saldo=-100"
                )
            );
        }
    }

    @Nested
    @DisplayName("Casos Extremos")
    class CasosExtremosTests {

        @Test
        @DisplayName("Deve lidar com saldos muito grandes")
        void deveLidarComSaldosMuitoGrandes() {
            // Given
            BigDecimal saldoGrande = new BigDecimal("999999999999999999.99");
            BeneficioDto dto = new BeneficioDto();

            // When
            dto.setSaldo(saldoGrande);

            // Then
            assertThat(dto.getSaldo()).isEqualTo(saldoGrande);
        }

        @Test
        @DisplayName("Deve lidar com strings muito longas")
        void deveLidarComStringsMuitoLongas() {
            // Given
            String nomeLongo = "A".repeat(1000);
            String descricaoLonga = "B".repeat(2000);
            BeneficioDto dto = new BeneficioDto();

            // When
            dto.setNome(nomeLongo);
            dto.setDescricao(descricaoLonga);

            // Then
            assertThat(dto.getNome()).isEqualTo(nomeLongo);
            assertThat(dto.getDescricao()).isEqualTo(descricaoLonga);
        }

        @Test
        @DisplayName("Deve lidar com datas extremas")
        void deveLidarComDatasExtremas() {
            // Given
            LocalDateTime dataMinima = LocalDateTime.MIN;
            LocalDateTime dataMaxima = LocalDateTime.MAX;
            BeneficioDto dto = new BeneficioDto();

            // When
            dto.setCriadoEm(dataMinima);
            dto.setAtualizadoEm(dataMaxima);

            // Then
            assertThat(dto.getCriadoEm()).isEqualTo(dataMinima);
            assertThat(dto.getAtualizadoEm()).isEqualTo(dataMaxima);
        }
    }
}