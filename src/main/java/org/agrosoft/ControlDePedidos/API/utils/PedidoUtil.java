package org.agrosoft.ControlDePedidos.API.utils;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.API.exception.InvalidParameterException;
import org.agrosoft.ControlDePedidos.API.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PedidoUtil {

    @Autowired
    private ProdutoService produtoService;

    public void fixPedidoReferences(Pedido pedido) throws InvalidParameterException {
        try {
            List<Producto> entityList = new ArrayList<>();
            for(Producto p : pedido.getProductos()) {
                entityList.add( produtoService.consultarPorNombre(p.getNombreProducto() )
                        .orElseThrow( () -> new InvalidParameterException("Invalid Producto submitted")) );
            }
            pedido.setProductos(entityList);
        } catch (Exception e) {
            throw new InvalidParameterException("Exception while fixing Pedido sub-entities: " + e.getMessage());
        }
    }
}
