package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador da página inicial
// Redireciona o usuário para a área correta após o login, dependendo do seu papel
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication auth) {
        // Se não está logado, mostra a página inicial pública
        if (auth == null || !auth.isAuthenticated()) return "index";

        // Admin, agência e cliente são todos representados pelo UserDetailsImpl
        if (auth.getPrincipal() instanceof UserDetailsImpl principal) {
            return switch (principal.getRole()) {
                case "ROLE_ADMIN" -> "redirect:/admin/clientes";    // área do admin
                case "ROLE_AGENCIA" -> "redirect:/agencia/pacotes"; // área da agência
                case "ROLE_CLIENTE" -> "redirect:/pacotes";         // lista pública de pacotes
                default -> "index";
            };
        }

        return "index";
    }
}
