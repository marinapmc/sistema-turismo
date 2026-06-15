package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.model.PacoteTuristico;
import com.marina.sistema_turismo.security.UserDetailsImpl;
import com.marina.sistema_turismo.service.CompraService;
import com.marina.sistema_turismo.service.PacoteTuristicoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador responsável pela compra de um pacote turístico 
@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final PacoteTuristicoService pacoteService;

    public CompraController(CompraService compraService, PacoteTuristicoService pacoteService) {
        this.compraService = compraService;
        this.pacoteService = pacoteService;
    }

    // Processa a compra quando o cliente clica em "Comprar" na página de detalhe do pacote
    // O id do pacote vem na URL e o cliente logado é injetado via @AuthenticationPrincipal
    @PostMapping("/novo/{pacoteId}")
    public String comprar(@PathVariable Long pacoteId,
                          @AuthenticationPrincipal UserDetailsImpl principal,
                          RedirectAttributes redirect) {
        PacoteTuristico pacote = pacoteService.buscarPorId(pacoteId)
                .orElseThrow(() -> new IllegalArgumentException("Pacote não encontrado."));

        Cliente cliente = (Cliente) principal.getUsuario(); // cast seguro pois o role é CLIENTE

        // Registra a compra, gera o link da reunião e envia os e-mails
        compraService.realizarCompra(cliente, pacote);

        // Mensagem exibida na página de histórico após o redirecionamento
        redirect.addFlashAttribute("sucesso",
                "Compra realizada! Verifique seu e-mail para detalhes e o link da reunião.");

        return "redirect:/cliente/pacotes"; // redireciona para o histórico de compras
    }
}
