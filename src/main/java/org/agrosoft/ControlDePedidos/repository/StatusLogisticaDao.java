package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.StatusLogistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusLogisticaDao extends JpaRepository<StatusLogistica, Integer> {
}
