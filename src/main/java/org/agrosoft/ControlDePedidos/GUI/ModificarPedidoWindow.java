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
import org.agrosoft.ControlDePedidos.GUI.utils.NumericDocumentListener;
import org.agrosoft.ControlDePedidos.GUI.utils.RequestUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ModificarPedidoWindow extends JFrame {
    private final JFrame parentForm;
    private final Pedido pedido;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private List<Producto> productoList;
    private float montoTotal;
    private float montoProductos;
    private final float totalPagos;

    private JPanel contentPane;
    private JComboBox<String> cbxStatusPedido;
    private JComboBox<String> cbxStatusLogistica;
    private JComboBox<String> cbxProductos;
    private JComboBox<String> cbxTipoEnvio;
    private JTable tblProductos;
    private JTextField txtMontoTotal;
    private JTextField txtMontoEnvio;
    private JTextField txtNombreCliente;
    private JTextField txtStatusPago;
    private JTextField txtFechaPedido;
    private JTextField txtNoGuia;
    private JButton btnAgregarProducto;
    private JButton btnGuardar;
    private JButton btnRegresar;
    private JButton btnQuitarProducto;
    private JLabel lblStatusPedido;
    private JLabel lblStatusLogistica;
    private JScrollPane scrollPane;
    private JLabel lblMontoTotal;
    private JLabel lblMontoEnvio;
    private JLabel lblCliente;
    private JLabel lblStatusPago;
    private JLabel lblTipoEnvio;
    private JLabel lblFechaPedido;
    private JLabel lblNoGuia;
    private JCheckBox ckbActivo;

    public ModificarPedidoWindow(JFrame parent, Pedido pedido) {
        this.parentForm = parent;
        this.pedido = pedido;
        log.info("ModificarPedidoWindow opened");
        this.setTitle("Detalles de pedido: <" + pedido.getIdPedido() + ">");
        this.setSize(1000, 650);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FormUtils.centrarVentanaEnPantalla(this);
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.montoTotal = 0F;
        this.montoProductos = 0F;
        this.totalPagos = calculaTotalPagos();
        this.productoList = this.pedido.getProductos();
        this.setContentPane(contentPane);
        this.txtMontoEnvio.setText(String.valueOf(pedido.getMontoEnvio()));
        this.txtNombreCliente.setText(RequestUtils.concatenaNombre(pedido.getCliente()));
        this.txtStatusPago.setText(pedido.getStatusPago().getDescripcion());
        this.txtFechaPedido.setText(formatter.format(pedido.getFechaPedido()));
        this.txtNoGuia.setText(pedido.getNumeroGuia());
        this.ckbActivo.setSelected(pedido.isActive());
        this.llenaComboboxes();
        this.llenaProductos();

        this.txtMontoEnvio.getDocument().addDocumentListener(new NumericDocumentListener(this.txtMontoEnvio));

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
        txtMontoEnvio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                actualizaMontoTotal();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                actualizaMontoTotal();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                actualizaMontoTotal();
            }
        });
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarPedido();
            }
        });
    }

    private float calculaTotalPagos() {
        float totalPago = 0F;
        for (Pago p : PagoClient.fethByPedido(this.pedido.getIdPedido())) {
            totalPago += p.getMontoPago();
        }
        return totalPago;
    }


    private void actualizarPedido() {
        List<String> validaciones = new ArrayList<>();
        if (this.productoList.isEmpty()) {
            validaciones.add("Debe seleccionar al menos un producto para el pedido");
        }
        if (this.cbxStatusPedido.getSelectedIndex() == -1) {
            validaciones.add("Debe seleccionar el estatus del pedido");
        }
        if (this.cbxTipoEnvio.getSelectedIndex() == -1) {
            validaciones.add("Debe seleccionar el estatus del pedido");
        }
        if (this.cbxStatusLogistica.getSelectedIndex() == -1) {
            validaciones.add("Debe seleccionar el estatus de logística del pedido");
        }
        if (!validaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, FormUtils.formateaMensaje("El pedido está" +
                    " incompleto", validaciones), "Error al actualizar el pedido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            StatusLogisticaEnum sle = FormUtils.getItemByDescription(StatusLogisticaEnum.class, this.cbxStatusLogistica
                    .getSelectedItem().toString());

            StatusLogistica statusLogistica = StatusLogisticaClient.fetchByItem(sle);

            StatusPedidoEnum spe = FormUtils.getItemByDescription(StatusPedidoEnum.class, this.cbxStatusPedido
                    .getSelectedItem().toString());

            StatusPedido statusPedido = StatusPedidoClient.fetchByItem(spe);

            TipoEnvioEnum tee = FormUtils.getItemByDescription(TipoEnvioEnum.class, this.cbxTipoEnvio
                    .getSelectedItem().toString());

            TipoEnvio tipoEnvio = TipoEnvioClient.fetchByItem(tee);

            StatusPago statusPago = StatusPagoClient.fetchByItem(this.validarStatusPago());


            this.pedido.setStatusPedido(statusPedido);
            this.pedido.setNumeroGuia(
                    this.txtNoGuia.getText().isBlank() ? null : this.txtNoGuia.getText()
            );
            this.pedido.setMontoTotal(this.montoTotal);
            this.pedido.setMontoEnvio(
                    this.txtMontoEnvio.getText().isBlank() ? 0 : FormUtils
                            .intentaConvertirFloat(this.txtMontoEnvio.getText())
            );
            this.pedido.setStatusLogistica(statusLogistica);
            this.pedido.setStatusPago(statusPago);
            this.pedido.setTipoEnvio(tipoEnvio);
            this.pedido.setProductos(this.productoList);
            this.pedido.setActive(this.ckbActivo.isSelected());

            if (PedidoClient.updatePedido(this.pedido) > 0) {
                JOptionPane.showMessageDialog(this, "Pedido guardado exitosamente",
                        "Pedido guardado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error guardar el pedido. Revise" +
                                " los datos e intente nuevamente",
                        "Error al crear el pedido", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            log.error("Exception caught while trying to update Pedido. Reason: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Hubo un error guardar el pedido. Revise" +
                            " los datos e intente nuevamente",
                    "Error al modificar el pedido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizaMontoTotal() {
        this.montoTotal = this.txtMontoEnvio.getText().isBlank() ? this.montoProductos : this.montoProductos
                + FormUtils.intentaConvertirFloat(txtMontoEnvio.getText());
        this.txtMontoTotal.setText(String.valueOf(this.montoTotal));
        this.txtStatusPago.setText(this.validarStatusPago().getDescripcion());
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
            String productoNombre = this.cbxProductos.getSelectedItem().toString();
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

    private void llenaComboboxes() {
        FormUtils.configuraComboBox(this.cbxStatusPedido, StatusPedidoClient.fetchAll().stream()
                .map(StatusPedido::getDescripcion)
                .toList(), 0);
        for (int i = 0; i < this.cbxStatusPedido.getItemCount(); i++) {
            if (this.cbxStatusPedido.getModel().getElementAt(i)
                    .equalsIgnoreCase(this.pedido.getStatusPedido().getDescripcion())) {
                this.cbxStatusPedido.setSelectedIndex(i);
            }
        }

        FormUtils.configuraComboBox(this.cbxStatusLogistica, StatusLogisticaClient.fetchAll().stream()
                .map(StatusLogistica::getDescripcion)
                .toList(), 0);
        for (int i = 0; i < this.cbxStatusLogistica.getItemCount(); i++) {
            if (this.cbxStatusLogistica.getModel().getElementAt(i)
                    .equalsIgnoreCase(this.pedido.getStatusLogistica().getDescripcion())) {
                this.cbxStatusLogistica.setSelectedIndex(i);
            }
        }

        FormUtils.configuraComboBox(this.cbxProductos, ProductoClient.fetchAll().stream()
                .filter(Producto::isActive)
                .map(Producto::getNombreProducto)
                .toList(), 0);



        FormUtils.configuraComboBox(this.cbxTipoEnvio, TipoEnvioClient.fetchAll().stream()
                .map(TipoEnvio::getDescripcion)
                .toList(), 0);
        for (int i = 0; i < this.cbxStatusLogistica.getItemCount(); i++) {
            if (this.cbxTipoEnvio.getModel().getElementAt(i)
                    .equalsIgnoreCase(this.pedido.getTipoEnvio().getDescripcion())) {
                this.cbxTipoEnvio.setSelectedIndex(i);
            }
        }
    }

    private void llenaProductos() {
        this.montoProductos = 0F;
        this.productoList.sort(Comparator.comparing(Producto::getNombreProducto));
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Producto", "Precio unitario"});
        for (Producto p : this.productoList) {
            model.addRow(new Object[]{p.getNombreProducto(), FormUtils.formateaDinero(p.getPrecioUnitario())});
            this.montoProductos += p.getPrecioUnitario();
        }
        this.tblProductos.setModel(model);
        FormUtils.redimensionarTabla(this.tblProductos);
        this.actualizaMontoTotal();
    }

    private StatusPagoEnum validarStatusPago() {
        log.info("Validating if actual StatusPago is correct");
        StatusPagoEnum statusPago;
        if (this.totalPagos > 0) {
            if (this.totalPagos >= this.montoTotal) {
                statusPago = StatusPagoEnum.PAGADO;
            } else {
                statusPago = StatusPagoEnum.APARTADO;
            }
        } else {
            statusPago = StatusPagoEnum.PENDIENTE;
        }
        return statusPago;
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
        contentPane.setLayout(new GridLayoutManager(23, 7, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        lblStatusPedido = new JLabel();
        lblStatusPedido.setText("Estatus Pedido:");
        contentPane.add(lblStatusPedido, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxStatusPedido = new JComboBox();
        contentPane.add(cbxStatusPedido, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        lblStatusLogistica = new JLabel();
        lblStatusLogistica.setText("Estatus logística");
        contentPane.add(lblStatusLogistica, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxStatusLogistica = new JComboBox();
        contentPane.add(cbxStatusLogistica, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, new GridConstraints(5, 1, 14, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tblProductos = new JTable();
        tblProductos.setAutoResizeMode(0);
        scrollPane.setViewportView(tblProductos);
        lblMontoTotal = new JLabel();
        lblMontoTotal.setText("Monto total:");
        contentPane.add(lblMontoTotal, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblMontoEnvio = new JLabel();
        lblMontoEnvio.setText("Monto Envío");
        contentPane.add(lblMontoEnvio, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblCliente = new JLabel();
        lblCliente.setText("Cliente");
        contentPane.add(lblCliente, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblStatusPago = new JLabel();
        lblStatusPago.setText("Estatus pago");
        contentPane.add(lblStatusPago, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTipoEnvio = new JLabel();
        lblTipoEnvio.setText("Tipo envío");
        contentPane.add(lblTipoEnvio, new GridConstraints(9, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblFechaPedido = new JLabel();
        lblFechaPedido.setText("Fecha pedido:");
        contentPane.add(lblFechaPedido, new GridConstraints(11, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblNoGuia = new JLabel();
        lblNoGuia.setText("No. de guía");
        contentPane.add(lblNoGuia, new GridConstraints(13, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtMontoTotal = new JTextField();
        txtMontoTotal.setEditable(false);
        contentPane.add(txtMontoTotal, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtMontoEnvio = new JTextField();
        contentPane.add(txtMontoEnvio, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtNombreCliente = new JTextField();
        txtNombreCliente.setEditable(false);
        contentPane.add(txtNombreCliente, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtStatusPago = new JTextField();
        txtStatusPago.setEditable(false);
        contentPane.add(txtStatusPago, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtFechaPedido = new JTextField();
        txtFechaPedido.setEditable(false);
        contentPane.add(txtFechaPedido, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txtNoGuia = new JTextField();
        contentPane.add(txtNoGuia, new GridConstraints(13, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cbxProductos = new JComboBox();
        contentPane.add(cbxProductos, new GridConstraints(15, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAgregarProducto = new JButton();
        btnAgregarProducto.setText("Agregar producto");
        contentPane.add(btnAgregarProducto, new GridConstraints(15, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnRegresar = new JButton();
        btnRegresar.setText("Regresar");
        contentPane.add(btnRegresar, new GridConstraints(21, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(16, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 40), null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(14, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        contentPane.add(spacer6, new GridConstraints(18, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        contentPane.add(spacer7, new GridConstraints(12, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentPane.add(spacer8, new GridConstraints(10, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        contentPane.add(spacer9, new GridConstraints(8, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        contentPane.add(spacer10, new GridConstraints(6, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        contentPane.add(spacer11, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        contentPane.add(spacer12, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer13 = new Spacer();
        contentPane.add(spacer13, new GridConstraints(21, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(150, 20), null, null, 0, false));
        final Spacer spacer14 = new Spacer();
        contentPane.add(spacer14, new GridConstraints(22, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        final Spacer spacer15 = new Spacer();
        contentPane.add(spacer15, new GridConstraints(21, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, 20), null, null, 0, false));
        btnQuitarProducto = new JButton();
        btnQuitarProducto.setText("Quitar producto");
        contentPane.add(btnQuitarProducto, new GridConstraints(17, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer16 = new Spacer();
        contentPane.add(spacer16, new GridConstraints(20, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 20), null, null, 0, false));
        btnGuardar = new JButton();
        btnGuardar.setText("Guardar");
        contentPane.add(btnGuardar, new GridConstraints(19, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ckbActivo = new JCheckBox();
        ckbActivo.setText("Está activo");
        contentPane.add(ckbActivo, new GridConstraints(17, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbxTipoEnvio = new JComboBox();
        contentPane.add(cbxTipoEnvio, new GridConstraints(9, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
