package org.agrosoft.ControlDePedidos.API.service.staticService;

import org.agrosoft.ControlDePedidos.API.entity.TipoEnvio;
import org.agrosoft.ControlDePedidos.API.enums.TipoEnvioEnum;
import org.agrosoft.ControlDePedidos.API.exception.TipoEnvioNotFoundException;
import org.agrosoft.ControlDePedidos.API.repository.TipoEnvioDao;
import org.springframework.stereotype.Component;

@Component
public class TipoEnvioService extends BaseStaticService <TipoEnvio, TipoEnvioDao,
        TipoEnvioEnum, TipoEnvioNotFoundException> {

    public TipoEnvio getEntity(TipoEnvioEnum item) throws TipoEnvioNotFoundException {
        return super.getEntity(item, () -> new TipoEnvioNotFoundException("TipoEnvio not found for item <" +
                item + ">"));
    }
}
