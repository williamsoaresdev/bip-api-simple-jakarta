package com.bip.application.dtos;

import java.math.BigDecimal;

/**
 * Classe base para DTOs de criação e atualização de benefícios.
 * Elimina a duplicação de código entre AtualizarBeneficioDto e CriarBeneficioDto.
 * As validações são definidas nas classes filhas para permitir diferentes regras.
 * 
 * @author BIP API Team
 * @since 1.0
 */
public abstract class BaseDto {
    
    protected String nome;
    protected String descricao;
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