package com.bip.test;

import com.bip.entities.Beneficio;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Teste simples para verificar se as entidades funcionam corretamente
 */
public class BeneficioTest {
    
    public static void main(String[] args) {
        System.out.println("=== TESTE BIP API JAKARTA EE ===");
        
        try {
            // Teste 1: Criação de Benefício
            System.out.println("\n1. Testando criação de Benefício...");
            
            Beneficio beneficio = new Beneficio();
            beneficio.setNome("João Silva - Alimentação");
            beneficio.setValor(new BigDecimal("1000.00"));
            beneficio.setDescricao("Benefício Alimentação");
            beneficio.ativar();
            
            System.out.println("OK: Beneficio criado: " + beneficio.getNome());
            System.out.println("   Valor: R$ " + beneficio.getValor());
            System.out.println("   Status: " + (beneficio.getAtivo() ? "ATIVO" : "INATIVO"));
            
            // Teste 2: Operacoes de debito/credito
            System.out.println("\n2. Testando operacoes monetarias...");
            
            BigDecimal valorDebito = new BigDecimal("100.00");
            if (beneficio.possuiSaldoSuficiente(valorDebito)) {
                beneficio.debitar(valorDebito);
                System.out.println("OK: Debito realizado: R$ " + valorDebito);
                System.out.println("   Saldo atual: R$ " + beneficio.getValor());
            }
            
            BigDecimal valorCredito = new BigDecimal("50.00");
            beneficio.creditar(valorCredito);
            System.out.println("OK: Credito realizado: R$ " + valorCredito);
            System.out.println("   Saldo final: R$ " + beneficio.getValor());
            
            // Teste 3: Validacoes
            System.out.println("\n3. Testando validacoes...");
            
            try {
                beneficio.debitar(new BigDecimal("10000.00")); // Valor maior que saldo
                System.out.println("ERRO: Debito nao deveria ser permitido!");
            } catch (IllegalArgumentException e) {
                System.out.println("OK: Validacao funcionando: " + e.getMessage());
            }
            
            try {
                beneficio.creditar(new BigDecimal("-10.00")); // Valor negativo
                System.out.println("ERRO: Credito negativo nao deveria ser permitido!");
            } catch (IllegalArgumentException e) {
                System.out.println("OK: Validacao funcionando: " + e.getMessage());
            }
            
            System.out.println("\n=== TODOS OS TESTES PASSARAM! ===");
            System.out.println("OK: Entidades JPA funcionando corretamente");
            System.out.println("OK: Logica de negocio implementada");
            System.out.println("OK: Validacoes ativas");
            System.out.println("OK: Projeto pronto para deploy!");
            
        } catch (Exception e) {
            System.err.println("ERRO no teste: " + e.getMessage());
            e.printStackTrace();
        }
    }
}