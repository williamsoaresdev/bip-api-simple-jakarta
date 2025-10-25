package com.bip.application.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para representar o histórico de transferências realizadas.
 * Usado para listar e exibir transferências já executadas.
 * 
 * @author BIP API Team
 * @since 1.0
 * @version 3.0.0
 */
public class HistoricoTransferenciaDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long beneficioOrigemId;
    private String beneficioOrigemNome;
    private Long beneficioDestinoId;
    private String beneficioDestinoNome;
    private BigDecimal valor;
    private BigDecimal taxa;
    private String descricao;
    private LocalDateTime dataExecucao;
    private String status;
    
    public HistoricoTransferenciaDto() {}
    
    public HistoricoTransferenciaDto(Long id, Long beneficioOrigemId, String beneficioOrigemNome,
                                    Long beneficioDestinoId, String beneficioDestinoNome,
                                    BigDecimal valor, BigDecimal taxa, String descricao,
                                    LocalDateTime dataExecucao, String status) {
        this.id = id;
        this.beneficioOrigemId = beneficioOrigemId;
        this.beneficioOrigemNome = beneficioOrigemNome;
        this.beneficioDestinoId = beneficioDestinoId;
        this.beneficioDestinoNome = beneficioDestinoNome;
        this.valor = valor;
        this.taxa = taxa;
        this.descricao = descricao;
        this.dataExecucao = dataExecucao;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBeneficioOrigemId() {
        return beneficioOrigemId;
    }
    
    public void setBeneficioOrigemId(Long beneficioOrigemId) {
        this.beneficioOrigemId = beneficioOrigemId;
    }
    
    public String getBeneficioOrigemNome() {
        return beneficioOrigemNome;
    }
    
    public void setBeneficioOrigemNome(String beneficioOrigemNome) {
        this.beneficioOrigemNome = beneficioOrigemNome;
    }
    
    public Long getBeneficioDestinoId() {
        return beneficioDestinoId;
    }
    
    public void setBeneficioDestinoId(Long beneficioDestinoId) {
        this.beneficioDestinoId = beneficioDestinoId;
    }
    
    public String getBeneficioDestinoNome() {
        return beneficioDestinoNome;
    }
    
    public void setBeneficioDestinoNome(String beneficioDestinoNome) {
        this.beneficioDestinoNome = beneficioDestinoNome;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public BigDecimal getTaxa() {
        return taxa;
    }
    
    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public LocalDateTime getDataExecucao() {
        return dataExecucao;
    }
    
    public void setDataExecucao(LocalDateTime dataExecucao) {
        this.dataExecucao = dataExecucao;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("HistoricoTransferenciaDto{id=%d, origem=%s->%s, valor=%s, data=%s}", 
                           id, beneficioOrigemNome, beneficioDestinoNome, valor, dataExecucao);
    }
}