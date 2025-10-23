package com.bip.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

/**
 * Configuração JAX-RS para a aplicação BIP
 * Define o path base para todos os endpoints REST
 */
@ApplicationPath("/api")
@OpenAPIDefinition(
    info = @Info(
        title = "BIP API - Sistema de Benefícios",
        version = "2.0.0-Jakarta-EE", 
        description = "API REST para gerenciamento de benefícios corporativos usando Jakarta EE com EJBs.\n\n" +
                     "🎯 **CORREÇÃO DO BUG EJB IMPLEMENTADA:**\n" +
                     "- Controle de concorrência pessimista\n" +
                     "- Transações ACID\n" +
                     "- Validações rigorosas de saldo\n" +
                     "- Prevenção de deadlocks\n\n" +
                     "**Stack Tecnológica:**\n" +
                     "- Jakarta EE 10\n" +
                     "- EJB 4.0 (Stateless)\n" +
                     "- JPA 3.1 (Jakarta Persistence)\n" +
                     "- CDI 4.0 (Context Dependency Injection)\n" +
                     "- JAX-RS 3.1 (REST Services)\n" +
                     "- MicroProfile 6.0",
        contact = @Contact(
            name = "Equipe BIP",
            email = "suporte@bip.com.br"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor de Desenvolvimento"),
        @Server(url = "http://localhost:9990", description = "WildFly Management Console")
    }
)
public class RestApplication extends Application {
    
    // A classe pode permanecer vazia
    // O Jakarta EE descobrirá automaticamente todos os endpoints REST
    // através da anotação @Path nos resources
    
}