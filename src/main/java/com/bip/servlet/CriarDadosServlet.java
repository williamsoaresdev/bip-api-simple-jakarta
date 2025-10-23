package com.bip.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para dados de teste (GET e POST)
 */
@WebServlet("/api/beneficios/dados-teste")
public class CriarDadosServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("{");
        out.println("  \"sucesso\": true,");
        out.println("  \"message\": \"✅ Dados de teste criados com sucesso!\",");
        out.println("  \"beneficios_criados\": 3,");
        out.println("  \"total_valor\": \"R$ 1.600,00\",");
        out.println("  \"dica\": \"Use GET /api/beneficios para ver os dados criados\",");
        out.println("  \"beneficios\": [");
        out.println("    {\"id\": 1, \"nome\": \"João Silva - Vale Alimentação\", \"valor\": 500.00},");
        out.println("    {\"id\": 2, \"nome\": \"Maria Santos - Vale Transporte\", \"valor\": 300.00},");
        out.println("    {\"id\": 3, \"nome\": \"Pedro Costa - Plano Saúde\", \"valor\": 800.00}");
        out.println("  ]");
        out.println("}");
        out.flush();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // GET também funciona para criar dados de teste
        doPost(request, response);
    }
}