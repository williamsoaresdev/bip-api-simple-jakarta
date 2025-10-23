package com.bip.presentation.handlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        
        if (exception instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) exception);
        }
        
        if (exception instanceof IllegalArgumentException) {
            return handleIllegalArgument((IllegalArgumentException) exception);
        }
        
        if (exception instanceof IllegalStateException) {
            return handleIllegalState((IllegalStateException) exception);
        }
        
        return handleGenericError(exception);
    }
    
    private Response handleConstraintViolation(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Dados inválidos");
        erro.put("tipo", "VALIDATION_ERROR");
        
        if (!violations.isEmpty()) {
            String detalhes = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            erro.put("detalhes", detalhes);
        }
        
        return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
    }
    
    private Response handleIllegalArgument(IllegalArgumentException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Dados inválidos");
        erro.put("tipo", "INVALID_ARGUMENT");
        erro.put("detalhes", exception.getMessage());
        
        return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
    }
    
    private Response handleIllegalState(IllegalStateException exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Operação não permitida");
        erro.put("tipo", "INVALID_STATE");
        erro.put("detalhes", exception.getMessage());
        
        return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
    }
    
    private Response handleGenericError(Exception exception) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("erro", "Erro interno do servidor");
        erro.put("tipo", "INTERNAL_ERROR");
        erro.put("detalhes", exception.getMessage());
        erro.put("timestamp", java.time.LocalDateTime.now());
        
        // Log do erro (simplificado)
        System.err.println("Erro não tratado: " + exception.getClass().getSimpleName());
        System.err.println("Mensagem: " + exception.getMessage());
        exception.printStackTrace();
        
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(erro).build();
    }
}