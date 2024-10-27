package org.agrosoft.ControlDePedidos.GUI.clients;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.XMLHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
public class PagoClient {

    public static List<Pago> fethByPedido(int idPedido) {
        List<Pago> response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/porPedido/{idPedido}")
                    .buildAndExpand(idPedido)
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<List<Pago>> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<Pago>>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(webResponse.getBody())) {
                response = webResponse.getBody();
                response.sort(Comparator.comparing(Pago::getFechaPago));
            } else {
                response = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = new ArrayList<>();
        }
        return response;
    }

    public static int deletePago(int idPago) {
        int response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/borrarPago/{idPago}")
                    .buildAndExpand(idPago)
                    .toUri();
            log.info("DELETE calling [{}]", uri);
            ResponseEntity<String> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.DELETE,
                            null,
                            new ParameterizedTypeReference<String>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(webResponse.getBody())) {
                response = 1;
            } else {
                response = -1;
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = -2;
        }
        return response;
    }

    public static int savePago(Pago pago) {
        int response;
        try {
            HttpEntity<Pago> requestEntity = new HttpEntity<>(pago, RequestUtils.getPostHeaders());
            URI uri = UriComponentsBuilder
                    .fromUri(URI.create(getBaseUrl() + "/crearPago"))
                    .build()
                    .toUri();
            log.info("POST calling [{}]", uri);
            ResponseEntity<Integer> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.POST,
                            requestEntity,
                            new ParameterizedTypeReference<Integer>() {}
                    );
            log.info("Status obtained: <{}>", webResponse.getStatusCode().value());
            if(webResponse.getStatusCode().is2xxSuccessful()) {
                response = webResponse.getBody();
            } else if (webResponse.getStatusCode().is4xxClientError()){
                response = -1;//error 4XX
            } else {
                response = -2;//error 5XX
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = -3;//error Al llamar al servicio
        }
        return response;
    }

    private static String getBaseUrl() throws ReadPropertyException {
        return XMLHandler.readXMLConfig("config.xml", "baseUrl") +
                XMLHandler.readXMLConfig("config.xml", "pagoContext");
    }
}
