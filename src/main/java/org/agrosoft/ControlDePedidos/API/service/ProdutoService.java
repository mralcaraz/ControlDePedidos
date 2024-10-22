package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    public Optional<Producto> consultarPorId(int idProducto);
    public Optional<Producto> consultarPorNombre(String nombre);
    public List<Producto> consultarTodos();
    public List<Producto> consultarPorNombreLike(String nombre);
    public List<Producto> consultarPorPedido(int idPedido);
    public int guardaProducto(Producto producto);
}
