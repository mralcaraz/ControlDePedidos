package org.agrosoft.ControlDePedidos.API.bo;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.API.service.PagoService;
import org.agrosoft.ControlDePedidos.API.service.PedidoService;
import org.agrosoft.ControlDePedidos.API.service.ProdutoService;
import org.agrosoft.ControlDePedidos.API.utils.PDFGenerator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CorteMesBO {

    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;
    private final PagoService pagoService;

    public CorteMesBO(YearMonth yearMonth, PedidoService pedidoService, ProdutoService produtoService,
                      PagoService pagoService) {
        this.fechaInicio = yearMonth.atDay(1);
        this.fechaFin = yearMonth.atEndOfMonth();
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
        this.pagoService = pagoService;
    }

    public String realizaCorteDeMes() {
        String rutaArchivo = "";
        try {
            List<Pedido> pedidosDelMes = pedidoService.encontrarPorParametros(null,this.fechaInicio,
                    this.fechaFin, null, null, null, null,true);
            List<Pago> pagosDelMes = pagoService.encontrarPorRangoFecha(this.fechaInicio, this.fechaFin);
            List<Pedido> pedidosDePagosDelMes = pagosDelMes.stream()
                                    .map(Pago::getPedido)
                                    .distinct()
                                    .toList();
            log.info("{} Pedidos, {} Pagos and {} Pedidos with activity found for this month", pedidosDelMes.size(),
                    pagosDelMes.size(), pedidosDePagosDelMes.size());
            if((pedidosDelMes.size() + pagosDelMes.size() + pedidosDePagosDelMes.size()) > 0) {
                List<Producto> productosDelMes = new ArrayList<>();
                for(Pedido pedido : pedidosDelMes) {
                    productosDelMes.addAll( produtoService.consultarPorPedido(pedido.getIdPedido()) );
                }
                rutaArchivo = PDFGenerator.generaCorte(pedidosDelMes, pagosDelMes, productosDelMes,
                        pedidosDePagosDelMes, this.fechaFin);
                log.info("PDF file created. Trying to inactivate completed Pedidos");
                List<Pedido> finalizables = pedidoService.encontrarFinalizables();
                log.info("{} complete Pedidos found", finalizables.size());
                if(!finalizables.isEmpty()) {
                    for(Pedido pedido : finalizables) {
                        pedido.setActive(false);
                        pedidoService.guardarPedido(pedido);
                    }
                }
                log.info("Inactivation process completed");
            } else {
                rutaArchivo = "Reporte vacío";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rutaArchivo;
    }


}
