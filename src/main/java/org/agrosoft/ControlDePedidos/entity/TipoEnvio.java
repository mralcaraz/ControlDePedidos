package org.agrosoft.ControlDePedidos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "tipo_envio")
public class TipoEnvio implements Serializable {

    @Id
    @Column(name = "id_tipo_envio")
    private Integer idTipoEnvio;

    @Column(name = "descripcion_envio", length = 30, nullable = false)
    private String descripcion;

}
