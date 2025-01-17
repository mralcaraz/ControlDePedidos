package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Cliente;
import org.agrosoft.ControlDePedidos.GUI.clients.ClienteClient;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AgregarClienteWindow extends JFrame {
    private final JFrame parentForm;
    private JPanel contentPane;
    private JLabel lblNombre;
    private JLabel lblPrimerApellido;
    private JLabel lblSegundoApellido;
    private JLabel lblContacto;
    private JTextField txtNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtContacto;
    private JButton btnGuardar;
    private JButton btnRegresar;
    private Optional<Cliente> optionalCliente;

    public AgregarClienteWindow(JFrame parent, Optional<Cliente> optionalCliente) {
        this.parentForm = parent;
        log.info("AgregarClienteWindow opened");
        this.setTitle("Agregar Cliente");
        this.setSize(500, 400);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.optionalCliente = optionalCliente;

        if (this.optionalCliente.isPresent()) {
            this.setTitle("Modificar cliente");
            this.txtNombre.setText(this.optionalCliente.get().getNombre());
            this.txtPrimerApellido.setText(this.optionalCliente.get().getPrimerApellido());
            this.txtSegundoApellido.setText(this.optionalCliente.get().getSegundoApellido());
            this.txtContacto.setText(this.optionalCliente.get().getContacto());
            this.txtContacto.setEditable(false);
        }

        btnRegresar.addActionListener(actionEvent -> {
            if (Objects.nonNull(parentForm)) {
                parentForm.setVisible(true);
                setVisible(false);
                log.info("Returning to parent form");
                dispose();
            } else {
                log.info("No parent form found. Exit with code 1");
                System.exit(1);
            }
        });
        btnGuardar.addActionListener(e -> {
            if (optionalCliente.isPresent()) {
                modificarCliente();
            } else {
                crearCliente();
            }
        });
    }

    private void modificarCliente() {
        Cliente cliente = Cliente.builder()
                .idCliente(this.optionalCliente.get().getIdCliente())
                .nombre(this.txtNombre.getText())
                .primerApellido(this.txtPrimerApellido.getText())
                .segundoApellido(this.txtSegundoApellido.getText().isBlank() ? null : this.txtSegundoApellido.getText())
                .contacto(this.txtContacto.getText())
                .build();
        switch (ClienteClient.updateCliente(cliente)) {
            case -1:
                JOptionPane.showMessageDialog(this, "Hubo un error al validar los datos " +
                                "ingresados y no se pudo crear el cliente. Favor de validar los datos del cliente",
                        "Error al crear el cliente", JOptionPane.WARNING_MESSAGE);
                break;
            case -2:
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar llamar a la" +
                                " base de datos. Intente más tarde", "Error al crear el cliente",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case -3:
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar llamar a " +
                                "los servicios requeridos", "Error al crear el cliente",
                        JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Se creó exitosamente el cliente",
                        "¡Cliente creado!", JOptionPane.INFORMATION_MESSAGE);
                this.btnRegresar.doClick();
        }
    }

    private void crearCliente() {
        Cliente cliente = Cliente.builder()
                .nombre(this.txtNombre.getText())
                .primerApellido(this.txtPrimerApellido.getText())
                .segundoApellido(this.txtSegundoApellido.getText().isBlank() ? null : this.txtSegundoApellido.getText())
                .contacto(this.txtContacto.getText())
                .build();
        switch (ClienteClient.saveCliente(cliente)) {
            case -1:
                JOptionPane.showMessageDialog(this, "Hubo un error al validar los datos " +
                                "ingresados y no se pudo crear el cliente. Favor de validar los datos del cliente",
                        "Error al crear el cliente", JOptionPane.WARNING_MESSAGE);
                break;
            case -2:
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar llamar a la" +
                                " base de datos. Intente más tarde", "Error al crear el cliente",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case -3:
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar llamar a " +
                                "los servicios requeridos", "Error al crear el cliente",
                        JOptionPane.ERROR_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Se creó exitosamente el cliente",
                        "¡Cliente creado!", JOptionPane.INFORMATION_MESSAGE);
                txtNombre.setText("");
                txtPrimerApellido.setText("");
                txtSegundoApellido.setText("");
                txtContacto.setText("");
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
        contentPane.setLayout(new GridLayoutManager(11, 7, new Insets(0, 0, 0, 0), -1, -1));
        lblNombre = new JLabel();
        lblNombre.setText("Nombre:");
        contentPane.add(lblNombre, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblPrimerApellido = new JLabel();
        lblPrimerApellido.setText("Primer apellido:");
        contentPane.add(lblPrimerApellido, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblSegundoApellido = new JLabel();
        lblSegundoApellido.setText("Segundo apellido:");
        contentPane.add(lblSegundoApellido, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblContacto = new JLabel();
        lblContacto.setText("Contacto:");
        contentPane.add(lblContacto, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        txtNombre = new JTextField();
        contentPane.add(txtNombre, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtPrimerApellido = new JTextField();
        contentPane.add(txtPrimerApellido, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtSegundoApellido = new JTextField();
        contentPane.add(txtSegundoApellido, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtContacto = new JTextField();
        contentPane.add(txtContacto, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        contentPane.add(btnGuardar, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(9, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        contentPane.add(spacer10, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
