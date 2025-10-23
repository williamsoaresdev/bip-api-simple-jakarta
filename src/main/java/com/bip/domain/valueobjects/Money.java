package com.bip.domain.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object para representar valores monetários
 * Garante imutabilidade e validações de negócio
 */
@Embeddable
public class Money implements Serializable, Comparable<Money> {
    
    private static final long serialVersionUID = 1L;
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    @NotNull
    @PositiveOrZero
    private BigDecimal valor;
    
    /**
     * Construtor padrão para JPA
     */
    protected Money() {
        this.valor = BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Construtor com valor
     */
    public Money(BigDecimal valor) {
        this.valor = normalizeValue(valor);
    }
    
    /**
     * Construtor com double
     */
    public Money(double valor) {
        this(BigDecimal.valueOf(valor));
    }
    
    /**
     * Factory method para valor zero
     */
    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }
    
    /**
     * Factory method a partir de BigDecimal
     */
    public static Money of(BigDecimal valor) {
        return new Money(valor);
    }
    
    /**
     * Factory method a partir de double
     */
    public static Money of(double valor) {
        return new Money(valor);
    }
    
    /**
     * Soma dois valores monetários
     */
    public Money add(Money other) {
        validateNotNull(other, "Valor para soma não pode ser nulo");
        return new Money(this.valor.add(other.valor));
    }
    
    /**
     * Subtrai dois valores monetários
     */
    public Money subtract(Money other) {
        validateNotNull(other, "Valor para subtração não pode ser nulo");
        return new Money(this.valor.subtract(other.valor));
    }
    
    /**
     * Multiplica por um fator
     */
    public Money multiply(BigDecimal factor) {
        validateNotNull(factor, "Fator de multiplicação não pode ser nulo");
        return new Money(this.valor.multiply(factor));
    }
    
    /**
     * Verifica se é maior que outro valor
     */
    public boolean isGreaterThan(Money other) {
        validateNotNull(other, "Valor para comparação não pode ser nulo");
        return this.valor.compareTo(other.valor) > 0;
    }
    
    /**
     * Verifica se é maior ou igual a outro valor
     */
    public boolean isGreaterThanOrEqual(Money other) {
        validateNotNull(other, "Valor para comparação não pode ser nulo");
        return this.valor.compareTo(other.valor) >= 0;
    }
    
    /**
     * Verifica se é menor que outro valor
     */
    public boolean isLessThan(Money other) {
        validateNotNull(other, "Valor para comparação não pode ser nulo");
        return this.valor.compareTo(other.valor) < 0;
    }
    
    /**
     * Verifica se é zero
     */
    public boolean isZero() {
        return this.valor.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Verifica se é positivo
     */
    public boolean isPositive() {
        return this.valor.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Verifica se é negativo
     */
    public boolean isNegative() {
        return this.valor.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Getter para JPA
     */
    public BigDecimal getValor() {
        return valor;
    }
    
    /**
     * Setter para JPA
     */
    protected void setValor(BigDecimal valor) {
        this.valor = normalizeValue(valor);
    }
    
    /**
     * Converte para double
     */
    public double toDouble() {
        return valor.doubleValue();
    }
    
    /**
     * Normaliza o valor com scale e rounding adequados
     */
    private BigDecimal normalizeValue(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Valor monetário não pode ser nulo");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor monetário não pode ser negativo: " + value);
        }
        return value.setScale(SCALE, ROUNDING_MODE);
    }
    
    /**
     * Valida se objeto não é nulo
     */
    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
    @Override
    public int compareTo(Money other) {
        validateNotNull(other, "Valor para comparação não pode ser nulo");
        return this.valor.compareTo(other.valor);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return Objects.equals(valor, money.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return String.format("R$ %.2f", valor);
    }
}