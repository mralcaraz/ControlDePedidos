package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;
import org.agrosoft.ControlDePedidos.API.repository.PedidoDetalleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoDetalleServiceImpl implements PedidoDetalleService{

    @Autowired
    private PedidoDetalleDao repository;

    @Override
    public List<PedidoDetalle> encuentraTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<PedidoDetalle> encuentraPorPedido(int fkPedido) {
        return repository.findByFkPedido(fkPedido);
    }

    @Override
    public Optional<PedidoDetalle> encontrarPorId(int idPedidoDetalle) {
        return repository.findById(idPedidoDetalle);
    }

    @Override
    public int guardaPedidoDetalle(PedidoDetalle pedidoDetalle) {
        return repository.saveAndFlush(pedidoDetalle).getIdPedidoDetalle();
    }

    @Override
    public void eliminaPedidoDetalle(int idPedidoDetalle) {
        repository.deleteById(idPedidoDetalle);
    }
}
