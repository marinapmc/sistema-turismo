package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.service.PacoteTuristicoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

// Controlador MVC para a área pública de pacotes — sem necessidade de login
// produces = TEXT_HTML_VALUE diferencia este controller do REST controller que usa a mesma rota /pacotes
// Quando o navegador solicita HTML, vem para cá; quando solicita JSON, vai para o PacoteRestController
@Controller
public class PacoteController {

    private final PacoteTuristicoService pacoteService;

    public PacoteController(PacoteTuristicoService pacoteService) {
        this.pacoteService = pacoteService;
    }

    // Lista todos os pacotes com filtros opcionais por destino, agência e data de partida
    @GetMapping(value = "/pacotes", produces = MediaType.TEXT_HTML_VALUE)
    public String listar(@RequestParam(required = false) String destino,
                         @RequestParam(required = false) String agencia,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPartida,
                         Model model) {
        model.addAttribute("pacotes", pacoteService.filtrar(destino, agencia, dataPartida));
        // Repassa os filtros para o template manter os campos preenchidos após a busca
        model.addAttribute("destino", destino);
        model.addAttribute("agencia", agencia);
        model.addAttribute("dataPartida", dataPartida);
        return "pacotes/lista";
    }

    // Exibe os detalhes de um pacote específico pelo id
    // Também mostra o botão de compra
    @GetMapping(value = "/pacotes/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String detalhe(@PathVariable Long id, Model model) {
        var pacote = pacoteService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pacote não encontrado."));
        model.addAttribute("pacote", pacote);
        return "pacotes/detalhe";
    }
}
