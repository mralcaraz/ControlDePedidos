package org.agrosoft.ControlDePedidos.API.utils;

import org.springframework.validation.Errors;

import java.util.Set;
import java.util.TreeSet;

public class ErrorUtils {
    public static Set<String> errorsToStringSet(Errors errores) {
        Set<String> errorSet = new TreeSet<>();
        errores.getFieldErrors().forEach( error -> errorSet.add(error.getDefaultMessage()) );
        return errorSet;
    }
}
