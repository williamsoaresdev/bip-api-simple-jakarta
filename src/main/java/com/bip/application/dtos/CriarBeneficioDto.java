package com.bip.application.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO para criação de benefício.
 * Usado na camada de aplicação para transferir dados de criação de um novo benefício.
 * 
 * @author BIP API Team
 * @since 1.0
 */
public class CriarBeneficioDto extends BaseDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Construtor padrão.
     */
    public CriarBeneficioDto() {
        super();
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome o nome do benefício
     * @param descricao a descrição do benefício
     * @param valorInicial o valor inicial do benefício
     */
    public CriarBeneficioDto(final String nome, final String descricao, final BigDecimal valorInicial) {
        super(nome, descricao, valorInicial);
    }
}