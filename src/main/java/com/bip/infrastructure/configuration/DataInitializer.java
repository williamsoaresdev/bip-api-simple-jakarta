package com.bip.infrastructure.configuration;

import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import com.bip.domain.valueobjects.Money;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    
    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    private BeneficioRepository beneficioRepository;
    
    @PostConstruct
    public void initializeData() {
        try {
            logger.info("Iniciando carregamento de dados de demonstração...");
            
            if (beneficioRepository.countActive() > 0) {
                logger.info("Dados já existem, pulando inicialização");
                return;
            }
            
            criarBeneficiosDemo();
            
            logger.info("Dados de demonstração carregados com sucesso!");
            
        } catch (Exception e) {
            logger.severe("Erro ao carregar dados iniciais: " + e.getMessage());
        }
    }
    
    private void criarBeneficiosDemo() {
        Beneficio auxAlimentacao = Beneficio.criar(
            "Auxilio Alimentacao",
            "Beneficio para alimentacao dos funcionarios",
            Money.of(new BigDecimal("500.00"))
        );
        beneficioRepository.save(auxAlimentacao);
        logger.info("Criado: " + auxAlimentacao.getNome());
        
        Beneficio valeTransporte = Beneficio.criar(
            "Vale Transporte", 
            "Beneficio para deslocamento ao trabalho",
            Money.of(new BigDecimal("200.00"))
        );
        beneficioRepository.save(valeTransporte);
        logger.info("Criado: " + valeTransporte.getNome());
        
        Beneficio planoSaude = Beneficio.criar(
            "Plano de Saude",
            "Cobertura medica e hospitalar",
            Money.of(new BigDecimal("1000.00"))
        );
        beneficioRepository.save(planoSaude);
        logger.info("Criado: " + planoSaude.getNome());
        
        Beneficio auxCombustivel = Beneficio.criar(
            "Auxilio Combustivel",
            "Ajuda de custo para combustivel",
            Money.of(new BigDecimal("300.00"))
        );
        beneficioRepository.save(auxCombustivel);
        logger.info("Criado: " + auxCombustivel.getNome());
        
        Beneficio planoOdonto = Beneficio.criar(
            "Plano Odontologico",
            "Cobertura odontologica completa",
            Money.of(new BigDecimal("150.00"))
        );
        beneficioRepository.save(planoOdonto);
        logger.info("Criado: " + planoOdonto.getNome());
        
        logger.info("Total de benefícios criados: 5");
    }
}