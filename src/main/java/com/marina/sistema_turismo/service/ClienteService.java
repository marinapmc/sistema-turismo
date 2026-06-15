package com.marina.sistema_turismo.service;

import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Camada de negócio para clientes
// Centraliza as regras de validação (e-mail único, CPF único) e o tratamento de senha
// Os controllers chamam este serviço em vez de acessar o repositório diretamente
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente salvar(Cliente cliente) {
        // Verifica se o e-mail já existe para outro cliente (não para o próprio, no caso de edição)
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent() &&
            (cliente.getId() == null || !clienteRepository.findByEmail(cliente.getEmail()).get().getId().equals(cliente.getId()))) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        // Mesma verificação para o CPF
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent() &&
            (cliente.getId() == null || !clienteRepository.findByCpf(cliente.getCpf()).get().getId().equals(cliente.getId()))) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        // Codifica a senha apenas no cadastro inicial (id == null significa novo cliente)
        if (cliente.getId() == null) {
            cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados) {
        // Carrega o registro existente para não sobrescrever campos que não foram enviados
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        existente.setNome(dados.getNome());
        existente.setTelefone(dados.getTelefone());
        existente.setSexo(dados.getSexo());
        existente.setDataNascimento(dados.getDataNascimento());
        // Só recodifica a senha se uma nova foi fornecida
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dados.getSenha()));
        }
        return clienteRepository.save(existente);
    }

    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
