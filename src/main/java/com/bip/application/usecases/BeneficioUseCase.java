package com.bip.application.usecases;

import com.bip.application.dtos.AtualizarBeneficioDto;
import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.application.mappers.BeneficioMapper;
import com.bip.domain.entities.Beneficio;
import com.bip.domain.repositories.BeneficioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

/**
 * Casos de uso para operações com Benefício
 * Orquestra operações entre domain e infrastructure
 */
@ApplicationScoped
@Transactional
public class BeneficioUseCase {
    
    @Inject
    private BeneficioRepository beneficioRepository;
    
    @Inject
    private BeneficioMapper beneficioMapper;
    
    /**
     * Lista todos os benefícios
     */
    public List<BeneficioDto> listarTodos() {
        List<Beneficio> beneficios = beneficioRepository.findAll();
        return beneficioMapper.toDtoList(beneficios);
    }
    
    /**
     * Lista apenas benefícios ativos
     */
    public List<BeneficioDto> listarAtivos() {
        List<Beneficio> beneficios = beneficioRepository.findAllActive();
        return beneficioMapper.toDtoList(beneficios);
    }
    
    /**
     * Busca benefício por ID
     */
    public Optional<BeneficioDto> buscarPorId(@NotNull @Positive Long id) {
        return beneficioRepository.findById(id)
                .map(beneficioMapper::toDto);
    }
    
    /**
     * Busca benefício por nome
     */
    public Optional<BeneficioDto> buscarPorNome(@NotNull String nome) {
        return beneficioRepository.findByNome(nome)
                .map(beneficioMapper::toDto);
    }
    
    /**
     * Cria novo benefício
     */
    public BeneficioDto criar(@Valid CriarBeneficioDto dto) {
        // Validar se já existe benefício com mesmo nome
        if (beneficioRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe benefício com o nome: " + dto.getNome());
        }
        
        // Converter DTO para entidade e salvar
        Beneficio beneficio = beneficioMapper.toEntity(dto);
        Beneficio salvo = beneficioRepository.save(beneficio);
        
        return beneficioMapper.toDto(salvo);
    }
    
    /**
     * Atualiza benefício existente
     */
    public BeneficioDto atualizar(@NotNull @Positive Long id, @Valid AtualizarBeneficioDto dto) {
        // Buscar benefício existente
        Beneficio beneficio = buscarBeneficioPorId(id);
        
        // Validar se o novo nome já existe em outro benefício
        if (!beneficio.getNome().equals(dto.getNome()) && 
            beneficioRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe benefício com o nome: " + dto.getNome());
        }
        
        // Atualizar dados
        beneficio.atualizarDados(dto.getNome(), dto.getDescricao(), dto.getValorInicial());
        
        // Salvar e retornar
        Beneficio atualizado = beneficioRepository.save(beneficio);
        return beneficioMapper.toDto(atualizado);
    }
    
    /**
     * Ativa benefício
     */
    public BeneficioDto ativar(@NotNull @Positive Long id) {
        Beneficio beneficio = buscarBeneficioPorId(id);
        beneficio.ativar();
        
        Beneficio atualizado = beneficioRepository.save(beneficio);
        return beneficioMapper.toDto(atualizado);
    }
    
    /**
     * Desativa benefício
     */
    public BeneficioDto desativar(@NotNull @Positive Long id) {
        Beneficio beneficio = buscarBeneficioPorId(id);
        beneficio.desativar();
        
        Beneficio atualizado = beneficioRepository.save(beneficio);
        return beneficioMapper.toDto(atualizado);
    }
    
    /**
     * Remove benefício
     */
    public void remover(@NotNull @Positive Long id) {
        if (!beneficioRepository.existsById(id)) {
            throw new IllegalArgumentException("Benefício não encontrado com ID: " + id);
        }
        
        beneficioRepository.deleteById(id);
    }
    
    /**
     * Conta benefícios ativos
     */
    public long contarAtivos() {
        return beneficioRepository.countActive();
    }
    
    /**
     * Soma valores de benefícios ativos
     */
    public java.math.BigDecimal somarValoresAtivos() {
        return beneficioRepository.sumActiveValues();
    }
    
    /**
     * Método auxiliar para buscar benefício com tratamento de erro
     */
    private Beneficio buscarBeneficioPorId(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Benefício não encontrado com ID: " + id));
    }
}