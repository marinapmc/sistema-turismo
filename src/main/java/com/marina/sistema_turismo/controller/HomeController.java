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

        // Agência e cliente são representados pelo nosso UserDetailsImpl
        if (auth.getPrincipal() instanceof UserDetailsImpl principal) {
            return switch (principal.getRole()) {
                case "ROLE_AGENCIA" -> "redirect:/agencia/pacotes"; // área da agência
                case "ROLE_CLIENTE" -> "redirect:/pacotes";         // lista pública de pacotes
                default -> "index";
            };
        }

        // O admin usa o User padrão do Spring (não o UserDetailsImpl),
        // então verificamos pelo nome da authority diretamente
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/clientes";
        }

        return "index";
    }
}
