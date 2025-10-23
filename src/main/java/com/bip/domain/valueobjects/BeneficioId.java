package com.bip.domain.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object para representar ID de Benefício
 * Garante tipagem forte e validações de negócio
 */
@Embeddable
public class BeneficioId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Positive
    private Long id;
    
    /**
     * Construtor padrão para JPA
     */
    protected BeneficioId() {
    }
    
    /**
     * Construtor com valor
     */
    public BeneficioId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do benefício não pode ser nulo");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID do benefício deve ser positivo: " + id);
        }
        this.id = id;
    }
    
    /**
     * Factory method para criar BeneficioId
     */
    public static BeneficioId of(Long id) {
        return new BeneficioId(id);
    }
    
    /**
     * Getter para JPA
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Setter para JPA
     */
    protected void setId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do benefício não pode ser nulo");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID do benefício deve ser positivo: " + id);
        }
        this.id = id;
    }
    
    /**
     * Converte para Long
     */
    public Long toLong() {
        return id;
    }
    
    /**
     * Verifica se é válido
     */
    public boolean isValid() {
        return id != null && id > 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BeneficioId that = (BeneficioId) obj;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "BeneficioId{" + id + "}";
    }
}