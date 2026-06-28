package com.marina.sistema_turismo.config;

import com.marina.sistema_turismo.model.Admin;
import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.model.PacoteTuristico;
import com.marina.sistema_turismo.model.Sexo;
import com.marina.sistema_turismo.repository.AdminRepository;
import com.marina.sistema_turismo.repository.AgenciaRepository;
import com.marina.sistema_turismo.repository.ClienteRepository;
import com.marina.sistema_turismo.repository.PacoteTuristicoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Popula o banco com dados de exemplo na primeira execução
// Executado automaticamente pelo Spring ao iniciar, antes de atender requisições
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializar(
            AdminRepository adminRepo,
            AgenciaRepository agenciaRepo,
            ClienteRepository clienteRepo,
            PacoteTuristicoRepository pacoteRepo,
            PasswordEncoder encoder,
            @Value("${app.admin.email}") String adminEmail,
            @Value("${app.admin.senha}") String adminSenha) {

        return args -> {
            // Cria o admin no banco caso ainda não exista 
            if (adminRepo.findByEmail(adminEmail).isEmpty()) {
                Admin admin = new Admin();
                admin.setEmail(adminEmail);
                admin.setSenha(encoder.encode(adminSenha));
                adminRepo.save(admin);
            }

            // Se já existir pelo menos uma agência, o banco já foi populado e pula a inicialização
            if (agenciaRepo.count() > 0) return;

            // Cria a agência de teste com senha em BCrypt
            Agencia agencia = new Agencia();
            agencia.setEmail("agencia@teste.com");
            agencia.setSenha(encoder.encode("agencia123"));
            agencia.setCnpj("12345678000195");
            agencia.setNome("Viagens Brasil");
            agencia.setDescricao("Especialistas em turismo nacional e internacional.");
            agenciaRepo.save(agencia);

            // Cria o cliente de teste com senha em BCrypt
            Cliente cliente = new Cliente();
            cliente.setEmail("cliente@teste.com");
            cliente.setSenha(encoder.encode("cliente123"));
            cliente.setCpf("12345678901");
            cliente.setNome("João Silva");
            cliente.setTelefone("11999999999");
            cliente.setSexo(Sexo.MASCULINO);
            cliente.setDataNascimento(LocalDate.of(1990, 5, 15));
            clienteRepo.save(cliente);

            // Cria dois pacotes de exemplo vinculados à agência de teste
            PacoteTuristico pacote1 = new PacoteTuristico();
            pacote1.setAgencia(agencia);
            pacote1.setCidade("Rio de Janeiro");
            pacote1.setEstado("RJ");
            pacote1.setPais("Brasil");
            pacote1.setDataPartida(LocalDate.now().plusMonths(2)); // partida daqui a 2 meses
            pacote1.setDuracao(7);
            pacote1.setValor(new BigDecimal("2500.00"));
            pacote1.setVagasDisponiveis(20);
            pacote1.setFotos(List.of());
            pacoteRepo.save(pacote1);

            PacoteTuristico pacote2 = new PacoteTuristico();
            pacote2.setAgencia(agencia);
            pacote2.setCidade("Florianópolis");
            pacote2.setEstado("SC");
            pacote2.setPais("Brasil");
            pacote2.setDataPartida(LocalDate.now().plusMonths(3));
            pacote2.setDuracao(5);
            pacote2.setValor(new BigDecimal("1800.00"));
            pacote2.setVagasDisponiveis(15);
            pacote2.setFotos(List.of());
            pacoteRepo.save(pacote2);
        };
    }
}
