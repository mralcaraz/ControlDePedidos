package org.agrosoft.ControlDePedidos.GUI.clients;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.StatusLogistica;
import org.agrosoft.ControlDePedidos.API.enums.StatusLogisticaEnum;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;
import org.agrosoft.ControlDePedidos.GUI.utils.XMLHandler;
import org.springframework.core.ParameterizedTypeReference;
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
public class StatusLogisticaClient {

    public static List<StatusLogistica> fetchAll() {
        List<StatusLogistica> response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUri(URI.create(getBaseUrl() + "/todos"))
                    .build()
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<List<StatusLogistica>> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<StatusLogistica>>() {}
                    );
            if(webResponse.getStatusCode().is2xxSuccessful() && Objects.nonNull(webResponse.getBody())) {
                response = webResponse.getBody();
                response.sort(Comparator.comparing(StatusLogistica::getDescripcion));
            } else {
                response = new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Exception ocurred while calling endpoint: {}", e.getMessage(), e);
            response = new ArrayList<>();
        }
        return response;
    }

    public static StatusLogistica fetchByItem(StatusLogisticaEnum status) {
        StatusLogistica response;
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(getBaseUrl() + "/porStatus")
                    .queryParam("item", status)
                    .build()
                    .toUri();
            log.info("GET calling [{}]", uri);
            ResponseEntity<StatusLogistica> webResponse = new RestTemplate()
                    .exchange(
                            uri,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<StatusLogistica>() {}
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
        log.info("Item retrieved: {}", response);
        return response;
    }

    private static String getBaseUrl() throws ReadPropertyException {
        return XMLHandler.readXMLConfig("config.xml", "baseUrl") +
                XMLHandler.readXMLConfig("config.xml", "statusLogisticaContext");
    }
}
