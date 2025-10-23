package com.bip.presentation.controllers;

import com.bip.application.dtos.AtualizarBeneficioDto;
import com.bip.application.dtos.BeneficioDto;
import com.bip.application.dtos.CriarBeneficioDto;
import com.bip.application.usecases.BeneficioUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioController {
    
    @Inject
    private BeneficioUseCase beneficioUseCase;
    
    @GET
    @Path("/status")
    public Response getStatus() {
        Map<String, Object> status = new HashMap<>();
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
            List<BeneficioDto> beneficios = beneficioUseCase.listarTodos();
            return Response.ok(beneficios).build();
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @GET
    @Path("/ativos")
    public Response listarAtivos() {
        try {
            List<BeneficioDto> beneficios = beneficioUseCase.listarAtivos();
            return Response.ok(beneficios).build();
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "ID deve ser um número positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            Optional<BeneficioDto> beneficio = beneficioUseCase.buscarPorId(id);
            
            if (beneficio.isPresent()) {
                return Response.ok(beneficio.get()).build();
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Benefício não encontrado");
                erro.put("id", id);
                return Response.status(Response.Status.NOT_FOUND).entity(erro).build();
            }
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @POST
    public Response criar(@Valid CriarBeneficioDto dto) {
        try {
            BeneficioDto beneficioCriado = beneficioUseCase.criar(dto);
            return Response.status(Response.Status.CREATED).entity(beneficioCriado).build();
                    
        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Dados inválidos");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
                    
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid AtualizarBeneficioDto dto) {
        try {
            if (id == null || id <= 0) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "ID deve ser um número positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            BeneficioDto beneficioAtualizado = beneficioUseCase.atualizar(id, dto);
            return Response.ok(beneficioAtualizado).build();
                    
        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Dados inválidos");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
                    
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "ID deve ser um número positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            beneficioUseCase.remover(id);
            
            Map<String, Object> sucesso = new HashMap<>();
            sucesso.put("mensagem", "Benefício removido com sucesso");
            sucesso.put("id", id);
            return Response.ok(sucesso).build();
                    
        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Benefício não encontrado");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(erro).build();
                    
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @GET
    @Path("/estatisticas")
    public Response estatisticas() {
        try {
            long totalAtivos = beneficioUseCase.contarAtivos();
            java.math.BigDecimal somaValores = beneficioUseCase.somarValoresAtivos();
            
            Map<String, Object> estatisticas = new HashMap<>();
            estatisticas.put("totalBeneficiosAtivos", totalAtivos);
            estatisticas.put("somaTotalValores", somaValores);
            estatisticas.put("timestamp", java.time.LocalDateTime.now());
            
            return Response.ok(estatisticas).build();
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
}