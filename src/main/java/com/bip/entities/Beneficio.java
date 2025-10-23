package com.bip.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA Jakarta para Benefício
 * Implementa controle de concorrência otimista com @Version
 * e validações com Bean Validation
 */
@Entity
@Table(name = "beneficio", 
       uniqueConstraints = @UniqueConstraint(columnNames = "nome"),
       indexes = {
           @Index(name = "idx_beneficio_ativo", columnList = "ativo"),
           @Index(name = "idx_beneficio_nome", columnList = "nome")
       })
@NamedQueries({
    @NamedQuery(name = "Beneficio.findAll", 
                query = "SELECT b FROM Beneficio b ORDER BY b.id"),
    @NamedQuery(name = "Beneficio.findAllActive", 
                query = "SELECT b FROM Beneficio b WHERE b.ativo = true ORDER BY b.nome"),
    @NamedQuery(name = "Beneficio.findByName", 
                query = "SELECT b FROM Beneficio b WHERE UPPER(b.nome) = UPPER(:nome)"),
    @NamedQuery(name = "Beneficio.findByIdsWithLock",
                query = "SELECT b FROM Beneficio b WHERE b.id IN :ids ORDER BY b.id"),
    @NamedQuery(name = "Beneficio.countActive",
                query = "SELECT COUNT(b) FROM Beneficio b WHERE b.ativo = true"),
    @NamedQuery(name = "Beneficio.sumActiveValues",
                query = "SELECT COALESCE(SUM(b.valor), 0) FROM Beneficio b WHERE b.ativo = true")
})
public class Beneficio implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;
    
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    @Column(length = 500)
    private String descricao;
    
    @NotNull(message = "Valor é obrigatório")
    @PositiveOrZero(message = "Valor deve ser positivo ou zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;
    
    @NotNull
    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;
    
    @NotNull
    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;
    
    @NotNull
    @Column(nullable = false, name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @Version
    @Column(nullable = false)
    private Long versao = 0L;

    // ================================
    // Construtores
    // ================================
    
    public Beneficio() {}
    
    public Beneficio(String nome, String descricao, BigDecimal valor) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor != null ? valor : BigDecimal.ZERO;
        this.ativo = Boolean.TRUE;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    // ================================
    // Métodos de Negócio
    // ================================
    
    /**
     * Verifica se possui saldo suficiente para débito
     */
    public boolean possuiSaldoSuficiente(BigDecimal valorDebito) {
        if (valorDebito == null || valorDebito.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return this.valor.compareTo(valorDebito) >= 0;
    }
    
    /**
     * Realiza débito no benefício com validação de saldo
     */
    public void debitar(BigDecimal valorDebito) {
        if (!ativo) {
            throw new IllegalStateException("Não é possível debitar de benefício inativo");
        }
        
        if (!possuiSaldoSuficiente(valorDebito)) {
            throw new IllegalArgumentException(
                String.format("Saldo insuficiente. Saldo atual: %s, Tentativa de débito: %s", 
                             valor, valorDebito));
        }
        
        this.valor = this.valor.subtract(valorDebito);
        this.atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Realiza crédito no benefício
     */
    public void creditar(BigDecimal valorCredito) {
        if (!ativo) {
            throw new IllegalStateException("Não é possível creditar em benefício inativo");
        }
        
        if (valorCredito == null || valorCredito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de crédito deve ser positivo");
        }
        
        this.valor = this.valor.add(valorCredito);
        this.atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Ativa o benefício
     */
    public void ativar() {
        if (ativo) {
            throw new IllegalStateException("Benefício já está ativo");
        }
        this.ativo = Boolean.TRUE;
        this.atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Desativa o benefício
     */
    public void desativar() {
        if (!ativo) {
            throw new IllegalStateException("Benefício já está inativo");
        }
        this.ativo = Boolean.FALSE;
        this.atualizadoEm = LocalDateTime.now();
    }

    // ================================
    // Callbacks JPA
    // ================================
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (criadoEm == null) {
            criadoEm = now;
        }
        atualizadoEm = now;
        
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        if (ativo == null) {
            ativo = Boolean.TRUE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    // ================================
    // Getters e Setters
    // ================================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
    
    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
    
    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
    
    public Long getVersao() {
        return versao;
    }
    
    public void setVersao(Long versao) {
        this.versao = versao;
    }

    // ================================
    // Object Methods
    // ================================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beneficio beneficio = (Beneficio) o;
        return Objects.equals(id, beneficio.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Beneficio{id=%d, nome='%s', valor=%s, ativo=%s}", 
                           id, nome, valor, ativo);
    }
}