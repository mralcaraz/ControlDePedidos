package org.agrosoft.ControlDePedidos.API.enums;

import lombok.Getter;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

@Getter
public enum TipoEnvioEnum implements CatalogoAbstracto {
    METROPOLITANO("Zona Metropolitana"),
    FORANEO("Foráneo"),
    BOSQUES("Bosques"),
    CASA("Recolección en casa");

    private final String descripcion;

    TipoEnvioEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
