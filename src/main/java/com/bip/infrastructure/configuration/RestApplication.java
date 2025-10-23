package com.bip.infrastructure.configuration;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import java.util.Set;
import java.util.HashSet;

@ApplicationPath("/api")
@OpenAPIDefinition(
    info = @Info(
        title = "BIP API - Sistema de Benefícios Clean Architecture",
        version = "3.0.0-Clean-Architecture", 
        description = "API REST para gerenciamento de benefícios corporativos seguindo Clean Architecture.\n\n" +
                     "🏗️ **CLEAN ARCHITECTURE IMPLEMENTADA:**\n" +
                     "- Domain: Entidades e regras de negócio\n" +
                     "- Application: Casos de uso e DTOs\n" +
                     "- Infrastructure: Implementações JPA\n" +
                     "- Presentation: Controllers REST\n\n" +
                     "**Stack Tecnológica:**\n" +
                     "- Jakarta EE 10\n" +
                     "- JPA 3.1 (Jakarta Persistence)\n" +
                     "- CDI 4.0 (Context Dependency Injection)\n" +
                     "- JAX-RS 3.1 (REST Services)\n" +
                     "- MicroProfile 6.0\n" +
                     "- Value Objects (Money, BeneficioId)",
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
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        classes.add(com.bip.presentation.controllers.BeneficioController.class);
        classes.add(com.bip.presentation.controllers.TransferenciaController.class);
        
        classes.add(com.bip.presentation.handlers.GlobalExceptionHandler.class);
        
        classes.add(JacksonConfig.class);
        
        return classes;
    }
}