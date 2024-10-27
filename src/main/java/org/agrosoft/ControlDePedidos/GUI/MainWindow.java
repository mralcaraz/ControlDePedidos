package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.enums.StatusPedidoEnum;
import org.agrosoft.ControlDePedidos.GUI.clients.PedidoClient;
import org.agrosoft.ControlDePedidos.GUI.enums.PedidosViewEnum;
import org.agrosoft.ControlDePedidos.GUI.enums.TipoEnvioEnum;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MainWindow extends JFrame {
    private final JFrame thisReference;
    private PedidosViewEnum tipoPedido;
    private TipoEnvioEnum tipoEnvio;

    private JPanel contentPane;
    private JTable tblPedidos;
    private JButton btnClientes;
    private JButton btnVerPagos;
    private JButton btnProductos;
    private JButton btnCrearPedido;
    private JButton btnSalir;
    private JScrollPane scrollPane;
    private JButton btnVerDetalles;
    private JLabel lblFiltros;
    private JComboBox<String> cbxTipoEnvio;
    private JComboBox<String> cbxTipoPedido;
    private JButton btnAplicarFiltros;

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
        this.tipoPedido = PedidosViewEnum.SOLO_ACTIVOS;
        this.tipoEnvio = TipoEnvioEnum.TODOS;
        this.llenaComboboxes();
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
                verPagos();
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
        btnAplicarFiltros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                llenaPedidos();
            }
        });
        btnVerDetalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verDetalles();
            }
        });
    }

    private void verDetalles() {
        try {
            int id = Integer.parseInt(this.tblPedidos.getValueAt(this.tblPedidos.getSelectedRow(), 0).toString());
            Pedido pedido = PedidoClient.fetchById(id);
            if (Objects.isNull(pedido)) {
                throw new ReadPropertyException("Unable to read Pedido id");
            }
            JFrame verDetalles = new ModificarPedidoWindow(thisReference, pedido);
            verDetalles.setVisible(true);
            thisReference.setVisible(false);
        } catch (Exception ex) {
            log.error("Exception caught while trying to create ModificarPedidoWindow view: {}", ex.getMessage());
            JOptionPane.showMessageDialog(thisReference, "Hubo un error al intentar cargar los detalles " +
                            "del pedido. Seleccione un pedido e intente nuevamente",
                    "Error al cargar detalles", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verPagos() {
        try {
            int id = Integer.parseInt(this.tblPedidos.getValueAt(this.tblPedidos.getSelectedRow(), 0).toString());
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

    private void llenaComboboxes() {
        FormUtils.configuraComboBox(this.cbxTipoPedido, Arrays.stream(PedidosViewEnum.values())
                .map(PedidosViewEnum::getDescripcion)
                .toList(), 1);

        FormUtils.configuraComboBox(this.cbxTipoEnvio, Arrays.stream(TipoEnvioEnum.values())
                .map(TipoEnvioEnum::getDescripcion)
                .toList(), 0);
    }

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
        for (Pedido p : this.fetchByParameters()) {
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
            this.btnVerDetalles.setEnabled(true);
        }
        this.tblPedidos.setModel(dtm);
        FormUtils.redimensionarTabla(this.tblPedidos);
    }

    private List<Pedido> fetchByParameters() {
        try {
            this.tipoEnvio = FormUtils.getItemByDescription(TipoEnvioEnum.class, this.cbxTipoEnvio.getSelectedItem()
                    .toString());
            this.tipoPedido = FormUtils.getItemByDescription(PedidosViewEnum.class, this.cbxTipoPedido.getSelectedItem()
                    .toString());
            return PedidoClient.fetchAll().stream()
                    .filter(pedido -> {
                        if (Objects.isNull(this.tipoPedido)) {
                            return true;
                        }
                        return switch (this.tipoPedido) {
                            case SOLO_ACTIVOS -> pedido.isActive();
                            case SOLO_INACTIVOS -> !pedido.isActive();
                            case ACTIVOS_Y_PENDIENTES -> pedido.isActive() && !pedido.getStatusPedido().getDescripcion()
                                    .equalsIgnoreCase(StatusPedidoEnum.FINALIZADO.getDescripcion());
                            case ACTIVOS_Y_TERMINADOS -> pedido.isActive() && pedido.getStatusPedido().getDescripcion()
                                    .equalsIgnoreCase(StatusPedidoEnum.FINALIZADO.getDescripcion());
                            default -> true;
                        };
                    })
                    .filter(pedido -> {
                        if (Objects.isNull(this.tipoEnvio)) {
                            return true;
                        }
                        return switch (this.tipoEnvio) {
                            case METROPOLITANO -> pedido.getTipoEnvio().getDescripcion()
                                    .equalsIgnoreCase(TipoEnvioEnum.METROPOLITANO.getDescripcion());
                            case FORANEO -> pedido.getTipoEnvio().getDescripcion()
                                    .equalsIgnoreCase(TipoEnvioEnum.FORANEO.getDescripcion());
                            case BOSQUES -> pedido.getTipoEnvio().getDescripcion()
                                    .equalsIgnoreCase(TipoEnvioEnum.BOSQUES.getDescripcion());
                            case CASA -> pedido.getTipoEnvio().getDescripcion()
                                    .equalsIgnoreCase(TipoEnvioEnum.CASA.getDescripcion());
                            default -> true;
                        };
                    })
                    .toList();
        } catch (Exception e) {
            log.error("Exception caugh while filtering list of Cliente for display in main window", e);
            return PedidoClient.fetchAll();
        }
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
        contentPane.setLayout(new GridLayoutManager(16, 8, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(2, 5, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnClientes = new JButton();
        btnClientes.setText("Clientes");
        contentPane.add(btnClientes, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnVerPagos = new JButton();
        btnVerPagos.setEnabled(false);
        btnVerPagos.setText("Ver Pagos");
        contentPane.add(btnVerPagos, new GridConstraints(4, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(5, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnProductos = new JButton();
        btnProductos.setText("Productos");
        contentPane.add(btnProductos, new GridConstraints(6, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnCrearPedido = new JButton();
        btnCrearPedido.setText("Crear Pedido");
        contentPane.add(btnCrearPedido, new GridConstraints(8, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(11, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnSalir = new JButton();
        btnSalir.setText("Salir");
        contentPane.add(btnSalir, new GridConstraints(12, 6, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(14, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(7, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        scrollPane = new JScrollPane();
        scrollPane.putClientProperty("html.disable", Boolean.FALSE);
        contentPane.add(scrollPane, new GridConstraints(1, 1, 12, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(750, -1), null, null, 0, false));
        tblPedidos = new JTable();
        tblPedidos.setAutoResizeMode(0);
        tblPedidos.setUpdateSelectionOnSort(true);
        scrollPane.setViewportView(tblPedidos);
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 200), null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        contentPane.add(spacer10, new GridConstraints(9, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        btnVerDetalles = new JButton();
        btnVerDetalles.setEnabled(false);
        btnVerDetalles.setText("Ver Detalles");
        contentPane.add(btnVerDetalles, new GridConstraints(10, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        contentPane.add(spacer11, new GridConstraints(13, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lblFiltros = new JLabel();
        lblFiltros.setText("Filtros");
        contentPane.add(lblFiltros, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxTipoPedido = new JComboBox();
        contentPane.add(cbxTipoPedido, new GridConstraints(14, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxTipoEnvio = new JComboBox();
        contentPane.add(cbxTipoEnvio, new GridConstraints(14, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAplicarFiltros = new JButton();
        btnAplicarFiltros.setText("Aplicar");
        contentPane.add(btnAplicarFiltros, new GridConstraints(14, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(100, -1), 0, false));
        final Spacer spacer12 = new Spacer();
        contentPane.add(spacer12, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
