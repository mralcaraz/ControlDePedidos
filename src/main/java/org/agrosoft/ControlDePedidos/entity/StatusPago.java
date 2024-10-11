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
@Table(name = "status_pago")
public class StatusPago implements Serializable {

    @Id
    @Column(name = "id_status_pago")
    private Integer idStatusPago;

    @Column(name = "descripcion_pago", length = 12, nullable = false)
    private String descripcion;

}
