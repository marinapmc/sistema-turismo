package com.marina.sistema_turismo.controller;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.PacoteTuristico;
import com.marina.sistema_turismo.security.UserDetailsImpl;
import com.marina.sistema_turismo.service.FileStorageService;
import com.marina.sistema_turismo.service.PacoteTuristicoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

// Controlador MVC para agências gerenciarem seus próprios pacotes 
@Controller
@RequestMapping("/agencia/pacotes")
public class AgenciaPacoteController {

    private static final Logger log = LoggerFactory.getLogger(AgenciaPacoteController.class);

    private final PacoteTuristicoService pacoteService;
    private final FileStorageService fileStorageService;

    public AgenciaPacoteController(PacoteTuristicoService pacoteService,
                                   FileStorageService fileStorageService) {
        this.pacoteService = pacoteService;
        this.fileStorageService = fileStorageService;
    }

    // Lista os pacotes da agência logada
    // O parâmetro ?vigentes=true filtra só os com data de partida futura
    @GetMapping
    public String listar(@AuthenticationPrincipal UserDetailsImpl principal,
                         @RequestParam(required = false) Boolean vigentes,
                         Model model) {
        Agencia agencia = (Agencia) principal.getUsuario();
        var pacotes = Boolean.TRUE.equals(vigentes)
                ? pacoteService.buscarVigentesPorAgencia(agencia)
                : pacoteService.buscarPorAgencia(agencia);
        model.addAttribute("pacotes", pacotes);
        model.addAttribute("vigentes", vigentes);
        return "agencia/pacotes";
    }

    // Formulário vazio para cadastrar novo pacote
    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("pacote", new PacoteTuristico());
        return "agencia/pacote-form";
    }

    // Salva o pacote com as fotos e o roteiro PDF enviados pelo formulário
    @PostMapping("/novo")
    public String salvar(@AuthenticationPrincipal UserDetailsImpl principal,
                         @Valid @ModelAttribute PacoteTuristico pacote,
                         BindingResult result,
                         MultipartHttpServletRequest multipartRequest,
                         Model model, RedirectAttributes redirect) {
        System.err.println("=== POST /agencia/pacotes/novo recebido ===");
        System.err.println("Parts multipart: " + multipartRequest.getMultiFileMap().keySet());
        List<MultipartFile> fotosUpload = multipartRequest.getFiles("fotosUpload");
        MultipartFile roteiro = multipartRequest.getFile("roteiro");
        System.err.println("fotosUpload count: " + fotosUpload.size());
        fotosUpload.forEach(f -> System.err.println("  foto: " + f.getOriginalFilename() + " size=" + f.getSize() + " empty=" + f.isEmpty()));

        if (result.hasErrors()) {
            log.info("Erros de validacao: {}", result.getAllErrors());
            return "agencia/pacote-form";
        }
        try {
            Agencia agencia = (Agencia) principal.getUsuario();
            pacote.setAgencia(agencia);

            // Filtra apenas arquivos não-vazios e limita a 10 fotos por pacote
            boolean temFotos = fotosUpload.stream().anyMatch(f -> !f.isEmpty());
            if (temFotos) {
                long count = fotosUpload.stream().filter(f -> !f.isEmpty()).count();
                if (count > 10) {
                    model.addAttribute("erro", "Máximo de 10 fotos permitidas.");
                    return "agencia/pacote-form";
                }
                pacote.setFotos(fileStorageService.salvarFotos(fotosUpload));
            } else {
                pacote.setFotos(List.of());
            }

            if (roteiro != null && !roteiro.isEmpty()) {
                pacote.setRoteiroPdf(fileStorageService.salvarRoteiro(roteiro));
            }

            pacoteService.salvar(pacote);
            redirect.addFlashAttribute("sucesso", "Pacote cadastrado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao salvar pacote: " + e.getMessage());
            return "agencia/pacote-form";
        }
        return "redirect:/agencia/pacotes";
    }

    // Remove o pacote pelo id
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            pacoteService.excluir(id);
            redirect.addFlashAttribute("sucesso", "Pacote excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Não foi possível excluir o pacote.");
        }
        return "redirect:/agencia/pacotes";
    }
}
