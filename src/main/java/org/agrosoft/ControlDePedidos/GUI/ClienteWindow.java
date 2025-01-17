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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ClienteWindow extends JFrame {
    private final JFrame parentForm;
    private final JFrame thisReference;

    private JPanel contentPane;
    private JTable tblClientes;
    private JScrollPane scrollPane;
    private JButton btnAgregar;
    private JButton btnBorrar;
    private JButton btnRegresar;
    private JButton btnEditar;

    public ClienteWindow(JFrame parent) {
        this.parentForm = parent;
        log.info("ClienteWindow opened");
        this.thisReference = this;
        this.setTitle("Clientes");
        this.setSize(700, 400);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);

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
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("Showing form to add a new Cliente");
                JFrame crearCliente = new AgregarClienteWindow(thisReference, Optional.empty());
                crearCliente.setVisible(true);
                thisReference.setVisible(false);
            }
        });
        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblClientes.getSelectedRow() < 0) {
                    return;
                }
                log.info("Trying to delete Cliente with id <{}>", tblClientes
                        .getValueAt(tblClientes.getSelectedRow(), 0).toString());
                borrarCliente();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblClientes.getSelectedRow() < 0) {
                    return;
                }
                int idCliente = Integer
                        .parseInt(tblClientes.getValueAt(tblClientes.getSelectedRow(), 0).toString());
                JFrame modificarCliente = new AgregarClienteWindow(thisReference, Optional
                        .of(ClienteClient.fethById(idCliente)));
                modificarCliente.setVisible(true);
                thisReference.setVisible(false);
            }
        });
    }

    private void borrarCliente() {
        int idParaBorrar;
        try {
            idParaBorrar = Integer.parseInt(tblClientes.getValueAt(tblClientes.getSelectedRow(), 0).toString());
            if (ClienteClient.deleteCliente(idParaBorrar) > 0) {
                JOptionPane.showMessageDialog(this, "Se borró exitosamente el cliente",
                        "¡Cliente borrado!", JOptionPane.INFORMATION_MESSAGE);
                this.llenaClientes();
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar borrar el " +
                                "cliente. Valide que el cliente no haya registrado ningún pedido.",
                        "Error al borrar el cliente",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            log.error("Exception caught while deleting Cliente: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Hubo un error al intentar borrar el " +
                            "cliente. Valide que el cliente no haya registrado ningún pedido.",
                    "Error al borrar el cliente",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenaClientes() {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dtm.setColumnIdentifiers(new String[]{"No. Cliente", "Nombre", "Primer apellido", "Segundo apellido",
                "Contacto"});
        for (Cliente c : ClienteClient.fetchAll()) {
            dtm.addRow(new Object[]{Integer.toString(c.getIdCliente()), c.getNombre(), c.getPrimerApellido(), c.getSegundoApellido(),
                    c.getContacto()});
        }
        this.tblClientes.setModel(dtm);
        FormUtils.redimensionarTabla(this.tblClientes);
        if (dtm.getRowCount() == 0) {
            this.btnBorrar.setEnabled(false);
        } else {
            this.btnBorrar.setEnabled(true);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.llenaClientes();
        }
        super.setVisible(visible);
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
        contentPane.setLayout(new GridLayoutManager(11, 5, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, new GridConstraints(1, 1, 9, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblClientes = new JTable();
        tblClientes.setAutoResizeMode(0);
        scrollPane.setViewportView(tblClientes);
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, new Dimension(20, 20), 0, false));
        btnAgregar = new JButton();
        btnAgregar.setText("Nuevo");
        contentPane.add(btnAgregar, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 40), null, null, 0, false));
        btnBorrar = new JButton();
        btnBorrar.setText("Borrar");
        contentPane.add(btnBorrar, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, new Dimension(20, 20), 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, new Dimension(20, 20), 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnEditar = new JButton();
        btnEditar.setText("Editar");
        contentPane.add(btnEditar, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
