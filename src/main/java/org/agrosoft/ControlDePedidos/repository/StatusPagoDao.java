package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.StatusPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPagoDao extends JpaRepository<StatusPago, Integer> {
}