package org.agrosoft.ControlDePedidos.API.service.staticService;

import org.agrosoft.ControlDePedidos.API.entity.StatusPago;
import org.agrosoft.ControlDePedidos.API.enums.StatusPagoEnum;
import org.agrosoft.ControlDePedidos.API.exception.StatusPagoNotFoundException;
import org.agrosoft.ControlDePedidos.API.repository.StatusPagoDao;
import org.springframework.stereotype.Component;

@Component
public class StatusPagoService extends BaseStaticService<StatusPago, StatusPagoDao,
        StatusPagoEnum, StatusPagoNotFoundException> {

    public StatusPago getEntity(StatusPagoEnum item) throws StatusPagoNotFoundException {
        return super.getEntity(item, () -> new StatusPagoNotFoundException("StatusPago not found for item <" +
                item + ">"));
    }
}
