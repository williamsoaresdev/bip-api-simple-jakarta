package com.bip.application.usecases;

import com.bip.application.dtos.TransferenciaDto;
import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import com.bip.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes abrangentes para TransferenciaUseCase.
 * Cobre todos os cenários de transferência entre benefícios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TransferenciaUseCase")
class TransferenciaUseCaseTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @InjectMocks
    private TransferenciaUseCase transferenciaUseCase;

    @Captor
    private ArgumentCaptor<Beneficio> beneficioCaptor;

    private Beneficio beneficioOrigem;
    private Beneficio beneficioDestino;
    private TransferenciaDto transferenciaDto;

    @BeforeEach
    void setUp() {
        beneficioOrigem = new Beneficio(
            "João Silva",
            "Benefício de João Silva - CPF: 12345678900",
            Money.of(new BigDecimal("1000.00"))
        );

        beneficioDestino = new Beneficio(
            "Maria Santos",
            "Benefício de Maria Santos - CPF: 98765432100",
            Money.of(new BigDecimal("500.00"))
        );

        transferenciaDto = new TransferenciaDto(
            1L, 2L,
            new BigDecimal("100.00"),
            "Transferência de teste"
        );
    }

    @Nested
    @DisplayName("Executar Transferência")
    class ExecutarTransferenciaTests {

        @Test
        @DisplayName("Deve executar transferência com sucesso")
        void deveExecutarTransferenciaComSucesso() {
            // Arrange
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            transferenciaUseCase.executarTransferencia(transferenciaDto);

            // Assert
            assertThat(beneficioOrigem.getSaldo()).isEqualTo(Money.of(new BigDecimal("900.00")));
            assertThat(beneficioDestino.getSaldo()).isEqualTo(Money.of(new BigDecimal("600.00")));
            
            verify(beneficioRepository, times(2)).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando benefício origem não existir")
        void deveLancarExcecaoQuandoBeneficioOrigemNaoExistir() {
            // Arrange
            when(beneficioRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(transferenciaDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício não encontrado com ID: 1");

            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando benefício destino não existir")
        void deveLancarExcecaoQuandoBeneficioDestinoNaoExistir() {
            // Arrange
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(transferenciaDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício não encontrado com ID: 2");

            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando origem e destino forem iguais")
        void deveLancarExcecaoQuandoOrigemDestinoIguais() {
            // Arrange
            TransferenciaDto dtoInvalido = new TransferenciaDto(
                1L, 1L, new BigDecimal("100.00"), "Transferência inválida"
            );

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(dtoInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício de origem e destino não podem ser iguais");

            verify(beneficioRepository, never()).findById(any());
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @ParameterizedTest
        @ValueSource(strings = {"-100.00", "0.00", "-0.01"})
        @DisplayName("Deve lançar exceção para valores inválidos")
        void deveLancarExcecaoParaValoresInvalidos(String valor) {
            // Arrange
            TransferenciaDto dtoInvalido = new TransferenciaDto(
                1L, 2L, new BigDecimal(valor), "Transferência inválida"
            );

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(dtoInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor da transferência deve ser positivo");

            verify(beneficioRepository, never()).findById(any());
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando benefício origem estiver inativo")
        void deveLancarExcecaoQuandoBeneficioOrigemInativo() {
            // Arrange
            beneficioOrigem.desativar();
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(transferenciaDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Benefício de origem está inativo");

            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando benefício destino estiver inativo")
        void deveLancarExcecaoQuandoBeneficioDestinoInativo() {
            // Arrange
            beneficioDestino.desativar();
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(transferenciaDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Benefício de destino está inativo");

            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando saldo insuficiente")
        void deveLancarExcecaoQuandoSaldoInsuficiente() {
            // Arrange
            TransferenciaDto transferenciaAlto = new TransferenciaDto(
                1L, 2L, new BigDecimal("2000.00"), "Transferência alta"
            );
            
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.executarTransferencia(transferenciaAlto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Saldo insuficiente");

            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }
    }

    @Nested
    @DisplayName("Validar Transferência")
    class ValidarTransferenciaTests {

        @Test
        @DisplayName("Deve retornar true para transferência válida")
        void deveRetornarTrueParaTransferenciaValida() {
            // Arrange
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            boolean resultado = transferenciaUseCase.validarTransferencia(transferenciaDto);

            // Assert
            assertThat(resultado).isTrue();
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve retornar false para transferência com saldo insuficiente")
        void deveRetornarFalseParaSaldoInsuficiente() {
            // Arrange
            TransferenciaDto transferenciaAlto = new TransferenciaDto(
                1L, 2L, new BigDecimal("2000.00"), "Transferência alta"
            );
            
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            boolean resultado = transferenciaUseCase.validarTransferencia(transferenciaAlto);

            // Assert
            assertThat(resultado).isFalse();
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve retornar false para transferência com dados inválidos")
        void deveRetornarFalseParaDadosInvalidos() {
            // Arrange
            TransferenciaDto dtoInvalido = new TransferenciaDto(
                1L, 1L, new BigDecimal("100.00"), "Transferência inválida"
            );

            // Act
            boolean resultado = transferenciaUseCase.validarTransferencia(dtoInvalido);

            // Assert
            assertThat(resultado).isFalse();
            verify(beneficioRepository, never()).findById(any());
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve retornar false quando benefício não existir")
        void deveRetornarFalseQuandoBeneficioNaoExistir() {
            // Arrange
            when(beneficioRepository.findById(1L)).thenReturn(Optional.empty());

            // Act
            boolean resultado = transferenciaUseCase.validarTransferencia(transferenciaDto);

            // Assert
            assertThat(resultado).isFalse();
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve retornar false quando benefício estiver inativo")
        void deveRetornarFalseQuandoBeneficioInativo() {
            // Arrange
            beneficioOrigem.desativar();
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            boolean resultado = transferenciaUseCase.validarTransferencia(transferenciaDto);

            // Assert
            assertThat(resultado).isFalse();
            verify(beneficioRepository, never()).save(any(Beneficio.class));
        }
    }

    @Nested
    @DisplayName("Calcular Taxa")
    class CalcularTaxaTests {

        @Test
        @DisplayName("Deve calcular taxa de 1% corretamente")
        void deveCalcularTaxaCorretamente() {
            // Arrange
            BigDecimal valor = new BigDecimal("100.00");

            // Act
            Money taxa = transferenciaUseCase.calcularTaxa(valor);

            // Assert
            assertThat(taxa).isEqualTo(Money.of(new BigDecimal("1.00")));
        }

        @ParameterizedTest
        @ValueSource(strings = {"50.00", "250.00", "1000.00", "0.01"})
        @DisplayName("Deve calcular taxa para diferentes valores")
        void deveCalcularTaxaParaDiferentesValores(String valorStr) {
            // Arrange
            BigDecimal valor = new BigDecimal(valorStr);
            BigDecimal taxaEsperada = valor.multiply(new BigDecimal("0.01"));

            // Act
            Money taxa = transferenciaUseCase.calcularTaxa(valor);

            // Assert
            assertThat(taxa).isEqualTo(Money.of(taxaEsperada));
        }

        @Test
        @DisplayName("Deve calcular taxa para valor zero")
        void deveCalcularTaxaParaValorZero() {
            // Arrange
            BigDecimal valor = BigDecimal.ZERO;

            // Act
            Money taxa = transferenciaUseCase.calcularTaxa(valor);

            // Assert
            assertThat(taxa).isEqualTo(Money.of(BigDecimal.ZERO));
        }

        @Test
        @DisplayName("Deve lançar exceção para valor null")
        void deveLancarExcecaoParaValorNull() {
            // Act & Assert
            assertThatThrownBy(() -> transferenciaUseCase.calcularTaxa(null))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Casos Especiais")
    class CasosEspeciaisTests {

        @Test
        @DisplayName("Deve executar transferência de valor muito pequeno")
        void deveExecutarTransferenciaPequena() {
            // Arrange
            TransferenciaDto transferenciaPequena = new TransferenciaDto(
                1L, 2L, new BigDecimal("0.01"), "Transferência mínima"
            );
            
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            transferenciaUseCase.executarTransferencia(transferenciaPequena);

            // Assert
            assertThat(beneficioOrigem.getSaldo()).isEqualTo(Money.of(new BigDecimal("999.99")));
            assertThat(beneficioDestino.getSaldo()).isEqualTo(Money.of(new BigDecimal("500.01")));
        }

        @Test
        @DisplayName("Deve executar transferência de todo o saldo")
        void deveExecutarTransferenciaTodoSaldo() {
            // Arrange
            TransferenciaDto transferenciaTodo = new TransferenciaDto(
                1L, 2L, new BigDecimal("1000.00"), "Transferência total"
            );
            
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            transferenciaUseCase.executarTransferencia(transferenciaTodo);

            // Assert
            assertThat(beneficioOrigem.getSaldo()).isEqualTo(Money.of(BigDecimal.ZERO));
            assertThat(beneficioDestino.getSaldo()).isEqualTo(Money.of(new BigDecimal("1500.00")));
        }

        @Test
        @DisplayName("Deve capturar benefícios salvos na ordem correta")
        void deveCapturarBeneficiosSalvosNaOrdem() {
            // Arrange
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficioOrigem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(beneficioDestino));

            // Act
            transferenciaUseCase.executarTransferencia(transferenciaDto);

            // Assert
            verify(beneficioRepository, times(2)).save(beneficioCaptor.capture());
            
            List<Beneficio> beneficiosSalvos = beneficioCaptor.getAllValues();
            assertThat(beneficiosSalvos).hasSize(2);
            assertThat(beneficiosSalvos.get(0)).isEqualTo(beneficioOrigem);
            assertThat(beneficiosSalvos.get(1)).isEqualTo(beneficioDestino);
        }
    }
}