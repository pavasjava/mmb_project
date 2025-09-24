package mmb.serviceImpl;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import mmb.model.Quotation;
import mmb.model.RawMaterial;

@Service
public class PdfService {

	public void generateQuotationPdf(HttpServletResponse response, Quotation quotation)
			throws IOException, DocumentException {

		// 1. Create document with standard margins
		Document document = new Document(PageSize.A4, 36, 36, 36, 36);
		PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		// 2. Fonts
		Font titleFont = new Font(Font.HELVETICA, 22, Font.BOLD, Color.RED);
		Font subTitleFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLUE);
		Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);
		Font normalFont = new Font(Font.HELVETICA, 10);
		// 3. Draw full-page border
		PdfContentByte canvas = writer.getDirectContent();
		float borderOffset = 0f; // distance from document margins
		float topborderOffset = 5f;
		float llx = document.left() - borderOffset;
		float lly = document.bottom() - borderOffset;
		float urx = document.right() + borderOffset;
		float ury = document.top() - topborderOffset;
		canvas.setLineWidth(1f);
		canvas.rectangle(llx, lly, urx - llx, ury - lly);
		canvas.stroke();
		// === Add light background image (watermark) ===
		Image bgImage = Image
				.getInstance("D:/Pravas/My practise project/MMB/src/main/resources/static/images/MMBLogo.jpg");

		// Set smaller size (e.g., 300x300)
		float imgWidth = 300f;
		float imgHeight = 300f;

		// Center position
		float posX = (document.getPageSize().getWidth() - imgWidth) / 2;
		float posY = (document.getPageSize().getHeight() - imgHeight) / 2;

		bgImage.setAbsolutePosition(posX, posY);
		bgImage.scaleAbsolute(imgWidth, imgHeight);

		// Add image to canvas
		canvas.addImage(bgImage);

		// Optional: draw semi-transparent white overlay **only on the image area**
		canvas.saveState();
		PdfGState gState = new PdfGState();
		gState.setFillOpacity(0.9f); // stronger blur effect
		canvas.setGState(gState);
		canvas.setColorFill(Color.WHITE);
		canvas.rectangle(posX, posY, imgWidth, imgHeight);
		canvas.fill();
		canvas.restoreState();

		// === Calculate header table width to match page border ===
		float tableWidth = urx - llx;
		// 4. Header table
		// === Header table with image on left and text on right ===
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.setTotalWidth(tableWidth); // same as page border
		headerTable.setLockedWidth(true);
		headerTable.setSpacingBefore(10f);
		headerTable.setSpacingAfter(10f);

		// 2. Nested table for image + text (2 columns)
		PdfPTable nestedTable = new PdfPTable(2);
		nestedTable.setWidthPercentage(100);
		nestedTable.setWidths(new float[] { 1f, 4f }); // 1 part image, 4 parts text

		// 3. Image cell
		Image logo = Image.getInstance("D:/Pravas/My practise project/MMB/src/main/resources/static/images/mmimg.png");
		logo.scaleToFit(100f, 100f); // adjust size as needed
		PdfPCell imageCell = new PdfPCell(logo, false);
		imageCell.setBorder(Rectangle.NO_BORDER);
		imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		imageCell.setPaddingRight(0f);
		nestedTable.addCell(imageCell);

		// 4. Text cell
		PdfPCell textCell = new PdfPCell();
		textCell.setBorder(Rectangle.NO_BORDER);
		textCell.setPadding(0f);
		textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		// Company Name
		Paragraph companyName = new Paragraph("MAA MANGALA BOREWELL", titleFont);
		companyName.setAlignment(Element.ALIGN_LEFT);
		textCell.addElement(companyName);

		// Services
		Paragraph services = new Paragraph("Deals with DTH Borewell, Inwell Borewell, Rotary of Callis Borewell,\n"
				+ "Compressor Bore Washing, All types of Motor Fitting", subTitleFont);
		services.setAlignment(Element.ALIGN_LEFT);
		textCell.addElement(services);

		// Address
		Paragraph address = new Paragraph(
				"Dumuduma HB Colony, Bhubaneswar-751019, Mob : 7978609919, 9437001922, 7077931922", normalFont);
		address.setAlignment(Element.ALIGN_LEFT);
		textCell.addElement(address);

		nestedTable.addCell(textCell);

		// 5. Wrap nested table inside main header cell
		PdfPCell headerCell = new PdfPCell(nestedTable);
		headerCell.setBorderWidth(2f); // header border
		headerCell.setBackgroundColor(new Color(255, 255, 200)); // yellow background
		headerCell.setPadding(10f);
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		headerTable.addCell(headerCell);
		document.add(headerTable);

		document.add(Chunk.NEWLINE);

//		Color customGreen = new Color(0x30, 0x7D, 0x7E);
		Font greenTitleFont = new Font(Font.HELVETICA, 22, Font.BOLD, Color.BLACK);
		Paragraph title = new Paragraph("Quotation", greenTitleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20);
		document.add(title);

		Paragraph customerPara = new Paragraph("Customer Name: " + quotation.getCustomerName(), boldFont);
		customerPara.setIndentationLeft(20f); // move 20 points to the right
		document.add(customerPara);

		Paragraph addressPara = new Paragraph("Work Address: " + quotation.getWorkAddress(), boldFont);
		addressPara.setIndentationLeft(20f);
		document.add(addressPara);
		document.add(Chunk.NEWLINE);

		List<RawMaterial> materials = quotation.getRequiredMaterials();
		if (materials != null && !materials.isEmpty()) {
			PdfPTable table = new PdfPTable(4);
			float tableWidth1 = urx - llx - 10f;
			table.setTotalWidth(tableWidth1);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);

			Font tableFont = new Font(Font.HELVETICA, 12);
			Font tableHeadFont = new Font(Font.HELVETICA, 12, Font.BOLD);
			
			PdfPCellEvent thinBorder = new PdfPCellEvent() {
			    @Override
			    public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
			        PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
			        cb.setLineWidth(0.5f); // thin line
			        cb.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
			        cb.stroke();
			    }
			};

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("Sl No", tableHeadFont));
			cell.setCellEvent(thinBorder);
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Description", tableHeadFont));
			cell.setCellEvent(thinBorder);
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Quantity", tableHeadFont));
			cell.setCellEvent(thinBorder);
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Price", tableHeadFont));
			cell.setCellEvent(thinBorder);
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			int slNo = 1;

			// First row from quotation
			String boringDia = quotation.getBoringDia() != null ? quotation.getBoringDia() : "";
			String boringType = quotation.getBoringType() != null ? quotation.getBoringType() : "";
			String boringDetails = (boringDia + "inch. " + boringType + "Boring").trim();

			String priceQntDtls = quotation.getPriceQntDtls() != null ? quotation.getPriceQntDtls() : "";
			String drillingPrice = quotation.getDrillingPrice() != null ? String.valueOf(quotation.getDrillingPrice())
					: "";

			cell = new PdfPCell(new Phrase(String.valueOf(slNo++), tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(boringDetails, tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(priceQntDtls, tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(drillingPrice, tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			// Remaining rows from materials
			for (RawMaterial mat : materials) {
				cell = new PdfPCell(new Phrase(String.valueOf(slNo++), tableFont));
				cell.setPaddingTop(8f);
				cell.setPaddingBottom(8f);
				table.addCell(cell);

				String materialName = mat.getMaterialType() != null ? mat.getMaterialType().getMaterialName() : "";
				String brandName = mat.getCompanyName() != null ? mat.getCompanyName().getCompanyName() : "";
				String quality = mat.getQuality() != null ? mat.getQuality() : "";
				String itemDetails = materialName + " (" + brandName + ") " + quality;

				cell = new PdfPCell(new Phrase(itemDetails.trim(), tableFont));
				cell.setPaddingTop(8f);
				cell.setPaddingBottom(8f);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(mat.getQuantity() != null ? mat.getQuantity() : "", tableFont));
				cell.setPaddingTop(8f);
				cell.setPaddingBottom(8f);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(
						mat.getMaterialPrice() != null ? String.valueOf(mat.getMaterialPrice()) : "", tableFont));
				cell.setPaddingTop(8f);
				cell.setPaddingBottom(8f);
				table.addCell(cell);
			}

			// Transport row
			String transportVehType = quotation.getTransportingVehicleType() != null
					? quotation.getTransportingVehicleType()
					: "";
			double transportingPrice = quotation.getTransportingPrice() != null ? quotation.getTransportingPrice()
					: 0.0;

			cell = new PdfPCell(new Phrase(String.valueOf(slNo++), tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(transportVehType, tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("", tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			String priceText = (transportingPrice == 0.0) ? "0" : String.valueOf(transportingPrice);
			cell = new PdfPCell(new Phrase(priceText, tableFont));
			cell.setPaddingTop(8f);
			cell.setPaddingBottom(8f);
			table.addCell(cell);

			document.add(table);
		}
		document.close();
	}
}
