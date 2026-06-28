package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Agencia;
import com.marina.sistema_turismo.model.PacoteTuristico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

// Repositório de pacotes turísticos com métodos de busca e filtro
public interface PacoteTuristicoRepository extends JpaRepository<PacoteTuristico, Long> {

    // Todos os pacotes de uma agência específica
    List<PacoteTuristico> findByAgencia(Agencia agencia);

    // Apenas os pacotes "vigentes" (data de partida futura) de uma agência
    // Usado no filtro "apenas vigentes" da área da agência
    List<PacoteTuristico> findByAgenciaAndDataPartidaAfter(Agencia agencia, LocalDate data);

    // Pacotes com data de partida a partir de uma data 
    List<PacoteTuristico> findByDataPartidaAfter(LocalDate data);

    List<PacoteTuristico> findByPais(String pais);
    List<PacoteTuristico> findByCidade(String cidade);

    // Busca pacotes pelo id da agência 
    List<PacoteTuristico> findByAgencia_Id(Long agenciaId);

    // Busca por destino em cidade, estado ou país 
    @Query("SELECT p FROM PacoteTuristico p WHERE " +
           "LOWER(p.cidade) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.estado) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.pais) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<PacoteTuristico> findByDestino(@Param("termo") String termo);

    // Busca pacotes pelo nome da agência — usado no filtro público da lista de pacotes
    @Query("SELECT p FROM PacoteTuristico p JOIN p.agencia a WHERE " +
           "LOWER(a.nome) LIKE LOWER(CONCAT('%', :nomeAgencia, '%'))")
    List<PacoteTuristico> findByNomeAgencia(@Param("nomeAgencia") String nomeAgencia);

    // Decrementa uma vaga de forma atômica, só se ainda houver vaga disponível
    // O UPDATE com a condição "vagasDisponiveis > 0" na própria query evita que duas
    // compras simultâneas decrementem além de zero (condição de corrida)
    // Retorna quantas linhas foram afetadas: 0 significa que não havia mais vagas
    @Modifying
    @Query("UPDATE PacoteTuristico p SET p.vagasDisponiveis = p.vagasDisponiveis - 1 " +
           "WHERE p.id = :id AND p.vagasDisponiveis > 0")
    int decrementarVaga(@Param("id") Long id);
}
