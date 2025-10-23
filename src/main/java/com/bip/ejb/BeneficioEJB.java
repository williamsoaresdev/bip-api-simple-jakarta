package com.bip.ejb;

import com.bip.entities.Beneficio;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * EJB Stateless para gerenciamento de benefícios
 * Implementa operações CRUD com controle transacional
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficioEJB {
    
    private static final Logger logger = Logger.getLogger(BeneficioEJB.class.getName());
    
    @PersistenceContext(unitName = "bipPU")
    private EntityManager entityManager;
    
    // ================================
    // CRUD Operations
    // ================================
    
    /**
     * Cria um novo benefício
     */
    public Beneficio criar(@Valid Beneficio beneficio) {
        logger.info("Criando novo benefício: " + beneficio.getNome());
        
        // Verificar se já existe benefício com o mesmo nome
        if (existeByNome(beneficio.getNome())) {
            throw new IllegalArgumentException("Já existe um benefício com o nome: " + beneficio.getNome());
        }
        
        entityManager.persist(beneficio);
        entityManager.flush(); // Força a sincronização para obter o ID
        
        logger.info("Benefício criado com sucesso. ID: " + beneficio.getId());
        return beneficio;
    }
    
    /**
     * Busca benefício por ID
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<Beneficio> buscarPorId(@NotNull Long id) {
        logger.fine("Buscando benefício por ID: " + id);
        
        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        return Optional.ofNullable(beneficio);
    }
    
    /**
     * Busca benefício por ID com lock pessimista
     */
    public Optional<Beneficio> buscarPorIdComLock(@NotNull Long id) {
        logger.fine("Buscando benefício por ID com lock: " + id);
        
        try {
            Beneficio beneficio = entityManager.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);
            return Optional.ofNullable(beneficio);
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Busca múltiplos benefícios por IDs com lock pessimista (ordenados para evitar deadlock)
     */
    public List<Beneficio> buscarPorIdsComLock(@NotNull List<Long> ids) {
        logger.info("Buscando benefícios por IDs com lock: " + ids);
        
        return entityManager.createNamedQuery("Beneficio.findByIdsWithLock", Beneficio.class)
                           .setParameter("ids", ids)
                           .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                           .getResultList();
    }
    
    /**
     * Lista todos os benefícios
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Beneficio> listarTodos() {
        logger.fine("Listando todos os benefícios");
        
        return entityManager.createNamedQuery("Beneficio.findAll", Beneficio.class)
                           .getResultList();
    }
    
    /**
     * Lista apenas benefícios ativos
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Beneficio> listarAtivos() {
        logger.fine("Listando benefícios ativos");
        
        return entityManager.createNamedQuery("Beneficio.findAllActive", Beneficio.class)
                           .getResultList();
    }
    
    /**
     * Atualiza benefício existente
     */
    public Beneficio atualizar(@Valid Beneficio beneficio) {
        logger.info("Atualizando benefício ID: " + beneficio.getId());
        
        if (beneficio.getId() == null) {
            throw new IllegalArgumentException("ID do benefício é obrigatório para atualização");
        }
        
        // Verificar se o benefício existe
        Beneficio existente = entityManager.find(Beneficio.class, beneficio.getId());
        if (existente == null) {
            throw new EntityNotFoundException("Benefício não encontrado com ID: " + beneficio.getId());
        }
        
        // Verificar se o novo nome não conflita com outro benefício
        if (!existente.getNome().equalsIgnoreCase(beneficio.getNome()) && 
            existeByNome(beneficio.getNome())) {
            throw new IllegalArgumentException("Já existe outro benefício com o nome: " + beneficio.getNome());
        }
        
        Beneficio atualizado = entityManager.merge(beneficio);
        logger.info("Benefício atualizado com sucesso. ID: " + atualizado.getId());
        
        return atualizado;
    }
    
    /**
     * Ativa benefício
     */
    public void ativar(@NotNull Long id) {
        logger.info("Ativando benefício ID: " + id);
        
        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        if (beneficio == null) {
            throw new EntityNotFoundException("Benefício não encontrado com ID: " + id);
        }
        
        beneficio.ativar();
        entityManager.merge(beneficio);
        
        logger.info("Benefício ativado com sucesso. ID: " + id);
    }
    
    /**
     * Desativa benefício
     */
    public void desativar(@NotNull Long id) {
        logger.info("Desativando benefício ID: " + id);
        
        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        if (beneficio == null) {
            throw new EntityNotFoundException("Benefício não encontrado com ID: " + id);
        }
        
        beneficio.desativar();
        entityManager.merge(beneficio);
        
        logger.info("Benefício desativado com sucesso. ID: " + id);
    }
    
    /**
     * Remove benefício (soft delete - apenas desativa)
     */
    public void remover(@NotNull Long id) {
        logger.info("Removendo benefício ID: " + id);
        
        // Em vez de delete físico, fazemos soft delete (desativação)
        desativar(id);
        
        logger.info("Benefício removido (desativado) com sucesso. ID: " + id);
    }
    
    // ================================
    // Métodos de Consulta
    // ================================
    
    /**
     * Verifica se existe benefício com o nome especificado
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean existeByNome(@NotNull String nome) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(b) FROM Beneficio b WHERE UPPER(b.nome) = UPPER(:nome)", Long.class)
            .setParameter("nome", nome)
            .getSingleResult();
        
        return count > 0;
    }
    
    /**
     * Busca benefício por nome
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<Beneficio> buscarPorNome(@NotNull String nome) {
        logger.fine("Buscando benefício por nome: " + nome);
        
        List<Beneficio> beneficios = entityManager.createNamedQuery("Beneficio.findByName", Beneficio.class)
                                                 .setParameter("nome", nome)
                                                 .getResultList();
        
        return beneficios.isEmpty() ? Optional.empty() : Optional.of(beneficios.get(0));
    }
    
    /**
     * Conta total de benefícios ativos
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public long contarAtivos() {
        return entityManager.createNamedQuery("Beneficio.countActive", Long.class)
                           .getSingleResult();
    }
    
    /**
     * Calcula valor total de benefícios ativos
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public BigDecimal calcularValorTotalAtivos() {
        return entityManager.createNamedQuery("Beneficio.sumActiveValues", BigDecimal.class)
                           .getSingleResult();
    }
    
    // ================================
    // Métodos de Negócio
    // ================================
    
    /**
     * Debita valor do benefício com validações
     */
    public void debitar(@NotNull Long beneficioId, @NotNull @Positive BigDecimal valor) {
        logger.info(String.format("Debitando R$ %s do benefício ID: %d", valor, beneficioId));
        
        Beneficio beneficio = entityManager.find(Beneficio.class, beneficioId, LockModeType.PESSIMISTIC_WRITE);
        if (beneficio == null) {
            throw new EntityNotFoundException("Benefício não encontrado com ID: " + beneficioId);
        }
        
        beneficio.debitar(valor);
        entityManager.merge(beneficio);
        
        logger.info(String.format("Débito realizado com sucesso. Saldo atual: R$ %s", beneficio.getValor()));
    }
    
    /**
     * Credita valor no benefício
     */
    public void creditar(@NotNull Long beneficioId, @NotNull @Positive BigDecimal valor) {
        logger.info(String.format("Creditando R$ %s no benefício ID: %d", valor, beneficioId));
        
        Beneficio beneficio = entityManager.find(Beneficio.class, beneficioId, LockModeType.PESSIMISTIC_WRITE);
        if (beneficio == null) {
            throw new EntityNotFoundException("Benefício não encontrado com ID: " + beneficioId);
        }
        
        beneficio.creditar(valor);
        entityManager.merge(beneficio);
        
        logger.info(String.format("Crédito realizado com sucesso. Saldo atual: R$ %s", beneficio.getValor()));
    }
}