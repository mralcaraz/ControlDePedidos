package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoDao extends JpaRepository<Pago, Integer> {
    public List<Pago> findByFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    public List<Pago> findByNumeroReferenciaContains(String referencia);
    @Query(value = "SELECT p.* FROM pago p WHERE p.fk_pedido = :idPedido", nativeQuery = true)
    public List<Pago> findByPedido(int idPedido);
}
