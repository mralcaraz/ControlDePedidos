package org.agrosoft.ControlDePedidos.API.enums;

import lombok.Getter;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

@Getter
public enum StatusPagoEnum implements CatalogoAbstracto {
    PENDIENTE("Pendiente"),
    APARTADO("Apartado"),
    PAGADO("Pagado");

    private final String descripcion;

    StatusPagoEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
