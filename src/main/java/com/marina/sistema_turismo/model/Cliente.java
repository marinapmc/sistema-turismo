package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity // Indica que esta classe é uma entidade JPA
@Table(name = "clientes")
public class Cliente {

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

    @Column(unique = true, nullable = false, length = 11) // CPF tem 11 dígitos
    @NotBlank
    private String cpf;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    private String telefone; // Telefone é opcional, então não tem @NotBlank

    @Enumerated(EnumType.STRING) 
    private Sexo sexo;

    @Column(name = "data_nascimento")
    @NotNull
    private LocalDate dataNascimento;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

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