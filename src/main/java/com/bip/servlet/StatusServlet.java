package com.bip.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet simples para testar se o servidor está funcionando
 */
@WebServlet("/api/beneficios/status")
public class StatusServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("{");
        out.println("  \"status\": \"ATIVO\",");
        out.println("  \"versao\": \"2.0.0-Jakarta-EE\",");
        out.println("  \"descricao\": \"Sistema de Benefícios - Jakarta EE com Servlets\",");
        out.println("  \"servidor\": \"Jetty 11.0.18\",");
        out.println("  \"stack\": \"Jakarta EE 10 + Servlets + JPA 3.1 + CDI 4.0\",");
        out.println("  \"bugEjbCorrigido\": \"SIM - Implementação CDI thread-safe\",");
        out.println("  \"mensagem\": \"✅ Servidor funcionando! Agora você pode testar com Postman/Swagger\"");
        out.println("}");
        out.flush();
    }
}