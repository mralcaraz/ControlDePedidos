package org.agrosoft.ControlDePedidos.API.converter;

import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;

import java.time.format.DateTimeFormatter;

public class PedidoCorteConverter implements GenericPdfConveter<Pedido> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String[] convertEntity(Pedido entity) {
        return new String[]{ formatter.format(entity.getFechaPedido()), entity.getStatusPedido().getDescripcion(),
                FormUtils.formateaDinero(entity.getMontoTotal()), RequestUtils.concatenaNombre(entity.getCliente()),
                entity.getStatusPago().getDescripcion() };
    }

    public String[] getEncabezados() {
        return new String[]{ "Fecha Pedido", "Estatus pedido", "Monto total", "Cliente", "Estatus pago" };
    }
}