package com.marina.sistema_turismo;

import com.marina.sistema_turismo.model.*;
import com.marina.sistema_turismo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication 
public class SistemaTurismoApplication {

    //inicando Spring
    public static void main(String[] args) {
        SpringApplication.run(SistemaTurismoApplication.class, args);
    }

    // Método para demonstrar operações CRUD
    @Bean
    CommandLineRunner demo(ClienteRepository clienteRepo,
                           AgenciaRepository agenciaRepo,
                           PacoteTuristicoRepository pacoteRepo,
                           CompraRepository compraRepo) {
        return args -> {

            // ========== CREATE ==========

            // Criando uma agência, cliente, pacote turístico e compra
            Agencia agencia = new Agencia();
            agencia.setEmail("contato@viajemais.com");
            agencia.setSenha("senha123");
            agencia.setCnpj("12345678000195");
            agencia.setNome("Viaje Mais Turismo");
            agencia.setDescricao("Especializada em pacotes internacionais");
            agenciaRepo.save(agencia); // Salva a agência no banco de dados

            Cliente cliente = new Cliente();
            cliente.setEmail("joao@email.com");
            cliente.setSenha("senha456");
            cliente.setCpf("12345678901");
            cliente.setNome("João Silva");
            cliente.setTelefone("35999990000");
            cliente.setSexo(Sexo.MASCULINO);
            cliente.setDataNascimento(LocalDate.of(1995, 6, 15));
            clienteRepo.save(cliente); // Salva o cliente no banco de dados

            PacoteTuristico pacote = new PacoteTuristico();
            pacote.setAgencia(agencia); // Define a agência responsável pelo pacote
            pacote.setCidade("Paris");
            pacote.setEstado("Île-de-France");
            pacote.setPais("França");
            pacote.setDataPartida(LocalDate.of(2026, 8, 10));
            pacote.setDuracao(10);
            pacote.setValor(new BigDecimal("5500.00"));
            pacote.setRoteiroPdf("roteiros/paris2026.pdf");

            pacote.setFotos(Arrays.asList(
                "paris1.jpg",
                "paris2.jpg",
                "paris3.jpg"
            ));
            pacoteRepo.save(pacote); // Salva o pacote turístico no banco de dados

            Compra compra = new Compra();
            compra.setCliente(cliente); // Define o cliente que realizou a compra
            compra.setPacote(pacote); // Define o pacote turístico comprado'
            compra.setDataCompra(LocalDateTime.now());
            compraRepo.save(compra); // Salva a compra no banco de dados

            // ========== READ ==========

            System.out.println("=== AGÊNCIAS ==="); // Exibe todas as agências cadastradas
            agenciaRepo.findAll().forEach(a ->
                System.out.println("- " + a.getNome() + " | CNPJ: " + a.getCnpj()));

            System.out.println("=== CLIENTES ==="); // Exibe todos os clientes cadastrados
            clienteRepo.findAll().forEach(c ->
                System.out.println("- " + c.getNome() + " | CPF: " + c.getCpf()));

            System.out.println("=== PACOTES ==="); // Exibe todos os pacotes turísticos cadastrados
            pacoteRepo.findAll().forEach(p ->
                System.out.println("- " + p.getCidade() + "/" + p.getPais()
                    + " | Partida: " + p.getDataPartida()
                    + " | R$" + p.getValor()));

            System.out.println("=== COMPRAS ==="); // Exibe todas as compras realizadas, 
                                    // mostrando o cliente, o pacote e a data da compra
            compraRepo.findAll().forEach(c ->
                System.out.println("- " + c.getCliente().getNome()
                    + " comprou: " + c.getPacote().getCidade()
                    + " em " + c.getDataCompra()));

            // ========== UPDATE ==========

            cliente.setTelefone("35988887777"); // Atualiza o telefone do cliente
            clienteRepo.save(cliente);
            System.out.println("=== UPDATE: telefone atualizado para "
                + clienteRepo.findById(cliente.getId()).get().getTelefone());

            // ========== DELETE ==========

            compraRepo.delete(compra); // Deleta a compra para evitar violação de chave estrangeira
            pacoteRepo.delete(pacote);
            System.out.println("=== DELETE: pacote e compra removidos. Pacotes restantes: "
                + pacoteRepo.count());
        };
    }
}