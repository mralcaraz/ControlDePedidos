package org.agrosoft.ControlDePedidos.API.converter;

import org.agrosoft.ControlDePedidos.API.dto.PedidoDetalleDTO;
import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;

import java.util.List;

public class PedidoDetalleConverter {

    public static PedidoDetalleDTO convertEntity(PedidoDetalle pedidoDetalle) {
        return PedidoDetalleDTO.builder()
                .id(pedidoDetalle.getIdPedidoDetalle())
                .numeroPedido(pedidoDetalle.getPedido().getIdPedido())
                .nombreDestinatario(RequestUtils.concatenaNombre(pedidoDetalle.getPedido().getCliente()))
                .telefono(pedidoDetalle.getTelefono())
                .direccion(pedidoDetalle.getDireccion())
                .nota(pedidoDetalle.getNotas())
                .build();
    }

    public static List<PedidoDetalleDTO> convertEntityList(List<PedidoDetalle> pedidoDetalleList) {
        return pedidoDetalleList.stream()
                .map(PedidoDetalleConverter::convertEntity)
                .toList();
    }

}
