package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoDetalleDao extends JpaRepository<PedidoDetalle, Integer> {

    @Query(value = "SELECT pd.* FROM pedido_detalle pd WHERE fk_pedido = :fkPedido", nativeQuery = true)
    public Optional<PedidoDetalle> findByFkPedido(int fkPedido);
}
