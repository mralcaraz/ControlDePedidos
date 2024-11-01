package org.agrosoft.ControlDePedidos.API.converter;

import java.util.List;

public interface GenericPdfConveter<T> {

    public String[] convertEntity(T entity);

    public default String[][] convertList(List<T> entityList) {
        return entityList.stream()
                .map(this::convertEntity)
                .toArray(String[][]::new);
    }
}
