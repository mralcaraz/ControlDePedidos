package org.agrosoft.ControlDePedidos.API.exception;

public class ProductoAlreadyExistsException extends Exception{
    public ProductoAlreadyExistsException(String msg) {
        super(msg);
    }
}
