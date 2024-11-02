package org.agrosoft.ControlDePedidos.API.converter;

import org.agrosoft.ControlDePedidos.API.dto.PedidoDetalleDTO;

public class PedidoPdfConverter implements GenericPdfConveter<PedidoDetalleDTO> {
    @Override
    public String[] convertEntity(PedidoDetalleDTO entity) {
        return new String[]{ String.valueOf(entity.getNumeroPedido()), entity.getNombreDestinatario(),
                entity.getTelefono(), entity.getDireccion(), entity.getNota() };
    }

    public String[] getEncabezados() {
        return new String[]{ "No. Pedido", "Destinatario", "Telefono", "Direccion", "Notas" };
    }
}
