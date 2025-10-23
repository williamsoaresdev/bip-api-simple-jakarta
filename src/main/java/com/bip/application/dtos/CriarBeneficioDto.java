package com.bip.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO para criação de benefício
 * Usado na camada de aplicação para transferir dados
 */
public class CriarBeneficioDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String descricao;
    
    @NotNull(message = "Valor inicial é obrigatório")
    @PositiveOrZero(message = "Valor inicial deve ser positivo ou zero")
    private BigDecimal valorInicial;
    
    // ================================
    // Construtores
    // ================================
    
    public CriarBeneficioDto() {}
    
    public CriarBeneficioDto(String nome, String descricao, BigDecimal valorInicial) {
        this.nome = nome;
        this.descricao = descricao;
        this.valorInicial = valorInicial;
    }
    
    // ================================
    // Getters e Setters
    // ================================
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValorInicial() {
        return valorInicial;
    }
    
    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }
    
    @Override
    public String toString() {
        return String.format("CriarBeneficioDto{nome='%s', valorInicial=%s}", 
                           nome, valorInicial);
    }
}