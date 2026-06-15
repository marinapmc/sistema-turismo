package com.marina.sistema_turismo.service;

import com.marina.sistema_turismo.model.Cliente;
import com.marina.sistema_turismo.model.Compra;
import com.marina.sistema_turismo.model.PacoteTuristico;
import com.marina.sistema_turismo.repository.CompraRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

// Serviço responsável por registrar compras de pacotes turísticos
// Além de salvar no banco, gera o link da videoconferência e dispara o envio de e-mails
@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final EmailService emailService;

    public CompraService(CompraRepository compraRepository, EmailService emailService) {
        this.compraRepository = compraRepository;
        this.emailService = emailService;
    }

    public Compra realizarCompra(Cliente cliente, PacoteTuristico pacote) {
        // Cria a compra associando o cliente e o pacote, e registra a data/hora 
        Compra compra = new Compra();
        compra.setCliente(cliente);
        compra.setPacote(pacote);
        compra.setDataCompra(LocalDateTime.now());

        // Salva primeiro para gerar o id, que será usado no link da reunião
        Compra salva = compraRepository.save(compra);

        // Gera link do Jitsi Meet com o id da compra para identificar a sala
        // O Jitsi é gratuito e não exige API key 
        String linkReuniao = "https://meet.jit.si/TurismoReuniao" + salva.getId();
        salva.setLinkReuniao(linkReuniao);
        salva = compraRepository.save(salva);

        // Envia e-mail ao cliente e à agência com os detalhes e o link da reunião
        emailService.enviarConfirmacaoCompra(salva);

        return salva;
    }

    // Usado na área do cliente 
    public List<Compra> listarPorCliente(Cliente cliente) {
        return compraRepository.findByCliente(cliente);
    }

    // Usado na API REST (busca por id sem precisar do objeto Cliente)
    public List<Compra> listarPorClienteId(Long clienteId) {
        return compraRepository.findByCliente_Id(clienteId);
    }
}
