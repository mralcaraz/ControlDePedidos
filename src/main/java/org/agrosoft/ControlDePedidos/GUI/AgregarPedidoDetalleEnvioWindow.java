package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.dto.PedidoDetalleDTO;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.entity.PedidoDetalle;
import org.agrosoft.ControlDePedidos.GUI.clients.PedidoClient;
import org.agrosoft.ControlDePedidos.GUI.clients.PedidoDetalleClient;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

@Slf4j
public class AgregarPedidoDetalleEnvioWindow extends JFrame {
    private final Pedido pedido;
    private final JFrame parentForm;
    private final JFrame thisReference;

    private JPanel contentPane;
    private JTable tblDirecciones;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtNotas;
    private JButton btnGuardar;
    private JButton btnRegresar;
    private JScrollPane scrollPane;
    private JLabel lblTelefono;
    private JLabel lblDireccion;
    private JLabel lblNotas;
    private JLabel lblInstrucciones;
    private JButton btnBorrarDireccion;

    public AgregarPedidoDetalleEnvioWindow(JFrame parent, Pedido pedido) {
        this.parentForm = parent;
        log.info("AgregarPedidoDetalleEnvioWindow opened");
        this.thisReference = this;
        this.pedido = pedido;
        this.setTitle("Agregar detalles de envío a Pedido <" + pedido.getIdPedido() + ">");
        this.setSize(800, 400);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.lblInstrucciones.setForeground(Color.LIGHT_GRAY);
        this.lblInstrucciones.setText("<HTML><P>" +
                "Seleccione la dirección que desea asociar al<BR/>" +
                "pedido, o bien, teclee una nueva</P><P>" +
                "También puede borrar las direcciones que ya<BR/>" +
                "No utilice, seleccionándolas y dando clic en<BR/>" +
                "El botón 'Borrar dirección'<BR/>&nbsp;" +
                "</P></HTML>");
        this.llenaDireccion();
        this.llenaDatos();

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
        this.tblDirecciones.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = tblDirecciones.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = Integer.parseInt(tblDirecciones.getModel()
                            .getValueAt(tblDirecciones.getSelectedRow(), 0).toString());
                    PedidoDetalleDTO detalleDTO = PedidoDetalleClient.fetchById(id);
                    if (Objects.nonNull(detalleDTO)) {
                        txtDireccion.setText(detalleDTO.getDireccion());
                        txtTelefono.setText(detalleDTO.getTelefono());
                        txtNotas.setText(detalleDTO.getNota());
                    }
                }
            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtDireccion.getText().isBlank() || txtTelefono.getText().isBlank()) {
                    JOptionPane.showMessageDialog(thisReference, "Los campos Dirección y Teléfono son " +
                            "requeridos", "Error al crear dirección de envío", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                guardarDireccion();
                btnRegresar.doClick();
            }
        });
        btnBorrarDireccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarDireccion();
            }
        });
    }

    private void borrarDireccion() {
        int selectedRow = tblDirecciones.getSelectedRow();
        try {
            if (selectedRow >= 0) {
                log.info("Delete PedidoDetalle selected. Checking for active Pedidos");
                int id = Integer.parseInt(tblDirecciones.getModel()
                        .getValueAt(tblDirecciones.getSelectedRow(), 0).toString());
                PedidoDetalleDTO detalleDTO = PedidoDetalleClient.fetchById(id);
                Pedido pedidoAsociado = PedidoClient.fetchById(detalleDTO.getNumeroPedido());
                if (pedidoAsociado.isActive()) {
                    log.info("PedidoDetalle with active Pedido found. Asking for confirmation");
                    int resultado = JOptionPane.showConfirmDialog(thisReference, "La dirección que está " +
                                    "intentando borrar está asociada a un pedido que aún está activo. Si la borra," +
                                    "el pedido perderá la información de entrega. " +
                                    "¿Realmente desea borrar la dirección?", "Confirmación de borrado",
                            JOptionPane.YES_NO_OPTION);
                    if (resultado == JOptionPane.YES_OPTION) {
                        log.info("Confirmation received. Trying to delete record");
                        borraDireccionEnvio(detalleDTO.getId());
                    } else {
                        log.info("Confirmation rejected.");
                    }
                } else {
                    log.info("No active Pedido found. Trying to delete record");
                    borraDireccionEnvio(detalleDTO.getId());
                }
            }
        } catch (Exception e) {
            log.error("Exception caught while trying to delete PedidoDetalle from database");
            JOptionPane.showMessageDialog(thisReference, "Hubo un error al intentar borrar" +
                    " la dirección de envío. Por favor intente nuevamente", "Error al " +
                    "borrar dirección de envío", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void borraDireccionEnvio(int idParaBorrar) {
        if (PedidoDetalleClient.deletePedidoDetalle(idParaBorrar) > 0) {
            JOptionPane.showMessageDialog(thisReference, "Se borró correctamente la dirección" +
                            " de envío", "¡Dirección de envío borrada!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(thisReference, "Hubo un error al intentar borrar" +
                    " la dirección de envío. Por favor intente nuevamente", "Error al " +
                    "borrar dirección de envío", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarDireccion() {
        log.info("Creating PedidoDetalle for Pedido<{}>. Verifying if Pedido already has PedidoDetalle",
                this.pedido.getIdPedido());
        PedidoDetalleDTO borrable = PedidoDetalleClient.fetchByPedidoId(this.pedido.getIdPedido());
        if (Objects.nonNull(borrable)) {
            log.info("PedidoDetalle found. Trying to delete PedidoDetalle");
            if (PedidoDetalleClient.deletePedidoDetalle(borrable.getId()) > 0) {
                log.info("PedidoDetalle deleted. Saving new  PedidoDetalle");
                this.creaDireccion();
            } else {
                log.error("PedidoDetalle cannot be deleted");
                JOptionPane.showMessageDialog(thisReference, "Hubo un error al intentar actualizar el " +
                        "detalle de envío del Pedido. Por favor intente nuevamente", "Error al " +
                        "actualizar detalle de envío del Pedido", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            log.info("No PedidoDetalle found. Creating new PedidoDetalle");
            this.creaDireccion();
        }
    }

    private void creaDireccion() {
        PedidoDetalle pedidoDetalle = PedidoDetalle.builder()
                .telefono(this.txtTelefono.getText())
                .direccion(this.txtDireccion.getText())
                .notas(this.txtNotas.getText())
                .pedido(this.pedido)
                .build();
        if (PedidoDetalleClient.savePedidoDetalle(pedidoDetalle) > 0) {
            JOptionPane.showMessageDialog(thisReference, "Se actualizó correctamente el detalle de envío" +
                    "del pedido", "¡Detalle de envío actualizado!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            log.error("PedidoDetalle cannot be created");
            JOptionPane.showMessageDialog(thisReference, "Hubo un error al intentar actualizar el " +
                    "detalle de envío del Pedido. Por favor intente nuevamente", "Error al " +
                    "actualizar detalle de envío del Pedido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenaDireccion() {
        PedidoDetalleDTO detalleDTO = PedidoDetalleClient.fetchByPedidoId(this.pedido.getIdPedido());
        if (Objects.nonNull(detalleDTO)) {
            this.txtDireccion.setText(detalleDTO.getDireccion());
            this.txtTelefono.setText(detalleDTO.getTelefono());
            this.txtNotas.setText(detalleDTO.getNota());
        }
    }

    private void llenaDatos() {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dtm.setColumnIdentifiers(new String[]{"Id", "Dirección", "Teléfono", "Nota"});
        for (PedidoDetalleDTO d : PedidoDetalleClient.fetchAll()) {
            dtm.addRow(new Object[]{d.getId(), d.getDireccion(), d.getTelefono(), d.getNota()});
        }
        this.tblDirecciones.setModel(dtm);
        this.tblDirecciones.removeColumn(this.tblDirecciones.getColumnModel().getColumn(0));
        FormUtils.redimensionarTabla(this.tblDirecciones);
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
        contentPane.setLayout(new GridLayoutManager(9, 8, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, new GridConstraints(1, 1, 7, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblDirecciones = new JTable();
        tblDirecciones.setAutoResizeMode(0);
        scrollPane.setViewportView(tblDirecciones);
        lblTelefono = new JLabel();
        lblTelefono.setText("Teléfono:");
        contentPane.add(lblTelefono, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, new Dimension(20, 20), 0, false));
        lblDireccion = new JLabel();
        lblDireccion.setText("Dirección:");
        contentPane.add(lblDireccion, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, new Dimension(20, 20), 0, false));
        lblNotas = new JLabel();
        lblNotas.setText("Notas:");
        contentPane.add(lblNotas, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTelefono = new JTextField();
        contentPane.add(txtTelefono, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtDireccion = new JTextField();
        contentPane.add(txtDireccion, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtNotas = new JTextField();
        contentPane.add(txtNotas, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        contentPane.add(btnGuardar, new GridConstraints(5, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(7, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, new Dimension(20, 20), 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(3, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(300, -1), null, null, 0, false));
        lblInstrucciones = new JLabel();
        lblInstrucciones.setText(".");
        contentPane.add(lblInstrucciones, new GridConstraints(1, 4, 1, 3, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnBorrarDireccion = new JButton();
        btnBorrarDireccion.setText("Borrar Direccion");
        contentPane.add(btnBorrarDireccion, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
