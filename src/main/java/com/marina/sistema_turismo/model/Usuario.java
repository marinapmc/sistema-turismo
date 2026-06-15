package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Classe base abstrata para todos os usuários do sistema (agências e clientes)
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    // Chave primária gerada automaticamente pelo banco (BIGSERIAL no PostgreSQL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // E-mail único usado como login 
    @Column(unique = true, nullable = false)
    @Email
    @NotBlank
    private String email;

    // Senha armazenada como hash BCrypt
    @Column(nullable = false)
    @NotBlank
    private String senha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
