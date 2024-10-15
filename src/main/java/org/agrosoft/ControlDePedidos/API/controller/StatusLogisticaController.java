package org.agrosoft.ControlDePedidos.API.controller;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.enums.StatusLogisticaEnum;
import org.agrosoft.ControlDePedidos.API.exception.StatusLogisticaNotFoundException;
import org.agrosoft.ControlDePedidos.API.service.staticService.StatusLogisticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/StatusLogistica")
public class StatusLogisticaController {

    @Autowired
    private StatusLogisticaService service;

    @GetMapping("/todos")
    public ResponseEntity<?> encontrarTodos() {
        log.info("encontrarTodos --START");
        return ResponseEntity.ok(service.getEntityList());
    }

    @GetMapping("/porStatus")
    public ResponseEntity<?> encontrarPorStatus(@RequestParam StatusLogisticaEnum item)
            throws StatusLogisticaNotFoundException {
        log.info("encontrarPorStatus --START");
        return ResponseEntity.ok(service.getEntity(item));
    }
}
