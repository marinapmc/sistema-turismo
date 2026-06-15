package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

// Representa uma agência de turismo cadastrada no sistema
// Herda id, email e senha de Usuario
// Os dados específicos da agência ficam na tabela "agencias",
// vinculada à tabela "usuarios" pelo mesmo id (usando JOINED)
@Entity
@Table(name = "agencias")
public class Agencia extends Usuario {

    // CNPJ único e obrigatório com 14 dígitos
    @Column(unique = true, nullable = false, length = 14)
    @NotBlank
    private String cnpj;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    // Descrição longa da agência — armazenada como TEXT no banco (sem limite de tamanho)
    @Column(columnDefinition = "TEXT")
    private String descricao;

    public String getCnpj() { return cnpj; } 
    public void setCnpj(String cnpj) { this.cnpj = cnpj; } 

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
