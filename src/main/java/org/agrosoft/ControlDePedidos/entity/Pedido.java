package org.agrosoft.ControlDePedidos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne
    @JoinColumn(name = "fk_status_pedido", nullable = false)
    private StatusPedido statusPedido;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Column(name = "numero_guia", length = 30)
    private String numeroGuia;

    @Column(name = "monto_total", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private Float montoTotal;

    @Column(name = "monto_envio", columnDefinition = "DECIMAL(7,2)")
    private Float montoEnvio;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "fk_status_pago", nullable = false)
    private StatusPago statusPago;

    @ManyToOne
    @JoinColumn(name = "fk_status_logistica", nullable = false)
    private  StatusLogistica statusLogistica;

    @ManyToOne
    @JoinColumn(name = "fk_tipo_envio", nullable = false)
    private TipoEnvio tipoEnvio;

    @ManyToMany(mappedBy = "pedidos")
    private List<Producto> productos;

}
