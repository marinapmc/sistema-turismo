package com.marina.sistema_turismo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Representa um pacote turístico cadastrado por uma agência
// Contém destino, datas, valor, fotos e roteiro em PDF
@Entity
@Table(name = "pacotes_turisticos")
public class PacoteTuristico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Agência responsável pelo pacote — chave estrangeira para a tabela agencias
    @ManyToOne
    @JoinColumn(name = "agencia_id", nullable = false)
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

    // Valor com até 10 dígitos no total e 2 casas decimais
    @NotNull
    @Positive
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    // Controle de estoque: quantas vagas ainda podem ser compradas
    // Sem @Column(nullable = false) de propósito — evita falha de migração em pacotes já
    // existentes no banco; a obrigatoriedade é garantida pela validação abaixo
    @Column(name = "vagas_disponiveis")
    @NotNull
    @Positive
    private Integer vagasDisponiveis;

    // Caminho do arquivo PDF do roteiro, salvo em disco 
    @Column(name = "roteiro_pdf")
    private String roteiroPdf;

    // Lista de caminhos das fotos do pacote, salvas em disco
    // @ElementCollection cria uma tabela separada "pacote_fotos" para guardar esses caminhos
    @ElementCollection
    @CollectionTable(
        name = "pacote_fotos",
        joinColumns = @JoinColumn(name = "pacote_id")
    )
    @Column(name = "foto")
    private List<String> fotos;

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

    public Integer getVagasDisponiveis() { return vagasDisponiveis; }
    public void setVagasDisponiveis(Integer vagasDisponiveis) { this.vagasDisponiveis = vagasDisponiveis; }

    public String getRoteiroPdf() { return roteiroPdf; }
    public void setRoteiroPdf(String roteiroPdf) { this.roteiroPdf = roteiroPdf; }

    public List<String> getFotos() { return fotos; }

    public void setFotos(List<String> fotos) {
        // Limita o número de fotos por pacote a 10
        if (fotos != null && fotos.size() > 10) {
            throw new IllegalArgumentException("O pacote pode possuir no máximo 10 fotos.");
        }
        this.fotos = fotos;
    }
}
