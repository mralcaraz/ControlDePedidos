package org.agrosoft.ControlDePedidos.GUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.entity.*;
import org.agrosoft.ControlDePedidos.API.enums.StatusLogisticaEnum;
import org.agrosoft.ControlDePedidos.API.enums.StatusPagoEnum;
import org.agrosoft.ControlDePedidos.API.enums.StatusPedidoEnum;
import org.agrosoft.ControlDePedidos.API.enums.TipoEnvioEnum;
import org.agrosoft.ControlDePedidos.GUI.clients.*;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Slf4j
public class AgregarPedidoWindow extends JFrame {
    private final JFrame parentForm;
    private final JFrame thisReference;
    private float montoTotal;
    private Cliente cliente;
    private List<Producto> productoList;

    private JPanel contentPane;
    private JTable tblProductos;
    private JScrollPane scrollPane;
    private JLabel lblMontoTotal;
    private JLabel lblCliente;
    private JTextField txtMontoTotal;
    private JTextField txtCliente;
    private JButton btnAgregarCliente;
    private JLabel lblTipoEnvio;
    private JComboBox<String> cbxTipoEnvio;
    private JComboBox<String> cbxProductos;
    private JButton btnAgregarProducto;
    private JButton btnGuardar;
    private JButton btnRegresar;
    private JButton btnQuitarProducto;

    public AgregarPedidoWindow(JFrame parent) {
        this.parentForm = parent;
        log.info("AgregarPedidoWindow opened");
        this.thisReference = this;
        this.setTitle("Agregar pedido");
        this.setSize(800, 500);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        this.montoTotal = 0F;
        this.cliente = null;
        this.productoList = new ArrayList<>();
        this.llenaProductos();
        this.llenaComboBoxes();

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

        btnAgregarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("Showing window to set Cliente for Pedido");
                JFrame agregarCliente = new AgregarClienteAPedidoWindow(thisReference, cliente, (selectedCliente) -> {
                    cliente = selectedCliente;
                });
                agregarCliente.setVisible(true);
                thisReference.setVisible(false);
            }
        });
        btnAgregarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregaProducto();
            }
        });
        btnQuitarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarProducto();
            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearPedido();
            }
        });
    }

    private void crearPedido() {
        List<String> validaciones = new ArrayList<>();
        if (Objects.isNull(this.cliente)) {
            validaciones.add("Debe seleccionar el cliente del pedido");
        }
        if (this.productoList.isEmpty()) {
            validaciones.add("Debe seleccionar al menos un producto para el pedido");
        }
        if (this.cbxTipoEnvio.getSelectedIndex() == -1) {
            validaciones.add("Debe seleccionar el tipo de envío del pedido");
        }
        if (!validaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, FormUtils.formateaMensaje("El pedido está" +
                    " incompleto", validaciones), "Error al crear el pedido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            StatusPedido statusPedidoDefault = StatusPedidoClient.fetchByItem(StatusPedidoEnum.EN_PROCESO);
            StatusLogistica statusLogisticaDefualt = StatusLogisticaClient.fetchByItem(StatusLogisticaEnum.PENDIENTE);
            StatusPago statusPagoDefault = StatusPagoClient.fetchByItem(StatusPagoEnum.PENDIENTE);
            TipoEnvioEnum item = FormUtils.getItemByDescription(TipoEnvioEnum.class, this.cbxTipoEnvio.getSelectedItem()
                    .toString());
            TipoEnvio tipoEnvio = TipoEnvioClient.fetchByItem(item);
            Pedido pedido = Pedido.builder()
                    .statusPedido(statusPedidoDefault)
                    .fechaPedido(LocalDate.now())
                    .numeroGuia(null)
                    .montoTotal(this.montoTotal)
                    .montoEnvio(0)
                    .cliente(this.cliente)
                    .statusPago(statusPagoDefault)
                    .statusLogistica(statusLogisticaDefualt)
                    .tipoEnvio(tipoEnvio)
                    .productos(this.productoList)
                    .build();
            if (PedidoClient.savePedido(pedido) > 0) {
                JOptionPane.showMessageDialog(this, "Pedido guardado exitosamente",
                        "Pedido guardado", JOptionPane.INFORMATION_MESSAGE);
                this.productoList.clear();
                this.cliente = null;
                this.llenaProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error guardar el pedido. Revise" +
                                " los datos e intente nuevamente",
                        "Error al crear el pedido", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            log.error("Exception caught while trying to save new Pedido. Reason: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Hubo un error guardar el pedido. Revise" +
                            " los datos e intente nuevamente",
                    "Error al crear el pedido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void quitarProducto() {
        if (tblProductos.getSelectedRow() == -1) {
            return;
        }
        String productoNombre = tblProductos.getValueAt(tblProductos.getSelectedRow(), 0).toString();
        for (int i = 0; i < this.productoList.size(); i++) {
            if (this.productoList.get(i).getNombreProducto().equals(productoNombre)) {
                this.productoList.remove(i);
                break;
            }
        }
        this.llenaProductos();
    }

    private void agregaProducto() {
        try {
            String productoNombre = URLEncoder.encode(this.cbxProductos.getSelectedItem().toString(), StandardCharsets.UTF_8);
            Producto producto = ProductoClient.fetchByName(productoNombre);
            if (Objects.isNull(producto)) {
                JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar agregar el" +
                                "producto. Seleccione nuevamente el producto e inténtelo de nuevo",
                        "Error al seleccionar el cliente", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.productoList.add(producto);
            this.llenaProductos();
        } catch (Exception e) {
            log.error("Exception caught while inserting producto to Pedido: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar agregar el" +
                            "producto. Seleccione nuevamente el producto e inténtelo de nuevo",
                    "Error al seleccionar el producto", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.llenaProductos();
        }
        super.setVisible(visible);
    }

    private void llenaProductos() {
        this.montoTotal = 0F;
        this.productoList.sort(Comparator.comparing(Producto::getNombreProducto));
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Producto", "Precio unitario"});
        for (Producto p : this.productoList) {
            model.addRow(new Object[]{p.getNombreProducto(), FormUtils.formateaDinero(p.getPrecioUnitario())});
            this.montoTotal += p.getPrecioUnitario();
        }
        this.tblProductos.setModel(model);
        FormUtils.redimensionarTabla(this.tblProductos);
        if (Objects.nonNull(this.cliente)) {
            this.txtCliente.setText(RequestUtils.concatenaNombre(this.cliente));
        } else {
            this.txtCliente.setText("");
        }
        this.txtMontoTotal.setText(FormUtils.formateaDinero(this.montoTotal));
    }

    private void llenaComboBoxes() {
        FormUtils.configuraComboBox(this.cbxTipoEnvio, TipoEnvioClient.fetchAll().stream()
                .map(TipoEnvio::getDescripcion)
                .toList(), 1);

        FormUtils.configuraComboBox(this.cbxProductos, ProductoClient.fetchAll().stream()
                .map(Producto::getNombreProducto)
                .toList(), 0);
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
        contentPane.setLayout(new GridLayoutManager(14, 6, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, new GridConstraints(1, 1, 12, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblProductos = new JTable();
        tblProductos.setAutoResizeMode(0);
        scrollPane.setViewportView(tblProductos);
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        lblMontoTotal = new JLabel();
        lblMontoTotal.setText("Monto total");
        contentPane.add(lblMontoTotal, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblCliente = new JLabel();
        lblCliente.setText("Cliente");
        contentPane.add(lblCliente, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        txtMontoTotal = new JTextField();
        txtMontoTotal.setEditable(false);
        contentPane.add(txtMontoTotal, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtCliente = new JTextField();
        txtCliente.setEditable(false);
        contentPane.add(txtCliente, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnAgregarCliente = new JButton();
        btnAgregarCliente.setText("Agregar Cliente");
        contentPane.add(btnAgregarCliente, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTipoEnvio = new JLabel();
        lblTipoEnvio.setText("Tipo de Envío");
        contentPane.add(lblTipoEnvio, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxTipoEnvio = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        cbxTipoEnvio.setModel(defaultComboBoxModel1);
        contentPane.add(cbxTipoEnvio, new GridConstraints(6, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        cbxProductos = new JComboBox();
        contentPane.add(cbxProductos, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(200, -1), 0, false));
        btnAgregarProducto = new JButton();
        btnAgregarProducto.setText("Agregar Producto");
        contentPane.add(btnAgregarProducto, new GridConstraints(8, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(9, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        contentPane.add(btnGuardar, new GridConstraints(10, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(11, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(12, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnQuitarProducto = new JButton();
        btnQuitarProducto.setText("QuitarProducto");
        contentPane.add(btnQuitarProducto, new GridConstraints(12, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        contentPane.add(spacer10, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(200, 20), null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        contentPane.add(spacer11, new GridConstraints(13, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(150, 20), null, null, 1, false));
        final Spacer spacer12 = new Spacer();
        contentPane.add(spacer12, new GridConstraints(13, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        contentPane.add(spacer13, new GridConstraints(13, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(150, 20), null, null, 1, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
