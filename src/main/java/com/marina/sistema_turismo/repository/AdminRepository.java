package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Repositório do administrador — mesma estrutura do ClienteRepository/AgenciaRepository
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
}
