package org.agrosoft.ControlDePedidos.API.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.CANNOT_BE_NULL_OR_EMPTY;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "producto")
public class Producto implements Serializable {

    @Id
    @Column(name = "id_producto")
    private int idProducto;

    @NotEmpty(message = "NombreProducto" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "nombre_producto", length = 30, nullable = false)
    private String nombreProducto;

    @NotNull(message = "PrecioUnitario" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "precio_unitario", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private float precioUnitario;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "pedidos_productos",
            joinColumns = @JoinColumn(name = "fk_producto", referencedColumnName = "id_producto", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "fk_pedido", referencedColumnName = "id_pedido", nullable = false)
    )
    private List<Pedido> pedidos;

}
