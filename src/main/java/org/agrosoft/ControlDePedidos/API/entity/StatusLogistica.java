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
@Table(name = "status_logistica")
public class StatusLogistica implements Serializable, CatalogoAbstracto {

    @Id
    @Column(name = "id_status_logistica")
    private int idLogistica;

    @Column(name = "descripcion_logistica", length = 12, nullable = false)
    private String descripcion;

}
