package org.agrosoft.ControlDePedidos.GUI.enums;

import lombok.Getter;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

@Getter
public enum PedidosViewEnum implements CatalogoAbstracto  {
    TODOS("Todos"),
    SOLO_ACTIVOS("Sólo activos"),
    SOLO_INACTIVOS("Sólo inactivos"),
    ACTIVOS_Y_PENDIENTES("Activos, pendientes"),
    ACTIVOS_Y_TERMINADOS("Activos, terminados");

    private final String descripcion;

    PedidosViewEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
