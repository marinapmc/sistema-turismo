package com.marina.sistema_turismo.repository;

import com.marina.sistema_turismo.model.Compra;
import com.marina.sistema_turismo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositório de compras
// Permite buscar o histórico de compras de um cliente de duas formas:
// passando o objeto Cliente diretamente, ou apenas o id (usado pela API REST)
public interface CompraRepository extends JpaRepository<Compra, Long> {
    // Usado nos controllers MVC onde o objeto Cliente já está disponível na sessão
    List<Compra> findByCliente(Cliente cliente);

    // Usado na API REST, onde só temos o id vindo da URL
    // O Spring Data entende "Cliente_Id" como acesso ao campo id dentro do objeto cliente
    List<Compra> findByCliente_Id(Long clienteId);
}
