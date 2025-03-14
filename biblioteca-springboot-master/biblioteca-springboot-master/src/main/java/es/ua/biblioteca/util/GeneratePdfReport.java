package es.ua.biblioteca.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import es.ua.biblioteca.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;



public class GeneratePdfReport {

    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

    public static ByteArrayInputStream booksReport(List<Book> books) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Libros de la Biblioteca", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Crear tabla
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(80);
            table.setWidths(new int[]{1, 3, 3});
            table.setSpacingBefore(10);

            // Estilos
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
            BaseColor headerColor = new BaseColor(79, 129, 189); // Azul claro

            // Encabezado
            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Id", headerFont));
            hcell.setBackgroundColor(headerColor);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Título", headerFont));
            hcell.setBackgroundColor(headerColor);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Autor", headerFont));
            hcell.setBackgroundColor(headerColor);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            // Datos
            boolean alternate = false;
            for (Book book : books) {
                PdfPCell cell;
                cell = new PdfPCell(new Phrase(book.getId().toString(), cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (alternate) cell.setBackgroundColor(new BaseColor(240, 240, 240));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(book.getTitle(), cellFont));
                cell.setPaddingLeft(5);
                if (alternate) cell.setBackgroundColor(new BaseColor(240, 240, 240));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(book.getAuthor(), cellFont));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                if (alternate) cell.setBackgroundColor(new BaseColor(240, 240, 240));
                table.addCell(cell);
                alternate = !alternate;
            }

            document.add(table);

            //Pie de página
            PdfPTable footer = new PdfPTable(1);
            footer.setWidthPercentage(100);
            PdfPCell footerCell = new PdfPCell();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            footerCell.addElement(new Phrase("Generado el: " + dateFormat.format(new Date())));
            footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer.addCell(footerCell);
            footer.setTotalWidth(document.right() - document.left());
            footer.writeSelectedRows(0, -1, document.leftMargin(), document.bottom() + 10, writer.getDirectContent());

            document.close();

        } catch (DocumentException ex) {
            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
