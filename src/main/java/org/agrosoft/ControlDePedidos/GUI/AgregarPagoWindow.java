package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.GUI.clients.PagoClient;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Slf4j
public class AgregarPagoWindow extends JFrame {
    private final JFrame thisReference;
    private final JFrame parentForm;
    private final Pedido pedido;
    private static final String placeholder = "Formato: yyyy-mm-dd";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JPanel contentPane;
    private JLabel lblMonto;
    private JLabel lblFecha;
    private JLabel lblReferencia;
    private JTextField txtMonto;
    private JTextField txtFecha;
    private JTextField txtReferencia;
    private JButton btnAgregar;
    private JButton btnRegresar;
    private JButton btnHoy;


    public AgregarPagoWindow(JFrame parent, Pedido pedido) {
        this.thisReference = this;
        this.parentForm = parent;
        this.pedido = pedido;
        log.info("AgregarPagoWindow opened");
        this.setTitle("Agregar pago al pedido: <" + pedido.getIdPedido() + ">");
        this.setSize(450, 300);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.txtFecha.setText(placeholder);
        this.txtFecha.setForeground(Color.GRAY);


        btnRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.nonNull(parentForm)) {
                    parentForm.setVisible(true);
                    setVisible(false);
                    log.info("Returning to parent form");
                    dispose();
                } else {
                    log.info("No parent form found. Exit with code 1");
                    System.exit(1);
                }
            }
        });
        txtFecha.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtFecha.getText().equals(placeholder)) {
                    txtFecha.setText("");
                    txtFecha.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtFecha.getText().isBlank()) {
                    txtFecha.setText(placeholder);
                    txtFecha.setForeground(Color.GRAY);
                }
            }
        });
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardaPago();
            }
        });
        btnHoy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate hoy = LocalDate.now();
                txtFecha.setForeground(Color.WHITE);
                txtFecha.setText(hoy.format(formatter));
            }
        });
    }

    private void guardaPago() {
        LocalDate fechaPago = null;
        float montoPago = 0F;
        try {
            fechaPago = LocalDate.parse(this.txtFecha.getText(), formatter);
            montoPago = Float.parseFloat(this.txtMonto.getText());
            if (montoPago <= 0F) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor que 0.00",
                        "Error al registrar pago", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            if (e instanceof DateTimeParseException) {
                log.error("Exception caught while converting FechaPago: {}", e.getMessage());
                JOptionPane.showMessageDialog(this, "Error al convertir fecha. Por favor use el " +
                        "formato yyyy-mm-dd", "Error al convertir fecha", JOptionPane.ERROR_MESSAGE);
            } else {
                log.error("Exception caught while converting monto: {}", e.getMessage());
                JOptionPane.showMessageDialog(this, "Error al convertir monto de pago. Por " +
                        "favor valide que sea un número", "Error al convertir pago", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        Pago pago = Pago.builder()
                .montoPago(montoPago)
                .fechaPago(fechaPago)
                .pedido(this.pedido)
                .numeroReferencia(this.txtReferencia.getText().isBlank() ? null : this.txtReferencia.getText())
                .build();
        switch (PagoClient.savePago(pago)) {
            case -1:
                JOptionPane.showMessageDialog(this, "Hubo un error al validar los datos " +
                                "ingresados y no se pudo crear el pago. Favor de validar los datos del pago",
                        "Error al crear el pago", JOptionPane.WARNING_MESSAGE);
                break;
            case -2:
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar llamar a la" +
                                " base de datos. Intente más tarde", "Error al crear el pago",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case -3:
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar llamar a " +
                                "los servicios requeridos", "Error al crear el pago",
                        JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Se creó exitosamente el pago",
                        "¡Pago creado!", JOptionPane.INFORMATION_MESSAGE);
                txtMonto.setText("");
                txtFecha.setText(placeholder);
                txtFecha.setForeground(Color.GRAY);
                txtReferencia.setText("");
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(9, 7, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        lblMonto = new JLabel();
        lblMonto.setText("Monto:");
        contentPane.add(lblMonto, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblFecha = new JLabel();
        lblFecha.setText("Fecha:");
        contentPane.add(lblFecha, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblReferencia = new JLabel();
        lblReferencia.setText("Referencia");
        contentPane.add(lblReferencia, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        txtMonto = new JTextField();
        contentPane.add(txtMonto, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtFecha = new JTextField();
        contentPane.add(txtFecha, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtReferencia = new JTextField();
        contentPane.add(txtReferencia, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnAgregar = new JButton();
        btnAgregar.setText("Agregar");
        contentPane.add(btnAgregar, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnHoy = new JButton();
        btnHoy.setText("Hoy");
        contentPane.add(btnHoy, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
