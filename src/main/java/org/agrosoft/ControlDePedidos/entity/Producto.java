package org.agrosoft.ControlDePedidos.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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
    private Integer idProducto;

    @Column(name = "nombre_producto", length = 30, nullable = false)
    private String nombreProducto;

    @Column(name = "precio_unitario", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private Float precioUnitario;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "pedidos_productos",
            joinColumns = @JoinColumn(name = "fk_producto", referencedColumnName = "id_producto", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "fk_pedido", referencedColumnName = "id_pedido", nullable = false)
    )
    private List<Pedido> pedidos;

}
