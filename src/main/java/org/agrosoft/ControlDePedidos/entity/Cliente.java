package org.agrosoft.ControlDePedidos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @Column(name = "primerApellido", length = 30, nullable = false)
    private String primerApellido;

    @Column(name = "segundoApellido", length = 30)
    private String segundoApellido;

    @Column(name = "contacto", nullable = false)
    private String contacto;
}
