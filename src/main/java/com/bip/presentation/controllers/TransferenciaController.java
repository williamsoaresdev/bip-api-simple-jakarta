package com.bip.presentation.controllers;

import com.bip.application.dtos.TransferenciaDto;
import com.bip.application.usecases.TransferenciaUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Path("/transferencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferenciaController {
    
    @Inject
    private TransferenciaUseCase transferenciaUseCase;
    
    @POST
    public Response executarTransferencia(@Valid TransferenciaDto dto) {
        try {
            transferenciaUseCase.executarTransferencia(dto);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("sucesso", true);
            resultado.put("mensagem", "Transferência realizada com sucesso");
            resultado.put("origem", dto.getBeneficioOrigemId());
            resultado.put("destino", dto.getBeneficioDestinoId());
            resultado.put("valor", dto.getValor());
            resultado.put("descricao", dto.getDescricao());
            resultado.put("timestamp", LocalDateTime.now());
            
            return Response.ok(resultado).build();
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Dados inválidos");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            
        } catch (IllegalStateException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Operação inválida");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro interno do servidor");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @POST
    @Path("/validar")
    public Response validarTransferencia(@Valid TransferenciaDto dto) {
        try {
            boolean valida = transferenciaUseCase.validarTransferencia(dto);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("valida", valida);
            resultado.put("origem", dto.getBeneficioOrigemId());
            resultado.put("destino", dto.getBeneficioDestinoId());
            resultado.put("valor", dto.getValor());
            
            if (!valida) {
                resultado.put("motivo", "Transferência não é possível (saldo insuficiente, benefícios inativos ou IDs iguais)");
            }
            
            return Response.ok(resultado).build();
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro na validação");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
    
    @GET
    @Path("/taxa")
    public Response calcularTaxa(@QueryParam("valor") BigDecimal valor) {
        try {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Valor deve ser positivo");
                return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
            }
            
            var taxa = transferenciaUseCase.calcularTaxa(valor);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("valorOriginal", valor);
            resultado.put("taxa", taxa.getValor());
            resultado.put("valorComTaxa", valor.add(taxa.getValor()));
            
            return Response.ok(resultado).build();
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Erro no cálculo da taxa");
            erro.put("detalhes", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
        }
    }
}