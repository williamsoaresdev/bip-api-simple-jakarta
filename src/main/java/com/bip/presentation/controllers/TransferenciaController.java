package com.bip.presentation.controllers;

import com.bip.application.dtos.HistoricoTransferenciaDto;
import com.bip.application.dtos.TransferenciaDto;
import com.bip.application.usecases.TransferenciaUseCase;
import com.bip.presentation.utils.ErrorResponseBuilder;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para operações de transferência entre benefícios.
 * 
 * <p>Fornece endpoints para:</p>
 * <ul>
 *   <li>Listar histórico de transferências</li>
 *   <li>Executar transferências entre benefícios</li>
 *   <li>Validar transferências antes da execução</li>
 *   <li>Calcular taxas de transferência</li>
 *   <li>Obter estatísticas de transferências</li>
 * </ul>
 * 
 * <p>Todos os endpoints seguem padrões REST e utilizam tratamento
 * de erro padronizado através do {@link ErrorResponseBuilder}.</p>
 * 
 * @author BIP API Team
 * @since 1.0
 * @version 3.0.0
 */
@Path("/transferencias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferenciaController {
    
    @Inject
    private TransferenciaUseCase transferenciaUseCase;
    
    @Inject
    private ErrorResponseBuilder errorResponseBuilder;
    
    /**
     * Retorna o status do módulo de transferências.
     * 
     * @return resposta com informações do status
     */
    @GET
    @Path("/status")
    public Response getStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("modulo", "Transferências");
            status.put("status", "Operacional");
            status.put("timestamp", LocalDateTime.now());
            status.put("totalTransferencias", transferenciaUseCase.contarTransferencias());
            status.put("endpoints", List.of(
                "GET /api/transferencias - Lista histórico de transferências",
                "GET /api/transferencias/{id} - Busca transferência por ID",
                "POST /api/transferencias - Executa nova transferência",
                "POST /api/transferencias/validar - Valida transferência",
                "GET /api/transferencias/taxa?valor=X - Calcula taxa",
                "GET /api/transferencias/status - Status do módulo"
            ));
            
            return errorResponseBuilder.buildSuccessResponse(status);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    /**
     * Lista todas as transferências realizadas.
     * 
     * @return resposta com lista de transferências
     */
    @GET
    public Response listarTransferencias() {
        try {
            List<HistoricoTransferenciaDto> transferencias = transferenciaUseCase.listarTransferencias();
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("transferencias", transferencias);
            resultado.put("total", transferencias.size());
            resultado.put("timestamp", LocalDateTime.now());
            
            return errorResponseBuilder.buildSuccessResponse(resultado);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    /**
     * Busca uma transferência específica por ID.
     * 
     * @param id ID da transferência
     * @return resposta com dados da transferência
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return errorResponseBuilder.buildBadRequestError(
                    new IllegalArgumentException("ID deve ser um número positivo"));
            }
            
            HistoricoTransferenciaDto transferencia = transferenciaUseCase.buscarPorId(id);
            
            if (transferencia != null) {
                return errorResponseBuilder.buildSuccessResponse(transferencia);
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Transferência não encontrada");
                erro.put("id", id);
                return Response.status(Response.Status.NOT_FOUND).entity(erro).build();
            }
            
        } catch (IllegalArgumentException e) {
            return errorResponseBuilder.buildBadRequestError(e);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    /**
     * Executa uma transferência entre dois benefícios.
     * 
     * @param dto dados da transferência
     * @return resposta com resultado da operação
     */
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
            
            return errorResponseBuilder.buildSuccessResponse(resultado);
            
        } catch (IllegalArgumentException e) {
            return errorResponseBuilder.buildBadRequestError(e);
            
        } catch (IllegalStateException e) {
            return errorResponseBuilder.buildBadRequestError(e);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    /**
     * Valida uma transferência sem executá-la.
     * 
     * @param dto dados da transferência
     * @return resposta indicando se a transferência é válida
     */
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
            
            return errorResponseBuilder.buildSuccessResponse(resultado);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
    
    /**
     * Calcula a taxa para uma transferência baseada no valor.
     * 
     * @param valor valor da transferência
     * @return resposta com cálculo da taxa
     */
    @GET
    @Path("/taxa")
    public Response calcularTaxa(@QueryParam("valor") BigDecimal valor) {
        try {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                return errorResponseBuilder.buildBadRequestError(
                    new IllegalArgumentException("Valor deve ser positivo"));
            }
            
            var taxa = transferenciaUseCase.calcularTaxa(valor);
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("valorOriginal", valor);
            resultado.put("taxa", taxa.getValor());
            resultado.put("valorComTaxa", valor.add(taxa.getValor()));
            
            return errorResponseBuilder.buildSuccessResponse(resultado);
            
        } catch (Exception e) {
            return errorResponseBuilder.buildInternalServerError(e);
        }
    }
}