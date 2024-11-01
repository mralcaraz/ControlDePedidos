package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoDao extends JpaRepository<Pedido, Integer> {
    @Query(value = "SELECT p.* FROM pedido p WHERE 1 = 1 " +
            "AND (:idStatusPedido IS NULL || p.fk_status_pedido = :idStatusPedido) " +
            "AND (:fechaIni IS NULL || p.fecha_pedido > :fechaIni) " +
            "AND (:fechaFin IS NULL || p.fecha_pedido < :fechaFin) " +
            "AND (:idCliente IS NULL || p.fk_cliente = :idCliente) " +
            "AND (:idStatusPago IS NULL || p.fk_status_pago = :idStatusPago) " +
            "AND (:idStatusLogistica IS NULL || p.fk_status_logistica = :idStatusLogistica) " +
            "AND (:idTipoEnvio IS NULL || p.fk_tipo_envio = :idTipoEnvio) " +
            "AND (:isActive IS NULL || p.active = :isActive)", nativeQuery = true)
    public List<Pedido> findByParameters(Integer idStatusPedido, LocalDate fechaIni, LocalDate fechaFin,
                                         Integer idCliente, Integer idStatusPago, Integer idStatusLogistica,
                                         Integer idTipoEnvio, Boolean isActive);
}
