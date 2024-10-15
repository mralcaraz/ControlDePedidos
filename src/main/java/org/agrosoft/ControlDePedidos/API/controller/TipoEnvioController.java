package org.agrosoft.ControlDePedidos.API.controller;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.enums.TipoEnvioEnum;
import org.agrosoft.ControlDePedidos.API.exception.TipoEnvioNotFoundException;
import org.agrosoft.ControlDePedidos.API.service.staticService.TipoEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/TipoEnvio")
public class TipoEnvioController {

    @Autowired
    private TipoEnvioService service;

    @GetMapping("/todos")
    public ResponseEntity<?> encontrarTodos() {
        log.info("encontrarTodos --START");
        return ResponseEntity.ok(service.getEntityList());
    }

    @GetMapping("/porStatus")
    public ResponseEntity<?> encontrarPorStatus(@RequestParam TipoEnvioEnum item)
            throws TipoEnvioNotFoundException {
        log.info("encontrarPorStatus --START");
        return ResponseEntity.ok(service.getEntity(item));
    }
}
