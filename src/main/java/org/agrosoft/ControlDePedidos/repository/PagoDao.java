package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoDao extends JpaRepository<Pago, Integer> {
}
