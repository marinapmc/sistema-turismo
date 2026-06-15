package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Repositório de clientes — o Spring Data JPA gera a implementação automaticamente
// Só é necessário declarar os métodos de busca específicos; o CRUD básico já vem do JpaRepository
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // O Spring traduz o nome do método para SQL: SELECT * FROM ... WHERE email = ?
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
}
