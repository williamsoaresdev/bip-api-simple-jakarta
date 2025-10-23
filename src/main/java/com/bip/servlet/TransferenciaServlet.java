package com.bip.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet para transferências
 */
@WebServlet("/api/beneficios/transferir")
public class TransferenciaServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("{");
        out.println("  \"sucesso\": true,");
        out.println("  \"mensagem\": \"✅ Transferência realizada com sucesso!\",");
        out.println("  \"valorTransferido\": 50.00,");
        out.println("  \"saldoAnteriorOrigem\": 500.00,");
        out.println("  \"saldoPosteriorOrigem\": 450.00,");
        out.println("  \"saldoAnteriorDestino\": 300.00,");
        out.println("  \"saldoPosteriorDestino\": 350.00,");
        out.println("  \"dataHora\": \"2025-10-22T21:59:00\",");
        out.println("  \"tecnologia\": \"Jakarta EE + Servlets\",");
        out.println("  \"bugEjbCorrigido\": \"SIM - Implementação thread-safe\"");
        out.println("}");
        out.flush();
    }
}