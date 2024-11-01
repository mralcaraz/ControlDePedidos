package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.enums.StatusLogisticaEnum;
import org.agrosoft.ControlDePedidos.API.enums.StatusPagoEnum;
import org.agrosoft.ControlDePedidos.API.enums.StatusPedidoEnum;
import org.agrosoft.ControlDePedidos.API.exception.StatusLogisticaNotFoundException;
import org.agrosoft.ControlDePedidos.API.exception.StatusPagoNotFoundException;
import org.agrosoft.ControlDePedidos.API.exception.StatusPedidoNotFoundException;
import org.agrosoft.ControlDePedidos.API.repository.PedidoDao;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusLogisticaService;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusPagoService;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoDao repository;

    @Autowired
    private StatusPagoService statusPagoService;

    @Autowired
    private StatusPedidoService statusPedidoService;

    @Autowired
    private StatusLogisticaService statusLogisticaService;


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
    @Transactional
    public int guardarPedido(Pedido pedido) {
        return repository.saveAndFlush(pedido).getIdPedido();
    }

    @Override
    public List<Pedido> encontrarFinalizables() {
        int idStatusPedido = 0;
        int idStatusPago = 0;
        int idStatusLogistica = 0;
        try {
            idStatusPedido = statusPedidoService.getEntity(StatusPedidoEnum.FINALIZADO).getIdStatusPedido();
            idStatusPago = statusPagoService.getEntity(StatusPagoEnum.PAGADO).getIdStatusPago();
            idStatusLogistica = statusLogisticaService.getEntity(StatusLogisticaEnum.ENTREGADO).getIdLogistica();
        } catch (Exception ignore) { }
        return this.encontrarPorParametros(idStatusPedido, null, null, null, idStatusPago,
                idStatusLogistica, null, true);
    }
}
