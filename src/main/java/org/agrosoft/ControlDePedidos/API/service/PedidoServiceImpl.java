package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.repository.PedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoDao repository;


    @Override
    public List<Pedido> encontrarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Pedido> encontrarPorId(int idPedido) {
        return repository.findById(idPedido);
    }

    @Override
    public List<Pedido> encontrarPorParametros(Integer idStatusPedido, LocalDate fechaIni, LocalDate fechaFin,
                                               Integer idCliente, Integer idStatusPago, Integer idStatusLogistica,
                                               Integer idTipoEnvio, Boolean isActive) {
        return repository.findByParameters(idStatusPedido, fechaIni, fechaFin, idCliente, idStatusPago,
                idStatusLogistica, idTipoEnvio, isActive);
    }

    @Override
    public int guardarPedido(Pedido pedido) {
        return repository.saveAndFlush(pedido).getIdPedido();
    }
}
