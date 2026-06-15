package com.marina.sistema_turismo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador responsável pela página de login
// O processamento do login em si é feito automaticamente pelo Spring Security
// Este controller só exibe a página e trata as mensagens de erro/logout
@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        // Spring Security adiciona "?error" na URL quando a senha está errada
        if (error != null) {
            model.addAttribute("erro", "E-mail ou senha incorretos.");
        }
        // Spring Security adiciona "?logout" na URL após o usuário sair
        if (logout != null) {
            model.addAttribute("mensagem", "Você saiu com sucesso.");
        }
        return "login";
    }
}
