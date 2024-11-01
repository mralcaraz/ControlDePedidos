package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.CorteMes;

import java.util.Optional;

public interface CorteMesService {
    public Optional<CorteMes> encontrarPorMesYAnio(int mes, int anio);
    public CorteMes guardarCorteMes(CorteMes toSave);
    public void estableceEnviado(CorteMes corteMes, boolean enviado);
}
