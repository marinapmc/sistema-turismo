package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// Representa um cliente do sistema
// Herda id, email e senha de Usuario
// No banco, os dados específicos do cliente ficam na tabela "clientes",
// ligada à tabela "usuarios" pelo mesmo id
@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

    // CPF único e obrigatório com exatamente 11 dígitos
    @Column(unique = true, nullable = false, length = 11)
    @NotBlank
    private String cpf;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    private String telefone;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(name = "data_nascimento")
    @NotNull
    private LocalDate dataNascimento;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
}
