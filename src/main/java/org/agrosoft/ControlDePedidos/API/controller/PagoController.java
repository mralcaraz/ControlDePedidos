package org.agrosoft.ControlDePedidos.API.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.API.exception.InvalidParameterException;
import org.agrosoft.ControlDePedidos.API.exception.PagoNotFoundException;
import org.agrosoft.ControlDePedidos.API.service.PagoService;
import org.agrosoft.ControlDePedidos.API.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/Pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;


    @GetMapping("/todos")
    public ResponseEntity<?> encuentraTodos() {
        log.info("encuentraTodos --START");
        return ResponseEntity.ok(pagoService.encontrarTodos());
    }

    @GetMapping("/porId/{idPago}")
    public ResponseEntity<?> encontrarPorId(@PathVariable int idPago) {
        log.info("encontrarPorId --START with Id <{}>", idPago);
        return ResponseEntity.ok(pagoService.encontrarPorId(idPago).orElse(null));
    }

    @GetMapping("/porPedido/{idPedido}")
    public ResponseEntity<?> encontrarPorPedido(@PathVariable int idPedido) {
        log.info("encontrarPorPedido --START with idPedido <{}>", idPedido);
        return ResponseEntity.ok(pagoService.encontrarPorPedido(idPedido));
    }

    @GetMapping("/porRangoFecha")
    public ResponseEntity<?> encontrarPorRangoFecha(@RequestParam LocalDate fechaIni, @RequestParam LocalDate fechaFin) {
        log.info("encontrarPorRangoFecha --START with Fecha between <{}> and <{}>", fechaIni, fechaFin);
        return ResponseEntity.ok(pagoService.encontrarPorRangoFecha(fechaIni, fechaFin));
    }

    @PostMapping("/crearPago")
    public ResponseEntity<?> crearPago(@Valid @RequestBody Pago toSave, Errors errors) throws InvalidParameterException {
        log.info("crearPago --START. Pago submitted: {}", toSave);
        HttpStatus status;
        int idGuardado;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Pago received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        try {
            idGuardado = pagoService.guardarPago(toSave);
            status = HttpStatus.OK;
        } catch (Exception e) {
            idGuardado = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(e.getMessage(), e);
        }
        log.info("crearPago --END");
        return new ResponseEntity<>(idGuardado, status);
    }

    @DeleteMapping("/borrarPago/{idPago}")
    public ResponseEntity<?> borrarPago(@PathVariable int idPago) throws PagoNotFoundException {
        log.info("borrarPago --START with id <{}>", idPago);
        HttpStatus status;
        String message;
        int idToDelete = pagoService.encontrarPorId(idPago)
                .orElseThrow(() -> new PagoNotFoundException("Pago with id <" + idPago + "> not found in database"))
                .getIdPago();
        try {
            pagoService.eliminarPago(idToDelete);
            status = HttpStatus.OK;
            message = "Pago with Id <" + idPago + "> has been deleted from database";
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = "Pago with Id <" + idPago + "> has NOT been deleted from database. Reason: " + e.getMessage();
            log.error(e.getMessage(), e);
        }
        log.info("borrarCliente --END. Result: {}", message);
        return new ResponseEntity<>(message, status);
    }
}
