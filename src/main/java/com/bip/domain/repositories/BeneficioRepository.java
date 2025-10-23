package com.bip.domain.repositories;

import com.bip.domain.entities.Beneficio;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para Benefício seguindo DDD
 * Define operações de persistência sem expor detalhes de implementação
 */
public interface BeneficioRepository {
    
    /**
     * Busca benefício por ID
     */
    Optional<Beneficio> findById(Long id);
    
    /**
     * Busca benefício por nome
     */
    Optional<Beneficio> findByNome(String nome);
    
    /**
     * Lista todos os benefícios
     */
    List<Beneficio> findAll();
    
    /**
     * Lista apenas benefícios ativos
     */
    List<Beneficio> findAllActive();
    
    /**
     * Busca benefícios por IDs com lock pessimista para transferências
     */
    List<Beneficio> findByIdsWithLock(List<Long> ids);
    
    /**
     * Conta benefícios ativos
     */
    long countActive();
    
    /**
     * Soma valores de benefícios ativos
     */
    java.math.BigDecimal sumActiveValues();
    
    /**
     * Salva benefício
     */
    Beneficio save(Beneficio beneficio);
    
    /**
     * Remove benefício
     */
    void delete(Beneficio beneficio);
    
    /**
     * Remove benefício por ID
     */
    void deleteById(Long id);
    
    /**
     * Verifica se existe benefício com nome
     */
    boolean existsByNome(String nome);
    
    /**
     * Verifica se existe benefício por ID
     */
    boolean existsById(Long id);
}