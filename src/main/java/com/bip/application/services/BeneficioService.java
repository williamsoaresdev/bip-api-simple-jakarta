package com.bip.application.services;

import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Serviço compartilhado para operações comuns com Benefício.
 * 
 * <p>Este serviço centraliza validações e operações que são utilizadas
 * por múltiplos use cases, eliminando duplicação de código e garantindo
 * consistência nas regras de negócio.</p>
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Busca padronizada de benefícios com tratamento de erro</li>
 *   <li>Validações de regras de negócio compartilhadas</li>
 *   <li>Operações auxiliares para transferências</li>
 * </ul>
 * 
 * @author BIP API Team
 * @since 1.0
 * @version 3.0.0
 */
@ApplicationScoped
public class BeneficioService {
    
    @Inject
    private BeneficioRepository beneficioRepository;
    
    /**
     * Busca benefício por ID com tratamento de erro padronizado
     * 
     * @param id ID do benefício
     * @return Benefício encontrado
     * @throws IllegalArgumentException se benefício não for encontrado
     */
    public Beneficio buscarPorId(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Benefício não encontrado com ID: " + id));
    }
    
    /**
     * Valida se o benefício está ativo
     * 
     * @param beneficio Benefício a ser validado
     * @throws IllegalStateException se benefício estiver inativo
     */
    public void validarAtivo(Beneficio beneficio) {
        if (!beneficio.getAtivo()) {
            throw new IllegalStateException("Benefício está inativo: " + beneficio.getId());
        }
    }
    
    /**
     * Valida se dois benefícios são diferentes
     * 
     * @param origemId ID do benefício origem
     * @param destinoId ID do benefício destino
     * @throws IllegalArgumentException se IDs forem iguais
     */
    public void validarBeneficiosDiferentes(Long origemId, Long destinoId) {
        if (origemId.equals(destinoId)) {
            throw new IllegalArgumentException("Benefício de origem e destino não podem ser iguais");
        }
    }
}