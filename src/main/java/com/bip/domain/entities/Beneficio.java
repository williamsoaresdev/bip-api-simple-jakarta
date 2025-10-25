package com.bip.domain.entities;

import com.bip.domain.valueobjects.Money;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio Benefício seguindo DDD
 * Encapsula regras de negócio e usa Value Objects
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
                query = "SELECT COALESCE(SUM(b.saldo.valor), 0) FROM Beneficio b WHERE b.ativo = true")
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
    
    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "valor", nullable = false, precision = 19, scale = 2))
    private Money saldo;
    
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
    
    /**
     * Construtor padrão para JPA
     */
    protected Beneficio() {
        this.saldo = Money.zero();
    }
    
    /**
     * Construtor para criação de benefício
     */
    public Beneficio(final String nome, final String descricao, final Money saldoInicial) {
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.descricao = descricao;
        this.saldo = saldoInicial != null ? saldoInicial : Money.zero();
        this.ativo = Boolean.TRUE;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Factory method para criar benefício
     */
    public static Beneficio criar(final String nome, final String descricao, final Money saldoInicial) {
        return new Beneficio(nome, descricao, saldoInicial);
    }

    // ================================
    // Métodos de Negócio (Domain Logic)
    // ================================
    
    /**
     * Verifica se possui saldo suficiente para débito
     */
    public boolean possuiSaldoSuficiente(final Money valorDebito) {
        if (valorDebito == null) {
            return false;
        }
        return this.saldo.isGreaterThanOrEqual(valorDebito);
    }
    
    /**
     * Realiza débito no benefício com validação de saldo
     */
    public void debitar(final Money valorDebito) {
        validarBeneficioAtivo("débito");
        
        if (valorDebito == null || !valorDebito.isPositive()) {
            throw new IllegalArgumentException("Valor de débito deve ser positivo");
        }
        
        if (!possuiSaldoSuficiente(valorDebito)) {
            throw new IllegalArgumentException(
                String.format("Saldo insuficiente. Saldo atual: %s, Tentativa de débito: %s", 
                             saldo, valorDebito));
        }
        
        this.saldo = this.saldo.subtract(valorDebito);
        this.atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Realiza crédito no benefício
     */
    public void creditar(Money valorCredito) {
        validarBeneficioAtivo("crédito");
        
        if (valorCredito == null || !valorCredito.isPositive()) {
            throw new IllegalArgumentException("Valor de crédito deve ser positivo");
        }
        
        this.saldo = this.saldo.add(valorCredito);
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
    
    /**
     * Atualiza dados do benefício (nome, descrição e saldo)
     */
    public void atualizarDados(String novoNome, String novaDescricao, java.math.BigDecimal novoValor) {
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            this.nome = novoNome.trim();
        }
        
        this.descricao = novaDescricao;
        
        if (novoValor != null && novoValor.compareTo(java.math.BigDecimal.ZERO) >= 0) {
            this.saldo = Money.of(novoValor);
        }
        
        this.atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Verifica se o benefício está ativo para operações
     */
    private void validarBeneficioAtivo(String operacao) {
        if (!ativo) {
            throw new IllegalStateException(
                String.format("Não é possível realizar %s em benefício inativo", operacao));
        }
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
        
        if (saldo == null) {
            saldo = Money.zero();
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
    // Getters (sem setters para manter encapsulamento)
    // ================================
    
    public Long getId() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public Money getSaldo() {
        return saldo != null ? saldo : Money.zero();
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
    
    public Long getVersao() {
        return versao;
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
        return String.format("Beneficio{id=%d, nome='%s', saldo=%s, ativo=%s}", 
                           id, nome, saldo, ativo);
    }
}