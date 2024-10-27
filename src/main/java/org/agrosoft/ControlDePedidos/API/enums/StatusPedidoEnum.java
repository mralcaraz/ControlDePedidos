package org.agrosoft.ControlDePedidos.API.enums;

import lombok.Getter;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

@Getter
public enum StatusPedidoEnum implements CatalogoAbstracto {
    EN_ESPERA_DE_PAGO("En espera de pago"),
    EN_PROCESO("En proceso"),
    ENVIADO("Enviado"),
    FINALIZADO("Finalizado");

    private final String descripcion;

    StatusPedidoEnum(String descripcion) {
        this.descripcion = descripcion;
    }
}
