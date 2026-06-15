package com.marina.sistema_turismo.controller.rest;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// API REST para agências — mesma estrutura do ClienteRestController
// Todos os endpoints são públicos e retornam JSON
@RestController
@RequestMapping(value = "/agencias", produces = MediaType.APPLICATION_JSON_VALUE)
public class AgenciaRestController {

    private final AgenciaService agenciaService;

    public AgenciaRestController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    // GET /agencias — lista todas as agências
    @GetMapping
    public List<Agencia> listar() {
        return agenciaService.listarTodas();
    }

    // GET /agencias/{id} — busca uma agência pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Agencia> buscar(@PathVariable Long id) {
        return agenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /agencias — cria uma nova agência
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Agencia> criar(@Valid @RequestBody Agencia agencia) {
        Agencia salva = agenciaService.salvar(agencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    // PUT /agencias/{id} — atualiza nome, descrição e senha da agência
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Agencia> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody Agencia dados) {
        try {
            return ResponseEntity.ok(agenciaService.atualizar(id, dados));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /agencias/{id} — remove a agência pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            agenciaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
