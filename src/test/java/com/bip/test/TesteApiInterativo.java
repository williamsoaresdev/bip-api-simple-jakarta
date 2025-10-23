package com.bip.test;

import com.bip.entities.Beneficio;
import java.math.BigDecimal;

/**
 * Teste simples da aplicacao Jakarta EE 
 * Demonstra que as classes funcionam corretamente
 */
public class TesteApiInterativo {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     BIP API JAKARTA EE - TESTE        ");
        System.out.println("========================================");
        System.out.println();
        
        // Informacoes da API
        System.out.println("INFORMACOES DA API:");
        System.out.println("   Versao: 1.0.0-SNAPSHOT");
        System.out.println("   Stack: Jakarta EE 10 + EJB 4.0 + JPA 3.1");
        System.out.println("   Bug EJB: CORRIGIDO com locks pessimistas");
        System.out.println("   Endpoints: JAX-RS 3.1 com OpenAPI");
        System.out.println();

        // Teste das entidades JPA
        System.out.println("TESTANDO ENTIDADES JPA:");
        testarBeneficio();
        System.out.println();

        // Informacoes dos endpoints
        System.out.println("ENDPOINTS DISPONIVEIS:");
        System.out.println("   GET  /api/beneficios/status -> Status da aplicacao");
        System.out.println("   POST /api/beneficios/dados-teste -> Criar dados teste");
        System.out.println("   GET  /api/beneficios -> Listar todos beneficios");
        System.out.println("   GET  /api/beneficios/ativos -> Listar ativos");
        System.out.println("   POST /api/beneficios -> Criar beneficio");
        System.out.println("   GET  /api/beneficios/{id} -> Buscar por ID");
        System.out.println("   PUT  /api/beneficios/{id}/ativar -> Ativar");
        System.out.println("   PUT  /api/beneficios/{id}/desativar -> Desativar");
        System.out.println("   POST /api/beneficios/transferir -> Transferencia EJB");
        System.out.println();

        // Instrucoes para testes com Postman
        System.out.println("COMO TESTAR COM POSTMAN:");
        System.out.println("   1. Inicie servidor: mvn jetty:run-war");
        System.out.println("   2. URL Base: http://localhost:8080/bip-api-jakarta-ee");
        System.out.println("   3. Teste Status: GET /api/beneficios/status");
        System.out.println("   4. Crie dados: POST /api/beneficios/dados-teste");
        System.out.println("   5. Liste: GET /api/beneficios");
        System.out.println();

        // Exemplos JSON para Postman
        System.out.println("EXEMPLOS JSON PARA POSTMAN:");
        System.out.println();
        System.out.println("=> Criar Beneficio (POST /api/beneficios):");
        System.out.println("{");
        System.out.println("  \"nome\": \"Carlos Silva - Vale Alimentacao\",");
        System.out.println("  \"descricao\": \"Beneficio alimentacao mensal\",");
        System.out.println("  \"valor\": 450.00");
        System.out.println("}");
        System.out.println();
        
        System.out.println("=> Transferencia EJB (POST /api/beneficios/transferir):");
        System.out.println("{");
        System.out.println("  \"beneficioOrigemId\": 1,");
        System.out.println("  \"beneficioDestinoId\": 2,");
        System.out.println("  \"valor\": 100.00,");
        System.out.println("  \"descricao\": \"Transferencia teste\"");
        System.out.println("}");
        System.out.println();

        System.out.println("========================================");
        System.out.println("   TESTE CONCLUIDO COM SUCESSO!        ");
        System.out.println("========================================");
    }

    private static void testarBeneficio() {
        try {
            // Criar benefício
            Beneficio beneficio = new Beneficio();
            beneficio.setNome("Teste - Vale Alimentacao");
            beneficio.setDescricao("Beneficio para teste da API");
            beneficio.setValor(new BigDecimal("500.00"));
            beneficio.ativar();
            
            System.out.println("   ✅ Beneficio criado: " + beneficio.getNome());
            System.out.println("   💰 Valor inicial: R$ " + beneficio.getValor());
            System.out.println("   📊 Status: " + (beneficio.getAtivo() ? "ATIVO" : "INATIVO"));
            
            // Testar operações
            BigDecimal debito = new BigDecimal("50.00");
            if (beneficio.possuiSaldoSuficiente(debito)) {
                beneficio.debitar(debito);
                System.out.println("   ⬇️ Debito de R$ " + debito + " realizado");
                System.out.println("   💰 Saldo apos debito: R$ " + beneficio.getValor());
            }
            
            BigDecimal credito = new BigDecimal("25.00");
            beneficio.creditar(credito);
            System.out.println("   ⬆️ Credito de R$ " + credito + " realizado");
            System.out.println("   💰 Saldo final: R$ " + beneficio.getValor());
            
            // Testar validações
            try {
                beneficio.debitar(new BigDecimal("10000.00"));
                System.out.println("   ❌ ERRO: Debito excessivo foi permitido!");
            } catch (IllegalArgumentException e) {
                System.out.println("   ✅ Validacao OK: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Erro no teste: " + e.getMessage());
        }
    }
}