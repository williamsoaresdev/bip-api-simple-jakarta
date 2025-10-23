package com.bip.application.usecases;

import com.bip.application.dtos.AtualizarBeneficioDto;
import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.application.mappers.BeneficioMapper;
import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import com.bip.domain.valueobjects.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para BeneficioUseCase.
 * 
 * Testa toda a lógica de negócio da camada de application,
 * incluindo criação, busca, atualização e operações com benefícios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BeneficioUseCase Tests")
class BeneficioUseCaseTest {

    @Mock
    private BeneficioRepository repository;

    @Mock
    private BeneficioMapper mapper;

    @InjectMocks
    private BeneficioUseCase useCase;

    private Beneficio beneficioSample;
    private BeneficioDto beneficioDtoSample;
    private CriarBeneficioDto criarBeneficioDtoSample;
    private AtualizarBeneficioDto atualizarBeneficioDtoSample;

    @BeforeEach
    void setUp() {
        // Dados de teste padrão
        beneficioSample = new Beneficio(
            "João Silva",
            "Benefício para João Silva",
            Money.of(new BigDecimal("1000.00"))
        );

        beneficioDtoSample = new BeneficioDto();
        beneficioDtoSample.setId(1L);
        beneficioDtoSample.setNome("João Silva");
        beneficioDtoSample.setDescricao("Benefício para João Silva");
        beneficioDtoSample.setSaldo(new BigDecimal("1000.00"));
        beneficioDtoSample.setAtivo(true);
        beneficioDtoSample.setCriadoEm(LocalDateTime.now());

        criarBeneficioDtoSample = new CriarBeneficioDto();
        criarBeneficioDtoSample.setNome("João Silva");
        criarBeneficioDtoSample.setDescricao("Benefício para João Silva");
        criarBeneficioDtoSample.setValorInicial(new BigDecimal("1000.00"));

        atualizarBeneficioDtoSample = new AtualizarBeneficioDto();
        atualizarBeneficioDtoSample.setNome("João Silva Santos");
        atualizarBeneficioDtoSample.setDescricao("Benefício atualizado");
        atualizarBeneficioDtoSample.setValorInicial(new BigDecimal("1500.00"));
    }

    @Nested
    @DisplayName("Criar Beneficio Tests")
    class CriarBeneficioTests {

        @Test
        @DisplayName("Deve criar benefício com sucesso")
        void deveCriarBeneficioComSucesso() {
            // Arrange
            when(repository.existsByNome("João Silva")).thenReturn(false);
            when(mapper.toEntity(criarBeneficioDtoSample)).thenReturn(beneficioSample);
            when(repository.save(beneficioSample)).thenReturn(beneficioSample);
            when(mapper.toDto(beneficioSample)).thenReturn(beneficioDtoSample);

            // Act
            BeneficioDto resultado = useCase.criar(criarBeneficioDtoSample);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getNome()).isEqualTo("João Silva");
            assertThat(resultado.getDescricao()).isEqualTo("Benefício para João Silva");
            assertThat(resultado.getSaldo()).isEqualByComparingTo(new BigDecimal("1000.00"));
            
            verify(repository).existsByNome("João Silva");
            verify(mapper).toEntity(criarBeneficioDtoSample);
            verify(repository).save(beneficioSample);
            verify(mapper).toDto(beneficioSample);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome já existe")
        void deveLancarExcecaoQuandoNomeJaExiste() {
            // Arrange
            when(repository.existsByNome("João Silva")).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> useCase.criar(criarBeneficioDtoSample))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Já existe benefício com o nome: João Silva");

            verify(repository).existsByNome("João Silva");
            verifyNoInteractions(mapper);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando repository.save falhar")
        void deveLancarExcecaoQuandoRepositorySaveFalhar() {
            // Arrange
            when(repository.existsByNome("João Silva")).thenReturn(false);
            when(mapper.toEntity(criarBeneficioDtoSample)).thenReturn(beneficioSample);
            when(repository.save(beneficioSample)).thenThrow(new RuntimeException("Erro no banco"));

            // Act & Assert
            assertThatThrownBy(() -> useCase.criar(criarBeneficioDtoSample))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro no banco");

            verify(repository).existsByNome("João Silva");
            verify(mapper).toEntity(criarBeneficioDtoSample);
            verify(repository).save(beneficioSample);
            verify(mapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Buscar Por ID Tests")
    class BuscarPorIdTests {

        @Test
        @DisplayName("Deve encontrar benefício por ID")
        void deveEncontrarBeneficioPorId() {
            // Arrange
            Long id = 1L;
            when(repository.findById(id)).thenReturn(Optional.of(beneficioSample));
            when(mapper.toDto(beneficioSample)).thenReturn(beneficioDtoSample);

            // Act
            Optional<BeneficioDto> resultado = useCase.buscarPorId(id);

            // Assert
            assertThat(resultado).isPresent();
            assertThat(resultado.get().getId()).isEqualTo(id);
            assertThat(resultado.get().getNome()).isEqualTo("João Silva");

            verify(repository).findById(id);
            verify(mapper).toDto(beneficioSample);
        }

        @Test
        @DisplayName("Deve retornar vazio quando benefício não existe")
        void deveRetornarVazioQuandoBeneficioNaoExiste() {
            // Arrange
            Long id = 999L;
            when(repository.findById(id)).thenReturn(Optional.empty());

            // Act
            Optional<BeneficioDto> resultado = useCase.buscarPorId(id);

            // Assert
            assertThat(resultado).isEmpty();

            verify(repository).findById(id);
            verify(mapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Listar Todos Tests")
    class ListarTodosTests {

        @Test
        @DisplayName("Deve listar todos os benefícios")
        void deveListarTodosBeneficios() {
            // Arrange
            Beneficio beneficio2 = new Beneficio(
                "Maria Santos",
                "Benefício para Maria Santos",
                Money.of(new BigDecimal("2000.00"))
            );

            List<Beneficio> beneficios = Arrays.asList(beneficioSample, beneficio2);
            List<BeneficioDto> beneficiosDto = Arrays.asList(beneficioDtoSample);

            when(repository.findAll()).thenReturn(beneficios);
            when(mapper.toDtoList(beneficios)).thenReturn(beneficiosDto);

            // Act
            List<BeneficioDto> resultado = useCase.listarTodos();

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNome()).isEqualTo("João Silva");

            verify(repository).findAll();
            verify(mapper).toDtoList(beneficios);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há benefícios")
        void deveRetornarListaVaziaQuandoNaoHaBeneficios() {
            // Arrange
            List<Beneficio> beneficiosVazios = Collections.emptyList();
            List<BeneficioDto> dtoVazio = Collections.emptyList();
            
            when(repository.findAll()).thenReturn(beneficiosVazios);
            when(mapper.toDtoList(beneficiosVazios)).thenReturn(dtoVazio);

            // Act
            List<BeneficioDto> resultado = useCase.listarTodos();

            // Assert
            assertThat(resultado).isEmpty();

            verify(repository).findAll();
            verify(mapper).toDtoList(beneficiosVazios);
        }
    }

    @Nested
    @DisplayName("Listar Ativos Tests")
    class ListarAtivosTests {

        @Test
        @DisplayName("Deve listar apenas benefícios ativos")
        void deveListarApenasBeneficiosAtivos() {
            // Arrange
            List<Beneficio> beneficiosAtivos = Arrays.asList(beneficioSample);
            List<BeneficioDto> beneficiosDto = Arrays.asList(beneficioDtoSample);

            when(repository.findAllActive()).thenReturn(beneficiosAtivos);
            when(mapper.toDtoList(beneficiosAtivos)).thenReturn(beneficiosDto);

            // Act
            List<BeneficioDto> resultado = useCase.listarAtivos();

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getAtivo()).isTrue();

            verify(repository).findAllActive();
            verify(mapper).toDtoList(beneficiosAtivos);
        }
    }

    @Nested
    @DisplayName("Buscar Por Nome Tests")
    class BuscarPorNomeTests {

        @Test
        @DisplayName("Deve encontrar benefício por nome")
        void deveEncontrarBeneficioPorNome() {
            // Arrange
            String nome = "João Silva";
            when(repository.findByNome(nome)).thenReturn(Optional.of(beneficioSample));
            when(mapper.toDto(beneficioSample)).thenReturn(beneficioDtoSample);

            // Act
            Optional<BeneficioDto> resultado = useCase.buscarPorNome(nome);

            // Assert
            assertThat(resultado).isPresent();
            assertThat(resultado.get().getNome()).isEqualTo(nome);

            verify(repository).findByNome(nome);
            verify(mapper).toDto(beneficioSample);
        }

        @Test
        @DisplayName("Deve retornar vazio quando benefício não existe")
        void deveRetornarVazioQuandoBeneficioNaoExiste() {
            // Arrange
            String nome = "Nome Inexistente";
            when(repository.findByNome(nome)).thenReturn(Optional.empty());

            // Act
            Optional<BeneficioDto> resultado = useCase.buscarPorNome(nome);

            // Assert
            assertThat(resultado).isEmpty();

            verify(repository).findByNome(nome);
            verify(mapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Ativar/Desativar Tests")
    class AtivarDesativarTests {

        @Test
        @DisplayName("Deve ativar benefício com sucesso")
        void deveAtivarBeneficioComSucesso() {
            // Arrange
            Long id = 1L;
            Beneficio beneficioInativo = new Beneficio(
                "João Silva Inativo",
                "Benefício inativo para teste",
                Money.of(new BigDecimal("1000.00"))
            );
            beneficioInativo.desativar(); // Desativa primeiro para poder ativar depois
            
            when(repository.findById(id)).thenReturn(Optional.of(beneficioInativo));
            when(repository.save(any(Beneficio.class))).thenReturn(beneficioInativo);
            when(mapper.toDto(any(Beneficio.class))).thenReturn(beneficioDtoSample);

            // Act
            BeneficioDto resultado = useCase.ativar(id);

            // Assert
            assertThat(resultado).isNotNull();

            verify(repository).findById(id);
            verify(repository).save(any(Beneficio.class));
            verify(mapper).toDto(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve desativar benefício com sucesso")
        void deveDesativarBeneficioComSucesso() {
            // Arrange
            Long id = 1L;
            
            when(repository.findById(id)).thenReturn(Optional.of(beneficioSample));
            when(repository.save(any(Beneficio.class))).thenReturn(beneficioSample);
            when(mapper.toDto(any(Beneficio.class))).thenReturn(beneficioDtoSample);

            // Act
            BeneficioDto resultado = useCase.desativar(id);

            // Assert
            assertThat(resultado).isNotNull();

            verify(repository).findById(id);
            verify(repository).save(any(Beneficio.class));
            verify(mapper).toDto(any(Beneficio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao ativar benefício inexistente")
        void deveLancarExcecaoAoAtivarBeneficioInexistente() {
            // Arrange
            Long id = 999L;
            when(repository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> useCase.ativar(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício não encontrado com ID: " + id);

            verify(repository).findById(id);
            verify(repository, never()).save(any());
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("Atualizar Tests")
    class AtualizarTests {

        @Test
        @DisplayName("Deve atualizar benefício com sucesso")
        void deveAtualizarBeneficioComSucesso() {
            // Arrange
            Long id = 1L;

            when(repository.findById(id)).thenReturn(Optional.of(beneficioSample));
            when(repository.existsByNome(atualizarBeneficioDtoSample.getNome())).thenReturn(false);
            when(repository.save(any(Beneficio.class))).thenReturn(beneficioSample);
            when(mapper.toDto(beneficioSample)).thenReturn(beneficioDtoSample);

            // Act
            BeneficioDto resultado = useCase.atualizar(id, atualizarBeneficioDtoSample);

            // Assert
            assertThat(resultado).isNotNull();

            verify(repository).findById(id);
            verify(repository).save(beneficioSample);
            verify(mapper).toDto(beneficioSample);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar benefício inexistente")
        void deveLancarExcecaoAoAtualizarBeneficioInexistente() {
            // Arrange
            Long id = 999L;
            when(repository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> useCase.atualizar(id, atualizarBeneficioDtoSample))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício não encontrado com ID: " + id);

            verify(repository).findById(id);
            verify(repository, never()).save(any());
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("Remover Tests")
    class RemoverTests {

        @Test
        @DisplayName("Deve remover benefício com sucesso")
        void deveRemoverBeneficioComSucesso() {
            // Arrange
            Long id = 1L;
            when(repository.existsById(id)).thenReturn(true);

            // Act
            useCase.remover(id);

            // Assert
            verify(repository).existsById(id);
            verify(repository).deleteById(id);
        }

        @Test
        @DisplayName("Deve lançar exceção ao remover benefício inexistente")
        void deveLancarExcecaoAoRemoverBeneficioInexistente() {
            // Arrange
            Long id = 999L;
            when(repository.existsById(id)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> useCase.remover(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício não encontrado com ID: " + id);

            verify(repository).existsById(id);
            verify(repository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Operações de Contagem Tests")
    class OperacoesContagemTests {

        @Test
        @DisplayName("Deve contar benefícios ativos")
        void deveContarBeneficiosAtivos() {
            // Arrange
            long countAtivos = 5L;
            when(repository.countActive()).thenReturn(countAtivos);

            // Act
            long resultado = useCase.contarAtivos();

            // Assert
            assertThat(resultado).isEqualTo(countAtivos);

            verify(repository).countActive();
        }

        @Test
        @DisplayName("Deve somar valores de benefícios ativos")
        void deveSomarValoresBeneficiosAtivos() {
            // Arrange
            BigDecimal somaValores = new BigDecimal("15000.00");
            when(repository.sumActiveValues()).thenReturn(somaValores);

            // Act
            BigDecimal resultado = useCase.somarValoresAtivos();

            // Assert
            assertThat(resultado).isEqualByComparingTo(somaValores);

            verify(repository).sumActiveValues();
        }
    }
}