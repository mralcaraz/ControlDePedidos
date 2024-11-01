package org.agrosoft.ControlDePedidos.API.config;

import org.agrosoft.ControlDePedidos.API.bo.CorteMesBO;
import org.agrosoft.ControlDePedidos.API.service.PagoService;
import org.agrosoft.ControlDePedidos.API.service.PedidoService;
import org.agrosoft.ControlDePedidos.API.service.ProdutoService;

import java.time.YearMonth;

public class CorteMesBOFactory {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;
    private final PagoService pagoService;

    public CorteMesBOFactory(PedidoService pedidoService, ProdutoService produtoService, PagoService pagoService) {
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
        this.pagoService = pagoService;
    }

    public CorteMesBO getCorteMesBOInstance(YearMonth yearMonth) {
        return new CorteMesBO(yearMonth, pedidoService, produtoService, pagoService);
    }
}
