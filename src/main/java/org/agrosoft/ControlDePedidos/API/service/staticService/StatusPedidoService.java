package org.agrosoft.ControlDePedidos.API.service.staticService;

import org.agrosoft.ControlDePedidos.API.entity.StatusPedido;
import org.agrosoft.ControlDePedidos.API.enums.StatusPedidoEnum;
import org.agrosoft.ControlDePedidos.API.exception.StatusPedidoNotFoundException;
import org.agrosoft.ControlDePedidos.API.repository.StatusPedidoDao;
import org.springframework.stereotype.Component;

@Component
public class StatusPedidoService extends BaseStaticService<StatusPedido, StatusPedidoDao,
        StatusPedidoEnum, StatusPedidoNotFoundException> {

    public StatusPedido getEntity(StatusPedidoEnum item) throws StatusPedidoNotFoundException {
        return super.getEntity(item, () -> new StatusPedidoNotFoundException("StatusPedido not found for item <" +
                item + ">"));
    }
}
