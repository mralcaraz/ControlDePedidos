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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
        String decodedNombre = URLDecoder.decode(nombre, StandardCharsets.UTF_8);
        log.info("encontrarPorNombre --START with nombre <{}>", decodedNombre);
        return ResponseEntity.ok(produtoService.consultarPorNombreLike(decodedNombre));
    }

    @GetMapping("/porNombreCompleto")
    public ResponseEntity<?> encontrarPorNombreCompleto(@RequestParam String nombre) {
        String decodedNombre = URLDecoder.decode(nombre, StandardCharsets.UTF_8);
        log.info("encontrarPorNombreCompleto --START with nombre <{}>", decodedNombre);
        return ResponseEntity.ok(produtoService.consultarPorNombre(decodedNombre).orElse(null));
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

    @PutMapping("/actualizaProducto")
    public ResponseEntity<?> actualizProducto(@Valid @RequestBody Producto producto, Errors errors) throws
            InvalidParameterException {
        log.info("actualizaProducto --START for Producto {} with new name: {}", producto.getIdProducto(),
                producto.getNombreProducto());
        HttpStatus status;
        if(errors.hasErrors()) {
            throw new InvalidParameterException("Producto received did not match all the validations: " +
                    ErrorUtils.errorsToStringSet(errors));
        }
        try {
            Producto entity = produtoService.consultarPorId(producto.getIdProducto()).orElseThrow(() -> new
                    InvalidParameterException("Producto with id <" + producto.getIdProducto() + "> does not exist"));
            entity.setNombreProducto(producto.getNombreProducto());
            entity.setActive(producto.isActive());
            produtoService.guardaProducto(entity);
            status = HttpStatus.OK;
        } catch(Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            log.error(e.getMessage(), e);
        }
        log.info("actualizaProducto --END");
        return new ResponseEntity<>(status);
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
