package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador MVC para o CRUD de agências — acessível apenas pelo admin 
@Controller
@RequestMapping("/admin/agencias")
public class AdminAgenciaController {

    private final AgenciaService agenciaService;

    public AdminAgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    // Lista todas as agências cadastradas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("agencias", agenciaService.listarTodas());
        return "admin/agencias";
    }

    // Formulário vazio para cadastrar nova agência
    @GetMapping("/nova")
    public String formNova(Model model) {
        model.addAttribute("agencia", new Agencia());
        return "admin/agencia-form";
    }

    // Salva nova agência após validação
    @PostMapping("/nova")
    public String salvar(@Valid @ModelAttribute Agencia agencia,
                         BindingResult result, Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) return "admin/agencia-form";
        try {
            agenciaService.salvar(agencia);
            redirect.addFlashAttribute("sucesso", "Agência cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "admin/agencia-form";
        }
        return "redirect:/admin/agencias";
    }

    // Formulário preenchido para editar agência existente
    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        Agencia agencia = agenciaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada."));
        model.addAttribute("agencia", agencia);
        return "admin/agencia-form";
    }

    // Atualiza os dados da agência
    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute Agencia agencia,
                            BindingResult result, Model model,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) return "admin/agencia-form";
        try {
            agenciaService.atualizar(id, agencia);
            redirect.addFlashAttribute("sucesso", "Agência atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "admin/agencia-form";
        }
        return "redirect:/admin/agencias";
    }

    // Remove a agência do banco de dados
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            agenciaService.excluir(id);
            redirect.addFlashAttribute("sucesso", "Agência excluída com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Não foi possível excluir a agência.");
        }
        return "redirect:/admin/agencias";
    }
}
