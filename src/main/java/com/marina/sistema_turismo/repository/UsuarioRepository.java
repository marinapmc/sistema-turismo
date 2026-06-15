package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Repositório da classe base Usuario
// Usado principalmente pelo sistema de autenticação para buscar qualquer usuário pelo e-mail,
// independentemente de ser agência ou cliente
// O Hibernate retorna a subclasse real (Agencia ou Cliente), então instanceof funciona normalmente
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
