package org.agrosoft.ControlDePedidos.API.service;

import org.agrosoft.ControlDePedidos.API.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    public Optional<Cliente> encontrarPorId(Integer id);
    public Optional<Cliente> encontrarPorContacto(String contacto);
    public List<Cliente> encontrarTodos();
    public List<Cliente> encontrarPorNombreOApellido(String nombreOApellido);
    public int guardarCliente(Cliente cliente);
    public void borrarCliente(Integer id);
}
