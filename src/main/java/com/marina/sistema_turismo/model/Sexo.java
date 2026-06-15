package com.marina.sistema_turismo.model;

// Enum para representar o sexo do cliente
// Usado com @Enumerated(EnumType.STRING) para salvar o nome no banco (ex: "FEMININO")
// em vez de um número inteiro, facilitando a leitura direta no banco de dados
public enum Sexo {
    MASCULINO,
    FEMININO,
    OUTRO
}
