package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Representa a compra de um pacote turístico por um cliente
// Registra quem comprou, qual pacote, quando e o link da videoconferência gerado
@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cliente que realizou a compra — chave estrangeira para a tabela clientes
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Pacote que foi comprado — chave estrangeira para a tabela pacotes_turisticos
    @ManyToOne
    @JoinColumn(name = "pacote_id", nullable = false)
    private PacoteTuristico pacote;

    // Data e hora exata em que a compra foi registrada
    @Column(name = "data_compra")
    private LocalDateTime dataCompra;

    // Link da videoconferência (Jitsi Meet) gerado automaticamente na hora da compra
    @Column(name = "link_reuniao")
    private String linkReuniao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public PacoteTuristico getPacote() { return pacote; }
    public void setPacote(PacoteTuristico pacote) { this.pacote = pacote; }

    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }

    public String getLinkReuniao() { return linkReuniao; }
    public void setLinkReuniao(String linkReuniao) { this.linkReuniao = linkReuniao; }
}
