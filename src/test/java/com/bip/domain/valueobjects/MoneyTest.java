package com.bip.domain.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Money Value Object Tests")
class MoneyTest {

    @Test
    @DisplayName("Deve criar Money com valor válido")
    void deveIncriarMoneyComValorValido() {
        // Given
        BigDecimal valor = new BigDecimal("100.50");
        
        // When
        Money money = new Money(valor);
        
        // Then
        assertThat(money.getValor()).isEqualByComparingTo(valor);
    }

    @Test
    @DisplayName("Deve lançar exceção para valor nulo")
    void deveLancarExcecaoParaValorNulo() {
        // When & Then
        assertThatThrownBy(() -> new Money((BigDecimal) null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor monetário não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção para valor negativo")
    void deveLancarExcecaoParaValorNegativo() {
        // Given
        BigDecimal valorNegativo = new BigDecimal("-10.00");
        
        // When & Then
        assertThatThrownBy(() -> new Money(valorNegativo))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Valor monetário não pode ser negativo");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.00", "100.50", "1000000"})
    @DisplayName("Deve aceitar valores válidos")
    void deveAceitarValoresValidos(String valor) {
        // Given & When
        Money money = new Money(new BigDecimal(valor));
        
        // Then
        assertThat(money.getValor()).isNotNull();
        assertThat(money.getValor()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve adicionar dois Money corretamente")
    void deveAdicionarDoisMoneyCorretamente() {
        // Given
        Money money1 = new Money(new BigDecimal("100.50"));
        Money money2 = new Money(new BigDecimal("50.25"));
        
        // When
        Money resultado = money1.add(money2);
        
        // Then
        assertThat(resultado.getValor()).isEqualByComparingTo(new BigDecimal("150.75"));
    }

    @Test
    @DisplayName("Deve subtrair dois Money corretamente")
    void deveSubtrairDoisMoneyCorretamente() {
        // Given
        Money money1 = new Money(new BigDecimal("100.50"));
        Money money2 = new Money(new BigDecimal("50.25"));
        
        // When
        Money resultado = money1.subtract(money2);
        
        // Then
        assertThat(resultado.getValor()).isEqualByComparingTo(new BigDecimal("50.25"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao subtrair valor maior")
    void deveLancarExcecaoAoSubtrairValorMaior() {
        // Given
        Money money1 = new Money(new BigDecimal("50.00"));
        Money money2 = new Money(new BigDecimal("100.00"));
        
        // When & Then
        assertThatThrownBy(() -> money1.subtract(money2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor monetário não pode ser negativo: -50.00");
    }

    @Test
    @DisplayName("Deve comparar Money corretamente - maior que")
    void deveCompararMoneyCorretamenteMaiorQue() {
        // Given
        Money money1 = new Money(new BigDecimal("100.00"));
        Money money2 = new Money(new BigDecimal("50.00"));
        
        // When & Then
        assertThat(money1.isGreaterThan(money2)).isTrue();
        assertThat(money2.isGreaterThan(money1)).isFalse();
    }

    @Test
    @DisplayName("Deve verificar igualdade corretamente")
    void deveVerificarIgualdadeCorretamente() {
        // Given
        Money money1 = new Money(new BigDecimal("100.00"));
        Money money2 = new Money(new BigDecimal("100.00"));
        Money money3 = new Money(new BigDecimal("50.00"));
        
        // When & Then
        assertThat(money1).isEqualTo(money2);
        assertThat(money1).isNotEqualTo(money3);
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    @DisplayName("Deve multiplicar por percentual corretamente")
    void deveMultiplicarPorPercentualCorretamente() {
        // Given
        Money money = new Money(new BigDecimal("100.00"));
        BigDecimal percentual = new BigDecimal("0.10"); // 10%
        
        // When
        Money resultado = money.multiply(percentual);
        
        // Then
        assertThat(resultado.getValor()).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    @DisplayName("ToString deve retornar formato adequado")
    void toStringDeveRetornarFormatoAdequado() {
        // Given
        Money money = new Money(new BigDecimal("100.50"));
        
        // When
        String resultado = money.toString();
        
        // Then
        assertThat(resultado).isEqualTo("R$ 100,50");
    }

    @Test
    @DisplayName("Deve criar Money usando factory method of")
    void deveCriarMoneyUsandoFactoryMethodOf() {
        // Given
        BigDecimal valor = new BigDecimal("75.25");
        
        // When
        Money money = Money.of(valor);
        
        // Then
        assertThat(money.getValor()).isEqualByComparingTo(valor);
    }

    @Test
    @DisplayName("Deve criar Money zero")
    void deveCriarMoneyZero() {
        // When
        Money money = Money.zero();
        
        // Then
        assertThat(money.getValor()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(money.isZero()).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exceção ao multiplicar por valor nulo")
    void deveLancarExcecaoAoMultiplicarPorValorNulo() {
        // Given
        Money money = new Money(new BigDecimal("100.00"));
        
        // When & Then
        assertThatThrownBy(() -> money.multiply(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Fator de multiplicação não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao adicionar Money nulo")
    void deveLancarExcecaoAoAdicionarMoneyNulo() {
        // Given
        Money money = new Money(new BigDecimal("100.00"));
        
        // When & Then
        assertThatThrownBy(() -> money.add(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor para soma não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao subtrair Money nulo")
    void deveLancarExcecaoAoSubtrairMoneyNulo() {
        // Given
        Money money = new Money(new BigDecimal("100.00"));
        
        // When & Then
        assertThatThrownBy(() -> money.subtract(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor para subtração não pode ser nulo");
    }

    @Test
    @DisplayName("Deve comparar Money corretamente - menor que")
    void deveCompararMoneyCorretamenteMenorQue() {
        // Given
        Money money1 = new Money(new BigDecimal("50.00"));
        Money money2 = new Money(new BigDecimal("100.00"));
        
        // When & Then
        assertThat(money1.isLessThan(money2)).isTrue();
        assertThat(money2.isLessThan(money1)).isFalse();
    }

    @Test
    @DisplayName("Deve comparar Money corretamente - maior ou igual")
    void deveCompararMoneyCorretamenteMaiorOuIgual() {
        // Given
        Money money1 = new Money(new BigDecimal("100.00"));
        Money money2 = new Money(new BigDecimal("100.00"));
        Money money3 = new Money(new BigDecimal("50.00"));
        
        // When & Then
        assertThat(money1.isGreaterThanOrEqual(money2)).isTrue();
        assertThat(money1.isGreaterThanOrEqual(money3)).isTrue();
        assertThat(money3.isGreaterThanOrEqual(money1)).isFalse();
    }

    @Test
    @DisplayName("Deve verificar se é zero")
    void deveVerificarSeEhZero() {
        // Given
        Money zero = new Money(BigDecimal.ZERO);
        Money naoZero = new Money(new BigDecimal("10.00"));
        
        // When & Then
        assertThat(zero.isZero()).isTrue();
        assertThat(naoZero.isZero()).isFalse();
    }

    @Test
    @DisplayName("Deve verificar se é positivo")
    void deveVerificarSeEhPositivo() {
        // Given
        Money positivo = new Money(new BigDecimal("10.00"));
        Money zero = Money.zero();
        
        // When & Then
        assertThat(positivo.isPositive()).isTrue();
        assertThat(zero.isPositive()).isFalse();
    }

    @Test
    @DisplayName("Deve converter para double corretamente")
    void deveConverterParaDoubleCorretamente() {
        // Given
        Money money = new Money(new BigDecimal("123.45"));
        
        // When
        double resultado = money.toDouble();
        
        // Then
        assertThat(resultado).isEqualTo(123.45);
    }

    @Test
    @DisplayName("Deve implementar Comparable corretamente")
    void deveImplementarComparableCorretamente() {
        // Given
        Money money1 = new Money(new BigDecimal("50.00"));
        Money money2 = new Money(new BigDecimal("100.00"));
        Money money3 = new Money(new BigDecimal("50.00"));
        
        // When & Then
        assertThat(money1.compareTo(money2)).isLessThan(0);
        assertThat(money2.compareTo(money1)).isGreaterThan(0);
        assertThat(money1.compareTo(money3)).isEqualTo(0);
    }
}