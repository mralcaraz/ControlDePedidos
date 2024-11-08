package org.agrosoft.ControlDePedidos.API.controller;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.converter.PedidoDetalleConverter;
import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;
import org.agrosoft.ControlDePedidos.API.service.PedidoDetalleService;
import org.agrosoft.ControlDePedidos.API.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/PedidoDetalle")
public class PedidoDetalleController {

    @Autowired
    private PedidoDetalleService service;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/todos")
    public ResponseEntity<?> encuentraTodos() {
        log.info("encuentraTodos --START");
        return ResponseEntity.ok(PedidoDetalleConverter.convertEntityList(service.encuentraTodos()));
    }

    @GetMapping("/porIdPedido/{idPedido}")
    public ResponseEntity<?> encuentraPorIdPedido(@PathVariable int idPedido) {
        log.info("encuentraPorIdPedido --START with id: {}", idPedido);
        PedidoDetalle pedidoDetalle = service.encuentraPorPedido(idPedido).orElse(null);
        return ResponseEntity.ok(
                Objects.isNull(pedidoDetalle) ? null : PedidoDetalleConverter.convertEntity(pedidoDetalle)
        );
    }

    @GetMapping("/porId/{idPedidoDetalle}")
    public ResponseEntity<?> encuentraPorId(@PathVariable int idPedidoDetalle) {
        log.info("encuentraPorIdPedidoDetalle --START with id: {}", idPedidoDetalle);
        PedidoDetalle pedidoDetalle = service.encontrarPorId(idPedidoDetalle).orElse(null);
        return ResponseEntity.ok(
                Objects.isNull(pedidoDetalle) ? null : PedidoDetalleConverter.convertEntity(pedidoDetalle)
        );
    }

    @PostMapping("/crearPedidoDetalle")
    public ResponseEntity<?> guardarPedidoDetalle(@RequestBody PedidoDetalle pedidoDetalle) {
        log.info("guardaPedidoDetalle --START");
        HttpStatus status;
        int idGuardado;
        try {
            idGuardado = service.guardaPedidoDetalle(pedidoDetalle);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Exception caught while trying to save a new PedidoDetalle. Original message: {}", e.getMessage(),
                    e);
            idGuardado = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        log.info("guardaPedidoDetalle --END");
        return new ResponseEntity<>(idGuardado, status);
    }

    @DeleteMapping("/borraPedidoDetalle/{idPedidoDetalle}")
    public ResponseEntity<?> borraPedidoDetalle(@PathVariable int idPedidoDetalle) {
        log.info("borraPedidoDetalle --START with id: {}", idPedidoDetalle);
        HttpStatus status;
        int idParaBorrar = service.encontrarPorId(idPedidoDetalle)
                .orElse(PedidoDetalle.builder().idPedidoDetalle(-1).build())
                .getIdPedidoDetalle();
        if(idParaBorrar > 0) {
            try {
                service.eliminaPedidoDetalle(idParaBorrar);
                status = HttpStatus.OK;
            } catch (Exception e) {
                log.error("Exception caught while trying to delete PedidoDetalle with id: {}. OriginalMessage: {}",
                        idPedidoDetalle, e.getMessage(), e);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            log.error("PedidoDetalle Not found");
            status = HttpStatus.BAD_REQUEST;
        }
        log.info("borraPedidoDetalle --END");
        return new ResponseEntity<>(status);
    }
}
