package org.agrosoft.ControlDePedidos.GUI.utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;

public class FormUtils {

    private static final DecimalFormat df = new DecimalFormat("####0.00");

    public static void redimensionarTabla(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 100;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static void centrarVentanaEnPantalla(JFrame window) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = ge.getScreenDevices();

        int pantallaIndex = XMLHandler.readXMLConfigInt("config.xml", "screenNbr");

        if (pantallaIndex >= 0 && pantallaIndex < devices.length) {
            Rectangle bounds = devices[pantallaIndex].getDefaultConfiguration().getBounds();
            int x = bounds.x + (bounds.width - window.getWidth()) / 2;
            int y = bounds.y + (bounds.height - window.getHeight()) / 2;
            window.setLocation(x, y);
        } else {
            window.setLocationRelativeTo(null);
        }
    }

    public static String formateaDinero(float cantidad) {
        return "$ " + df.format(cantidad);
    }
}
