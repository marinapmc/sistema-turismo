package com.marina.sistema_turismo.service;

import com.marina.sistema_turismo.model.Compra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Serviço responsável pelo envio de e-mails de confirmação de compra
// Usa o JavaMailSender do Spring, configurado com as propriedades spring.mail.* no application.properties
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Envia dois e-mails: um para o cliente e outro para a agência
    // Ambos contêm o link da videoconferência para acertar os detalhes da compra
    public void enviarConfirmacaoCompra(Compra compra) {
        String destino = compra.getPacote().getCidade() + ", " +
                         compra.getPacote().getEstado() + ", " +
                         compra.getPacote().getPais();

        // E-mail para o cliente com os dados completos da viagem
        String corpoCliente = String.format(
            "Olá, %s!\n\nSua compra do pacote turístico para %s foi confirmada.\n" +
            "Agência: %s\nData de partida: %s\nDuração: %d dias\nValor: R$ %.2f\n\n" +
            "Uma reunião foi agendada com a agência para acertar os detalhes.\n" +
            "Link da videoconferência: %s\n\nObrigado por escolher nosso sistema!",
            compra.getCliente().getNome(), destino,
            compra.getPacote().getAgencia().getNome(),
            compra.getPacote().getDataPartida(),
            compra.getPacote().getDuracao(),
            compra.getPacote().getValor(),
            compra.getLinkReuniao()
        );

        // E-mail para a agência avisando sobre a nova venda e identificando o cliente
        String corpoAgencia = String.format(
            "Nova compra realizada!\n\nCliente: %s (%s)\nPacote: %s\nData de partida: %s\n" +
            "Link da videoconferência para reunião com o cliente: %s",
            compra.getCliente().getNome(), compra.getCliente().getEmail(),
            destino, compra.getPacote().getDataPartida(),
            compra.getLinkReuniao()
        );

        enviar(compra.getCliente().getEmail(), "Confirmação de Compra — Pacote Turístico", corpoCliente);
        enviar(compra.getPacote().getAgencia().getEmail(), "Nova Compra Recebida", corpoAgencia);
    }

    private void enviar(String para, String assunto, String corpo) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(para);
            mensagem.setSubject(assunto);
            mensagem.setText(corpo);
            mailSender.send(mensagem);
        } catch (MailException e) {
            // Registra o erro no log mas não lança exceção para não cancelar a compra
            log.error("Falha ao enviar e-mail para {}: {}", para, e.getMessage());
        }
    }
}
