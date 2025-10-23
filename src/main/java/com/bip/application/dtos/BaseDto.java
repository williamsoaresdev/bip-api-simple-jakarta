package com.bip.application.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Classe base para DTOs de criação e atualização de benefícios.
 * Elimina a duplicação de código entre AtualizarBeneficioDto e CriarBeneficioDto.
 * 
 * @author BIP API Team
 * @since 1.0
 */
public abstract class BaseDto {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    protected String nome;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    protected String descricao;
    
    @NotNull(message = "Valor inicial é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "Valor inicial deve ser positivo ou zero")
    protected BigDecimal valorInicial;
    
    /**
     * Construtor padrão.
     */
    public BaseDto() {
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome o nome do benefício
     * @param descricao a descrição do benefício
     * @param valorInicial o valor inicial do benefício
     */
    public BaseDto(final String nome, final String descricao, final BigDecimal valorInicial) {
        this.nome = nome;
        this.descricao = descricao;
        this.valorInicial = valorInicial;
    }
    
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    
    public void setNome(final String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValorInicial() {
        return valorInicial;
    }
    
    public void setValorInicial(final BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valorInicial=" + valorInicial +
                '}';
    }
}