package org.agrosoft.ControlDePedidos.GUI.clients;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
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
public class PedidoClient {

    public static List<Pedido> fetchAll() {
        List<Pedido> response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUri(URI.create(getBaseUrl() + "/todos"))
                    .build()
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<List<Pedido>> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<Pedido>>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(webResponse.getBody())) {
                response = webResponse.getBody();
                response.sort(Comparator.comparing(Pedido::getFechaPedido));
            } else {
                response = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = new ArrayList<>();
        }
        return response;
    }

    public static Pedido fetchById(int idPedido) {
        Pedido response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/porId/{idPedido}")
                    .buildAndExpand(idPedido)
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<Pedido> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Pedido>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(webResponse.getBody())) {
                response = webResponse.getBody();
            } else {
                response = null;
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = null;
        }
        return response;
    }

    public static int updatePedido(Pedido pedido) {
        int response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/actualizarPedido/{idPedido}")
                    .buildAndExpand(pedido.getIdPedido())
                    .toUri();
            log.info("PUT calling [{}]", uri);
            HttpEntity<Pedido> requestEntity = new HttpEntity<>(pedido, RequestUtils.getPostHeaders());
            ResponseEntity<Void> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.PUT,
                            requestEntity,
                            new ParameterizedTypeReference<Void>() {}
                    );
            log.info("Status obtained: <{}>", webResponse.getStatusCode().value());
            if(webResponse.getStatusCode().is2xxSuccessful()) {
                response = 1;
            } else if (webResponse.getStatusCode().is4xxClientError()){
                response = -1;//error 4XX
            } else {
                response = -2;//error 5XX
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = -3;
        }
        return response;
    }

    private static String getBaseUrl() throws ReadPropertyException {
        return XMLHandler.readXMLConfig("config.xml", "baseUrl") +
                XMLHandler.readXMLConfig("config.xml", "pedidoContext");
    }

}
