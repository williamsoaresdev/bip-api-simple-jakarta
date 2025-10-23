package com.bip.application.usecases;

import com.bip.application.dtos.TransferenciaDto;
import com.bip.application.services.BeneficioService;
import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import com.bip.domain.valueobjects.Money;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApplicationScoped
public class TransferenciaUseCase {
    
    @Inject
    private BeneficioRepository beneficioRepository;
    
    @Inject
    private BeneficioService beneficioService;
    
    public void executarTransferencia(@Valid @NotNull TransferenciaDto dto) {
        validarParametrosTransferencia(dto);
        
        Beneficio origem = beneficioService.buscarPorId(dto.getBeneficioOrigemId());
        Beneficio destino = beneficioService.buscarPorId(dto.getBeneficioDestinoId());
        
        validarBeneficios(origem, destino);
        
        Money valorTransferencia = Money.of(dto.getValor());
        origem.debitar(valorTransferencia);
        destino.creditar(valorTransferencia);
        
        beneficioRepository.save(origem);
        beneficioRepository.save(destino);
    }
    
    public boolean validarTransferencia(@Valid @NotNull TransferenciaDto dto) {
        try {
            validarParametrosTransferencia(dto);
            
            Beneficio origem = beneficioService.buscarPorId(dto.getBeneficioOrigemId());
            Beneficio destino = beneficioService.buscarPorId(dto.getBeneficioDestinoId());
            
            validarBeneficios(origem, destino);
            
            Money valorTransferencia = Money.of(dto.getValor());
            return origem.possuiSaldoSuficiente(valorTransferencia);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    public Money calcularTaxa(@NotNull BigDecimal valor) {
        BigDecimal taxa = valor.multiply(new BigDecimal("0.01"));
        return Money.of(taxa);
    }
    
    private void validarParametrosTransferencia(TransferenciaDto dto) {
        beneficioService.validarBeneficiosDiferentes(dto.getBeneficioOrigemId(), dto.getBeneficioDestinoId());
        
        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo");
        }
    }
    
    private void validarBeneficios(Beneficio origem, Beneficio destino) {
        beneficioService.validarAtivo(origem);
        beneficioService.validarAtivo(destino);
    }
}