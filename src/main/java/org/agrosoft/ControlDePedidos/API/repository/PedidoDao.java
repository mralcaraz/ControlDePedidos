package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDao extends JpaRepository<Pedido, Integer> {
}
