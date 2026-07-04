package com.marina.turismo_cliente.controller;

import com.marina.turismo_cliente.dto.ClienteDTO;
import com.marina.turismo_cliente.dto.Sexo;
import com.marina.turismo_cliente.service.ClienteApiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

// Aplicação cliente: telas Thymeleaf que consomem, via RestClient, a API REST de
// clientes exposta pelo projeto sistema-turismo. Não acessa banco de dados diretamente.
@Controller
@RequestMapping("/clientes")
public class ClienteWebController {

    private final ClienteApiService clienteApiService;

    public ClienteWebController(ClienteApiService clienteApiService) {
        this.clienteApiService = clienteApiService;
    }

    // READ (lista) — GET /clientes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteApiService.listarTodos());
        return "clientes/lista";
    }

    // READ (formulário de criação) — GET /clientes/novo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new ClienteDTO());
        model.addAttribute("sexos", Sexo.values());
        return "clientes/form";
    }

    // CREATE — POST /clientes
    @PostMapping
    public String criar(@Valid @ModelAttribute("cliente") ClienteDTO cliente,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            return "clientes/form";
        }
        try {
            clienteApiService.salvar(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso.");
            return "redirect:/clientes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("sexos", Sexo.values());
            return "clientes/form";
        }
    }

    // READ (formulário de edição) — GET /clientes/{id}/editar
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        return clienteApiService.buscarPorId(id)
                .map(cliente -> {
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("sexos", Sexo.values());
                    return "clientes/form";
                })
                .orElse("redirect:/clientes");
    }

    // UPDATE — POST /clientes/{id}
    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("cliente") ClienteDTO cliente,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            return "clientes/form";
        }
        try {
            clienteApiService.atualizar(id, cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso.");
            return "redirect:/clientes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("sexos", Sexo.values());
            return "clientes/form";
        }
    }

    // DELETE — POST /clientes/{id}/excluir
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteApiService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/clientes";
    }
}
