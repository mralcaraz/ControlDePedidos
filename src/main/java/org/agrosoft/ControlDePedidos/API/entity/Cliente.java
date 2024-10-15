package org.agrosoft.ControlDePedidos.API.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.CANNOT_BE_NULL_OR_EMPTY;
import static org.agrosoft.ControlDePedidos.API.constant.ValidationConstants.LENGTH_NOT_VALID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @Column(name = "id_cliente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int idCliente;

    @NotEmpty(message = "NombreCliente" + CANNOT_BE_NULL_OR_EMPTY)
    @Size(min = 3, max = 30, message = "nombre" + LENGTH_NOT_VALID)
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;

    @NotEmpty(message = "PrimerApellido" + CANNOT_BE_NULL_OR_EMPTY)
    @Size(min = 3, max = 30, message = "primerApellido" + LENGTH_NOT_VALID)
    @Column(name = "primerApellido", length = 30, nullable = false)
    private String primerApellido;

    @Nullable
    @Size(min = 3, max = 30, message = "nombre" + LENGTH_NOT_VALID)
    @Column(name = "segundoApellido", length = 30)
    private String segundoApellido;

    @NotEmpty(message = "Contacto" + CANNOT_BE_NULL_OR_EMPTY)
    @Column(name = "contacto", nullable = false)
    private String contacto;
}
