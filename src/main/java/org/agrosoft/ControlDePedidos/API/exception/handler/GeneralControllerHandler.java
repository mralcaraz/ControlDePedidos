package org.agrosoft.ControlDePedidos.API.exception.handler;

import org.agrosoft.ControlDePedidos.API.controller.*;
import org.agrosoft.ControlDePedidos.API.exception.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {StatusLogisticaController.class, StatusPagoController.class,
        StatusPedidoController.class, TipoEnvioController.class, ClienteController.class, PagoController.class,
        ProductoController.class, PedidoController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GeneralControllerHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, NumberFormatException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadParamRequest(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Exception caught related with received parameters in URL: " + e.getMessage());
    }

    @ExceptionHandler({
            StatusLogisticaNotFoundException.class, StatusPagoNotFoundException.class,
            StatusPedidoNotFoundException.class, TipoEnvioNotFoundException.class,
            ClienteAlreadyExistsException.class, ClienteNotFoundException.class,
            PagoNotFoundException.class, ProductoAlreadyExistsException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNotFoundAndAlreadyExistsExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidRequest(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Exception caught related to received parameters. "
                        + e.getMessage());
    }
}
