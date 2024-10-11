package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDao extends JpaRepository<Pedido, Integer> {
}
