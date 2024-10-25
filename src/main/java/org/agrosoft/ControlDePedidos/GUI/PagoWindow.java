package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.entity.StatusPago;
import org.agrosoft.ControlDePedidos.API.enums.StatusPagoEnum;
import org.agrosoft.ControlDePedidos.GUI.clients.PagoClient;
import org.agrosoft.ControlDePedidos.GUI.clients.PedidoClient;
import org.agrosoft.ControlDePedidos.GUI.clients.StatusPagoClient;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

@Slf4j
public class PagoWindow extends JFrame {
    private final JFrame thisReference;
    private final JFrame parentForm;
    private final Pedido pedido;
    private float totalPago;

    private JTable tblPagos;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JButton btnAgregar;
    private JButton btnBorrar;
    private JButton btnRegresar;
    private JLabel lblTotal;
    private JLabel lblStatusPago;

    public PagoWindow(JFrame parent, Pedido pedido) {
        this.thisReference = this;
        this.parentForm = parent;
        this.pedido = pedido;
        this.totalPago = 0F;
        log.info("PagoWindow opened");
        this.setTitle("Control de Pagos del pedido: <" + pedido.getIdPedido() + ">");
        this.setSize(600, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.llenaPagos();

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
        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borraPago();
            }
        });
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("Showing form to add a new Pago");
                JFrame agregarPago = new AgregarPagoWindow(thisReference, pedido);
                agregarPago.setVisible(true);
                thisReference.setVisible(false);
            }
        });
    }

    private void borraPago() {
        int idParaBorrar;
        try {
            idParaBorrar = Integer.parseInt(this.tblPagos.getModel()
                    .getValueAt(this.tblPagos.getSelectedRow(), 3).toString());
            if (PagoClient.deletePago(idParaBorrar) > 0) {
                JOptionPane.showMessageDialog(this, "Se borró exitosamente el pago",
                        "¡Pago borrado!", JOptionPane.INFORMATION_MESSAGE);
                this.llenaPagos();
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al intentar borrar el " +
                                "pago. Valide que haya seleccionado un pago.",
                        "Error al borrar el pago",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            log.error("Exception caught while deleting Pago: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Hubo un error al intentar borrar el " +
                            "pago. Valide que haya seleccionado un pago.",
                    "Error al borrar el pago",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenaPagos() {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.totalPago = 0F;
        dtm.setColumnIdentifiers(new String[]{"Fecha", "Monto", "No. de referencia", "id"});
        for (Pago p : PagoClient.fethByPedido(this.pedido.getIdPedido())) {
            dtm.addRow(new Object[]{p.getFechaPago(), FormUtils.formateaDinero(p.getMontoPago()),
                    p.getNumeroReferencia(), p.getIdPago()});
            this.totalPago += p.getMontoPago();
        }
        this.tblPagos.setModel(dtm);
        this.tblPagos.removeColumn(this.tblPagos.getColumnModel().getColumn(3));
        FormUtils.redimensionarTabla(this.tblPagos);
        this.lblTotal.setText("Total: " + FormUtils.formateaDinero(this.totalPago));
        this.validarStatusPago();
    }

    private void validarStatusPago() {
        log.info("Validating if actual StatusPago is correct");
        StatusPagoEnum statusPago;
        if (this.totalPago > 0) {
            if (this.totalPago >= this.pedido.getMontoTotal()) {
                statusPago = StatusPagoEnum.PAGADO;
            } else {
                statusPago = StatusPagoEnum.APARTADO;
            }
        } else {
            statusPago = StatusPagoEnum.PENDIENTE;
        }
        if (!this.pedido.getStatusPago().getDescripcion().equalsIgnoreCase(statusPago.getDescripcion())) {
            log.info("Updating StatusPago due to new Pagos registered");
            StatusPago statusPagoEntity = StatusPagoClient.fetchByItem(statusPago);
            if (Objects.nonNull(statusPagoEntity)) {
                this.pedido.setStatusPago(statusPagoEntity);
                if (PedidoClient.updatePedido(this.pedido) < 0) {
                    log.error("Exception while trying to update StatusPago for pedido with id <{}>",
                            this.pedido.getIdPedido());
                    JOptionPane.showMessageDialog(this, "Hubo un error al intentar actualizar el " +
                                    "status de pago del pedido. Revise que el pedido tenga status correcto",
                            "Error al actualizar pedido", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            log.info("No update for StatusPago required");
        }
        this.lblStatusPago.setText("Estatus de pago del pedido: " + this.pedido.getStatusPago().getDescripcion());
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.llenaPagos();
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
        contentPane.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, new GridConstraints(1, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblPagos = new JTable();
        tblPagos.setAutoResizeMode(0);
        scrollPane.setViewportView(tblPagos);
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnAgregar = new JButton();
        btnAgregar.setText("Agregar");
        contentPane.add(btnAgregar, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnBorrar = new JButton();
        btnBorrar.setText("Borrar");
        contentPane.add(btnBorrar, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 200), null, null, 0, false));
        lblTotal = new JLabel();
        lblTotal.setText("Label");
        contentPane.add(lblTotal, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblStatusPago = new JLabel();
        lblStatusPago.setText("Label");
        contentPane.add(lblStatusPago, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
