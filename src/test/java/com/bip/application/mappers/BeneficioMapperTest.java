package com.bip.application.mappers;

import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.domain.entities.Beneficio;
import com.bip.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes para BeneficioMapper
 * Verifica conversões entre entidades e DTOs
 */
@DisplayName("BeneficioMapper Tests")
class BeneficioMapperTest {
    
    private BeneficioMapper mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new BeneficioMapper();
    }
    
    @Nested
    @DisplayName("Entity to DTO Conversion")
    class EntityToDtoConversionTests {
        
        @Test
        @DisplayName("Should convert entity to DTO with all fields")
        void shouldConvertEntityToDtoWithAllFields() {
            // Given
            Beneficio beneficio = Beneficio.criar(
                "Vale Alimentação",
                "Benefício para alimentação",
                Money.of(new BigDecimal("500.00"))
            );
            
            // When
            BeneficioDto dto = mapper.toDto(beneficio);
            
            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isNull(); // ID é null em entidade recém-criada
            assertThat(dto.getNome()).isEqualTo("Vale Alimentação");
            assertThat(dto.getDescricao()).isEqualTo("Benefício para alimentação");
            assertThat(dto.getSaldo()).isEqualByComparingTo(new BigDecimal("500.00"));
            assertThat(dto.getAtivo()).isTrue();
            assertThat(dto.getCriadoEm()).isNotNull();
            assertThat(dto.getAtualizadoEm()).isNotNull();
        }
        
        @Test
        @DisplayName("Should handle null entity")
        void shouldHandleNullEntity() {
            // When
            BeneficioDto dto = mapper.toDto(null);
            
            // Then
            assertThat(dto).isNull();
        }
        
        @Test
        @DisplayName("Should convert entity with null description")
        void shouldConvertEntityWithNullDescription() {
            // Given
            Beneficio beneficio = Beneficio.criar(
                "Vale Transporte",
                null,
                Money.of(new BigDecimal("200.00"))
            );
            
            // When
            BeneficioDto dto = mapper.toDto(beneficio);
            
            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getNome()).isEqualTo("Vale Transporte");
            assertThat(dto.getDescricao()).isNull();
            assertThat(dto.getSaldo()).isEqualByComparingTo(new BigDecimal("200.00"));
        }
        
        @Test
        @DisplayName("Should convert inactive entity")
        void shouldConvertInactiveEntity() {
            // Given
            Beneficio beneficio = Beneficio.criar(
                "Vale Refeição",
                "Benefício para refeições",
                Money.of(new BigDecimal("300.00"))
            );
            beneficio.desativar();
            
            // When
            BeneficioDto dto = mapper.toDto(beneficio);
            
            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getAtivo()).isFalse();
        }
    }
    
    @Nested
    @DisplayName("Entity List to DTO List Conversion")
    class EntityListToDtoListConversionTests {
        
        @Test
        @DisplayName("Should convert list of entities to list of DTOs")
        void shouldConvertListOfEntitiesToListOfDtos() {
            // Given
            List<Beneficio> beneficios = Arrays.asList(
                Beneficio.criar("Vale Alimentação", "Desc 1", Money.of(new BigDecimal("500.00"))),
                Beneficio.criar("Vale Transporte", "Desc 2", Money.of(new BigDecimal("200.00"))),
                Beneficio.criar("Vale Refeição", "Desc 3", Money.of(new BigDecimal("300.00")))
            );
            
            // When
            List<BeneficioDto> dtos = mapper.toDtoList(beneficios);
            
            // Then
            assertThat(dtos).hasSize(3);
            assertThat(dtos).extracting(BeneficioDto::getNome)
                    .containsExactly("Vale Alimentação", "Vale Transporte", "Vale Refeição");
            assertThat(dtos).extracting(BeneficioDto::getSaldo)
                    .containsExactly(
                        new BigDecimal("500.00"),
                        new BigDecimal("200.00"),
                        new BigDecimal("300.00")
                    );
        }
        
        @Test
        @DisplayName("Should handle null list")
        void shouldHandleNullList() {
            // When
            List<BeneficioDto> dtos = mapper.toDtoList(null);
            
            // Then
            assertThat(dtos).isNotNull().isEmpty();
        }
        
        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            // When
            List<BeneficioDto> dtos = mapper.toDtoList(Collections.emptyList());
            
            // Then
            assertThat(dtos).isNotNull().isEmpty();
        }
        
        @Test
        @DisplayName("Should convert list with null elements")
        void shouldConvertListWithNullElements() {
            // Given
            List<Beneficio> beneficios = Arrays.asList(
                Beneficio.criar("Vale Alimentação", "Desc 1", Money.of(new BigDecimal("500.00"))),
                null,
                Beneficio.criar("Vale Transporte", "Desc 2", Money.of(new BigDecimal("200.00")))
            );
            
            // When
            List<BeneficioDto> dtos = mapper.toDtoList(beneficios);
            
            // Then
            assertThat(dtos).hasSize(3);
            assertThat(dtos.get(0)).isNotNull();
            assertThat(dtos.get(1)).isNull();
            assertThat(dtos.get(2)).isNotNull();
        }
    }
    
    @Nested
    @DisplayName("DTO to Entity Conversion")
    class DtoToEntityConversionTests {
        
        @Test
        @DisplayName("Should convert DTO to entity with all fields")
        void shouldConvertDtoToEntityWithAllFields() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Vale Alimentação",
                "Benefício para alimentação",
                new BigDecimal("500.00")
            );
            
            // When
            Beneficio entity = mapper.toEntity(dto);
            
            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getNome()).isEqualTo("Vale Alimentação");
            assertThat(entity.getDescricao()).isEqualTo("Benefício para alimentação");
            assertThat(entity.getSaldo().getValor()).isEqualByComparingTo(new BigDecimal("500.00"));
            assertThat(entity.getAtivo()).isTrue();
            assertThat(entity.getCriadoEm()).isNotNull();
            assertThat(entity.getAtualizadoEm()).isNotNull();
        }
        
        @Test
        @DisplayName("Should handle null DTO")
        void shouldHandleNullDto() {
            // When
            Beneficio entity = mapper.toEntity(null);
            
            // Then
            assertThat(entity).isNull();
        }
        
        @Test
        @DisplayName("Should convert DTO with null valor inicial")
        void shouldConvertDtoWithNullValorInicial() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Vale Transporte",
                "Benefício para transporte",
                null
            );
            
            // When
            Beneficio entity = mapper.toEntity(dto);
            
            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getSaldo().getValor()).isEqualByComparingTo(BigDecimal.ZERO);
        }
        
        @Test
        @DisplayName("Should convert DTO with null description")
        void shouldConvertDtoWithNullDescription() {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(
                "Vale Refeição",
                null,
                new BigDecimal("300.00")
            );
            
            // When
            Beneficio entity = mapper.toEntity(dto);
            
            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getNome()).isEqualTo("Vale Refeição");
            assertThat(entity.getDescricao()).isNull();
            assertThat(entity.getSaldo().getValor()).isEqualByComparingTo(new BigDecimal("300.00"));
        }
        
        @ParameterizedTest
        @MethodSource("provideDtoVariations")
        @DisplayName("Should convert various DTO configurations")
        void shouldConvertVariousDtoConfigurations(
                String nome, String descricao, BigDecimal valor, 
                String expectedNome, String expectedDescricao, BigDecimal expectedValor) {
            // Given
            CriarBeneficioDto dto = new CriarBeneficioDto(nome, descricao, valor);
            
            // When
            Beneficio entity = mapper.toEntity(dto);
            
            // Then
            assertThat(entity).isNotNull();
            assertThat(entity.getNome()).isEqualTo(expectedNome);
            assertThat(entity.getDescricao()).isEqualTo(expectedDescricao);
            assertThat(entity.getSaldo().getValor()).isEqualByComparingTo(expectedValor);
        }
        
        static Stream<Arguments> provideDtoVariations() {
            return Stream.of(
                Arguments.of("Vale Alimentação", "Desc", new BigDecimal("100.00"), 
                           "Vale Alimentação", "Desc", new BigDecimal("100.00")),
                Arguments.of("Vale Transporte", null, new BigDecimal("50.00"), 
                           "Vale Transporte", null, new BigDecimal("50.00")),
                Arguments.of("Vale Refeição", "Descrição", null, 
                           "Vale Refeição", "Descrição", BigDecimal.ZERO),
                Arguments.of("Vale Cultura", "", new BigDecimal("200.00"), 
                           "Vale Cultura", "", new BigDecimal("200.00"))
            );
        }
    }
}