package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email); // Método para buscar cliente por email
    Optional<Cliente> findByCpf(String cpf); // Método para buscar cliente por CPF
}