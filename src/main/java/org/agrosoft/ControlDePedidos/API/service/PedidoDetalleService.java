package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;

import java.util.List;
import java.util.Optional;

public interface PedidoDetalleService {
    public List<PedidoDetalle> encuentraTodos();
    public Optional<PedidoDetalle> encuentraPorPedido(int fkPedido);
    public Optional<PedidoDetalle> encontrarPorId(int idPedidoDetalle);
    public int guardaPedidoDetalle(PedidoDetalle pedidoDetalle);
    public void eliminaPedidoDetalle(int idPedidoDetalle);
}
