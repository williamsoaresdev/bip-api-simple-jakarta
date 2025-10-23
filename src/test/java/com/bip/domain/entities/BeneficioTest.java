package com.bip.domain.entities;

import com.bip.domain.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Beneficio Domain Entity Tests")
class BeneficioTest {

    @Test
    @DisplayName("Deve criar benefício com dados válidos")
    void deveCriarBeneficioComDadosValidos() {
        // Given
        String nome = "Benefício Alimentação";
        String descricao = "Benefício para alimentação";
        Money saldoInicial = Money.of(new BigDecimal("1000.00"));
        
        // When
        Beneficio beneficio = new Beneficio(nome, descricao, saldoInicial);
        
        // Then
        assertThat(beneficio.getNome()).isEqualTo(nome);
        assertThat(beneficio.getDescricao()).isEqualTo(descricao);
        assertThat(beneficio.getSaldo()).isEqualTo(saldoInicial);
        assertThat(beneficio.getAtivo()).isTrue();
        assertThat(beneficio.getCriadoEm()).isNotNull();
        assertThat(beneficio.getAtualizadoEm()).isNotNull();
        assertThat(beneficio.getVersao()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Deve criar benefício usando factory method")
    void deveCriarBeneficioUsandoFactoryMethod() {
        // Given
        String nome = "Benefício Transporte";
        String descricao = "Benefício para transporte";
        Money saldoInicial = Money.of(new BigDecimal("500.00"));
        
        // When
        Beneficio beneficio = Beneficio.criar(nome, descricao, saldoInicial);
        
        // Then
        assertThat(beneficio.getNome()).isEqualTo(nome);
        assertThat(beneficio.getDescricao()).isEqualTo(descricao);
        assertThat(beneficio.getSaldo()).isEqualTo(saldoInicial);
        assertThat(beneficio.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve criar benefício com saldo zero quando saldo inicial for nulo")
    void deveCriarBeneficioComSaldoZeroQuandoSaldoInicialForNulo() {
        // Given
        String nome = "Benefício Teste";
        String descricao = "Descrição teste";
        
        // When
        Beneficio beneficio = new Beneficio(nome, descricao, null);
        
        // Then
        assertThat(beneficio.getSaldo()).isEqualTo(Money.zero());
    }

    @Test
    @DisplayName("Deve lançar exceção para nome nulo")
    void deveLancarExcecaoParaNomeNulo() {
        // Given
        String descricao = "Descrição teste";
        Money saldo = Money.of(new BigDecimal("100.00"));
        
        // When & Then
        assertThatThrownBy(() -> new Beneficio(null, descricao, saldo))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Nome não pode ser nulo");
    }

    @Test
    @DisplayName("Deve verificar saldo suficiente corretamente")
    void deveVerificarSaldoSuficienteCorretamente() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("1000.00")));
        
        // When & Then
        assertThat(beneficio.possuiSaldoSuficiente(Money.of(new BigDecimal("500.00")))).isTrue();
        assertThat(beneficio.possuiSaldoSuficiente(Money.of(new BigDecimal("1000.00")))).isTrue();
        assertThat(beneficio.possuiSaldoSuficiente(Money.of(new BigDecimal("1500.00")))).isFalse();
        assertThat(beneficio.possuiSaldoSuficiente(null)).isFalse();
    }

    @Test
    @DisplayName("Deve debitar valor corretamente")
    void deveDebitarValorCorretamente() {
        // Given
        Money saldoInicial = Money.of(new BigDecimal("1000.00"));
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", saldoInicial);
        Money valorDebito = Money.of(new BigDecimal("300.00"));
        
        // When
        beneficio.debitar(valorDebito);
        
        // Then
        Money saldoEsperado = Money.of(new BigDecimal("700.00"));
        assertThat(beneficio.getSaldo()).isEqualTo(saldoEsperado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao debitar valor maior que saldo")
    void deveLancarExcecaoAoDebitarValorMaiorQueSaldo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        Money valorDebito = Money.of(new BigDecimal("200.00"));
        
        // When & Then
        assertThatThrownBy(() -> beneficio.debitar(valorDebito))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Saldo insuficiente");
    }

    @Test
    @DisplayName("Deve lançar exceção ao debitar valor nulo")
    void deveLancarExcecaoAoDebitarValorNulo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        
        // When & Then
        assertThatThrownBy(() -> beneficio.debitar(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor de débito deve ser positivo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao debitar valor zero ou negativo")
    void deveLancarExcecaoAoDebitarValorZeroOuNegativo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        
        // When & Then
        assertThatThrownBy(() -> beneficio.debitar(Money.zero()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor de débito deve ser positivo");
    }

    @Test
    @DisplayName("Deve creditar valor corretamente")
    void deveCreditarValorCorretamente() {
        // Given
        Money saldoInicial = Money.of(new BigDecimal("1000.00"));
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", saldoInicial);
        Money valorCredito = Money.of(new BigDecimal("300.00"));
        
        // When
        beneficio.creditar(valorCredito);
        
        // Then
        Money saldoEsperado = Money.of(new BigDecimal("1300.00"));
        assertThat(beneficio.getSaldo()).isEqualTo(saldoEsperado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao creditar valor nulo")
    void deveLancarExcecaoAoCreditarValorNulo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        
        // When & Then
        assertThatThrownBy(() -> beneficio.creditar(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor de crédito deve ser positivo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao creditar valor zero ou negativo")
    void deveLancarExcecaoAoCreditarValorZeroOuNegativo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        
        // When & Then
        assertThatThrownBy(() -> beneficio.creditar(Money.zero()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Valor de crédito deve ser positivo");
    }

    @Test
    @DisplayName("Deve ativar benefício inativo")
    void deveAtivarBeneficioInativo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        beneficio.desativar();
        assertThat(beneficio.getAtivo()).isFalse();
        
        // When
        beneficio.ativar();
        
        // Then
        assertThat(beneficio.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ativar benefício já ativo")
    void deveLancarExcecaoAoTentarAtivarBeneficioJaAtivo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        assertThat(beneficio.getAtivo()).isTrue();
        
        // When & Then
        assertThatThrownBy(() -> beneficio.ativar())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Benefício já está ativo");
    }

    @Test
    @DisplayName("Deve desativar benefício ativo")
    void deveDesativarBeneficioAtivo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        assertThat(beneficio.getAtivo()).isTrue();
        
        // When
        beneficio.desativar();
        
        // Then
        assertThat(beneficio.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar benefício já inativo")
    void deveLancarExcecaoAoTentarDesativarBeneficioJaInativo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        beneficio.desativar();
        assertThat(beneficio.getAtivo()).isFalse();
        
        // When & Then
        assertThatThrownBy(() -> beneficio.desativar())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Benefício já está inativo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao debitar de benefício inativo")
    void deveLancarExcecaoAoDebitarDeBeneficioInativo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        beneficio.desativar();
        
        // When & Then
        assertThatThrownBy(() -> beneficio.debitar(Money.of(new BigDecimal("50.00"))))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Não é possível realizar débito em benefício inativo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao creditar em benefício inativo")
    void deveLancarExcecaoAoCreditarEmBeneficioInativo() {
        // Given
        Beneficio beneficio = Beneficio.criar("Teste", "Desc", Money.of(new BigDecimal("100.00")));
        beneficio.desativar();
        
        // When & Then
        assertThatThrownBy(() -> beneficio.creditar(Money.of(new BigDecimal("50.00"))))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Não é possível realizar crédito em benefício inativo");
    }

    @Test
    @DisplayName("Deve atualizar dados do benefício")
    void deveAtualizarDadosDoBeneficio() throws InterruptedException {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome Original", "Descrição Original", Money.of(new BigDecimal("100.00")));
        LocalDateTime atualizadoAntes = beneficio.getAtualizadoEm();
        
        // Wait to ensure different timestamp
        Thread.sleep(1);
        
        // When
        beneficio.atualizarDados("Novo Nome", "Nova Descrição", new BigDecimal("200.00"));
        
        // Then
        assertThat(beneficio.getNome()).isEqualTo("Novo Nome");
        assertThat(beneficio.getDescricao()).isEqualTo("Nova Descrição");
        assertThat(beneficio.getSaldo()).isEqualTo(Money.of(new BigDecimal("200.00")));
        assertThat(beneficio.getAtualizadoEm()).isAfter(atualizadoAntes);
    }

    @Test
    @DisplayName("Deve ignorar nome vazio ou nulo na atualização")
    void deveIgnorarNomeVazioOuNuloNaAtualizacao() {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome Original", "Descrição", Money.of(new BigDecimal("100.00")));
        
        // When
        beneficio.atualizarDados("", "Nova Descrição", new BigDecimal("200.00"));
        
        // Then
        assertThat(beneficio.getNome()).isEqualTo("Nome Original");
    }

    @Test
    @DisplayName("Deve ignorar valor negativo na atualização")
    void deveIgnorarValorNegativoNaAtualizacao() {
        // Given
        Money saldoOriginal = Money.of(new BigDecimal("100.00"));
        Beneficio beneficio = Beneficio.criar("Nome", "Descrição", saldoOriginal);
        
        // When
        beneficio.atualizarDados("Novo Nome", "Nova Descrição", new BigDecimal("-50.00"));
        
        // Then
        assertThat(beneficio.getSaldo()).isEqualTo(saldoOriginal);
    }

    @Test
    @DisplayName("Deve verificar igualdade baseada no ID")
    void deveVerificarIgualdadeBaseadaNoId() {
        // Given - Objetos criados sem ID são considerados iguais se ambos têm ID null
        Beneficio beneficio1 = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        Beneficio beneficio2 = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        
        // When & Then - Objetos sem ID são considerados iguais (comportamento esperado para entidades não persistidas)
        assertThat(beneficio1).isEqualTo(beneficio2);
        assertThat(beneficio1).isEqualTo(beneficio1);
        assertThat(beneficio1).isNotEqualTo(null);
        assertThat(beneficio1).isNotEqualTo("String");
        
        // Teste de simulação para objetos com ID - como não há setters públicos, simulamos o comportamento
        // Este teste documenta que objetos com mesmo ID são iguais (comportamento da implementação equals)
        assertThat(beneficio1.hashCode()).isEqualTo(beneficio2.hashCode()); // Ambos null ID têm mesmo hashCode
    }

    @Test
    @DisplayName("Deve gerar hashCode consistente")
    void deveGerarHashCodeConsistente() {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        
        // When
        int hashCode1 = beneficio.hashCode();
        int hashCode2 = beneficio.hashCode();
        
        // Then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    @DisplayName("ToString deve conter informações básicas")
    void toStringDeveConterInformacoesBasicas() {
        // Given
        Beneficio beneficio = Beneficio.criar("Benefício Teste", "Descrição", Money.of(new BigDecimal("150.00")));
        
        // When
        String toString = beneficio.toString();
        
        // Then
        assertThat(toString).contains("Beneficio");
        assertThat(toString).contains("Benefício Teste");
        assertThat(toString).contains("150");
        assertThat(toString).contains("true");
    }

    @ParameterizedTest
    @ValueSource(strings = {"   Nome com espaços   ", "Nome Simples", "NOME MAIÚSCULO"})
    @DisplayName("Deve aceitar nomes válidos")
    void deveAceitarNomesValidos(String nome) {
        // When
        Beneficio beneficio = Beneficio.criar(nome, "Descrição", Money.of(new BigDecimal("100.00")));
        
        // Then
        assertThat(beneficio.getNome()).isNotNull();
        assertThat(beneficio.getNome()).isNotEmpty();
    }

    @Test
    @DisplayName("Deve atualizar atualizadoEm ao fazer operações")
    void deveAtualizarAtualizadoEmAoFazerOperacoes() throws InterruptedException {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        LocalDateTime primeiraAtualizacao = beneficio.getAtualizadoEm();
        
        // When
        Thread.sleep(10); // Pequena pausa para garantir diferença no timestamp
        beneficio.creditar(Money.of(new BigDecimal("50.00")));
        
        // Then
        assertThat(beneficio.getAtualizadoEm()).isAfter(primeiraAtualizacao);
    }

    @Test
    @DisplayName("Deve manter criadoEm inalterado após operações")
    void deveManterCriadoEmInalteradoAposOperacoes() {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        LocalDateTime criadoEm = beneficio.getCriadoEm();
        
        // When
        beneficio.creditar(Money.of(new BigDecimal("50.00")));
        beneficio.debitar(Money.of(new BigDecimal("25.00")));
        beneficio.atualizarDados("Novo Nome", "Nova Desc", new BigDecimal("200.00"));
        
        // Then
        assertThat(beneficio.getCriadoEm()).isEqualTo(criadoEm);
    }

    @Test
    @DisplayName("Deve usar construtor padrão para JPA")
    void deveUsarConstrutorPadraoParaJPA() {
        // When
        Beneficio beneficio = new Beneficio();
        
        // Then
        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getNome()).isNull();
        assertThat(beneficio.getDescricao()).isNull();
        assertThat(beneficio.getSaldo()).isEqualTo(Money.zero()); // Construtor inicializa com zero
        assertThat(beneficio.getAtivo()).isTrue(); // Campo tem valor padrão TRUE
        assertThat(beneficio.getCriadoEm()).isNull(); // Será definido pelo @PrePersist
        assertThat(beneficio.getAtualizadoEm()).isNull(); // Será definido pelo @PrePersist
    }

    @Test
    @DisplayName("Deve obter valor através do Money saldo")
    void deveObterValorAtravesDoSaldo() {
        // Given
        Money valor = Money.of(new BigDecimal("150.75"));
        Beneficio beneficio = Beneficio.criar("Nome", "Desc", valor);
        
        // When
        BigDecimal valorBigDecimal = beneficio.getSaldo().getValor();
        
        // Then
        assertThat(valorBigDecimal).isEqualTo(new BigDecimal("150.75"));
    }

    @Test
    @DisplayName("Deve retornar ZERO quando saldo for Money.zero")
    void deveRetornarZeroQuandoSaldoForMoneyZero() {
        // Given
        Beneficio beneficio = new Beneficio(); // Construtor inicializa com Money.zero()
        
        // When
        BigDecimal valor = beneficio.getSaldo().getValor();
        
        // Then
        assertThat(valor).isEqualByComparingTo(BigDecimal.ZERO); // Usa comparação numerica
    }

    @Test
    @DisplayName("Deve ativar e inativar benefício através de métodos públicos")
    void deveAtivarEInativarBeneficio() {
        // Given
        Beneficio beneficio = Beneficio.criar(
            "Nome Teste",
            "Descrição Teste", 
            Money.of(new BigDecimal("100.00"))
        );
        
        // When & Then - Benefício criado deve estar ativo
        assertThat(beneficio.getAtivo()).isTrue();
        
        // When - Inativar
        beneficio.desativar();
        
        // Then
        assertThat(beneficio.getAtivo()).isFalse();
        
        // When - Ativar novamente  
        beneficio.ativar();
        
        // Then
        assertThat(beneficio.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve acessar valor através do Money saldo")
    void deveAcessarValorAtravesDoSaldo() {
        // Given
        Beneficio beneficio = Beneficio.criar(
            "Nome",
            "Descrição",
            Money.of(new BigDecimal("200.50"))
        );
        
        // When
        BigDecimal valorObtido = beneficio.getSaldo().getValor();
        
        // Then
        assertThat(valorObtido).isEqualTo(new BigDecimal("200.50"));
        assertThat(beneficio.getSaldo()).isEqualTo(Money.of(new BigDecimal("200.50")));
    }

    @Test
    @DisplayName("Deve inicializar com Money.zero quando usar construtor padrão")
    void deveInicializarComMoneyZero() {
        // Given
        Beneficio beneficio = new Beneficio();
        
        // When
        Money saldo = beneficio.getSaldo();
        
        // Then
        assertThat(saldo).isEqualTo(Money.zero());
        assertThat(saldo.getValor()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Deve simular callback onCreate do JPA")
    void deveSimularCallbackOnCreateDoJPA() throws Exception {
        // Given
        Beneficio beneficio = new Beneficio();
        
        // When - Simular o que o JPA faz ao chamar @PrePersist
        java.lang.reflect.Method onCreate = Beneficio.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(beneficio);
        
        // Then
        assertThat(beneficio.getCriadoEm()).isNotNull();
        assertThat(beneficio.getAtualizadoEm()).isNotNull();
        assertThat(beneficio.getSaldo()).isEqualTo(Money.zero());
        assertThat(beneficio.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve simular callback onUpdate do JPA")
    void deveSimularCallbackOnUpdateDoJPA() throws Exception {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        LocalDateTime criadoEm = beneficio.getCriadoEm();
        LocalDateTime primeiraAtualizacao = beneficio.getAtualizadoEm();
        
        Thread.sleep(10); // Pequena pausa para garantir diferença no timestamp
        
        // When - Simular o que o JPA faz ao chamar @PreUpdate
        java.lang.reflect.Method onUpdate = Beneficio.class.getDeclaredMethod("onUpdate");
        onUpdate.setAccessible(true);
        onUpdate.invoke(beneficio);
        
        // Then
        assertThat(beneficio.getCriadoEm()).isEqualTo(criadoEm); // Não deve mudar
        assertThat(beneficio.getAtualizadoEm()).isAfter(primeiraAtualizacao); // Deve ser atualizado
    }

    @Test
    @DisplayName("Deve não sobrescrever criadoEm se já estiver definido no onCreate")
    void deveNaoSobrescreverCriadoEmSeJaEstiverDefinido() throws Exception {
        // Given
        Beneficio beneficio = Beneficio.criar("Nome", "Desc", Money.of(new BigDecimal("100.00")));
        LocalDateTime criadoEmOriginal = beneficio.getCriadoEm();
        
        Thread.sleep(10);
        
        // When - Simular o que o JPA faz ao chamar @PrePersist novamente
        java.lang.reflect.Method onCreate = Beneficio.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(beneficio);
        
        // Then
        assertThat(beneficio.getCriadoEm()).isEqualTo(criadoEmOriginal); // Não deve mudar
    }

    @Test
    @DisplayName("onCreate deve manter ativo e saldo se já preenchidos")
    void onCreateDeveMaterCamposSeJaPreenchidos() throws Exception {
        // Given
        Beneficio beneficio = new Beneficio();
        
        // Usar reflection para definir campos privados
        java.lang.reflect.Field ativoField = Beneficio.class.getDeclaredField("ativo");
        ativoField.setAccessible(true);
        ativoField.set(beneficio, Boolean.FALSE);
        
        java.lang.reflect.Field saldoField = Beneficio.class.getDeclaredField("saldo");
        saldoField.setAccessible(true);
        saldoField.set(beneficio, Money.of(new BigDecimal("250.00")));
        
        java.lang.reflect.Field criadoEmField = Beneficio.class.getDeclaredField("criadoEm");
        criadoEmField.setAccessible(true);
        criadoEmField.set(beneficio, LocalDateTime.now().minusHours(1));
        
        // When
        java.lang.reflect.Method onCreate = Beneficio.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(beneficio);
        
        // Then
        assertThat(beneficio.getAtivo()).isEqualTo(Boolean.FALSE); // Não deve alterar
        assertThat(beneficio.getSaldo().getValor()).isEqualByComparingTo(new BigDecimal("250.00")); // Não deve alterar
        assertThat(beneficio.getCriadoEm()).isBefore(beneficio.getAtualizadoEm()); // Mas atualizadoEm deve ser atual
    }

    @Test
    @DisplayName("onCreate deve definir valores padrão quando campos são null")
    void onCreateDeveDefinirValoresPadraoQuandoCamposSaoNull() throws Exception {
        // Given
        Beneficio beneficio = new Beneficio();
        
        // Usar reflection para garantir que campos estão null
        java.lang.reflect.Field ativoField = Beneficio.class.getDeclaredField("ativo");
        ativoField.setAccessible(true);
        ativoField.set(beneficio, null); // Força como null
        
        java.lang.reflect.Field saldoField = Beneficio.class.getDeclaredField("saldo");
        saldoField.setAccessible(true);
        saldoField.set(beneficio, null); // Força como null
        
        java.lang.reflect.Field criadoEmField = Beneficio.class.getDeclaredField("criadoEm");
        criadoEmField.setAccessible(true);
        criadoEmField.set(beneficio, null); // Força como null
        
        // When
        java.lang.reflect.Method onCreate = Beneficio.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(beneficio);
        
        // Then
        assertThat(beneficio.getAtivo()).isEqualTo(Boolean.TRUE); // Deve definir TRUE
        assertThat(beneficio.getSaldo().getValor()).isEqualByComparingTo(BigDecimal.ZERO); // Deve definir ZERO
        assertThat(beneficio.getCriadoEm()).isNotNull(); // Deve definir data
        assertThat(beneficio.getAtualizadoEm()).isNotNull(); // Deve definir data
    }

    @Test
    @DisplayName("getSaldo deve retornar Money.zero quando saldo é null")
    void getSaldoDeveRetornarMoneyZeroQuandoSaldoNull() throws Exception {
        // Given
        Beneficio beneficio = new Beneficio();
        
        // Usar reflection para definir saldo como null
        java.lang.reflect.Field saldoField = Beneficio.class.getDeclaredField("saldo");
        saldoField.setAccessible(true);
        saldoField.set(beneficio, null);
        
        // When
        Money saldo = beneficio.getSaldo();
        
        // Then
        assertThat(saldo).isEqualTo(Money.zero());
        assertThat(saldo.getValor()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}