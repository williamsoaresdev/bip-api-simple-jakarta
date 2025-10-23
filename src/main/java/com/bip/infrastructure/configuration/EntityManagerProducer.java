package com.bip.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Configuração CDI para produção do EntityManager
 * Parte da camada de infraestrutura seguindo Clean Architecture
 */
@ApplicationScoped
public class EntityManagerProducer {
    
    private EntityManagerFactory emf;
    
    @PostConstruct
    public void init() {
        emf = Persistence.createEntityManagerFactory("bipPU");
    }
    
    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
    
    @PreDestroy
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}