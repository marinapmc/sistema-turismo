package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "agencias")
public class Agencia {

    @Id // Indica que este campo é a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática de ID
    private Long id;

    @Column(unique = true, nullable = false) // Define a coluna como única e não nula
    @Email
    @NotBlank
    private String email;

    @Column(nullable = false)
    @NotBlank
    private String senha;

    @Column(unique = true, nullable = false, length = 14) // CNPJ tem 14 dígitos
    @NotBlank
    private String cnpj;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    @Column(columnDefinition = "TEXT") // Permite uma descrição mais longa
    private String descricao;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}