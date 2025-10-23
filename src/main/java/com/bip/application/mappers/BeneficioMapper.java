package com.bip.application.mappers;

import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.domain.entities.Beneficio;
import com.bip.domain.valueobjects.Money;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre entidades Beneficio e DTOs
 * Isola as camadas e facilita manutenção
 */
@ApplicationScoped
public class BeneficioMapper {
    
    /**
     * Converte entidade para DTO de visualização
     */
    public BeneficioDto toDto(Beneficio beneficio) {
        if (beneficio == null) {
            return null;
        }
        
        return new BeneficioDto(
            beneficio.getId(),
            beneficio.getNome(),
            beneficio.getDescricao(),
            beneficio.getSaldo().getValor(),
            beneficio.getAtivo(),
            beneficio.getCriadoEm(),
            beneficio.getAtualizadoEm()
        );
    }
    
    /**
     * Converte lista de entidades para lista de DTOs
     */
    public List<BeneficioDto> toDtoList(List<Beneficio> beneficios) {
        if (beneficios == null) {
            return List.of();
        }
        
        return beneficios.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte DTO de criação para entidade
     */
    public Beneficio toEntity(CriarBeneficioDto dto) {
        if (dto == null) {
            return null;
        }
        
        Money saldoInicial = dto.getValorInicial() != null 
            ? Money.of(dto.getValorInicial()) 
            : Money.zero();
            
        return Beneficio.criar(
            dto.getNome(),
            dto.getDescricao(),
            saldoInicial
        );
    }
}