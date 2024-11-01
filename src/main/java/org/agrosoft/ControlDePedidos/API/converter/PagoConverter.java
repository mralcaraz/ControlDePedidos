package org.agrosoft.ControlDePedidos.API.converter;

import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PagoConverter implements GenericPdfConveter<Pago> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String[] convertEntity(Pago entity) {
        return new String[]{ FormUtils.formateaDinero(entity.getMontoPago()), formatter.format(entity.getFechaPago()),
                Objects.isNull(entity.getNumeroReferencia()) ? "" : entity.getNumeroReferencia() };
    }

    public String[] getEncabezados() {
        return new String[]{ "Monto de pago", "Fecha de pago", "Referencia" };
    }
}
