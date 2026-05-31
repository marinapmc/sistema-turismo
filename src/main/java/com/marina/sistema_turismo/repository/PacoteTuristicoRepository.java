package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.PacoteTuristico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PacoteTuristicoRepository extends JpaRepository<PacoteTuristico, Long> {
    List<PacoteTuristico> findByAgencia(Agencia agencia);
    List<PacoteTuristico> findByPais(String pais);
    List<PacoteTuristico> findByCidade(String cidade);
    List<PacoteTuristico> findByDataPartidaAfter(LocalDate data);
}