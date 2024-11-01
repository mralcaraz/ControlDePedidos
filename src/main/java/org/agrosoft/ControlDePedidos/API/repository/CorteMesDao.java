package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.CorteMes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorteMesDao extends JpaRepository<CorteMes, Integer> {
    public Optional<CorteMes> findByMesEnvioAndAnioEnvio(int mes, int anio);
}
