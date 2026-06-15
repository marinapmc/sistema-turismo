package com.marina.sistema_turismo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ponto de entrada da aplicação Spring Boot
// @SpringBootApplication ativa a varredura automática de componentes,
// a configuração automática e o suporte a properties
@SpringBootApplication
public class SistemaTurismoApplication {

    public static void main(String[] args) {
        // Inicializa o contexto do Spring, sobe o servidor Tomcat embutido e carrega tudo
        SpringApplication.run(SistemaTurismoApplication.class, args);
    }
}
