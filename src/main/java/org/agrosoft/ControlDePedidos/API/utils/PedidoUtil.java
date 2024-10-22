package org.agrosoft.ControlDePedidos.API.utils;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.API.enums.StatusLogisticaEnum;
import org.agrosoft.ControlDePedidos.API.enums.StatusPagoEnum;
import org.agrosoft.ControlDePedidos.API.enums.StatusPedidoEnum;
import org.agrosoft.ControlDePedidos.API.enums.TipoEnvioEnum;
import org.agrosoft.ControlDePedidos.API.exception.InvalidParameterException;
import org.agrosoft.ControlDePedidos.API.service.ClienteService;
import org.agrosoft.ControlDePedidos.API.service.ProdutoService;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusLogisticaService;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusPagoService;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusPedidoService;
import org.agrosoft.ControlDePedidos.API.service.staticService.TipoEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class PedidoUtil {

    @Autowired
    private StatusLogisticaService logisticaService;

    @Autowired
    private StatusPagoService pagoService;

    @Autowired
    private StatusPedidoService pedidoService;

    @Autowired
    private TipoEnvioService envioService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    public void fixPedidoReferences(Pedido pedido) throws InvalidParameterException {
        try {
            StatusLogisticaEnum logisticaEnum = Arrays.stream(StatusLogisticaEnum.values())
                    .filter(e -> e.getDescripcion().equalsIgnoreCase(pedido.getStatusLogistica().getDescripcion()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidParameterException("Invalid StatusLogistica submitted"));
            pedido.setStatusLogistica( logisticaService.getEntity(logisticaEnum) );

            StatusPagoEnum pagoEnum = Arrays.stream(StatusPagoEnum.values())
                    .filter(e -> e.getDescripcion().equalsIgnoreCase(pedido.getStatusPago().getDescripcion()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidParameterException("Invalid StatusPago submitted"));
            pedido.setStatusPago( pagoService.getEntity(pagoEnum) );

            StatusPedidoEnum pedidoEnum = Arrays.stream(StatusPedidoEnum.values())
                    .filter(e -> e.getDescripcion().equalsIgnoreCase(pedido.getStatusPedido().getDescripcion()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidParameterException("Invalid StatusPago submitted"));
            pedido.setStatusPedido( pedidoService.getEntity(pedidoEnum) );

            TipoEnvioEnum envioEnum = Arrays.stream(TipoEnvioEnum.values())
                    .filter(e -> e.getDescripcion().equalsIgnoreCase(pedido.getTipoEnvio().getDescripcion()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidParameterException("Invalid TipoEnvio submitted"));
            pedido.setTipoEnvio( envioService.getEntity(envioEnum) );

            pedido.setCliente(
                    clienteService.encontrarPorContacto( pedido.getCliente().getContacto() )
                            .orElseThrow( () -> new InvalidParameterException("Invalid Cliente submitted") )
            );

            List<Producto> entityList = new ArrayList<>();
            for(Producto p : pedido.getProductos()) {
                entityList.add( produtoService.consultarPorNombre(p.getNombreProducto())
                        .orElseThrow( () -> new InvalidParameterException("Invalid Producto submitted")) );
            }
            pedido.setProductos(entityList);
        } catch (Exception e) {
            throw new InvalidParameterException("Exception while fixing Pedido sub-entities: " + e.getMessage());
        }
    }

    public void addProductoToPedido(Pedido pedido, int idProducto) throws InvalidParameterException {
        Producto producto = produtoService.consultarPorId(idProducto)
                .orElseThrow( () -> new InvalidParameterException("Invalid Producto submitted"));
        pedido.getProductos().add(producto);
    }

}
