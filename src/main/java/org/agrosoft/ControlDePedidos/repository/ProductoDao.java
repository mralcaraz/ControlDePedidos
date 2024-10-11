package org.agrosoft.ControlDePedidos.repository;

import org.agrosoft.ControlDePedidos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoDao extends JpaRepository<Producto, Integer> {
}
