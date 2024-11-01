package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.CorteMes;
import org.agrosoft.ControlDePedidos.API.repository.CorteMesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CorteMesServiceImpl implements CorteMesService {

    @Autowired
    private CorteMesDao repository;

    @Override
    public Optional<CorteMes> encontrarPorMesYAnio(int mes, int anio) {
        return repository.findByMesEnvioAndAnioEnvio(mes, anio);
    }

    @Override
    public CorteMes guardarCorteMes(CorteMes toSave) {
        return repository.saveAndFlush(toSave);
    }

    @Override
    public void estableceEnviado(CorteMes corteMes, boolean enviado) {
        corteMes.setEnviado(enviado);
        corteMes.setFechaHoraEnvio(LocalDateTime.now());
        repository.saveAndFlush(corteMes);
    }
}
