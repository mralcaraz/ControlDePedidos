package org.agrosoft.ControlDePedidos.API.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.CANNOT_BE_NULL_OR_EMPTY;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pago")
public class Pago implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private int idPago;

    @NotNull(message = "MontoPago" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "monto_pago", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private float montoPago;

    @NotNull(message = "FechaPago" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "numero_referencia", length = 30)
    private String numeroReferencia;

    @ManyToOne
    @JoinColumn(name = "fk_pedido", nullable = false)
    private Pedido pedido;

}
