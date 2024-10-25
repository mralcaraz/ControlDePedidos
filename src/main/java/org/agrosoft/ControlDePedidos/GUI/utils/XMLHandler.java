package org.agrosoft.ControlDePedidos.GUI.utils;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class XMLHandler {
    public static final String OS_SEPARATOR = System.getProperty("file.separator");

    public static String readXMLConfig(String fileName, String keyName) throws ReadPropertyException {
        String ret = "";
        Properties prop = new Properties();
        InputStream input;

        try {
            input = XMLHandler.class.getClassLoader().getResourceAsStream(fileName);
            log.info("Trying to access <{}> to read <{}> value", fileName, keyName);
            if (input == null) {
                String absPath = System.getProperty("user.dir") + OS_SEPARATOR + "config" + OS_SEPARATOR + fileName;
                input = new FileInputStream(absPath);
            }
            prop.loadFromXML(input);
            ret = prop.getProperty(keyName);
            input.close();
        } catch(Exception e) {
            log.error("Exception caught while reading property from file: {} ", e.getMessage());
            throw new ReadPropertyException("Error al obtener el valor " + keyName + ". Detalles: " + e.getMessage());
        }
        return ret;
    }

    public static int readXMLConfigInt(String fileName, String keyName) {
        int value = 0;
        try {
            String strVal = readXMLConfig(fileName,keyName);
            log.info("Trying to convert <{}> to integer", strVal);
            value = Integer.parseInt(strVal);
        } catch (Exception e) {
            log.error("Error while reading {} property as Integer: {}", keyName, e.getMessage());
        }
        return value;
    }
}

