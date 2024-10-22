package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.API.repository.PagoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoDao repository;

    @Override
    public Optional<Pago> encontrarPorId(int idPago) {
        return repository.findById(idPago);
    }

    @Override
    public List<Pago> encontrarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Pago> encontrarPorPedido(int idPedido) {
        return repository.findByPedido(idPedido);
    }

    @Override
    public List<Pago> encontrarPorRangoFecha(LocalDate fechaIni, LocalDate fechaFin) {
        return repository.findByFechaPagoBetween(fechaIni, fechaFin);
    }

    @Override
    public List<Pago> encontrarPorReferencia(String referencia) {
        return repository.findByNumeroReferenciaContains(referencia);
    }

    @Override
    public int guardarPago(Pago pago) {
        return repository.saveAndFlush(pago).getIdPago();
    }

    @Override
    public void eliminarPago(int idPago) {
        repository.deleteById(idPago);
    }
}
