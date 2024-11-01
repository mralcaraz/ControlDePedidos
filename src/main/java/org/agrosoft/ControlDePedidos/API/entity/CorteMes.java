package org.agrosoft.ControlDePedidos.API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "corte_mes_schedule")
public class CorteMes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corte_mes_schedule")
    private int idCorteMes;

    @Column(name = "mes_envio", nullable = false)
    private int mesEnvio;

    @Column(name = "anio_envio", nullable = false)
    private int anioEnvio;

    @Builder.Default
    @Column(name = "numero_intentos", nullable = false)
    private int numeroIntentos = 0;

    @Builder.Default
    @Column(name = "enviado", nullable = false)
    private boolean enviado = false;

    @Column(name = "fecha_hora_envio")
    private LocalDateTime fechaHoraEnvio;
}