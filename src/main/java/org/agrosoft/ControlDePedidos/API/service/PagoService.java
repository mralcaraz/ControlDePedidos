package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Pago;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    public Optional<Pago> encontrarPorId(int idPago);
    public List<Pago> encontrarTodos();
    public List<Pago> encontrarPorPedido(int idPedido);
    public List<Pago> encontrarPorRangoFecha(LocalDate fechaIni, LocalDate fechaFin);
    public int guardarPago(Pago pago);
    public void eliminarPago(int idPago);
}
