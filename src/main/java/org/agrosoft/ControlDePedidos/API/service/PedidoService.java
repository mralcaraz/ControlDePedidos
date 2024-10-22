package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PedidoService {

    public List<Pedido> encontrarTodos();
    public Optional<Pedido> encontrarPorId(int idPedido);
    public List<Pedido> encontrarPorParametros(Integer idStatusPedido, LocalDate fechaIni, LocalDate fechaFin,
                                               Integer idCliente, Integer idStatusPago, Integer idStatusLogistica,
                                               Integer idTipoEnvio, Boolean isActive);
    public int guardarPedido(Pedido pedido);

}
