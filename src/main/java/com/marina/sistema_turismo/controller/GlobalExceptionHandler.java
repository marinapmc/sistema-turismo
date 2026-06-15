package com.marina.sistema_turismo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

// Tratamento global de exceções para todos os controllers
// @ControllerAdvice faz com que os métodos aqui se apliquem a qualquer controller da aplicação
// Detecta se a requisição veio de um cliente REST (espera JSON) ou do navegador (espera HTML)
// e retorna a resposta no formato adequado
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Verifica se a requisição é de um cliente REST pelo cabeçalho Accept ou pelo caminho da URL
    private boolean isRestRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String uri = request.getRequestURI();
        return (accept != null && accept.contains("application/json"))
            || uri.startsWith("/clientes")
            || uri.startsWith("/agencias")
            || uri.startsWith("/pacotes/agencias")
            || uri.startsWith("/pacotes/clientes")
            || uri.startsWith("/pacotes/destinos");
    }

    // Trata 404 — quando nenhuma rota corresponde à URL acessada
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFound(NoHandlerFoundException ex, Model model, HttpServletRequest request) {
        // favicon.ico é pedido automaticamente pelo browser
        if (ex.getRequestURL().endsWith("favicon.ico")) {
            return ResponseEntity.noContent().build();
        }
        log.error("Página não encontrada: {}", ex.getRequestURL());
        if (isRestRequest(request)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", 404, "mensagem", "Recurso não encontrado."));
        }
        model.addAttribute("status", 404);
        model.addAttribute("mensagem", "Página não encontrada.");
        return "error";
    }

    // Trata violações de unicidade no banco (ex: e-mail ou CPF duplicado via REST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDuplicado(DataIntegrityViolationException ex, Model model, HttpServletRequest request) {
        log.error("Violação de integridade no banco: {}", ex.getMessage());
        if (isRestRequest(request)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("status", 409, "mensagem", "Registro duplicado."));
        }
        model.addAttribute("status", 409);
        model.addAttribute("mensagem", "Registro duplicado. Verifique os dados e tente novamente.");
        return "error";
    }

    // Trata erros de validação de negócio lançados pelos serviços (ex: "CPF já cadastrado")
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleArgumentoInvalido(IllegalArgumentException ex, Model model, HttpServletRequest request) {
        log.error("Argumento inválido: {}", ex.getMessage());
        if (isRestRequest(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", 400, "mensagem", ex.getMessage()));
        }
        model.addAttribute("status", 400);
        model.addAttribute("mensagem", ex.getMessage());
        return "error";
    }

    // Captura qualquer outra exceção não tratada explicitamente
    @ExceptionHandler(Exception.class)
    public Object handleGenerico(Exception ex, Model model, HttpServletRequest request) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        if (isRestRequest(request)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", 500, "mensagem", "Ocorreu um problema técnico."));
        }
        model.addAttribute("status", 500);
        model.addAttribute("mensagem", "Ocorreu um problema técnico. Tente novamente mais tarde.");
        return "error";
    }
}
