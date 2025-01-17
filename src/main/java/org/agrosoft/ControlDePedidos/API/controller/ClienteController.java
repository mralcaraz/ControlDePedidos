package org.agrosoft.ControlDePedidos.API.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Cliente;
import org.agrosoft.ControlDePedidos.API.exception.ClienteAlreadyExistsException;
import org.agrosoft.ControlDePedidos.API.exception.ClienteNotFoundException;
import org.agrosoft.ControlDePedidos.API.exception.InvalidParameterException;
import org.agrosoft.ControlDePedidos.API.service.ClienteService;
import org.agrosoft.ControlDePedidos.API.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/Clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping("/todos")
    public ResponseEntity<?> encuentraTodos() {
        log.info("encuentraTodos --START");
        return ResponseEntity.ok(service.encontrarTodos());
    }

    @GetMapping("/porId/{idCliente}")
    public ResponseEntity<?> encontrarPorId(@PathVariable int idCliente) {
        log.info("encontrarPorId --START with Id <{}>", idCliente);
        return ResponseEntity.ok(service.encontrarPorId(idCliente).orElse(null));
    }

    @GetMapping("/porNombreOApellido")
    public ResponseEntity<?> encontrarPorNombreOApellido(@RequestParam String nombreOApellido)
            throws InvalidParameterException {
        log.info("encontrarPorNombreOApellido with parameter <{}> --START", nombreOApellido);
        if(nombreOApellido.isBlank()) {
            throw new InvalidParameterException("nombreOApellido cannot be null or empty");
        }
        return ResponseEntity.ok(service.encontrarPorNombreOApellido(nombreOApellido));
    }

    @PostMapping("/crearCliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente toSave, Errors errors)
            throws InvalidParameterException, ClienteAlreadyExistsException {
        log.info("crearCliente --START. Cliente submitted: {}", toSave);
        HttpStatus status;
        int idGuardado;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Cliente received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        validarInexistenciaDeCliente(toSave.getContacto());
        try {
            idGuardado = service.guardarCliente(toSave);
            status = HttpStatus.OK;
        } catch (Exception e) {
            idGuardado = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(e.getMessage(), e);
        }
        log.info("crearCliente --END");
        return new ResponseEntity<>(idGuardado, status);
    }

    @PutMapping("/actualizarCliente")
    public ResponseEntity<?> actualizarCliente(@Valid @RequestBody Cliente toSave, Errors errors)
            throws InvalidParameterException, ClienteNotFoundException {
        log.info("actualizarCliente --START. Cliente submitted: {}", toSave);
        HttpStatus status;
        int idGuardado;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Cliente received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        Cliente entity = validarExistenciaDeCliente(toSave.getIdCliente());
        try {
            log.info("Cliente found. updating entity");
            entity.setNombre(toSave.getNombre());
            entity.setPrimerApellido(toSave.getPrimerApellido());
            entity.setSegundoApellido(toSave.getSegundoApellido());
            idGuardado = service.guardarCliente(entity);
            status = HttpStatus.OK;
        } catch (Exception e) {
            idGuardado = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error("Exception caught while updating Cliente. Original message: {}", e.getMessage(), e);
        }
        log.info("actualizarCliente --END");
        return new ResponseEntity<>(idGuardado, status);
    }

    @DeleteMapping("/borrarCliente/{idCliente}")
    public ResponseEntity<?> borrarCliente(@PathVariable int idCliente) throws ClienteNotFoundException {
        log.info("borrarCliente --START with id <{}>", idCliente);
        Cliente toDelete = validarExistenciaDeCliente(idCliente);
        HttpStatus status;
        String msg;
        try {
            service.borrarCliente(toDelete.getIdCliente());
            status = HttpStatus.OK;
            msg = "Cliente with Id <" + idCliente + "> has been deleted from database";
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            msg = "Cliente with Id <" + idCliente + "> has NOT been deleted from database. Reason: " + e.getMessage();
            log.error(e.getMessage(), e);
        }
        log.info("borrarCliente --END. Result: {}", msg);
        return new ResponseEntity<>(msg, status);
    }

    private void validarInexistenciaDeCliente(String contacto) throws ClienteAlreadyExistsException {
        int id = service.encontrarPorContacto(contacto)
                .orElse(Cliente.builder().idCliente(-1).build())
                .getIdCliente();
        if(id > 0) {
            throw new ClienteAlreadyExistsException("Cliente with Contacto <" + contacto
                    + "> already exists in database");
        }
    }

    private Cliente validarExistenciaDeCliente(int id) throws ClienteNotFoundException {
        return service.encontrarPorId(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente with Id <" + id +
                        "> not found in database"));
    }
}
