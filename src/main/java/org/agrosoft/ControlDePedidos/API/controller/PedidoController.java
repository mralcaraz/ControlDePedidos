package org.agrosoft.ControlDePedidos.API.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.exception.InvalidParameterException;
import org.agrosoft.ControlDePedidos.API.service.PedidoService;
import org.agrosoft.ControlDePedidos.API.utils.ErrorUtils;
import org.agrosoft.ControlDePedidos.API.utils.PedidoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/Pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoUtil pedidoUtil;

    @GetMapping("/todos")
    public ResponseEntity<?> encontrarTodos() {
        log.info("encontrarTodos --START");
        return ResponseEntity.ok(pedidoService.encontrarTodos());
    }

    @GetMapping("/porId/{idPedido}")
    public ResponseEntity<?> encontrarPorId(@PathVariable int idPedido){
        log.info("encontrarPorId --START with id <{}>", idPedido);
        return ResponseEntity.ok(pedidoService.encontrarPorId(idPedido).orElse(null));
    }

    @GetMapping("porParametros")
    public ResponseEntity<?> encontrarPorParametros(@RequestParam(required = false) Integer idStatusPedido,
                                                    @RequestParam(required = false) LocalDate fechaIni,
                                                    @RequestParam(required = false) LocalDate fechaFin,
                                                    @RequestParam(required = false) Integer idCliente,
                                                    @RequestParam(required = false) Integer idStatusPago,
                                                    @RequestParam(required = false) Integer idStatusLogistica,
                                                    @RequestParam(required = false) Integer idTipoEnvio,
                                                    @RequestParam(required = false) Boolean isActive) {
        log.info("encontrarPorParametros --START with parameters: idStatusPedido<{}>, fechaIni<{}>, fechaFin<{}>, " +
                "idCliente<{}>, idStatusPago<{}>, idStatusLogistica<{}>, idTipoEnvio<{}>, isActive<{}>", idStatusPedido,
                fechaIni, fechaFin, idCliente, idStatusPago, idStatusLogistica, idTipoEnvio, isActive);
        return ResponseEntity.ok(pedidoService.encontrarPorParametros(idStatusPedido, fechaIni, fechaFin, idCliente,
                idStatusPago, idStatusLogistica, idTipoEnvio, isActive));
    }

    @PostMapping("/crearPedido")
    public ResponseEntity<?> crearPedido(@Valid @RequestBody Pedido toSave, Errors errors)
            throws InvalidParameterException {
        log.info("crearPedido --START");
        HttpStatus status;
        int idGuardado;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Pedido received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        pedidoUtil.fixPedidoReferences(toSave);
        try {
            idGuardado = pedidoService.guardarPedido(toSave);
            log.info("Pedido saved successfully with id <{}>", idGuardado);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Exception while saving pedido: {}", e.getMessage(), e);
            idGuardado = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(idGuardado, status);
    }

    @PutMapping("/actualizarPedido")
    public ResponseEntity<?> actualizarPedido(@Valid @RequestBody Pedido pedido,
                                              Errors errors) throws InvalidParameterException {
        log.info("actualizarPedido --START for Pedido with id <{}>", pedido.getIdPedido());
        HttpStatus status;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Pedido received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        pedidoUtil.fixPedidoReferences(pedido);
        try {
            pedidoService.guardarPedido(pedido);
            log.info("Pedido updated successfully with id <{}>", pedido.getIdPedido());
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Exception while updating pedido: {}", e.getMessage(), e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/borrarPedido/{idPedido}")
    public ResponseEntity<?> borrarPedido(@PathVariable int idPedido) throws InvalidParameterException {
        log.info("borrarPedido --START with id: <{}>", idPedido);
        HttpStatus status;
        Pedido pedido = pedidoService.encontrarPorId(idPedido)
                .orElseThrow(() -> new InvalidParameterException("Pedido submitted not found"));
        pedido.setActive(false);
        try {
            pedidoService.guardarPedido(pedido);
            log.info("Pedido with id <{}> configured as inactive successfully", idPedido);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Exception while updating pedido: {}", e.getMessage(), e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(status);
    }

}
