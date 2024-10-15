package org.agrosoft.ControlDePedidos.API.enums;

import lombok.Getter;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

@Getter
public enum StatusLogisticaEnum implements CatalogoAbstracto {
    PENDIENTE("Pendiente"),
    EN_RUTA("En ruta"),
    ENVIADO("Enviado");

    private final String descripcion;

    StatusLogisticaEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
