package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática de ID
    private Long id;

    @ManyToOne // Relacionamento muitos-para-um com Cliente
    @JoinColumn(name = "cliente_id", nullable = false) // Chave estrangeira para Cliente
    private Cliente cliente;

    @ManyToOne // Relacionamento muitos-para-um com PacoteTuristico
    @JoinColumn(name = "pacote_id", nullable = false) // Chave estrangeira para PacoteTuristico
    private PacoteTuristico pacote;

    @Column(name = "data_compra") 
    private LocalDateTime dataCompra;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public PacoteTuristico getPacote() { return pacote; }
    public void setPacote(PacoteTuristico pacote) { this.pacote = pacote; }

    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }
}