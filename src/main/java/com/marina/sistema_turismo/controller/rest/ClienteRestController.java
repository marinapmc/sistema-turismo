package com.marina.sistema_turismo.controller.rest;

import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// API REST para clientes — todos os endpoints retornam JSON e não exigem autenticação (AA-2)
// @RestController combina @Controller + @ResponseBody: os métodos retornam dados, não views
// produces = APPLICATION_JSON_VALUE garante que qualquer requisição aqui receba JSON
@RestController
@RequestMapping(value = "/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteRestController {

    private final ClienteService clienteService;

    public ClienteRestController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // GET /clientes — retorna a lista de todos os clientes
    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listarTodos();
    }

    // GET /clientes/{id} — retorna um cliente pelo id, ou 404 se não existir
    // ResponseEntity permite controlar o código HTTP da resposta
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)              // 200 OK com o objeto
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // POST /clientes — cria um novo cliente com os dados do corpo JSON
    // @RequestBody converte o JSON recebido em objeto Java automaticamente
    // @Valid executa as validações da entidade (@NotBlank, @Email, etc.)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente) {
        Cliente salvo = clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo); // 201 Created
    }

    // PUT /clientes/{id} — atualiza os dados de um cliente existente
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody Cliente dados) {
        try {
            return ResponseEntity.ok(clienteService.atualizar(id, dados));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /clientes/{id} — remove o cliente pelo id
    // 204 No Content é a resposta padrão para DELETE bem-sucedido (sem corpo de resposta)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            clienteService.excluir(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
