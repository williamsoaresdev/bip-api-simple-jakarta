package com.bip.application.usecases;

import com.bip.application.dtos.HistoricoTransferenciaDto;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * Lista todas as transferências realizadas (simulação para demonstração).
     * Em um cenário real, isso consultaria uma tabela de histórico de transferências.
     * 
     * @return lista de transferências realizadas
     */
    public List<HistoricoTransferenciaDto> listarTransferencias() {
        // Simulação de dados para demonstração
        // Em um cenário real, isso consultaria o repositório de transferências
        List<HistoricoTransferenciaDto> transferencias = new ArrayList<>();
        
        // Buscar alguns benefícios para simular transferências
        List<Beneficio> beneficios = beneficioRepository.findAll();
        
        if (beneficios.size() >= 2) {
            Beneficio origem = beneficios.get(0);
            Beneficio destino = beneficios.get(1);
            
            // Simular algumas transferências de exemplo
            transferencias.add(new HistoricoTransferenciaDto(
                1L,
                origem.getId(),
                origem.getNome(),
                destino.getId(), 
                destino.getNome(),
                new BigDecimal("100.00"),
                new BigDecimal("1.00"),
                "Transferência de exemplo",
                LocalDateTime.now().minusDays(1),
                "CONCLUIDA"
            ));
            
            transferencias.add(new HistoricoTransferenciaDto(
                2L,
                destino.getId(),
                destino.getNome(),
                origem.getId(),
                origem.getNome(),
                new BigDecimal("250.00"),
                new BigDecimal("2.50"),
                "Transferência de retorno",
                LocalDateTime.now().minusHours(6),
                "CONCLUIDA"
            ));
            
            transferencias.add(new HistoricoTransferenciaDto(
                3L,
                origem.getId(),
                origem.getNome(),
                destino.getId(),
                destino.getNome(),
                new BigDecimal("50.00"),
                new BigDecimal("0.50"),
                "Transferência recente",
                LocalDateTime.now().minusMinutes(30),
                "CONCLUIDA"
            ));
        }
        
        return transferencias;
    }
    
    /**
     * Conta o total de transferências realizadas (simulação).
     * 
     * @return total de transferências
     */
    public Long contarTransferencias() {
        // Em um cenário real, isso faria um COUNT na tabela de transferências
        return (long) listarTransferencias().size();
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