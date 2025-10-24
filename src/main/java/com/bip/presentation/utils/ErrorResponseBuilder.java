package com.bip.presentation.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

/**
 * Utilitário para construção de respostas de erro padronizadas.
 * Elimina duplicação de código nos controllers e centraliza o tratamento de exceções.
 * 
 * @author BIP API Team
 * @since 1.0
 */
@ApplicationScoped
public class ErrorResponseBuilder {

    /**
     * Cria uma resposta de erro com status interno do servidor (500).
     * 
     * @param exception a exceção que causou o erro
     * @return Response com o erro formatado
     */
    public Response buildInternalServerError(final Exception exception) {
        return buildErrorResponse(
            Response.Status.INTERNAL_SERVER_ERROR, 
            "Erro interno do servidor", 
            exception.getMessage()
        );
    }

    /**
     * Cria uma resposta de erro com status de requisição inválida (400).
     * 
     * @param exception a exceção que causou o erro
     * @return Response com o erro formatado
     */
    public Response buildBadRequestError(final Exception exception) {
        return buildErrorResponse(
            Response.Status.BAD_REQUEST, 
            "Requisição inválida", 
            exception.getMessage()
        );
    }

    /**
     * Cria uma resposta de erro com status não encontrado (404).
     * 
     * @param message mensagem específica do erro
     * @return Response com o erro formatado
     */
    public Response buildNotFoundError(final String message) {
        return buildErrorResponse(
            Response.Status.NOT_FOUND, 
            "Recurso não encontrado", 
            message
        );
    }

    /**
     * Cria uma resposta de erro com status não encontrado (404).
     * 
     * @param exception a exceção que causou o erro
     * @return Response com o erro formatado
     */
    public Response buildNotFoundError(final Exception exception) {
        return buildNotFoundError(exception.getMessage());
    }

    /**
     * Cria uma resposta de erro com status e mensagens personalizadas.
     * 
     * @param status o status HTTP da resposta
     * @param errorMessage a mensagem principal do erro
     * @param details os detalhes específicos do erro
     * @return Response com o erro formatado
     */
    public Response buildErrorResponse(final Response.Status status, 
                                     final String errorMessage, 
                                     final String details) {
        final Map<String, Object> erro = new ConcurrentHashMap<>();
        erro.put("erro", errorMessage != null ? errorMessage : "Erro não especificado");
        erro.put("detalhes", details != null ? details : "Detalhes não disponíveis");
        
        return Response.status(status).entity(erro).build();
    }

    /**
     * Cria uma resposta de sucesso sem entidade.
     * 
     * @return Response com status OK
     */
    public Response buildSuccessResponse() {
        final Map<String, Object> sucesso = new ConcurrentHashMap<>();
        sucesso.put("mensagem", "Operação realizada com sucesso");
        
        return Response.ok(sucesso).build();
    }

    /**
     * Cria uma resposta de sucesso com entidade.
     * 
     * @param entity a entidade a ser retornada
     * @return Response com status OK e a entidade
     */
    public Response buildSuccessResponse(final Object entity) {
        return Response.ok(entity).build();
    }
}