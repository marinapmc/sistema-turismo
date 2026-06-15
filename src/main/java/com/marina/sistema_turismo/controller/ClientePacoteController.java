package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.security.UserDetailsImpl;
import com.marina.sistema_turismo.service.CompraService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Controlador do histórico de compras do cliente 
// Mostra todos os pacotes que o cliente logado já comprou, com o link da videoconferência
@Controller
@RequestMapping("/cliente")
public class ClientePacoteController {

    private final CompraService compraService;

    public ClientePacoteController(CompraService compraService) {
        this.compraService = compraService;
    }

    // Busca as compras do cliente logado e envia para o template
    // O template exibe cada compra com os dados do pacote e o botão de link da reunião
    @GetMapping("/pacotes")
    public String meusPackotes(@AuthenticationPrincipal UserDetailsImpl principal, Model model) {
        Cliente cliente = (Cliente) principal.getUsuario();
        model.addAttribute("compras", compraService.listarPorCliente(cliente));
        return "cliente/pacotes";
    }
}
