package org.agrosoft.ControlDePedidos.API.repository;

import org.agrosoft.ControlDePedidos.API.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteDao extends JpaRepository<Cliente, Integer> {

    public Optional<Cliente> findByContacto(String contacto);

    @Query(value = "SELECT c.* FROM cliente c " +
            "WHERE c.nombre LIKE %:nombreOApellido% " +
            "OR c.primer_apellido LIKE %:nombreOApellido% " +
            "OR c.segundo_apellido LIKE %:nombreOApellido%", nativeQuery = true)
    public List<Cliente> findByNombreOrApellidoContains(@Param("nombreOApellido") String nombreOApellido);
}
