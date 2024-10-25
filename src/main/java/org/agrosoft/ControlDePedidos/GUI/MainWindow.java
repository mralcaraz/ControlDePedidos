package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.GUI.clients.PedidoClient;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

@Slf4j
public class MainWindow extends JFrame {
    private JFrame thisReference;

    private JPanel contentPane;
    private JTable tblPedidos;
    private JButton btnClientes;
    private JButton btnVerPagos;
    private JButton btnProductos;
    private JButton btnCrearPedido;
    private JButton btnSalir;
    private JScrollPane scrollPane;
    private JButton btnModificarPedido;

    private void llenaPedidos() {
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dtm.setColumnIdentifiers(new String[]{"No. Pedido", "Estatus pedido", "Fecha pedido", "No. Guía",
                "Monto total", "Monto envío", "Cliente", "Estatus pago", "Estatus logística", "Tipo envío",
                "Está activo", "No. de productos"});
        for (Pedido p : PedidoClient.fetchAll()) {
            dtm.addRow(new Object[]{p.getIdPedido(), p.getStatusPedido().getDescripcion(), p.getFechaPedido(),
                    p.getNumeroGuia(), FormUtils.formateaDinero(p.getMontoTotal()),
                    FormUtils.formateaDinero(p.getMontoEnvio()),
                    RequestUtils.concatenaNombre(p.getCliente()), p.getStatusPago().getDescripcion(),
                    p.getStatusLogistica().getDescripcion(), p.getTipoEnvio().getDescripcion(),
                    (p.isActive() ? "Sí" : "No"), p.getProductos().size()
            });
        }
        if (dtm.getRowCount() > 0) {
            this.btnVerPagos.setEnabled(true);
            this.btnModificarPedido.setEnabled(true);
        }
        this.tblPedidos.setModel(dtm);
        FormUtils.redimensionarTabla(this.tblPedidos);
    }

    public MainWindow() {
        this.thisReference = this;
        log.info("MainWindow opened");
        this.setTitle("Control de pedidos");
        this.setSize(1000, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.llenaPedidos();


        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame clienteWindow = new ClienteWindow(thisReference);
                clienteWindow.setVisible(true);
                thisReference.setVisible(false);
            }
        });
        btnVerPagos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tblPedidos.getValueAt(tblPedidos.getSelectedRow(), 0).toString());
                    Pedido pedido = PedidoClient.fetchById(id);
                    if (Objects.isNull(pedido)) {
                        throw new ReadPropertyException("Unable to read Pedido id");
                    }
                    JFrame pagoWindow = new PagoWindow(thisReference, pedido);
                    pagoWindow.setVisible(true);
                    thisReference.setVisible(false);
                } catch (Exception ex) {
                    log.error("Exception caught while trying to create PagoWindow view: {}", ex.getMessage());
                    JOptionPane.showMessageDialog(thisReference, "Hubo un error al intentar cargar los pagos " +
                                    "del pedido. Seleccione un pedido e intente nuevamente",
                            "Error al cargar pagos", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame productoWindow = new ProductoWindow(thisReference);
                productoWindow.setVisible(true);
                thisReference.setVisible(false);
            }
        });
        btnCrearPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame agregarPedidoWindow = new AgregarPedidoWindow(thisReference);
                agregarPedidoWindow.setVisible(true);
                thisReference.setVisible(false);
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.llenaPedidos();
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
        contentPane.setLayout(new GridLayoutManager(14, 5, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(2, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnClientes = new JButton();
        btnClientes.setText("Clientes");
        contentPane.add(btnClientes, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnVerPagos = new JButton();
        btnVerPagos.setEnabled(false);
        btnVerPagos.setText("Ver Pagos");
        contentPane.add(btnVerPagos, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnProductos = new JButton();
        btnProductos.setText("Productos");
        contentPane.add(btnProductos, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCrearPedido = new JButton();
        btnCrearPedido.setText("Crear Pedido");
        contentPane.add(btnCrearPedido, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(11, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnSalir = new JButton();
        btnSalir.setText("Salir");
        contentPane.add(btnSalir, new GridConstraints(12, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(13, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        scrollPane = new JScrollPane();
        scrollPane.putClientProperty("html.disable", Boolean.FALSE);
        contentPane.add(scrollPane, new GridConstraints(1, 1, 12, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(750, -1), null, null, 0, false));
        tblPedidos = new JTable();
        tblPedidos.setAutoResizeMode(0);
        tblPedidos.setUpdateSelectionOnSort(true);
        scrollPane.setViewportView(tblPedidos);
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 200), null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        contentPane.add(spacer10, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnModificarPedido = new JButton();
        btnModificarPedido.setEnabled(false);
        btnModificarPedido.setText("Modificar Pedido");
        contentPane.add(btnModificarPedido, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
