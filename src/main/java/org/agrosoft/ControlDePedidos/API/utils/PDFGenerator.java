package org.agrosoft.ControlDePedidos.API.utils;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.agrosoft.ControlDePedidos.API.converter.PagoConverter;
import org.agrosoft.ControlDePedidos.API.converter.PedidoCorteConverter;
import org.agrosoft.ControlDePedidos.API.converter.PedidoPdfConverter;
import org.agrosoft.ControlDePedidos.API.converter.ProductoConverter;
import org.agrosoft.ControlDePedidos.API.dto.PedidoDetalleDTO;
import org.agrosoft.ControlDePedidos.API.entity.Pago;
import org.agrosoft.ControlDePedidos.API.entity.Pedido;
import org.agrosoft.ControlDePedidos.API.entity.Producto;
import org.agrosoft.ControlDePedidos.GUI.exception.ReadPropertyException;
import org.agrosoft.ControlDePedidos.GUI.utils.FormUtils;
import org.agrosoft.ControlDePedidos.GUI.utils.XMLHandler;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class PDFGenerator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    public static String generaCorte(List<Pedido> pedidosDelMes, List<Pago> pagosDelMes, List<Producto> productos,
                                   List<Pedido> pedidosRelacionados, LocalDate fechaCorte) throws ReadPropertyException,
            FileNotFoundException {

        log.info("Retrieving path for new PDF document");
        String path = XMLHandler.readXMLConfig("config.xml", "rutaReportes");
        path += "CorteDelMes" + fechaCorte.getMonth() + fechaCorte.getYear() + ".pdf";
        log.info("Path retrieved. PDF file to create: {}", path);
        log.info("Preparing PDF file");
        PagoConverter pagoConverter = new PagoConverter();
        PedidoCorteConverter pedidoCorteConverter = new PedidoCorteConverter();
        ProductoConverter productoConverter = new ProductoConverter();

        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        log.info("PDF File created. Adding title");
        document.add(
                new Paragraph("Corte del mes")
                        .setBold()
                        .setFontSize(22)
                        .setTextAlignment(TextAlignment.CENTER)
        );

        if(!pedidosDelMes.isEmpty()) {
            log.info("Pedidos for this month found, adding table");
            document.add(new Paragraph("Pedidos realizados en el mes"));
            document.add(getTable(pedidoCorteConverter.getEncabezados(), pedidoCorteConverter
                    .convertList(pedidosDelMes)));
            document.add(new Paragraph(""));
        }

        if(!pagosDelMes.isEmpty()) {
            log.info("Pagos for this month found, adding table");
            document.add(new Paragraph("Pagos realizados en el mes"));
            document.add(getTable(pagoConverter.getEncabezados(), pagoConverter.convertList(pagosDelMes)));
            document.add(new Paragraph(""));
        }

        if(!pedidosRelacionados.isEmpty()) {
            log.info("Pedidos related to pagos found, adding table");
            document.add(new Paragraph("Pedidos con actividad este mes"));
            document.add(getTable(pedidoCorteConverter.getEncabezados(), pedidoCorteConverter
                    .convertList(pedidosRelacionados)));
            document.add(new Paragraph(""));
        }

        if(!productos.isEmpty()) {
            log.info("Productos sold this month found, adding table");
            document.add(new Paragraph("Productos vendidos este mes"));
            document.add(getTable(productoConverter.getEncabezados(), productoConverter.convertList(productos)));
            document.add(new AreaBreak());
        }

        log.info("Adding summary table");
        String[][] resumen = new String[][] {
                { "Número de pedidos del mes", String.valueOf(pedidosDelMes.size()) },
                { "Número de pagos recibidos en el mes", String.valueOf(pagosDelMes.size()) },
                { "Número de productos vendidos este mes", String.valueOf(productos.size()) },
                { "Monto total recibido este mes", FormUtils.formateaDinero((float) pagosDelMes.stream()
                        .mapToDouble(Pago::getMontoPago).sum()) }
        };
        String[] headers = new String[] { "Concepto", "Total" };
        document.add(new Paragraph("Resumen del corte"));
        document.add(getTable(headers, resumen));

        log.info("PDF file successfully created. Closing and preparing");
        document.close();
        return path;
    }

    public static String generaReporte(List<PedidoDetalleDTO> pedidos) throws ReadPropertyException,
            FileNotFoundException {
        log.info("Retrieving path for new PDF document");
        String path = XMLHandler.readXMLConfig("config.xml", "rutaImpresiones");
        path += "ImpresionPedidos" + formatter.format(LocalDateTime.now()) + ".pdf";
        log.info("Path retrieved. PDF file to create: {}", path);
        log.info("Preparing PDF file");
        PedidoPdfConverter pedidoPdfConverter = new PedidoPdfConverter();

        PdfWriter writer = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);
        log.info("PDF File created. Adding title");
        document.add(
                new Paragraph("May Arellano")
                        .setBold()
                        .setFontSize(22)
                        .setTextAlignment(TextAlignment.CENTER)
        );
        document.add(
                new Paragraph("Entrega en domicilio")
                        .setBold()
                        .setFontSize(22)
                        .setTextAlignment(TextAlignment.CENTER)
        );

        if(!pedidos.isEmpty()) {
            log.info("Pedidos found, adding table");
            document.add(getTable(pedidoPdfConverter.getEncabezados(), pedidoPdfConverter.convertList(pedidos)));
        }

        log.info("PDF file successfully created. Closing and preparing");
        document.close();
        return path;
    }

    public static Table getTable(String[] encabezados, String[][] datos) {
        Table table = new Table(encabezados.length);
        table.setWidth(UnitValue.createPercentValue(100));
        Cell tmp;
        for(String header : encabezados) {
            tmp = new Cell()
                    .add(new Paragraph(header).setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.BLACK)
                    .setTextAlignment(TextAlignment.CENTER);
            table.addCell(tmp);
        }
        for(int i=0; i<datos.length; i++){
            for(int j=0; j<datos[i].length; j++) {
                tmp = new Cell()
                        .add(new Paragraph(datos[i][j]))
                        .setTextAlignment(TextAlignment.CENTER);
                if (i%2 == 0) {
                    tmp.setBackgroundColor(ColorConstants.LIGHT_GRAY);
                }
                table.addCell(tmp);
            }
        }
        return table;
    }
}
