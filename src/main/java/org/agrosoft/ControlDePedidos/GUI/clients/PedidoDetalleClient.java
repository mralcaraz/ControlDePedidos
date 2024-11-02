package org.agrosoft.ControlDePedidos.GUI.clients;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.dto.PedidoDetalleDTO;
import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;
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
public class PedidoDetalleClient {

    public static List<PedidoDetalleDTO> fetchAll() {
        List<PedidoDetalleDTO> response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUri(URI.create(getBaseUrl() + "/todos"))
                    .build()
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<List<PedidoDetalleDTO>> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<PedidoDetalleDTO>>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(webResponse.getBody())) {
                response = webResponse.getBody();
                response.sort(Comparator.comparing(PedidoDetalleDTO::getNumeroPedido));
            } else {
                response = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = new ArrayList<>();
        }
        return response;
    }

    public static PedidoDetalleDTO fetchByPedidoId(int idPedido) {
        PedidoDetalleDTO response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/porIdPedido/{idPedido}")
                    .buildAndExpand(idPedido)
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<PedidoDetalleDTO> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<PedidoDetalleDTO>() {}
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

    public static PedidoDetalleDTO fetchById(int idPedidoDetalle) {
        PedidoDetalleDTO response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/porId/{idPedidoDetalle}")
                    .buildAndExpand(idPedidoDetalle)
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<PedidoDetalleDTO> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<PedidoDetalleDTO>() {}
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

    public static int savePedidoDetalle(PedidoDetalle pedidoDetalle) {
        int response;
        try {
            HttpEntity<PedidoDetalle> requestEntity = new HttpEntity<>(pedidoDetalle, RequestUtils.getPostHeaders());
            URI uri = UriComponentsBuilder
                    .fromUri(URI.create(getBaseUrl() + "/crearPedidoDetalle"))
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

    public static int deletePedidoDetalle(int idPedidoDetalle) {
        int response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/borraPedidoDetalle/{idPedidoDetalle}")
                    .buildAndExpand(idPedidoDetalle)
                    .toUri();
            log.info("DELETE calling [{}]", uri);
            ResponseEntity<Void> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.DELETE,
                            null,
                            new ParameterizedTypeReference<Void>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful()) {
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

    private static String getBaseUrl() throws ReadPropertyException {
        return XMLHandler.readXMLConfig("config.xml", "baseUrl") +
                XMLHandler.readXMLConfig("config.xml", "pedidoDetalleContext");
    }
}
