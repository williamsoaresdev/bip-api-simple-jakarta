package com.bip.domain.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BeneficioId Value Object Tests")
class BeneficioIdTest {

    @Test
    @DisplayName("Deve criar BeneficioId com valor válido")
    void deveCriarBeneficioIdComValorValido() {
        // Given
        Long id = 1L;
        
        // When
        BeneficioId beneficioId = new BeneficioId(id);
        
        // Then
        assertThat(beneficioId.getId()).isEqualTo(id);
        assertThat(beneficioId.toLong()).isEqualTo(id);
        assertThat(beneficioId.isValid()).isTrue();
    }

    @Test
    @DisplayName("Deve criar BeneficioId usando factory method")
    void deveCriarBeneficioIdUsandoFactoryMethod() {
        // Given
        Long id = 42L;
        
        // When
        BeneficioId beneficioId = BeneficioId.of(id);
        
        // Then
        assertThat(beneficioId.getId()).isEqualTo(id);
        assertThat(beneficioId.isValid()).isTrue();
    }

    @Test
    @DisplayName("Deve criar BeneficioId usando construtor padrão (JPA)")
    void deveCriarBeneficioIdUsandoConstrutorPadrao() throws Exception {
        // Given - Usar reflection para acessar construtor protected
        Constructor<BeneficioId> constructor = BeneficioId.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When
        BeneficioId beneficioId = constructor.newInstance();

        // Then
        assertThat(beneficioId).isNotNull();
        assertThat(beneficioId.getId()).isNull();
        assertThat(beneficioId.isValid()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar exceção para ID nulo")
    void deveLancarExcecaoParaIdNulo() {
        // When & Then
        assertThatThrownBy(() -> new BeneficioId(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ID do benefício não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção para ID nulo no factory method")
    void deveLancarExcecaoParaIdNuloNoFactoryMethod() {
        // When & Then
        assertThatThrownBy(() -> BeneficioId.of(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ID do benefício não pode ser nulo");
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -10L, -100L})
    @DisplayName("Deve lançar exceção para IDs inválidos")
    void deveLancarExcecaoParaIdsInvalidos(Long id) {
        // When & Then
        assertThatThrownBy(() -> new BeneficioId(id))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("ID do benefício deve ser positivo: " + id);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 10L, 100L, 999L, 1000L, Long.MAX_VALUE})
    @DisplayName("Deve aceitar IDs válidos")
    void deveAceitarIdsValidos(Long id) {
        // When
        BeneficioId beneficioId = new BeneficioId(id);
        
        // Then
        assertThat(beneficioId.getId()).isEqualTo(id);
        assertThat(beneficioId.isValid()).isTrue();
    }

    @Test
    @DisplayName("Deve definir ID usando setter com valor válido (JPA)")
    void deveDefinirIdUsandoSetterComValorValido() throws Exception {
        // Given
        Constructor<BeneficioId> constructor = BeneficioId.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        BeneficioId beneficioId = constructor.newInstance();

        Method setIdMethod = BeneficioId.class.getDeclaredMethod("setId", Long.class);
        setIdMethod.setAccessible(true);

        Long novoId = 999L;

        // When
        setIdMethod.invoke(beneficioId, novoId);

        // Then
        assertThat(beneficioId.getId()).isEqualTo(novoId);
        assertThat(beneficioId.isValid()).isTrue();
    }

    @Test
    @DisplayName("Deve falhar ao definir ID nulo usando setter (JPA)")
    void deveFalharAoDefinirIdNuloUsandoSetter() throws Exception {
        // Given
        Constructor<BeneficioId> constructor = BeneficioId.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        BeneficioId beneficioId = constructor.newInstance();

        Method setIdMethod = BeneficioId.class.getDeclaredMethod("setId", Long.class);
        setIdMethod.setAccessible(true);

        // When & Then
        assertThatThrownBy(() -> setIdMethod.invoke(beneficioId, (Object) null))
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("ID do benefício não pode ser nulo");
    }

    @Test
    @DisplayName("Deve falhar ao definir ID inválido usando setter (JPA)")
    void deveFalharAoDefinirIdInvalidoUsandoSetter() throws Exception {
        // Given
        Constructor<BeneficioId> constructor = BeneficioId.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        BeneficioId beneficioId = constructor.newInstance();

        Method setIdMethod = BeneficioId.class.getDeclaredMethod("setId", Long.class);
        setIdMethod.setAccessible(true);

        // When & Then
        assertThatThrownBy(() -> setIdMethod.invoke(beneficioId, -10L))
                .hasCauseInstanceOf(IllegalArgumentException.class)
                .hasRootCauseMessage("ID do benefício deve ser positivo: -10");
    }

    @Test
    @DisplayName("Deve verificar igualdade corretamente")
    void deveVerificarIgualdadeCorretamente() {
        // Given
        BeneficioId id1 = new BeneficioId(1L);
        BeneficioId id2 = new BeneficioId(1L);
        BeneficioId id3 = new BeneficioId(2L);
        
        // When & Then
        assertThat(id1).isEqualTo(id2);
        assertThat(id1).isNotEqualTo(id3);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        assertThat(id1.hashCode()).isNotEqualTo(id3.hashCode());
    }

    @Test
    @DisplayName("Deve retornar false para equals com null")
    void deveRetornarFalseParaEqualsComNull() {
        // Given
        BeneficioId id = new BeneficioId(1L);
        
        // When & Then
        assertThat(id.equals(null)).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false para equals com objeto de classe diferente")
    void deveRetornarFalseParaEqualsComObjetoDeClasseDiferente() {
        // Given
        BeneficioId id = new BeneficioId(1L);
        String outroObjeto = "1";
        
        // When & Then
        assertThat(id.equals(outroObjeto)).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true para equals com mesmo objeto")
    void deveRetornarTrueParaEqualsComMesmoObjeto() {
        // Given
        BeneficioId id = new BeneficioId(1L);
        
        // When & Then
        assertThat(id.equals(id)).isTrue();
    }

    @Test
    @DisplayName("ToString deve retornar formato adequado")
    void toStringDeveRetornarFormatoAdequado() {
        // Given
        BeneficioId id = new BeneficioId(123L);
        
        // When
        String resultado = id.toString();
        
        // Then
        assertThat(resultado).isEqualTo("BeneficioId{123}");
    }

    @Test
    @DisplayName("Deve converter para Long corretamente")
    void deveConverterParaLongCorretamente() {
        // Given
        Long valorOriginal = 456L;
        BeneficioId id = new BeneficioId(valorOriginal);
        
        // When
        Long valorConvertido = id.toLong();
        
        // Then
        assertThat(valorConvertido).isEqualTo(valorOriginal);
        assertThat(valorConvertido).isSameAs(valorOriginal);
    }

    @Test
    @DisplayName("Deve validar ID corretamente")
    void deveValidarIdCorretamente() {
        // Given
        BeneficioId idValido = new BeneficioId(1L);
        
        // When & Then
        assertThat(idValido.isValid()).isTrue();
    }
}