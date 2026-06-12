package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.PacoteTuristico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PacoteTuristicoRepository extends JpaRepository<PacoteTuristico, Long> {
    List<PacoteTuristico> findByAgencia(Agencia agencia); // Método para buscar pacotes por agência
    List<PacoteTuristico> findByPais(String pais); // Método para buscar pacotes por país
    List<PacoteTuristico> findByCidade(String cidade); // Método para buscar pacotes por cidade
    List<PacoteTuristico> findByDataPartidaAfter(LocalDate data); // Método para buscar pacotes com data de partida após uma data específica
}