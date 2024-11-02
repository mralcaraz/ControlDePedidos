package org.agrosoft.ControlDePedidos.API.utils;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class FileUtils {
    public static void abreArchivo(String path) {
        File file = new File(path);
        if(file.exists() && file.isFile() && file.canRead()) {
            if(!Desktop.isDesktopSupported()) {
                log.info("Cannot open the file due to restrictions of Windows");
            } else {
                Desktop desktop = Desktop.getDesktop();
                if(!desktop.isSupported(Desktop.Action.OPEN)) {
                    log.info("Cannot open file. Operation is unsupported");
                    return;
                }
                try {
                    desktop.open(file);
                    log.info("File opened");
                } catch (IOException e) {
                    log.info("Exception caught while opening file. Original message: {}", e.getMessage());
                }
            }
        }
    }
}
