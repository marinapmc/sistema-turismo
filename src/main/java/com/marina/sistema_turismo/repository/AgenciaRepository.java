package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
    Optional<Agencia> findByEmail(String email); // Método para buscar agência por email
    Optional<Agencia> findByCnpj(String cnpj); // Método para buscar agência por CNPJ
}