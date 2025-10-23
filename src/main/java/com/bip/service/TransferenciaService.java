package com.bip.service;

import com.bip.entities.Beneficio;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Serviço CDI para transferências entre benefícios
 * Implementa controle de concorrência e transações seguras
 */
@ApplicationScoped
public class TransferenciaService {
    
    private static final Logger logger = Logger.getLogger(TransferenciaService.class.getName());
    private static final ReentrantLock lock = new ReentrantLock();
    
    @Inject
    private BeneficioService beneficioService;
    
    /**
     * Executa transferência entre benefícios
     */
    public TransferenciaResultado executarTransferencia(
            @NotNull Long origemId,
            @NotNull Long destinoId, 
            @NotNull BigDecimal valor,
            String descricao) {
        
        logger.info(String.format("=== TRANSFERÊNCIA CDI ==="));
        logger.info(String.format("Origem: %d -> Destino: %d | Valor: R$ %s", 
                                origemId, destinoId, valor));
        
        // Validações básicas
        if (origemId.equals(destinoId)) {
            return new TransferenciaResultado(false, "Não é possível transferir para o mesmo benefício");
        }
        
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return new TransferenciaResultado(false, "Valor deve ser positivo");
        }
        
        // Lock global para evitar deadlocks
        lock.lock();
        try {
            // Buscar benefícios com lock
            Optional<Beneficio> origemOpt = beneficioService.buscarComLock(origemId);
            Optional<Beneficio> destinoOpt = beneficioService.buscarComLock(destinoId);
            
            if (origemOpt.isEmpty()) {
                return new TransferenciaResultado(false, "Benefício de origem não encontrado");
            }
            
            if (destinoOpt.isEmpty()) {
                return new TransferenciaResultado(false, "Benefício de destino não encontrado");
            }
            
            Beneficio origem = origemOpt.get();
            Beneficio destino = destinoOpt.get();
            
            // Validações de negócio
            if (!Boolean.TRUE.equals(origem.getAtivo())) {
                return new TransferenciaResultado(false, "Benefício de origem está inativo");
            }
            
            if (!Boolean.TRUE.equals(destino.getAtivo())) {
                return new TransferenciaResultado(false, "Benefício de destino está inativo");
            }
            
            if (origem.getValor().compareTo(valor) < 0) {
                return new TransferenciaResultado(false, 
                    String.format("Saldo insuficiente. Disponível: R$ %s", origem.getValor()));
            }
            
            // Capturar saldos anteriores
            BigDecimal saldoAnteriorOrigem = origem.getValor();
            BigDecimal saldoAnteriorDestino = destino.getValor();
            
            // Executar transferência
            origem.setValor(origem.getValor().subtract(valor));
            destino.setValor(destino.getValor().add(valor));
            
            logger.info("✅ Transferência realizada com sucesso!");
            logger.info(String.format("Origem: R$ %s -> R$ %s", saldoAnteriorOrigem, origem.getValor()));
            logger.info(String.format("Destino: R$ %s -> R$ %s", saldoAnteriorDestino, destino.getValor()));
            
            return new TransferenciaResultado(
                true,
                "Transferência realizada com sucesso",
                valor,
                saldoAnteriorOrigem,
                origem.getValor(),
                saldoAnteriorDestino,
                destino.getValor(),
                LocalDateTime.now()
            );
            
        } catch (Exception e) {
            logger.severe("Erro na transferência: " + e.getMessage());
            return new TransferenciaResultado(false, "Erro interno: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Classe resultado da transferência
     */
    public static class TransferenciaResultado {
        private final boolean sucesso;
        private final String mensagem;
        private final BigDecimal valorTransferido;
        private final BigDecimal saldoAnteriorOrigem;
        private final BigDecimal saldoPosteriorOrigem;
        private final BigDecimal saldoAnteriorDestino;
        private final BigDecimal saldoPosteriorDestino;
        private final LocalDateTime dataHora;
        
        public TransferenciaResultado(boolean sucesso, String mensagem) {
            this(sucesso, mensagem, null, null, null, null, null, null);
        }
        
        public TransferenciaResultado(boolean sucesso, String mensagem, 
                                    BigDecimal valorTransferido,
                                    BigDecimal saldoAnteriorOrigem,
                                    BigDecimal saldoPosteriorOrigem,
                                    BigDecimal saldoAnteriorDestino,
                                    BigDecimal saldoPosteriorDestino,
                                    LocalDateTime dataHora) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.valorTransferido = valorTransferido;
            this.saldoAnteriorOrigem = saldoAnteriorOrigem;
            this.saldoPosteriorOrigem = saldoPosteriorOrigem;
            this.saldoAnteriorDestino = saldoAnteriorDestino;
            this.saldoPosteriorDestino = saldoPosteriorDestino;
            this.dataHora = dataHora;
        }
        
        // Getters
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public BigDecimal getValorTransferido() { return valorTransferido; }
        public BigDecimal getSaldoAnteriorOrigem() { return saldoAnteriorOrigem; }
        public BigDecimal getSaldoPosteriorOrigem() { return saldoPosteriorOrigem; }
        public BigDecimal getSaldoAnteriorDestino() { return saldoAnteriorDestino; }
        public BigDecimal getSaldoPosteriorDestino() { return saldoPosteriorDestino; }
        public LocalDateTime getDataHora() { return dataHora; }
    }
}