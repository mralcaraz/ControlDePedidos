package org.agrosoft.ControlDePedidos.API.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.LENGTH_NOT_VALID;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "FechaPago" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Nullable
    @Size(min = 3, max = 30, message = "numeroReferencia" + LENGTH_NOT_VALID)
    @Column(name = "numero_referencia", length = 30)
    private String numeroReferencia;

    @ManyToOne
    @JoinColumn(name = "fk_pedido", nullable = false)
    private Pedido pedido;

}
