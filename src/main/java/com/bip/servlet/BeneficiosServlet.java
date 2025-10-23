package com.bip.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para listar benefícios
 */
@WebServlet("/api/beneficios")
public class BeneficiosServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("[");
        out.println("  {");
        out.println("    \"id\": 1,");
        out.println("    \"nome\": \"João Silva - Vale Alimentação\",");
        out.println("    \"descricao\": \"Benefício alimentação para colaborador\",");
        out.println("    \"valor\": 500.00,");
        out.println("    \"ativo\": true");
        out.println("  },");
        out.println("  {");
        out.println("    \"id\": 2,");
        out.println("    \"nome\": \"Maria Santos - Vale Transporte\",");
        out.println("    \"descricao\": \"Benefício transporte para colaboradora\",");
        out.println("    \"valor\": 300.00,");
        out.println("    \"ativo\": true");
        out.println("  },");
        out.println("  {");
        out.println("    \"id\": 3,");
        out.println("    \"nome\": \"Pedro Costa - Plano Saúde\",");
        out.println("    \"descricao\": \"Benefício saúde para colaborador\",");
        out.println("    \"valor\": 800.00,");
        out.println("    \"ativo\": true");
        out.println("  }");
        out.println("]");
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(201);
        
        PrintWriter out = response.getWriter();
        out.println("{");
        out.println("  \"sucesso\": true,");
        out.println("  \"mensagem\": \"Benefício criado com sucesso\",");
        out.println("  \"id\": 4,");
        out.println("  \"nome\": \"Novo Benefício\",");
        out.println("  \"valor\": 100.00,");
        out.println("  \"ativo\": true");
        out.println("}");
        out.flush();
    }
}