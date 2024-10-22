package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.API.repository.ProductoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProdutoService {

    @Autowired
    private ProductoDao repository;

    @Override
    public Optional<Producto> consultarPorId(int idProducto) {
        return repository.findById(idProducto);
    }

    @Override
    public Optional<Producto> consultarPorNombre(String nombre) {
        return repository.findByNombreProducto(nombre);
    }

    @Override
    public List<Producto> consultarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Producto> consultarPorNombreLike(String nombre) {
        return repository.findByNombreProductoContains(nombre);
    }

    @Override
    public List<Producto> consultarPorPedido(int idPedido) {
        return repository.findByPedido(idPedido);
    }

    @Override
    public int guardaProducto(Producto producto) {
        return repository.saveAndFlush(producto).getIdProducto();
    }
}
