package org.agrosoft.ControlDePedidos.API.converter;

import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

public class ProductoConverter implements GenericPdfConveter<Producto> {
    @Override
    public String[] convertEntity(Producto entity) {
        return new String[] { entity.getNombreProducto(), FormUtils.formateaDinero(entity.getPrecioUnitario()) };
    }

    public String[] getEncabezados() {
        return new String[]{ "Producto", "Precio unitario" };
    }
}
