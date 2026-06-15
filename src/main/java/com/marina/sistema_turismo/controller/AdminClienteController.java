package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.model.Sexo;
import com.marina.sistema_turismo.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador MVC para o CRUD de clientes — acessível apenas pelo admin 
@Controller
@RequestMapping("/admin/clientes")
public class AdminClienteController {

    private final ClienteService clienteService;

    public AdminClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Lista todos os clientes cadastrados
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "admin/clientes";
    }

    // Exibe o formulário vazio para cadastrar um novo cliente
    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("cliente", new Cliente()); 
        model.addAttribute("sexos", Sexo.values());   
        return "admin/cliente-form";
    }

    // Processa o envio do formulário de cadastro
    // @Valid dispara as validações das anotações na entidade (@NotBlank, @Email, etc.)
    // BindingResult captura os erros de validação sem lançar exceção
    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute Cliente cliente,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            return "admin/cliente-form"; // volta ao formulário com os erros destacados
        }
        try {
            clienteService.salvar(cliente);
            redirect.addFlashAttribute("sucesso", "Cliente cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            // Captura erros de negócio (e-mail ou CPF duplicado)
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("sexos", Sexo.values());
            return "admin/cliente-form";
        }
        return "redirect:/admin/clientes";
    }

    // Exibe o formulário preenchido com os dados do cliente para edição
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        model.addAttribute("cliente", cliente);
        model.addAttribute("sexos", Sexo.values());
        return "admin/cliente-form";
    }

    // Processa o envio do formulário de edição
    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute Cliente cliente,
                            BindingResult result, Model model,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("sexos", Sexo.values());
            return "admin/cliente-form";
        }
        try {
            clienteService.atualizar(id, cliente);
            redirect.addFlashAttribute("sucesso", "Cliente atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("sexos", Sexo.values());
            return "admin/cliente-form";
        }
        return "redirect:/admin/clientes";
    }

    // Exclui o cliente e volta para a listagem
    // Usa POST em vez de DELETE por limitação dos formulários HTML
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            clienteService.excluir(id);
            redirect.addFlashAttribute("sucesso", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Não foi possível excluir o cliente.");
        }
        return "redirect:/admin/clientes";
    }
}
