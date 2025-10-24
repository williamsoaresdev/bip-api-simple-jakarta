package com.bip.infrastructure.persistence;

import com.bip.domain.entities.Beneficio;
import com.bip.domain.valueobjects.Money;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("BeneficioRepositoryImpl")
class BeneficioRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Beneficio> typedQuery;

    @Mock
    private TypedQuery<Long> longQuery;

    @Mock
    private TypedQuery<BigDecimal> bigDecimalQuery;

    @InjectMocks
    private BeneficioRepositoryImpl repository;

    private Beneficio beneficioExistente;
    private Beneficio novoBeneficio;

    @BeforeEach
    void setUp() {
        // Criar objetos reais para testes que não precisam de ID
        beneficioExistente = new Beneficio(
            "Benefício Teste",
            "Descrição teste",
            Money.of(BigDecimal.valueOf(1000))
        );
        
        novoBeneficio = new Beneficio(
            "Novo Benefício",
            "Nova descrição",
            Money.of(BigDecimal.valueOf(500))
        );

        // Setup padrão para transaction
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(transaction.isActive()).thenReturn(false);
    }

    @Nested
    @DisplayName("findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve encontrar benefício existente por ID")
        void shouldFindExistingBeneficioById() {
            // Arrange
            Long id = 1L;
            when(entityManager.find(Beneficio.class, id)).thenReturn(beneficioExistente);

            // Act
            Optional<Beneficio> result = repository.findById(id);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(beneficioExistente);
            verify(entityManager).find(Beneficio.class, id);
        }

        @Test
        @DisplayName("Deve retornar empty quando benefício não encontrado")
        void shouldReturnEmptyWhenBeneficioNotFound() {
            // Arrange
            Long id = 999L;
            when(entityManager.find(Beneficio.class, id)).thenReturn(null);

            // Act
            Optional<Beneficio> result = repository.findById(id);

            // Assert
            assertThat(result).isEmpty();
            verify(entityManager).find(Beneficio.class, id);
        }

        @Test
        @DisplayName("Deve retornar empty quando ID é nulo")
        void shouldReturnEmptyWhenIdIsNull() {
            // Act
            Optional<Beneficio> result = repository.findById(null);

            // Assert
            assertThat(result).isEmpty();
            verify(entityManager, never()).find(any(), any());
        }
    }

    @Nested
    @DisplayName("findByNome")
    class FindByNomeTests {

        @Test
        @DisplayName("Deve encontrar benefício por nome")
        void shouldFindBeneficioByNome() {
            // Arrange
            String nome = "Benefício Teste";
            when(entityManager.createNamedQuery("Beneficio.findByName", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.setParameter("nome", nome)).thenReturn(typedQuery);
            when(typedQuery.getSingleResult()).thenReturn(beneficioExistente);

            // Act
            Optional<Beneficio> result = repository.findByNome(nome);

            // Assert
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(beneficioExistente);
            verify(typedQuery).setParameter("nome", nome);
        }

        @Test
        @DisplayName("Deve retornar empty quando nome não encontrado")
        void shouldReturnEmptyWhenNomeNotFound() {
            // Arrange
            String nome = "Nome Inexistente";
            when(entityManager.createNamedQuery("Beneficio.findByName", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.setParameter("nome", nome)).thenReturn(typedQuery);
            when(typedQuery.getSingleResult()).thenThrow(new NoResultException());

            // Act
            Optional<Beneficio> result = repository.findByNome(nome);

            // Assert
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Deve retornar empty quando nome é nulo")
        void shouldReturnEmptyWhenNomeIsNull() {
            // Act
            Optional<Beneficio> result = repository.findByNome(null);

            // Assert
            assertThat(result).isEmpty();
            verify(entityManager, never()).createNamedQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve retornar empty quando nome é vazio")
        void shouldReturnEmptyWhenNomeIsEmpty() {
            // Act
            Optional<Beneficio> result = repository.findByNome("   ");

            // Assert
            assertThat(result).isEmpty();
            verify(entityManager, never()).createNamedQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve fazer trim do nome")
        void shouldTrimNome() {
            // Arrange
            String nomeComEspacos = "  Benefício Teste  ";
            String nomeTrimmed = "Benefício Teste";
            when(entityManager.createNamedQuery("Beneficio.findByName", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.setParameter("nome", nomeTrimmed)).thenReturn(typedQuery);
            when(typedQuery.getSingleResult()).thenReturn(beneficioExistente);

            // Act
            repository.findByNome(nomeComEspacos);

            // Assert
            verify(typedQuery).setParameter("nome", nomeTrimmed);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAllTests {

        @Test
        @DisplayName("Deve retornar todos os benefícios")
        void shouldReturnAllBeneficios() {
            // Arrange
            List<Beneficio> beneficios = Arrays.asList(beneficioExistente, novoBeneficio);
            when(entityManager.createNamedQuery("Beneficio.findAll", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(beneficios);

            // Act
            List<Beneficio> result = repository.findAll();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(beneficioExistente, novoBeneficio);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há benefícios")
        void shouldReturnEmptyListWhenNoBeneficios() {
            // Arrange
            when(entityManager.createNamedQuery("Beneficio.findAll", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(List.of());

            // Act
            List<Beneficio> result = repository.findAll();

            // Assert
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllActive")
    class FindAllActiveTests {

        @Test
        @DisplayName("Deve retornar apenas benefícios ativos")
        void shouldReturnOnlyActiveBeneficios() {
            // Arrange
            List<Beneficio> beneficiosAtivos = Arrays.asList(beneficioExistente);
            when(entityManager.createNamedQuery("Beneficio.findAllActive", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(beneficiosAtivos);

            // Act
            List<Beneficio> result = repository.findAllActive();

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result).contains(beneficioExistente);
        }
    }

    @Nested
    @DisplayName("findByIdsWithLock")
    class FindByIdsWithLockTests {

        @Test
        @DisplayName("Deve buscar benefícios por IDs com lock pessimista")
        void shouldFindBeneficiosByIdsWithLock() {
            // Arrange
            List<Long> ids = Arrays.asList(1L, 2L);
            List<Beneficio> beneficios = Arrays.asList(beneficioExistente);
            
            when(entityManager.createNamedQuery("Beneficio.findByIdsWithLock", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.setParameter("ids", ids)).thenReturn(typedQuery);
            when(typedQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(beneficios);

            // Act
            List<Beneficio> result = repository.findByIdsWithLock(ids);

            // Assert
            assertThat(result).isEqualTo(beneficios);
            verify(typedQuery).setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando IDs é nulo")
        void shouldReturnEmptyListWhenIdsIsNull() {
            // Act
            List<Beneficio> result = repository.findByIdsWithLock(null);

            // Assert
            assertThat(result).isEmpty();
            verify(entityManager, never()).createNamedQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando IDs é vazio")
        void shouldReturnEmptyListWhenIdsIsEmpty() {
            // Act
            List<Beneficio> result = repository.findByIdsWithLock(List.of());

            // Assert
            assertThat(result).isEmpty();
            verify(entityManager, never()).createNamedQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve ordenar e remover duplicatas dos IDs")
        void shouldSortAndRemoveDuplicateIds() {
            // Arrange
            List<Long> idsDesordenados = Arrays.asList(3L, 1L, 2L, 1L);
            List<Long> idsOrdenados = Arrays.asList(1L, 2L, 3L);
            
            when(entityManager.createNamedQuery("Beneficio.findByIdsWithLock", Beneficio.class)).thenReturn(typedQuery);
            when(typedQuery.setParameter("ids", idsOrdenados)).thenReturn(typedQuery);
            when(typedQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(List.of());

            // Act
            repository.findByIdsWithLock(idsDesordenados);

            // Assert
            verify(typedQuery).setParameter("ids", idsOrdenados);
        }
    }

    @Nested
    @DisplayName("countActive")
    class CountActiveTests {

        @Test
        @DisplayName("Deve contar benefícios ativos")
        void shouldCountActiveBeneficios() {
            // Arrange
            Long count = 5L;
            when(entityManager.createNamedQuery("Beneficio.countActive", Long.class)).thenReturn(longQuery);
            when(longQuery.getSingleResult()).thenReturn(count);

            // Act
            long result = repository.countActive();

            // Assert
            assertThat(result).isEqualTo(count);
        }
    }

    @Nested
    @DisplayName("sumActiveValues")
    class SumActiveValuesTests {

        @Test
        @DisplayName("Deve somar valores dos benefícios ativos")
        void shouldSumActiveValues() {
            // Arrange
            BigDecimal sum = BigDecimal.valueOf(5000);
            when(entityManager.createNamedQuery("Beneficio.sumActiveValues", BigDecimal.class)).thenReturn(bigDecimalQuery);
            when(bigDecimalQuery.getSingleResult()).thenReturn(sum);

            // Act
            BigDecimal result = repository.sumActiveValues();

            // Assert
            assertThat(result).isEqualTo(sum);
        }

        @Test
        @DisplayName("Deve retornar zero quando resultado é nulo")
        void shouldReturnZeroWhenResultIsNull() {
            // Arrange
            when(entityManager.createNamedQuery("Beneficio.sumActiveValues", BigDecimal.class)).thenReturn(bigDecimalQuery);
            when(bigDecimalQuery.getSingleResult()).thenReturn(null);

            // Act
            BigDecimal result = repository.sumActiveValues();

            // Assert
            assertThat(result).isEqualTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("save")
    class SaveTests {

        @Test
        @DisplayName("Deve persistir novo benefício")
        void shouldPersistNewBeneficio() {
            // Arrange
            doNothing().when(transaction).begin();
            doNothing().when(transaction).commit();
            doNothing().when(entityManager).persist(novoBeneficio);
            doNothing().when(entityManager).flush();

            // Act
            Beneficio result = repository.save(novoBeneficio);

            // Assert
            assertThat(result).isEqualTo(novoBeneficio);
            verify(entityManager).persist(novoBeneficio);
            verify(entityManager).flush();
            verify(transaction).begin();
            verify(transaction).commit();
        }

        @Test
        @DisplayName("Deve fazer merge de benefício existente")
        void shouldMergeExistingBeneficio() {
            // Arrange
            when(entityManager.merge(beneficioExistente)).thenReturn(beneficioExistente);
            doNothing().when(transaction).begin();
            doNothing().when(transaction).commit();

            // Act
            Beneficio result = repository.save(beneficioExistente);

            // Assert
            assertThat(result).isEqualTo(beneficioExistente);
            verify(entityManager).merge(beneficioExistente);
            verify(entityManager, never()).persist(any());
            verify(transaction).begin();
            verify(transaction).commit();
        }

        @Test
        @DisplayName("Deve lançar exceção quando benefício é nulo")
        void shouldThrowExceptionWhenBeneficioIsNull() {
            // Act & Assert
            assertThatThrownBy(() -> repository.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Benefício não pode ser nulo");
        }

        @Test
        @DisplayName("Deve fazer rollback em caso de erro")
        void shouldRollbackOnError() {
            // Arrange
            when(transaction.isActive()).thenReturn(true);
            doNothing().when(transaction).begin();
            doThrow(new RuntimeException("Erro no banco")).when(entityManager).persist(novoBeneficio);
            doNothing().when(transaction).rollback();

            // Act & Assert
            assertThatThrownBy(() -> repository.save(novoBeneficio))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao salvar benefício");

            verify(transaction).rollback();
        }

        @Test
        @DisplayName("Não deve iniciar transação se já estiver ativa")
        void shouldNotStartTransactionIfAlreadyActive() {
            // Arrange
            when(transaction.isActive()).thenReturn(true);
            when(entityManager.merge(beneficioExistente)).thenReturn(beneficioExistente);

            // Act
            repository.save(beneficioExistente);

            // Assert
            verify(transaction, never()).begin();
            verify(transaction, never()).commit();
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar benefício gerenciado")
        void shouldDeleteManagedBeneficio() {
            // Arrange
            when(entityManager.contains(beneficioExistente)).thenReturn(true);
            doNothing().when(transaction).begin();
            doNothing().when(transaction).commit();
            doNothing().when(entityManager).remove(beneficioExistente);

            // Act
            repository.delete(beneficioExistente);

            // Assert
            verify(entityManager).remove(beneficioExistente);
            verify(transaction).begin();
            verify(transaction).commit();
        }

        @Test
        @DisplayName("Deve buscar e deletar benefício não gerenciado")
        void shouldFindAndDeleteUnmanagedBeneficio() {
            // Arrange
            Beneficio mockBeneficio = mock(Beneficio.class);
            Long idTeste = 1L;
            when(mockBeneficio.getId()).thenReturn(idTeste);
            when(entityManager.contains(mockBeneficio)).thenReturn(false);
            when(entityManager.find(Beneficio.class, idTeste)).thenReturn(mockBeneficio);
            doNothing().when(transaction).begin();
            doNothing().when(transaction).commit();
            doNothing().when(entityManager).remove(mockBeneficio);

            // Act
            repository.delete(mockBeneficio);

            // Assert
            verify(entityManager).find(Beneficio.class, idTeste);
            verify(entityManager).remove(mockBeneficio);
        }

        @Test
        @DisplayName("Não deve fazer nada quando benefício é nulo")
        void shouldDoNothingWhenBeneficioIsNull() {
            // Act
            repository.delete(null);

            // Assert
            verify(entityManager, never()).remove(any());
            verify(transaction, never()).begin();
        }

        @Test
        @DisplayName("Deve fazer rollback em caso de erro")
        void shouldRollbackOnError() {
            // Arrange
            when(entityManager.contains(beneficioExistente)).thenReturn(true);
            when(transaction.isActive()).thenReturn(true);
            doNothing().when(transaction).begin();
            doThrow(new RuntimeException("Erro no banco")).when(entityManager).remove(beneficioExistente);
            doNothing().when(transaction).rollback();

            // Act & Assert
            assertThatThrownBy(() -> repository.delete(beneficioExistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro ao deletar benefício");

            verify(transaction).rollback();
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteByIdTests {

        @Test
        @DisplayName("Deve deletar benefício por ID")
        void shouldDeleteBeneficioById() {
            // Arrange
            Long id = 1L;
            when(entityManager.find(Beneficio.class, id)).thenReturn(beneficioExistente);
            doNothing().when(entityManager).remove(beneficioExistente);

            // Act
            repository.deleteById(id);

            // Assert
            verify(entityManager).find(Beneficio.class, id);
            verify(entityManager).remove(beneficioExistente);
        }

        @Test
        @DisplayName("Não deve fazer nada quando benefício não encontrado")
        void shouldDoNothingWhenBeneficioNotFound() {
            // Arrange
            Long id = 999L;
            when(entityManager.find(Beneficio.class, id)).thenReturn(null);

            // Act
            repository.deleteById(id);

            // Assert
            verify(entityManager).find(Beneficio.class, id);
            verify(entityManager, never()).remove(any());
        }

        @Test
        @DisplayName("Não deve fazer nada quando ID é nulo")
        void shouldDoNothingWhenIdIsNull() {
            // Act
            repository.deleteById(null);

            // Assert
            verify(entityManager, never()).find(any(), any());
            verify(entityManager, never()).remove(any());
        }
    }

    @Nested
    @DisplayName("existsByNome")
    class ExistsByNomeTests {

        @Test
        @DisplayName("Deve retornar true quando nome existe")
        void shouldReturnTrueWhenNomeExists() {
            // Arrange
            String nome = "Benefício Existente";
            when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
            when(longQuery.setParameter("nome", nome)).thenReturn(longQuery);
            when(longQuery.getSingleResult()).thenReturn(1L);

            // Act
            boolean result = repository.existsByNome(nome);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando nome não existe")
        void shouldReturnFalseWhenNomeDoesNotExist() {
            // Arrange
            String nome = "Nome Inexistente";
            when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
            when(longQuery.setParameter("nome", nome)).thenReturn(longQuery);
            when(longQuery.getSingleResult()).thenReturn(0L);

            // Act
            boolean result = repository.existsByNome(nome);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Deve retornar false quando nome é nulo")
        void shouldReturnFalseWhenNomeIsNull() {
            // Act
            boolean result = repository.existsByNome(null);

            // Assert
            assertThat(result).isFalse();
            verify(entityManager, never()).createQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve retornar false quando nome é vazio")
        void shouldReturnFalseWhenNomeIsEmpty() {
            // Act
            boolean result = repository.existsByNome("   ");

            // Assert
            assertThat(result).isFalse();
            verify(entityManager, never()).createQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve retornar false em caso de exceção")
        void shouldReturnFalseOnException() {
            // Arrange
            String nome = "Teste";
            when(entityManager.createQuery(anyString(), eq(Long.class))).thenThrow(new RuntimeException());

            // Act
            boolean result = repository.existsByNome(nome);

            // Assert
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsById")
    class ExistsByIdTests {

        @Test
        @DisplayName("Deve retornar true quando ID existe")
        void shouldReturnTrueWhenIdExists() {
            // Arrange
            Long id = 1L;
            when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
            when(longQuery.setParameter("id", id)).thenReturn(longQuery);
            when(longQuery.getSingleResult()).thenReturn(1L);

            // Act
            boolean result = repository.existsById(id);

            // Assert
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Deve retornar false quando ID não existe")
        void shouldReturnFalseWhenIdDoesNotExist() {
            // Arrange
            Long id = 999L;
            when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
            when(longQuery.setParameter("id", id)).thenReturn(longQuery);
            when(longQuery.getSingleResult()).thenReturn(0L);

            // Act
            boolean result = repository.existsById(id);

            // Assert
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Deve retornar false quando ID é nulo")
        void shouldReturnFalseWhenIdIsNull() {
            // Act
            boolean result = repository.existsById(null);

            // Assert
            assertThat(result).isFalse();
            verify(entityManager, never()).createQuery(anyString(), any());
        }

        @Test
        @DisplayName("Deve retornar false em caso de exceção")
        void shouldReturnFalseOnException() {
            // Arrange
            Long id = 1L;
            when(entityManager.createQuery(anyString(), eq(Long.class))).thenThrow(new RuntimeException());

            // Act
            boolean result = repository.existsById(id);

            // Assert
            assertThat(result).isFalse();
        }
    }
}