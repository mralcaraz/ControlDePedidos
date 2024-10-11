package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPedidoDao extends JpaRepository<StatusPedido, Integer> {
}
