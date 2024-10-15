package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Cliente;
import org.agrosoft.ControlDePedidos.API.repository.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteDao repository;

    @Override
    public Optional<Cliente> encontrarPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Cliente> encontrarPorContacto(String contacto) {
        return repository.findByContacto(contacto);
    }

    @Override
    public List<Cliente> encontrarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Cliente> encontrarPorNombreOApellido(String nombreOApellido) {
        return repository.findByNombreOrApellidoContains(nombreOApellido);
    }

    @Override
    public int guardarCliente(Cliente cliente) {
        return repository.saveAndFlush(cliente).getIdCliente();
    }

    @Override
    public void borrarCliente(Integer id) {
        repository.deleteById(id);
    }
}
