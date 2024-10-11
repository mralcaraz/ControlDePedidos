package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.TipoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoEnvioDao extends JpaRepository<TipoEnvio, Integer> {
}
