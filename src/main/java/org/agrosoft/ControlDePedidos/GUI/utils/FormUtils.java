package org.agrosoft.ControlDePedidos.GUI.utils;

import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.utils.CatalogoAbstracto;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

@Slf4j
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

    public static String formateaMensaje(String mensajeOriginal, List<String> detalles) {
        StringBuilder html = new StringBuilder("<HTML><B>" + mensajeOriginal + "</B><BR/>");
        for(String detalle : detalles) {
            html.append("<LI>").append(detalle).append("</LI>");
        }
        html.append("</HTML>");
        return html.toString();
    }

    public static <E extends Enum<E> & CatalogoAbstracto> E getItemByDescription(Class<E> enumClass, String desc) {
        for(E enumConstant : enumClass.getEnumConstants()) {
            if(enumConstant.getDescripcion().equalsIgnoreCase(desc)) {
                return enumConstant;
            }
        }
        return null;
    }

    public static void configuraComboBox(JComboBox<String> comboBox, List<String> items, int defaultIndex) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addAll(items);
        comboBox.setModel(model);
        comboBox.setEditable(false);
        if(defaultIndex >= 0 && defaultIndex < model.getSize()) {
            comboBox.setSelectedIndex(defaultIndex);
        }
    }

    public static float intentaConvertirFloat(String text) {
        float valor = 0F;
        try {
            valor = Float.parseFloat(text);
        } catch (NumberFormatException e) {
            log.error("Exception caught while trying to parse <{}> to float. Returning default 0. Reason: {}", text,
                    e.getMessage());
        }
        return valor;
    }
}
