package com.bip.presentation.controllers;

import com.bip.application.dtos.AtualizarBeneficioDto;
import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.application.usecases.BeneficioUseCase;
import com.bip.presentation.utils.ErrorResponseBuilder;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioController {
    
    @Inject
    private BeneficioUseCase beneficioUseCase;
    
    @Inject
    private ErrorResponseBuilder errorResponseBuilder;
    
    @GET
    @Path("/status")
    public Response getStatus() {
        final Map<String, Object> status = new ConcurrentHashMap<>();
        status.put("status", "API funcionando");
        status.put("timestamp", java.time.LocalDateTime.now());
        status.put("message", "Clean Architecture implementada com sucesso");
        status.put("version", "3.0.0-Clean-Architecture");
        status.put("endpoints", List.of(
            "GET /api/beneficios - Lista todos os benefícios",
            "POST /api/beneficios - Cria novo benefício", 
            "GET /api/beneficios/{id} - Busca benefício por ID",
            "PUT /api/beneficios/{id} - Atualiza benefício",
            "DELETE /api/beneficios/{id} - Remove benefício",
            "GET /api/beneficios/ativos - Lista benefícios ativos",
            "GET /api/beneficios/estatisticas - Estatísticas dos benefícios",
            "GET /api/beneficios/status - Status da API"
        ));
        
        return Response.ok(status).build();
    }

    @GET
    public Response listarTodos() {
        try {
            final List<BeneficioDto> beneficios = beneficioUseCase.listarTodos();
            return errorResponseBuilder.buildSuccessResponse(beneficios);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    @GET
    @Path("/ativos")
    public Response listarAtivos() {
        try {
            final List<BeneficioDto> beneficios = beneficioUseCase.listarAtivos();
            return errorResponseBuilder.buildSuccessResponse(beneficios);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") final Long id) {
        try {
            if (id == null || id <= 0) {
                final Map<String, Object> erro = new ConcurrentHashMap<>();
                erro.put("erro", "ID deve ser um número positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            final Optional<BeneficioDto> beneficio = beneficioUseCase.buscarPorId(id);
            
            if (beneficio.isPresent()) {
                return errorResponseBuilder.buildSuccessResponse(beneficio.get());
            } else {
                final Map<String, Object> erro = new ConcurrentHashMap<>();
                erro.put("erro", "Benefício não encontrado");
                erro.put("id", id);
                return Response.status(Response.Status.NOT_FOUND).entity(erro).build();
            }
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    @POST
    public Response criar(@Valid final CriarBeneficioDto dto) {
        try {
            final BeneficioDto beneficioCriado = beneficioUseCase.criar(dto);
            return Response.status(Response.Status.CREATED).entity(beneficioCriado).build();
                    
        } catch (IllegalArgumentException e) {
            final Map<String, Object> erro = new ConcurrentHashMap<>();
            erro.put("erro", "Dados inválidos");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
                    
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") final Long id, @Valid final AtualizarBeneficioDto dto) {
        try {
            if (id == null || id <= 0) {
                final Map<String, Object> erro = new ConcurrentHashMap<>();
                erro.put("erro", "ID deve ser um número positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            final BeneficioDto beneficioAtualizado = beneficioUseCase.atualizar(id, dto);
            return errorResponseBuilder.buildSuccessResponse(beneficioAtualizado);
                    
        } catch (IllegalArgumentException e) {
            final Map<String, Object> erro = new ConcurrentHashMap<>();
            erro.put("erro", "Dados inválidos");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
                    
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") final Long id) {
        try {
            if (id == null || id <= 0) {
                final Map<String, Object> erro = new ConcurrentHashMap<>();
                erro.put("erro", "ID deve ser um número positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            beneficioUseCase.remover(id);
            final Map<String, Object> sucesso = new ConcurrentHashMap<>();
            sucesso.put("mensagem", "Benefício removido com sucesso");
            sucesso.put("id", id);
            return Response.ok(sucesso).build();
                    
        } catch (IllegalArgumentException e) {
            final Map<String, Object> erro = new ConcurrentHashMap<>();
            erro.put("erro", "Benefício não encontrado");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(erro).build();
                    
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    @GET
    @Path("/estatisticas")
    public Response estatisticas() {
        try {
            final long totalAtivos = beneficioUseCase.contarAtivos();
            final java.math.BigDecimal somaValores = beneficioUseCase.somarValoresAtivos();
            
            final java.util.Map<String, Object> estatisticas = new java.util.concurrent.ConcurrentHashMap<>();
            estatisticas.put("totalBeneficiosAtivos", totalAtivos);
            estatisticas.put("somaTotalValores", somaValores);
            estatisticas.put("timestamp", java.time.LocalDateTime.now());
            
            return errorResponseBuilder.buildSuccessResponse(estatisticas);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
}