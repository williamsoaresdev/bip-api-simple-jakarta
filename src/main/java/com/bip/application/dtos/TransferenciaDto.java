package com.bip.application.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

public class TransferenciaDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "ID do benefício de origem é obrigatório")
    @Positive(message = "ID do benefício de origem deve ser positivo")
    private Long beneficioOrigemId;
    
    @NotNull(message = "ID do benefício de destino é obrigatório")
    @Positive(message = "ID do benefício de destino deve ser positivo")
    private Long beneficioDestinoId;
    
    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor da transferência deve ser maior que zero")
    private BigDecimal valor;
    
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String descricao;
    
    public TransferenciaDto() {}
    
    public TransferenciaDto(Long beneficioOrigemId, Long beneficioDestinoId, 
                           BigDecimal valor, String descricao) {
        this.beneficioOrigemId = beneficioOrigemId;
        this.beneficioDestinoId = beneficioDestinoId;
        this.valor = valor;
        this.descricao = descricao;
    }
    
    public Long getBeneficioOrigemId() {
        return beneficioOrigemId;
    }
    
    public void setBeneficioOrigemId(Long beneficioOrigemId) {
        this.beneficioOrigemId = beneficioOrigemId;
    }
    
    public Long getBeneficioDestinoId() {
        return beneficioDestinoId;
    }
    
    public void setBeneficioDestinoId(Long beneficioDestinoId) {
        this.beneficioDestinoId = beneficioDestinoId;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Override
    public String toString() {
        return String.format("TransferenciaDto{origem=%d, destino=%d, valor=%s}", 
                           beneficioOrigemId, beneficioDestinoId, valor);
    }
}