package com.marina.sistema_turismo.service;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.repository.AgenciaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Camada de negócio para agências 
// Valida e-mail e CNPJ únicos antes de salvar, e codifica a senha em BCrypt
@Service
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;
    private final PasswordEncoder passwordEncoder;

    public AgenciaService(AgenciaRepository agenciaRepository, PasswordEncoder passwordEncoder) {
        this.agenciaRepository = agenciaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Agencia> listarTodas() {
        return agenciaRepository.findAll();
    }

    public Optional<Agencia> buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public Optional<Agencia> buscarPorEmail(String email) {
        return agenciaRepository.findByEmail(email);
    }

    public Agencia salvar(Agencia agencia) {
        // Impede cadastro duplicado de e-mail por outra agência
        if (agenciaRepository.findByEmail(agencia.getEmail()).isPresent() &&
            (agencia.getId() == null || !agenciaRepository.findByEmail(agencia.getEmail()).get().getId().equals(agencia.getId()))) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        // Impede cadastro duplicado de CNPJ
        if (agenciaRepository.findByCnpj(agencia.getCnpj()).isPresent() &&
            (agencia.getId() == null || !agenciaRepository.findByCnpj(agencia.getCnpj()).get().getId().equals(agencia.getId()))) {
            throw new IllegalArgumentException("CNPJ já cadastrado.");
        }
        // Codifica a senha apenas no primeiro cadastro
        if (agencia.getId() == null) {
            agencia.setSenha(passwordEncoder.encode(agencia.getSenha()));
        }
        return agenciaRepository.save(agencia);
    }

    public Agencia atualizar(Long id, Agencia dados) {
        Agencia existente = agenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agência não encontrada."));
        existente.setNome(dados.getNome());
        existente.setDescricao(dados.getDescricao());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dados.getSenha()));
        }
        return agenciaRepository.save(existente);
    }

    public void excluir(Long id) {
        if (!agenciaRepository.existsById(id)) {
            throw new IllegalArgumentException("Agência não encontrada.");
        }
        agenciaRepository.deleteById(id);
    }
}
