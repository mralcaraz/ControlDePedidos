package org.agrosoft.ControlDePedidos.API.service.staticService;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
public abstract class BaseStaticService<T extends CatalogoAbstracto,
        R extends JpaRepository<T, Integer>,
        S extends  CatalogoAbstracto,
        E extends Exception> {

    @Autowired
    private R repository;

    private List<T> entityList;

    public List<T> getEntityList() {
        if(Objects.isNull(this.entityList)) {
            log.info("[{}] List not found. Calling database", getClass().getSimpleName());
            this.entityList = repository.findAll();
        }
        log.info("Returning [{}] list from memory", getClass().getSimpleName());
        return this.entityList;
    }

    protected T getEntity(S item, Supplier<E> exceptionSupplier) throws E {
        log.info("Getting [{}] entity for item: {}", getClass().getSimpleName(), item);
        return this.getEntityList().stream()
                .filter( e -> e.getDescripcion().equalsIgnoreCase(item.getDescripcion()))
                .findFirst()
                .orElseThrow(exceptionSupplier);
    }
}
