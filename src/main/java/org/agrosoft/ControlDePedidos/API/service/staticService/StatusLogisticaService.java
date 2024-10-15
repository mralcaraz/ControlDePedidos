package org.agrosoft.ControlDePedidos.API.service.staticService;

import org.agrosoft.ControlDePedidos.API.entity.StatusLogistica;
import org.agrosoft.ControlDePedidos.API.enums.StatusLogisticaEnum;
import org.agrosoft.ControlDePedidos.API.exception.StatusLogisticaNotFoundException;
import org.agrosoft.ControlDePedidos.API.repository.StatusLogisticaDao;
import org.springframework.stereotype.Component;

@Component
public class StatusLogisticaService extends BaseStaticService<StatusLogistica, StatusLogisticaDao,
        StatusLogisticaEnum, StatusLogisticaNotFoundException> {

    public StatusLogistica getEntity(StatusLogisticaEnum item) throws StatusLogisticaNotFoundException {
        return super.getEntity(item, () -> new StatusLogisticaNotFoundException("StatusLogistica not found for item <" +
                item + ">"));
    }
}
