package com.marina.sistema_turismo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

// Representa o usuário administrador do sistema
// Herda id, email e senha de Usuario; não tem campos próprios além desses
// Os dados ficam na tabela "admins", ligada à tabela "usuarios" pelo mesmo id
@Entity
@Table(name = "admins")
public class Admin extends Usuario {
}
