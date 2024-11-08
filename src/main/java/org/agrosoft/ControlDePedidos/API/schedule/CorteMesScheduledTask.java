package org.agrosoft.ControlDePedidos.API.schedule;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.bo.CorteMesBO;
import org.agrosoft.ControlDePedidos.API.config.CorteMesBOFactory;
import org.agrosoft.ControlDePedidos.API.entity.CorteMes;
import org.agrosoft.ControlDePedidos.API.service.CorteMesService;
import org.agrosoft.ControlDePedidos.API.utils.FileUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

@Slf4j
@Component
public class CorteMesScheduledTask {

    @Autowired
    private CorteMesService service;

    @Autowired
    private CorteMesBOFactory corteMesBOFactory;

    private boolean hasBeenSent = false;

    @Scheduled(cron = "${scheduled.sales.report.cron}")
    private void validateScheduleTask() {
        if(!hasBeenSent) {
            LocalDate fecha = LocalDate.now().minusMonths(1);
            log.info("Checking Sales report for month {} and year {}", fecha.getMonth(), fecha.getYear());
            CorteMes corteMes = service.encontrarPorMesYAnio(fecha.getMonthValue(), fecha.getYear()).orElse(null);
            if(Objects.isNull(corteMes)) {
                corteMes = CorteMes.builder()
                        .mesEnvio(fecha.getMonthValue())
                        .anioEnvio(fecha.getYear())
                        .build();
                corteMes = service.guardarCorteMes(corteMes);
                this.generaCorteMes(corteMes, fecha);
            } else {
                if(corteMes.isEnviado()) {
                    log.info("Sales report for {} has been already sent. No more actions required", fecha.getMonth());
                    this.hasBeenSent = true;
                } else {
                    this.generaCorteMes(corteMes, fecha);
                }
            }
        }
    }

    private void generaCorteMes(CorteMes corteMes, LocalDate fecha) {
        log.info("Trying to generate CorteMes for {}", fecha.getMonth());
        corteMes.setNumeroIntentos( corteMes.getNumeroIntentos() + 1 );
        CorteMesBO corteMesBO = corteMesBOFactory.getCorteMesBOInstance(YearMonth.from(fecha));
        String result = corteMesBO.realizaCorteDeMes();
        if(!result.isBlank()) {
            FormUtils.mostrarDialogoEnPantalla("Reporte de fin de mes creado", "Reporte creado",
                    JOptionPane.INFORMATION_MESSAGE);
            FileUtils.abreArchivo(result);
            service.estableceEnviado(corteMes, true);
            this.hasBeenSent = true;
        } else {
            FormUtils.mostrarDialogoEnPantalla("Hubo un error al intentar generar el reporte del mes",
                    "Error al generar reporte del mes", JOptionPane.ERROR_MESSAGE);
            service.estableceEnviado(corteMes, false);
        }
    }

}
