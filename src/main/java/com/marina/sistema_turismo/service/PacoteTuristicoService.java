package com.marina.sistema_turismo.service;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.PacoteTuristico;
import com.marina.sistema_turismo.repository.PacoteTuristicoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Camada de negócio para pacotes turísticos
// Além do CRUD básico, oferece métodos de filtro usados na listagem pública e na área da agência
@Service
public class PacoteTuristicoService {

    private final PacoteTuristicoRepository pacoteRepository;

    public PacoteTuristicoService(PacoteTuristicoRepository pacoteRepository) {
        this.pacoteRepository = pacoteRepository;
    }

    public List<PacoteTuristico> listarTodos() {
        return pacoteRepository.findAll();
    }

    public Optional<PacoteTuristico> buscarPorId(Long id) {
        return pacoteRepository.findById(id);
    }

    public List<PacoteTuristico> buscarPorAgencia(Agencia agencia) {
        return pacoteRepository.findByAgencia(agencia);
    }

    // Retorna só os pacotes com data de partida futura (pacotes "vigentes")
    public List<PacoteTuristico> buscarVigentesPorAgencia(Agencia agencia) {
        return pacoteRepository.findByAgenciaAndDataPartidaAfter(agencia, LocalDate.now());
    }

    // Filtro combinado para a listagem pública
    public List<PacoteTuristico> filtrar(String destino, String nomeAgencia, LocalDate dataPartida) {
        if (destino != null && !destino.isBlank()) {
            return pacoteRepository.findByDestino(destino.trim());
        }
        if (nomeAgencia != null && !nomeAgencia.isBlank()) {
            return pacoteRepository.findByNomeAgencia(nomeAgencia.trim());
        }
        if (dataPartida != null) {
            // minusDays(1) para incluir pacotes com partida exatamente na data informada
            return pacoteRepository.findByDataPartidaAfter(dataPartida.minusDays(1));
        }
        return pacoteRepository.findAll();
    }

    // Usado pela API REST: busca por destino (cidade, estado ou país)
    public List<PacoteTuristico> buscarPorDestino(String termo) {
        return pacoteRepository.findByDestino(termo);
    }

    // Usado pela API REST: pacotes de uma agência pelo id
    public List<PacoteTuristico> buscarPorAgenciaId(Long agenciaId) {
        return pacoteRepository.findByAgencia_Id(agenciaId);
    }

    public PacoteTuristico salvar(PacoteTuristico pacote) {
        return pacoteRepository.save(pacote);
    }

    public void excluir(Long id) {
        pacoteRepository.deleteById(id);
    }
}
