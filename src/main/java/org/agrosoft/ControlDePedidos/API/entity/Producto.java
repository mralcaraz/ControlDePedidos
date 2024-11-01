package org.agrosoft.ControlDePedidos.API.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.LENGTH_NOT_VALID_50;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int idProducto;

    @Size(min = 3, max = 50, message = "nombre" + LENGTH_NOT_VALID_50)
    @NotEmpty(message = "NombreProducto" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "nombre_producto", length = 50, nullable = false)
    private String nombreProducto;

    @NotNull(message = "PrecioUnitario" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "precio_unitario", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private float precioUnitario;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "productos")
    private List<Pedido> pedidos;

    @NotNull
    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", nombreProducto='" + nombreProducto + '\'' +
                '}';
    }
}
