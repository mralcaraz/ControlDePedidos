package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoDao extends JpaRepository<Producto, Integer> {
    public List<Producto> findByNombreProductoContains(String nombre);
    @Query(value = "SELECT p.* FROM producto p " +
            "INNER JOIN pedidos_productos pp ON pp.fk_producto = p.id_producto " +
            "WHERE pp.fk_pedido = :idPedido", nativeQuery = true)
    public List<Producto> findByPedido(int idPedido);
    public Optional<Producto> findByNombreProducto(String nombre);
}
