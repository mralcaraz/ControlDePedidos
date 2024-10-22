package org.agrosoft.ControlDePedidos.API.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.CANNOT_BE_NULL_OR_EMPTY;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "id_pedido")
    private int idPedido;

    @ManyToOne
    @JoinColumn(name = "fk_status_pedido", nullable = false)
    private StatusPedido statusPedido;

    @NotNull(message = "FechaPedido" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Nullable
    @Column(name = "numero_guia", length = 30)
    private String numeroGuia;

    @NotNull(message = "MontoTotal" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "monto_total", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private float montoTotal;

    @Column(name = "monto_envio", columnDefinition = "DECIMAL(7,2)")
    private float montoEnvio;

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
    @NotNull(message = "TipoEnvio" + CANNOT_BE_NULL_OR_EMPTY)
    @JoinColumn(name = "fk_tipo_envio", nullable = false)
    private TipoEnvio tipoEnvio;

    @ManyToMany(mappedBy = "pedidos")
    private List<Producto> productos;

    @NotNull
    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

}
