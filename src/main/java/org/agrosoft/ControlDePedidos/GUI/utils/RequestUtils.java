package org.agrosoft.ControlDePedidos.GUI.utils;

import org.agrosoft.ControlDePedidos.API.entity.Cliente;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

public class RequestUtils {

    public static HttpHeaders getPostHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public static String concatenaNombre(Cliente cliente) {
        return cliente.getNombre() + " " + cliente.getPrimerApellido() +
                (Objects.isNull(cliente.getSegundoApellido()) ? "" : " " + cliente.getSegundoApellido());
    }
}
