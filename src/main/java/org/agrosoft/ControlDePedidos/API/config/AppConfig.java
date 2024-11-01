package org.agrosoft.ControlDePedidos.API.config;

import org.agrosoft.ControlDePedidos.API.service.PagoService;
import org.agrosoft.ControlDePedidos.API.service.PedidoService;
import org.agrosoft.ControlDePedidos.API.service.ProdutoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CorteMesBOFactory corteMesBOFactory(PedidoService pedidoService, ProdutoService produtoService,
                                               PagoService pagoService) {
        return new CorteMesBOFactory(pedidoService, produtoService, pagoService);
    }
}
