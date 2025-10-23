package com.bip.infrastructure.persistence;

import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação JPA do repositório de Benefício
 * Implementa interface do domain sem depender de detalhes externos
 */
@ApplicationScoped
public class BeneficioRepositoryImpl implements BeneficioRepository {
    
    @Inject
    private EntityManager entityManager;
    
    @Override
    public Optional<Beneficio> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        
        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        return Optional.ofNullable(beneficio);
    }
    
    @Override
    public Optional<Beneficio> findByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return Optional.empty();
        }
        
        try {
            TypedQuery<Beneficio> query = entityManager.createNamedQuery("Beneficio.findByName", Beneficio.class);
            query.setParameter("nome", nome.trim());
            
            Beneficio beneficio = query.getSingleResult();
            return Optional.of(beneficio);
            
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Beneficio> findAll() {
        TypedQuery<Beneficio> query = entityManager.createNamedQuery("Beneficio.findAll", Beneficio.class);
        return query.getResultList();
    }
    
    @Override
    public List<Beneficio> findAllActive() {
        TypedQuery<Beneficio> query = entityManager.createNamedQuery("Beneficio.findAllActive", Beneficio.class);
        return query.getResultList();
    }
    
    @Override
    public List<Beneficio> findByIdsWithLock(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        
        // Ordenar IDs para evitar deadlocks
        List<Long> sortedIds = ids.stream()
                .distinct()
                .sorted()
                .toList();
        
        TypedQuery<Beneficio> query = entityManager.createNamedQuery("Beneficio.findByIdsWithLock", Beneficio.class);
        query.setParameter("ids", sortedIds);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        
        return query.getResultList();
    }
    
    @Override
    public long countActive() {
        TypedQuery<Long> query = entityManager.createNamedQuery("Beneficio.countActive", Long.class);
        return query.getSingleResult();
    }
    
    @Override
    public BigDecimal sumActiveValues() {
        TypedQuery<BigDecimal> query = entityManager.createNamedQuery("Beneficio.sumActiveValues", BigDecimal.class);
        BigDecimal result = query.getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }
    
    @Override
    public Beneficio save(Beneficio beneficio) {
        if (beneficio == null) {
            throw new IllegalArgumentException("Benefício não pode ser nulo");
        }
        
        EntityTransaction transaction = entityManager.getTransaction();
        boolean startedTransaction = false;
        
        try {
            if (!transaction.isActive()) {
                transaction.begin();
                startedTransaction = true;
            }
            
            Beneficio resultado;
            if (beneficio.getId() == null) {
                // Nova entidade
                entityManager.persist(beneficio);
                entityManager.flush(); // Força a execução do INSERT para gerar o ID
                resultado = beneficio;
            } else {
                // Entidade existente
                resultado = entityManager.merge(beneficio);
            }
            
            if (startedTransaction) {
                transaction.commit();
            }
            
            return resultado;
            
        } catch (Exception e) {
            if (startedTransaction && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao salvar benefício: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(Beneficio beneficio) {
        if (beneficio == null) {
            return;
        }
        
        EntityTransaction transaction = entityManager.getTransaction();
        boolean startedTransaction = false;
        
        try {
            if (!transaction.isActive()) {
                transaction.begin();
                startedTransaction = true;
            }
            
            if (entityManager.contains(beneficio)) {
                entityManager.remove(beneficio);
            } else {
                // Buscar entidade gerenciada para remover
                Beneficio managed = entityManager.find(Beneficio.class, beneficio.getId());
                if (managed != null) {
                    entityManager.remove(managed);
                }
            }
            
            if (startedTransaction) {
                transaction.commit();
            }
            
        } catch (Exception e) {
            if (startedTransaction && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao deletar benefício: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }
        
        Beneficio beneficio = entityManager.find(Beneficio.class, id);
        if (beneficio != null) {
            entityManager.remove(beneficio);
        }
    }
    
    @Override
    public boolean existsByNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }
        
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Beneficio b WHERE UPPER(b.nome) = UPPER(:nome)", 
                Long.class);
            query.setParameter("nome", nome.trim());
            
            return query.getSingleResult() > 0;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(b) FROM Beneficio b WHERE b.id = :id", 
                Long.class);
            query.setParameter("id", id);
            
            return query.getSingleResult() > 0;
            
        } catch (Exception e) {
            return false;
        }
    }
}