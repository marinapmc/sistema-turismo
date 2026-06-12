package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Compra;
import com.marina.sistema_turismo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByCliente(Cliente cliente); // Método para buscar compras por cliente
}