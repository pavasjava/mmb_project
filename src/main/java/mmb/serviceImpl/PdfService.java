package mmb.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import mmb.model.Quotation;
import mmb.model.RawMaterial;

@Service
public class PdfService {

    public void generateQuotationPdf(HttpServletResponse response, Quotation quotation) throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Quotation Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Basic info
        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        document.add(new Paragraph("Customer Name: " + quotation.getCustomerName(), boldFont));
        document.add(new Paragraph("Work Address: " + quotation.getWorkAddress(), boldFont));
        document.add(new Paragraph("Boring Type: " + quotation.getBoringType(), boldFont));
        document.add(new Paragraph("Boring Dia: " + quotation.getBoringDia(), boldFont));
        document.add(new Paragraph("Drilling Price: " + quotation.getDrillingPrice(), boldFont));
        document.add(new Paragraph("Transport Vehicle Type: " + quotation.getTransportingVehicleType(), boldFont));
        document.add(new Paragraph("Transport Price: " + quotation.getTransportingPrice(), boldFont));
        document.add(Chunk.NEWLINE);

        // Materials table
        List<RawMaterial> materials = quotation.getRequiredMaterials();
        if (materials != null && !materials.isEmpty()) {
            PdfPTable table = new PdfPTable(6); // 2 columns: Name & Type
            table.setWidthPercentage(100);
            table.addCell("Material Name");
            table.addCell("Brand Name");
            table.addCell("Quality");
            table.addCell("Size");
            table.addCell("Quantity");
            table.addCell("Price");

            for (RawMaterial mat : materials) {
                table.addCell(mat.getMaterialType());
                table.addCell(mat.getCompanyName());
                table.addCell(mat.getQuality());
                table.addCell(mat.getMatrialSize());
                table.addCell(mat.getQuantity());
                table.addCell(String.valueOf(mat.getMaterialPrice()));
            }
            document.add(table);
        }

        document.close();
    }
}
