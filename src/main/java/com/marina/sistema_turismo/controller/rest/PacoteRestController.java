package com.marina.sistema_turismo.controller.rest;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.PacoteTuristico;
import com.marina.sistema_turismo.service.AgenciaService;
import com.marina.sistema_turismo.service.CompraService;
import com.marina.sistema_turismo.service.PacoteTuristicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// API REST para pacotes turísticos — todos os endpoints são públicos e retornam JSON
// Não tem prefixo de rota na classe pois as rotas variam entre /pacotes, /pacotes/agencias/*, etc.
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PacoteRestController {

    private final PacoteTuristicoService pacoteService;
    private final AgenciaService agenciaService;
    private final CompraService compraService;

    public PacoteRestController(PacoteTuristicoService pacoteService,
                                AgenciaService agenciaService,
                                CompraService compraService) {
        this.pacoteService = pacoteService;
        this.agenciaService = agenciaService;
        this.compraService = compraService;
    }

    // GET /pacotes — lista todos os pacotes disponíveis
    // produces = JSON aqui diferencia do PacoteController (MVC) que produz HTML na mesma rota
    @GetMapping(value = "/pacotes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PacoteTuristico> listarTodos() {
        return pacoteService.listarTodos();
    }

    // GET /pacotes/clientes/{id} — retorna os pacotes que um cliente específico comprou
    @GetMapping("/pacotes/clientes/{id}")
    public List<PacoteTuristico> listarPorCliente(@PathVariable Long id) {
        return compraService.listarPorClienteId(id).stream()
                .map(c -> c.getPacote())
                .toList();
    }

    // POST /pacotes/agencias/{id} — cria um novo pacote para a agência com o id informado
    // A agência é buscada pelo id e associada ao pacote antes de salvar
    @PostMapping(value = "/pacotes/agencias/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PacoteTuristico> criarParaAgencia(@PathVariable Long id,
                                                             @Valid @RequestBody PacoteTuristico pacote) {
        Agencia agencia = agenciaService.buscarPorId(id).orElse(null);
        if (agencia == null) return ResponseEntity.notFound().build();
        pacote.setAgencia(agencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacoteService.salvar(pacote));
    }

    // GET /pacotes/agencias/{id} — lista os pacotes de uma agência pelo id dela
    @GetMapping("/pacotes/agencias/{id}")
    public List<PacoteTuristico> listarPorAgencia(@PathVariable Long id) {
        return pacoteService.buscarPorAgenciaId(id);
    }

    // GET /pacotes/destinos/{nome} — busca pacotes por cidade, estado ou país
    // A busca é case-insensitive e usa LIKE (ex: "rio" encontra "Rio de Janeiro")
    @GetMapping("/pacotes/destinos/{nome}")
    public List<PacoteTuristico> listarPorDestino(@PathVariable String nome) {
        return pacoteService.buscarPorDestino(nome);
    }
}
