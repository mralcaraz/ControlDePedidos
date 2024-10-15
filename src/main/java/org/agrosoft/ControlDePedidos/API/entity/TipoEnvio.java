package org.agrosoft.ControlDePedidos.API.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

import java.io.Serializable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_envio")
public class TipoEnvio implements Serializable, CatalogoAbstracto {

    @Id
    @Column(name = "id_tipo_envio")
    private int idTipoEnvio;

    @Column(name = "descripcion_envio", length = 30, nullable = false)
    private String descripcion;

}
