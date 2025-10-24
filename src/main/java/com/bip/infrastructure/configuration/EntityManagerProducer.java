package com.bip.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Configuração CDI para produção do EntityManager
 * Parte da camada de infraestrutura seguindo Clean Architecture
 */
@ApplicationScoped
public class EntityManagerProducer {
    
    private static final Logger LOGGER = Logger.getLogger(EntityManagerProducer.class.getName());
    private EntityManagerFactory emf;
    
    @PostConstruct
    public void init() {
        try {
            LOGGER.info("Inicializando EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("bipPU");
            LOGGER.info("EntityManagerFactory inicializado com sucesso!");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao inicializar EntityManagerFactory", e);
            throw new RuntimeException("Falha na inicialização do EntityManagerFactory", e);
        }
    }
    
    @Produces
    public EntityManager createEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory não foi inicializado");
        }
        return emf.createEntityManager();
    }
    
    @PreDestroy
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            LOGGER.info("Fechando EntityManagerFactory...");
            emf.close();
        }
    }
}