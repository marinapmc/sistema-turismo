package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pacotes_turisticos")
public class PacoteTuristico {

    @Id // Indica que este campo é a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática de ID
    private Long id;

    @ManyToOne // Relacionamento muitos-para-um com Agencia
    @JoinColumn(name = "agencia_id", nullable = false) // Chave estrangeira para Agencia
    private Agencia agencia;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    @NotBlank
    private String pais;

    @Column(name = "data_partida")
    @NotNull
    private LocalDate dataPartida;

    @NotNull
    @Positive
    private Integer duracao;

    @NotNull
    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal valor; // Valor do pacote turístico

    @Column(name = "roteiro_pdf")
    private String roteiroPdf; // URL ou caminho para o arquivo PDF do roteiro

    // Lista de URLs das fotos do pacote
    @ElementCollection
    @CollectionTable(
        name = "pacote_fotos", 
        joinColumns = @JoinColumn(name = "pacote_id")
    )
    @Column(name = "foto")
    private List<String> fotos;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Agencia getAgencia() { return agencia; }
    public void setAgencia(Agencia agencia) { this.agencia = agencia; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public LocalDate getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDate dataPartida) { this.dataPartida = dataPartida; }

    public Integer getDuracao() { return duracao; }
    public void setDuracao(Integer duracao) { this.duracao = duracao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getRoteiroPdf() { return roteiroPdf; }
    public void setRoteiroPdf(String roteiroPdf) { this.roteiroPdf = roteiroPdf; }

    public List<String> getFotos() { return fotos; } // Getter para a lista de fotos

    public void setFotos(List<String> fotos) {

    if (fotos != null && fotos.size() > 10) {
        throw new IllegalArgumentException(
            "O pacote pode possuir no máximo 10 fotos."
        );
    }

    this.fotos = fotos;
    }
}