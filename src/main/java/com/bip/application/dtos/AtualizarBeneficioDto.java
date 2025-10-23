package com.bip.application.dtos;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO para atualização de benefício.
 * Usado na camada de aplicação para atualizar dados de um benefício existente.
 * 
 * @author BIP API Team
 * @since 1.0
 */
public class AtualizarBeneficioDto extends BaseDto {
    
    // Sobrescreve a validação do BaseDto para permitir valor nulo em atualizações
    @DecimalMin(value = "0.00", inclusive = true, message = "Valor inicial deve ser positivo ou zero")
    protected BigDecimal valorInicial;
    
    /**
     * Construtor padrão.
     */
    public AtualizarBeneficioDto() {
        super();
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome o nome do benefício
     * @param descricao a descrição do benefício
     * @param valorInicial o valor inicial do benefício
     */
    public AtualizarBeneficioDto(final String nome, final String descricao, final BigDecimal valorInicial) {
        super(nome, descricao, valorInicial);
    }
    
    @Override
    public BigDecimal getValorInicial() {
        return valorInicial;
    }
    
    @Override
    public void setValorInicial(final BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }
}