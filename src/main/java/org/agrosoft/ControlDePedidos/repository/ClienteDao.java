package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDao extends JpaRepository<Cliente, Integer> {
}
