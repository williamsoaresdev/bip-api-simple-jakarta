package com.bip.service;

import com.bip.entities.Beneficio;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Serviço CDI para gerenciamento de benefícios
 * Implementa operações CRUD com controle transacional
 */
@ApplicationScoped
public class BeneficioService {
    
    private static final Logger logger = Logger.getLogger(BeneficioService.class.getName());
    
    @Inject
    private EntityManager entityManager;
    
    // ================================
    // CRUD Operations
    // ================================
    
    /**
     * Cria um novo benefício
     */
    public Beneficio criar(@Valid Beneficio beneficio) {
        logger.info("Criando novo benefício: " + beneficio.getNome());
        
        entityManager.getTransaction().begin();
        try {
            // Verificar se já existe benefício com o mesmo nome
            if (existeByNome(beneficio.getNome())) {
                throw new IllegalArgumentException("Já existe um benefício com o nome: " + beneficio.getNome());
            }
            
            entityManager.persist(beneficio);
            entityManager.flush(); // Força a sincronização para obter o ID
            entityManager.getTransaction().commit();
            
            logger.info("Benefício criado com sucesso. ID: " + beneficio.getId());
            return beneficio;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    /**
     * Verifica se já existe benefício com o nome informado
     */
    private boolean existeByNome(String nome) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(b) FROM Beneficio b WHERE b.nome = :nome", Long.class);
        query.setParameter("nome", nome);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Lista todos os benefícios
     */
    public List<Beneficio> listarTodos() {
        logger.info("Listando todos os benefícios");
        
        TypedQuery<Beneficio> query = entityManager.createQuery(
            "SELECT b FROM Beneficio b ORDER BY b.id DESC", Beneficio.class);
        
        List<Beneficio> beneficios = query.getResultList();
        logger.info("Encontrados " + beneficios.size() + " benefícios");
        
        return beneficios;
    }
    
    /**
     * Lista apenas benefícios ativos
     */
    public List<Beneficio> listarAtivos() {
        logger.info("Listando benefícios ativos");
        
        TypedQuery<Beneficio> query = entityManager.createQuery(
            "SELECT b FROM Beneficio b WHERE b.ativo = true ORDER BY b.id DESC", 
            Beneficio.class);
        
        List<Beneficio> beneficios = query.getResultList();
        logger.info("Encontrados " + beneficios.size() + " benefícios ativos");
        
        return beneficios;
    }
    
    /**
     * Busca benefício por ID
     */
    public Optional<Beneficio> buscarPorId(@NotNull Long id) {
        logger.info("Buscando benefício por ID: " + id);
        
        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        return Optional.ofNullable(beneficio);
    }
    
    /**
     * Ativa um benefício
     */
    public void ativar(@NotNull Long id) {
        logger.info("Ativando benefício ID: " + id);
        
        Optional<Beneficio> beneficioOpt = buscarPorId(id);
        if (beneficioOpt.isEmpty()) {
            throw new IllegalArgumentException("Benefício não encontrado com ID: " + id);
        }
        
        Beneficio beneficio = beneficioOpt.get();
        if (Boolean.TRUE.equals(beneficio.getAtivo())) {
            throw new IllegalStateException("Benefício já está ativo");
        }
        
        beneficio.ativar();
        entityManager.merge(beneficio);
        
        logger.info("Benefício ativado com sucesso");
    }
    
    /**
     * Desativa um benefício
     */
    public void desativar(@NotNull Long id) {
        logger.info("Desativando benefício ID: " + id);
        
        Optional<Beneficio> beneficioOpt = buscarPorId(id);
        if (beneficioOpt.isEmpty()) {
            throw new IllegalArgumentException("Benefício não encontrado com ID: " + id);
        }
        
        Beneficio beneficio = beneficioOpt.get();
        if (!Boolean.TRUE.equals(beneficio.getAtivo())) {
            throw new IllegalStateException("Benefício já está inativo");
        }
        
        beneficio.desativar();
        entityManager.merge(beneficio);
        
        logger.info("Benefício desativado com sucesso");
    }
    
    /**
     * Conta benefícios ativos
     */
    public long contarAtivos() {
        logger.info("Contando benefícios ativos");
        
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(b) FROM Beneficio b WHERE b.ativo = true", Long.class);
        
        return query.getSingleResult();
    }
    
    /**
     * Calcula valor total dos benefícios ativos
     */
    public BigDecimal calcularValorTotalAtivos() {
        logger.info("Calculando valor total dos benefícios ativos");
        
        TypedQuery<BigDecimal> query = entityManager.createQuery(
            "SELECT COALESCE(SUM(b.valor), 0) FROM Beneficio b WHERE b.ativo = true", 
            BigDecimal.class);
        
        return query.getSingleResult();
    }
    
    /**
     * Busca benefício com lock para transferência
     * Usado em operações que necessitam controle de concorrência
     */
    public Optional<Beneficio> buscarComLock(@NotNull Long id) {
        logger.info("Buscando benefício com lock ID: " + id);
        
        try {
            Beneficio beneficio = entityManager.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);
            return Optional.ofNullable(beneficio);
        } catch (Exception e) {
            logger.warning("Erro ao buscar benefício com lock: " + e.getMessage());
            throw new RuntimeException("Erro ao acessar benefício para operação", e);
        }
    }
}