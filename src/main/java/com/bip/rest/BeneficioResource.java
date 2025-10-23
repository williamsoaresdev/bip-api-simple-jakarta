package com.bip.rest;

import com.bip.service.BeneficioService;
import com.bip.service.TransferenciaService;
import com.bip.entities.Beneficio;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * REST Resource para gerenciamento de benefícios usando Jakarta EE
 * Implementa todas as operações CRUD e transferências seguras
 */
@Path("/beneficios")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios com Jakarta EE e EJBs")
public class BeneficioResource {
    
    private static final Logger logger = Logger.getLogger(BeneficioResource.class.getName());
    
    @Inject
    private BeneficioService beneficioService;
    
    @Inject
    private TransferenciaService transferenciaService;
    
    // ================================
    // Status e Informações
    // ================================
    
    @GET
    @Path("/status")
    @Operation(
        summary = "Status da aplicação Jakarta EE",
        description = "Retorna informações sobre o status da aplicação e correções implementadas"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Status da aplicação",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))
    })
    public Response status() {
        logger.info("Consultando status da aplicação Jakarta EE");
        
        Map<String, Object> status = new HashMap<>();
        status.put("status", "ATIVO");
        status.put("versao", "2.0.0-Jakarta-EE");
        status.put("descricao", "Sistema de Benefícios - Jakarta EE com EJBs");
        status.put("stack", "Jakarta EE 10 + EJB 4.0 + JPA 3.1 + CDI 4.0 + JAX-RS 3.1");
        status.put("bugEjbCorrigido", "SIM - Transferências thread-safe com controle de concorrência");
        status.put("servidor", System.getProperty("jboss.server.name", "WildFly/Payara"));
        status.put("openapi", "/openapi");
        
        // Estatísticas dos benefícios
        try {
            long totalAtivos = beneficioService.contarAtivos();
            BigDecimal valorTotal = beneficioService.calcularValorTotalAtivos();
            
            Map<String, Object> estatisticas = new HashMap<>();
            estatisticas.put("totalBeneficiosAtivos", totalAtivos);
            estatisticas.put("valorTotalBeneficios", valorTotal);
            
            status.put("estatisticas", estatisticas);
        } catch (Exception e) {
            logger.warning("Erro ao obter estatísticas: " + e.getMessage());
            status.put("estatisticas", "Indisponível");
        }
        
        return Response.ok(status).build();
    }
    
    @POST
    @Path("/dados-teste")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Cria dados de teste", description = "Popula a base com dados para testes")
    public Response criarDadosTeste() {
        logger.info("Criando dados de teste...");
        
        try {
            // Criar alguns benefícios de exemplo
            Beneficio b1 = new Beneficio();
            b1.setNome("João Silva - Vale Alimentação");
            b1.setDescricao("Benefício alimentação para colaborador");
            b1.setValor(new java.math.BigDecimal("500.00"));
            b1.ativar();
            beneficioService.criar(b1);

            Beneficio b2 = new Beneficio();
            b2.setNome("Maria Santos - Vale Transporte"); 
            b2.setDescricao("Benefício transporte para colaboradora");
            b2.setValor(new java.math.BigDecimal("300.00"));
            b2.ativar();
            beneficioService.criar(b2);

            Beneficio b3 = new Beneficio();
            b3.setNome("Pedro Costa - Plano Saúde");
            b3.setDescricao("Benefício saúde para colaborador");
            b3.setValor(new java.math.BigDecimal("800.00"));
            b3.ativar();
            beneficioService.criar(b3);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("message", "✅ Dados de teste criados com sucesso!");
            resultado.put("beneficios_criados", 3);
            resultado.put("total_valor", "R$ 1.600,00");
            resultado.put("dica", "Use GET /api/beneficios para ver os dados criados");
            
            return Response.ok(resultado).build();
            
        } catch (Exception e) {
            logger.severe("Erro ao criar dados de teste: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Erro ao criar dados: " + e.getMessage()))
                    .build();
        }
    }
    
    // ================================
    // CRUD Operations
    // ================================
    
    @GET
    @Operation(
        summary = "Listar todos os benefícios",
        description = "Retorna lista completa de benefícios (ativos e inativos)"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de benefícios retornada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, 
                                     schema = @Schema(implementation = Beneficio.class)))
    })
    public Response listarTodos() {
        logger.info("Listando todos os benefícios");
        
        try {
            List<Beneficio> beneficios = beneficioService.listarTodos();
            return Response.ok(beneficios).build();
        } catch (Exception e) {
            logger.severe("Erro ao listar benefícios: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    @GET
    @Path("/ativos")
    @Operation(
        summary = "Listar benefícios ativos",
        description = "Retorna apenas os benefícios com status ativo"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Lista de benefícios ativos")
    })
    public Response listarAtivos() {
        logger.info("Listando benefícios ativos");
        
        try {
            List<Beneficio> beneficios = beneficioService.listarAtivos();
            return Response.ok(beneficios).build();
        } catch (Exception e) {
            logger.severe("Erro ao listar benefícios ativos: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar benefício por ID",
        description = "Retorna um benefício específico pelo seu identificador"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Benefício encontrado"),
        @APIResponse(responseCode = "404", description = "Benefício não encontrado")
    })
    public Response buscarPorId(
            @PathParam("id") @NotNull Long id) {
        
        logger.info("Buscando benefício por ID: " + id);
        
        try {
            Optional<Beneficio> beneficio = beneficioService.buscarPorId(id);
            
            if (beneficio.isPresent()) {
                return Response.ok(beneficio.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity(criarErroResponse("Não encontrado", 
                                     "Benefício não encontrado com ID: " + id))
                              .build();
            }
        } catch (Exception e) {
            logger.severe("Erro ao buscar benefício: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    @POST
    @Operation(
        summary = "Criar novo benefício",
        description = "Cria um novo benefício no sistema"
    )
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Benefício criado com sucesso"),
        @APIResponse(responseCode = "400", description = "Dados inválidos"),
        @APIResponse(responseCode = "409", description = "Conflito - nome já existe")
    })
    public Response criar(
            @RequestBody(description = "Dados do novo benefício", required = true,
                        content = @Content(schema = @Schema(implementation = Beneficio.class)))
            @Valid Beneficio beneficio) {
        
        logger.info("Criando novo benefício: " + beneficio.getNome());
        
        try {
            Beneficio novoBeneficio = beneficioService.criar(beneficio);
            return Response.status(Response.Status.CREATED)
                          .entity(novoBeneficio)
                          .build();
                          
        } catch (IllegalArgumentException e) {
            logger.warning("Erro de validação ao criar benefício: " + e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                          .entity(criarErroResponse("Conflito", e.getMessage()))
                          .build();
        } catch (Exception e) {
            logger.severe("Erro interno ao criar benefício: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    @PUT
    @Path("/{id}/ativar")
    @Operation(
        summary = "Ativar benefício",
        description = "Ativa um benefício que estava inativo"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Benefício ativado com sucesso"),
        @APIResponse(responseCode = "404", description = "Benefício não encontrado"),
        @APIResponse(responseCode = "409", description = "Benefício já está ativo")
    })
    public Response ativar(
            @PathParam("id") @NotNull Long id) {
        
        logger.info("Ativando benefício ID: " + id);
        
        try {
            beneficioService.ativar(id);
            return Response.ok()
                          .entity(criarSucessoResponse("Benefício ativado com sucesso"))
                          .build();
                          
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                          .entity(criarErroResponse("Conflito", e.getMessage()))
                          .build();
        } catch (Exception e) {
            logger.severe("Erro ao ativar benefício: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    @PUT
    @Path("/{id}/desativar")
    @Operation(
        summary = "Desativar benefício", 
        description = "Desativa um benefício ativo"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Benefício desativado com sucesso"),
        @APIResponse(responseCode = "404", description = "Benefício não encontrado"),
        @APIResponse(responseCode = "409", description = "Benefício já está inativo")
    })
    public Response desativar(
            @PathParam("id") @NotNull Long id) {
        
        logger.info("Desativando benefício ID: " + id);
        
        try {
            beneficioService.desativar(id);
            return Response.ok()
                          .entity(criarSucessoResponse("Benefício desativado com sucesso"))
                          .build();
                          
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                          .entity(criarErroResponse("Conflito", e.getMessage()))
                          .build();
        } catch (Exception e) {
            logger.severe("Erro ao desativar benefício: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    // ================================
    // Transferências (Correção do Bug EJB)
    // ================================
    
    @POST
    @Path("/transferir")
    @Operation(
        summary = "Realizar transferência entre benefícios",
        description = "🎯 **CORREÇÃO DO BUG EJB:** Transferência segura com controle de concorrência pessimista,\n" +
                     "transações ACID, validações rigorosas e prevenção de deadlocks.\n\n" +
                     "**Melhorias implementadas:**\n" +
                     "- Lock pessimista (`PESSIMISTIC_WRITE`)\n" +
                     "- Ordenação de IDs para evitar deadlocks\n" +
                     "- Validações de saldo e estado\n" +
                     "- Rollback automático em caso de erro\n" +
                     "- Auditoria completa da operação"
    )
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
        @APIResponse(responseCode = "400", description = "Dados de transferência inválidos"),
        @APIResponse(responseCode = "404", description = "Benefício não encontrado"),
        @APIResponse(responseCode = "409", description = "Saldo insuficiente ou benefício inativo")
    })
    public Response transferir(
            @RequestBody(description = "Dados da transferência", required = true,
                        content = @Content(schema = @Schema(implementation = TransferenciaRequest.class)))
            @Valid TransferenciaRequest request) {
        
        logger.info(String.format("=== TRANSFERÊNCIA JAKARTA EE ==="));
        logger.info(String.format("Origem: %d -> Destino: %d | Valor: R$ %s", 
                                request.getBeneficioOrigemId(), 
                                request.getBeneficioDestinoId(), 
                                request.getValor()));
        
        try {
            TransferenciaService.TransferenciaResultado resultado = transferenciaService.executarTransferencia(
                request.getBeneficioOrigemId(),
                request.getBeneficioDestinoId(),
                request.getValor(),
                request.getDescricao()
            );
            
            if (resultado.isSucesso()) {
                Map<String, Object> response = new HashMap<>();
                response.put("sucesso", true);
                response.put("mensagem", resultado.getMensagem());
                response.put("valorTransferido", resultado.getValorTransferido());
                response.put("saldoAnteriorOrigem", resultado.getSaldoAnteriorOrigem());
                response.put("saldoPosteriorOrigem", resultado.getSaldoPosteriorOrigem());
                response.put("saldoAnteriorDestino", resultado.getSaldoAnteriorDestino());
                response.put("saldoPosteriorDestino", resultado.getSaldoPosteriorDestino());
                response.put("dataHora", resultado.getDataHora());
                response.put("tecnologia", "Jakarta EE + EJB");
                
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                              .entity(criarErroResponse("Erro na transferência", resultado.getMensagem()))
                              .build();
            }
            
        } catch (Exception e) {
            logger.severe("Erro interno na transferência: " + e.getMessage());
            return Response.serverError()
                          .entity(criarErroResponse("Erro interno", e.getMessage()))
                          .build();
        }
    }
    
    // ================================
    // Classes Auxiliares
    // ================================
    
    /**
     * DTO para requisições de transferência
     */
    public static class TransferenciaRequest {
        @NotNull(message = "ID do benefício de origem é obrigatório")
        private Long beneficioOrigemId;
        
        @NotNull(message = "ID do benefício de destino é obrigatório") 
        private Long beneficioDestinoId;
        
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        private BigDecimal valor;
        
        private String descricao;
        
        // Construtores
        public TransferenciaRequest() {}
        
        public TransferenciaRequest(Long beneficioOrigemId, Long beneficioDestinoId, 
                                   BigDecimal valor, String descricao) {
            this.beneficioOrigemId = beneficioOrigemId;
            this.beneficioDestinoId = beneficioDestinoId;
            this.valor = valor;
            this.descricao = descricao;
        }
        
        // Getters e Setters
        public Long getBeneficioOrigemId() { return beneficioOrigemId; }
        public void setBeneficioOrigemId(Long beneficioOrigemId) { this.beneficioOrigemId = beneficioOrigemId; }
        
        public Long getBeneficioDestinoId() { return beneficioDestinoId; }
        public void setBeneficioDestinoId(Long beneficioDestinoId) { this.beneficioDestinoId = beneficioDestinoId; }
        
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
    }
    
    // ================================
    // Métodos Auxiliares
    // ================================
    
    private Map<String, Object> criarErroResponse(String erro, String mensagem) {
        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", false);
        response.put("erro", erro);
        response.put("mensagem", mensagem);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    private Map<String, Object> criarSucessoResponse(String mensagem) {
        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", true);
        response.put("mensagem", mensagem);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}