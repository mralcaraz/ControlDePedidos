package org.agrosoft.ControlDePedidos.API.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.API.exception.InvalidParameterException;
import org.agrosoft.ControlDePedidos.API.exception.ProductoAlreadyExistsException;
import org.agrosoft.ControlDePedidos.API.service.ProdutoService;
import org.agrosoft.ControlDePedidos.API.utils.ErrorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/Productos")
public class ProductoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/todos")
    public ResponseEntity<?> encontrarTodos() {
        log.info("encontrarTodos --START");
        return ResponseEntity.ok(produtoService.consultarTodos());
    }

    @GetMapping("/porId/{idProducto}")
    public ResponseEntity<?> encontrarPorId(@PathVariable int idProducto) {
        log.info("encontrarPorId --START with Id <{}>", idProducto);
        return ResponseEntity.ok(produtoService.consultarPorId(idProducto).orElse(null));
    }

    @GetMapping("/porNombre")
    public ResponseEntity<?> encontrarPorNombre(@RequestParam String nombre) {
        log.info("encontrarPorNombre --START with nombre <{}>", nombre);
        return ResponseEntity.ok(produtoService.consultarPorNombreLike(nombre));
    }

    @GetMapping("/porPedido/{idPedido}")
    public ResponseEntity<?> encontrarPorPedido(@PathVariable int idPedido) {
        log.info("encontrarPorPedido --START with idPedido <{}>", idPedido);
        return ResponseEntity.ok(produtoService.consultarPorPedido(idPedido));
    }

    @PostMapping("/crearProducto")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto toSave, Errors errors)
            throws InvalidParameterException, ProductoAlreadyExistsException {
        log.info("crearProducto --START. ProductoSubmitted: {}", toSave);
        HttpStatus status;
        int idGuardado;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Producto received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        validarInexistenciaDeProducto(toSave.getNombreProducto());
        try {
            idGuardado = produtoService.guardaProducto(toSave);
            status = HttpStatus.OK;
        } catch (Exception e) {
            idGuardado = -1;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(e.getMessage(), e);
        }
        log.info("crearProducto --END");
        return new ResponseEntity<>(idGuardado, status);
    }

    private void validarInexistenciaDeProducto(String nombreProducto) throws ProductoAlreadyExistsException {
        int id = produtoService.consultarPorNombre(nombreProducto)
                .orElse(Producto.builder().idProducto(-1).build())
                .getIdProducto();
        if(id > 0) {
            throw new ProductoAlreadyExistsException("Product with Nombre <" + nombreProducto + "> already exists in " +
                    "database");
        }
    }

}
