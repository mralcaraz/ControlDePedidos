package org.agrosoft.ControlDePedidos.GUI.bo;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.dto.PedidoDetalleDTO;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.utils.FileUtils;
import org.agrosoft.ControlDePedidos.API.utils.PDFGenerator;
import org.agrosoft.ControlDePedidos.GUI.clients.PedidoDetalleClient;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ImprimirPedidosBO {
    public static void imprimePedidosConDetalle(List<Pedido> pedidoList) {
        try {
            log.info("Preparing to print Pedidos with PedidoDetalle. Pedidos received: {}", pedidoList.size());
            List<PedidoDetalleDTO> detalleDTOS = new ArrayList<>();
            PedidoDetalleDTO tmp;
            log.info("Fetching PedidoDetalle");
            for(Pedido p : pedidoList) {
                tmp = PedidoDetalleClient.fetchByPedidoId(p.getIdPedido());
                if(Objects.nonNull(tmp)) {
                    detalleDTOS.add(tmp);
                }
            }
            log.info("{} PedidoDetalle fetched from database", detalleDTOS.size());
            if(!detalleDTOS.isEmpty()) {
                String rutaArchivo = PDFGenerator.generaReporte(detalleDTOS);
                if(!rutaArchivo.isBlank()) {
                    FormUtils.mostrarDialogoEnPantalla("Reporte de pedidos creado", "Reporte creado",
                            JOptionPane.INFORMATION_MESSAGE);
                    FileUtils.abreArchivo(rutaArchivo);
                } else {
                    FormUtils.mostrarDialogoEnPantalla("Hubo un error al intentar generar el reporte de" +
                                    " pedidos", "Error al generar reporte de pedidos", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                FormUtils.mostrarDialogoEnPantalla("No hay detalle de envío disponible para los pedidos " +
                        "actuales", "Error al generar reporte de pedidos", JOptionPane.ERROR_MESSAGE);
                log.info("No PedidoDetalle found in database for requested Pedido. Skipping PDF Creation");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
