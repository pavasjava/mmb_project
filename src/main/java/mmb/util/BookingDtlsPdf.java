package mmb.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletContext;
import mmb.dto.BookingDTO;

@Service
public class BookingDtlsPdf {

	@Autowired
	private ServletContext servletContext;

	private static final Color DARK_BLUE = new Color(30, 60, 114);
	private static final Color BLUE_50 = new Color(239, 246, 255);
	private static final Color BLUE_100 = new Color(219, 234, 254);
	private static final Color GREEN_100 = new Color(220, 252, 231);
	private static final Color GRAY_50 = new Color(248, 250, 252);
	private static final Color GRAY_200 = new Color(226, 232, 240);
	private static final Color GRAY_300 = new Color(203, 213, 225);
	private static final Color WHITE = Color.WHITE;

	public byte[] generateBookingPdf(BookingDTO booking) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4, 20, 20, 10, 20);
		PdfWriter writer = PdfWriter.getInstance(document, out);
		document.open();

		// Fonts
		Font poppinsBold = new Font(Font.HELVETICA, 18, Font.BOLD);
		Font poppinsRegular = new Font(Font.HELVETICA, 11, Font.NORMAL);
		Font poppinsMedium = new Font(Font.HELVETICA, 12, Font.BOLD);
		Font poppinsSmall = new Font(Font.HELVETICA, 9, Font.NORMAL);

		try {
			String fontPath = servletContext.getRealPath("/WEB-INF/fonts/Poppins-Regular.ttf");
			BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			poppinsRegular = new Font(baseFont, 11);
			poppinsMedium = new Font(baseFont, 12, Font.BOLD);
			poppinsBold = new Font(baseFont, 18, Font.BOLD);
			poppinsSmall = new Font(baseFont, 9);
		} catch (Exception e) {
			System.out.println("Custom font not found, using default fonts");
		}

		PdfPTable mainTable = new PdfPTable(1);
		mainTable.setWidthPercentage(100);
		mainTable.setKeepTogether(true); // keep on one page

		// ================= HEADER =================
		PdfContentByte canvas = writer.getDirectContent();
		float llx = document.left();
		float lly = document.bottom();
		float urx = document.right();
		float ury = document.top();
		canvas.setLineWidth(1f);
		canvas.rectangle(llx, lly, urx - llx, ury - lly);
		canvas.stroke();

		PdfPTable headerTable = new PdfPTable(1);
		headerTable.setWidthPercentage(100);
		headerTable.setSpacingBefore(5f);
		headerTable.setSpacingAfter(5f);
		headerTable.setKeepTogether(true);

		PdfPTable nestedTable = new PdfPTable(2);
		nestedTable.setWidthPercentage(100);
		nestedTable.setWidths(new float[] { 1f, 4f });

		Image logo = Image.getInstance("D:/Pravas/My practise project/MMB/src/main/resources/static/images/mmimg.png");
		logo.scaleToFit(80f, 80f);
		PdfPCell imageCell = new PdfPCell(logo, false);
		imageCell.setBorder(Rectangle.NO_BORDER);
		imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		imageCell.setPaddingRight(5f);
		nestedTable.addCell(imageCell);

		PdfPCell textCell = new PdfPCell();
		textCell.setBorder(Rectangle.NO_BORDER);
		textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		Paragraph companyName = new Paragraph("MAA MANGALA BOREWELL",
				new Font(Font.HELVETICA, 20, Font.BOLD, Color.RED));
		companyName.setAlignment(Element.ALIGN_LEFT);
		textCell.addElement(companyName);

		Paragraph services = new Paragraph(
				"Deals with DTH Borewell, Inwell Borewell, Rotary of Callis Borewell,\n"
						+ "Compressor Bore Washing, All types of Motor Fitting",
				new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLUE));
		services.setAlignment(Element.ALIGN_LEFT);
		textCell.addElement(services);

		Paragraph address = new Paragraph(
				"Dumuduma HB Colony, Bhubaneswar-751019, Mob: 7978609919, 9437001922, 7077931922", new Font(Font.HELVETICA, 10));
		address.setAlignment(Element.ALIGN_LEFT);
		textCell.addElement(address);

		nestedTable.addCell(textCell);

		PdfPCell headerCell1 = new PdfPCell(nestedTable);
		headerCell1.setBorderWidth(1f);
		headerCell1.setBackgroundColor(new Color(255, 255, 200));
		headerCell1.setPadding(5f);
		headerTable.addCell(headerCell1);
		mainTable.addCell(headerTable);

		document.add(Chunk.NEWLINE);

		Paragraph title = new Paragraph("Booking Receipt", new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK));
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(5);
		mainTable.addCell(new PdfPCell(title) {
			{
				setBorder(Rectangle.NO_BORDER);
				setHorizontalAlignment(Element.ALIGN_CENTER);
			}
		});

		// ================= CUSTOMER & BOOKING INFO =================
		PdfPTable infoTable = new PdfPTable(2);
		infoTable.setWidthPercentage(100);
		infoTable.setWidths(new float[] { 1, 1 });
		infoTable.setKeepTogether(true);

		PdfPCell customerCard = createInfoCard("Customer Details", booking.getCustomerName(),
				new String[] { "Phone: " + booking.getPhoneNumber(),
						"Address: " + booking.getCity().getCityName() + ", "
								+ booking.getArea().getLocationAreaName() },
				BLUE_100, poppinsRegular, poppinsMedium, poppinsSmall);

		String formattedDate = booking.getBookingDate() != null
				? booking.getBookingDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy"))
				: "";

		PdfPCell bookingCard = createInfoCard("Booking Date", formattedDate,
				new String[] { "Borewell Type: " + booking.getBorewellType().getName(),
						"Drilling Size: " + booking.getDrillingSize() + " inch" },
				GREEN_100, poppinsRegular, poppinsMedium, poppinsSmall);

		infoTable.addCell(customerCard);
		infoTable.addCell(bookingCard);

		PdfPCell infoContainer = new PdfPCell(infoTable);
		infoContainer.setBorder(Rectangle.NO_BORDER);
		infoContainer.setPadding(5);
		mainTable.addCell(infoContainer);

//        mainTable.addCell(createSpacerCell(10));

		// ================= BILLING DETAILS =================
		Paragraph billingHeader = new Paragraph("Billing Details", poppinsMedium);
		billingHeader.setAlignment(Element.ALIGN_CENTER);
		mainTable.addCell(new PdfPCell(billingHeader) {
			{
				setBorder(Rectangle.NO_BORDER);
				setHorizontalAlignment(Element.ALIGN_CENTER);
				setPadding(5);
			}
		});
//        billingHeaderCell.setBorder(Rectangle.NO_BORDER);
//        billingHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        mainTable.addCell(billingHeaderCell);

		PdfPTable billingTable = new PdfPTable(4);
		billingTable.setWidthPercentage(100);
		billingTable.setWidths(new float[] { 3, 1, 1, 1 });
		billingTable.setKeepTogether(true);

		String[] headers = { "Service Description", "Unit Price (₹)", "Units", "Amount (₹)" };
		for (String h : headers) {
			PdfPCell hc = new PdfPCell(new Phrase(h, poppinsMedium));
			hc.setBackgroundColor(DARK_BLUE);
			hc.setHorizontalAlignment(Element.ALIGN_CENTER);
			hc.setVerticalAlignment(Element.ALIGN_MIDDLE);
			hc.setPadding(5);
			hc.setBorderColor(WHITE);
			hc.setBorderWidth(1);
			billingTable.addCell(hc);
		}

		// === Add all billing rows dynamically ===
		addBillingRow(billingTable, "Drilling Service", booking.getPrice(), booking.getTotalDrillingUnit(),
				booking.getTotalDrillingPrice(), poppinsRegular, GRAY_200);

		addBillingRow(billingTable, "Casing Pipe", booking.getCasingPrice(), booking.getTotalUnitCasing(),
				booking.getTotalCasingPrice(), poppinsRegular, GRAY_200);

		if (booking.getTotalMasterCasingPrice() != null && booking.getTotalMasterCasingPrice() > 0)
			addBillingRow(billingTable, "Master Casing", booking.getMasterCasingPricePerUnit(),
					booking.getTotalUnitMasterCasing(), booking.getTotalMasterCasingPrice(), poppinsRegular, GRAY_200);
		
		if (booking.getTotal2_5kgPrice() != null && booking.getTotal2_5kgPrice() > 0)
			addBillingRow(billingTable, "2.5kg. Master Casing", booking.getPricePerUnit2_5kg(),
					booking.getTotalUnit2_5kg(), booking.getTotal2_5kgPrice(), poppinsRegular, GRAY_200);
		
		if (booking.getTotal6kgPrice() != null && booking.getTotal6kgPrice() > 0)
			addBillingRow(billingTable, "6kg. Master Casing", booking.getPricePerUnit6kg(),
					booking.getTotalUnit6kg(), booking.getTotal6kgPrice(), poppinsRegular, GRAY_200);
		
		//====
		if (booking.getTotalMaster10CasingPrice() != null && booking.getTotalMaster10CasingPrice() > 0)
			addBillingRow(billingTable, "10 inch. Master Casing", booking.getMc10PricePerUnit(),
					booking.getTotalUnitMC10(), booking.getTotalMaster10CasingPrice(), poppinsRegular, GRAY_200);
		
		if (booking.getTotalMaster12CasingPrice() != null && booking.getTotalMaster12CasingPrice() > 0)
			addBillingRow(billingTable, "12 inch. Master Casing", booking.getMc12PricePerUnit(),
					booking.getTotalUnitMC12(), booking.getTotalMaster12CasingPrice(), poppinsRegular, GRAY_200);
		
		if (booking.getTotalMasterCasing14Price() != null && booking.getTotalMasterCasing14Price() > 0)
			addBillingRow(billingTable, "14 inch. Master Casing", booking.getMc14PricePerUnit(),
					booking.getTotalUnitMC14(), booking.getTotalMasterCasing14Price(), poppinsRegular, GRAY_200);
		
		if (booking.getTotalSlotingPrice() != null && booking.getTotalSlotingPrice() > 0)
			addBillingRow(billingTable, "Casing Pipe Slotting", booking.getCasingSlotingPerUnit(),
					booking.getTotalUnitSloting(), booking.getTotalSlotingPrice(), poppinsRegular, GRAY_200);

		if (booking.getTotalWashingPrice() != null && booking.getTotalWashingPrice() > 0)
			addBillingRow(billingTable, "Bore Washing Service", booking.getWashingPricePerUnit(),
					booking.getTotalWashingUnit(), booking.getTotalWashingPrice(), poppinsRegular, GRAY_200);

		if (booking.getCasingTransporting() != null && booking.getCasingTransporting() > 0)
			addBillingRow(billingTable, "Transportation Charges", booking.getCasingTransporting(), 1,
					booking.getCasingTransporting(), poppinsRegular, GRAY_200);

		if (booking.getTotalUnitGravelPrice() != null && booking.getTotalUnitGravelPrice() > 0)
			addBillingRow(billingTable, "Gravel Packing", booking.getPrice(), booking.getTotalUnitGravel(),
					booking.getTotalUnitGravelPrice(), poppinsRegular, GRAY_200);

		if (booking.getTotalModPowderPrice() != null && booking.getTotalModPowderPrice() > 0)
			addBillingRow(billingTable, "Mod-Powder", booking.getPrice(), booking.getTotalUnitModPowder(),
					booking.getTotalModPowderPrice(), poppinsRegular, GRAY_200);

		// Subtotal
		PdfPCell subtotalLabel = new PdfPCell(new Phrase("Subtotal", poppinsMedium));
		subtotalLabel.setColspan(3);
		subtotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subtotalLabel.setBorder(Rectangle.NO_BORDER);
		subtotalLabel.setPadding(5);
		subtotalLabel.setBackgroundColor(BLUE_50);
		PdfPCell subtotalValue = new PdfPCell(
				new Phrase(String.format("₹%.2f", booking.getTotAmtBeforeDiscount()), poppinsMedium));
		subtotalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
		subtotalValue.setBorder(Rectangle.NO_BORDER);
		subtotalValue.setPadding(5);
		subtotalValue.setBackgroundColor(BLUE_50);
		billingTable.addCell(subtotalLabel);
		billingTable.addCell(subtotalValue);

		// Discount
		addTaxRow(billingTable, "Discount ("+booking.getDiscountPer()+"%)", booking.getTotDiscountAmt(), poppinsRegular, GRAY_50);

		// Taxable amount
		addTaxRow(billingTable, "Taxable Amount", booking.getTotalAmtAfterDiscount(), poppinsMedium, BLUE_50);

		// Taxes
		addTaxRow(billingTable, "CGST (6%)", booking.getCgst(), poppinsRegular, GRAY_50);
		addTaxRow(billingTable, "SGST (6%)", booking.getSgst(), poppinsRegular, GRAY_50);

		// Grand total
		PdfPCell grandTotalLabel = new PdfPCell(new Phrase("GRAND TOTAL", poppinsMedium));
		grandTotalLabel.setColspan(3);
		grandTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
		grandTotalLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
		grandTotalLabel.setBorder(Rectangle.NO_BORDER);
		grandTotalLabel.setPaddingTop(8);
		grandTotalLabel.setPaddingBottom(8);
		grandTotalLabel.setPaddingRight(20);
		grandTotalLabel.setBackgroundColor(GREEN_100);
		grandTotalLabel.setNoWrap(true);


		PdfPCell grandTotalValue = new PdfPCell(
		        new Phrase(String.format("₹ %.2f", booking.getGrandTotal()), poppinsMedium)
		);
		grandTotalValue.setHorizontalAlignment(Element.ALIGN_LEFT); 
		grandTotalValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
		grandTotalValue.setBorder(Rectangle.NO_BORDER);
		grandTotalValue.setPaddingTop(8);
		grandTotalValue.setPaddingBottom(8);
		grandTotalValue.setPaddingLeft(35);        
		grandTotalValue.setBackgroundColor(GREEN_100);
		grandTotalValue.setNoWrap(true);

		billingTable.addCell(grandTotalLabel);
		billingTable.addCell(grandTotalValue);


        PdfPCell billingContainer = new PdfPCell(billingTable);
        billingContainer.setBorder(Rectangle.NO_BORDER);
        billingContainer.setPadding(10);
        mainTable.addCell(billingContainer);

		// ================= PAYMENT SUMMARY =================
		PdfPTable summaryTable = new PdfPTable(2);
		summaryTable.setWidthPercentage(100);

		PdfPCell summaryLeft = new PdfPCell();
		summaryLeft.setBorder(Rectangle.NO_BORDER);
		summaryLeft.setPadding(5);
		summaryLeft.setBackgroundColor(BLUE_50);

		Paragraph summaryTitle = new Paragraph("Payment Summary", poppinsMedium);
		Paragraph summarySub = new Paragraph("All prices in ₹", poppinsSmall);
		summarySub.setSpacingBefore(3);

		summaryLeft.addElement(summaryTitle);
		summaryLeft.addElement(summarySub);

		PdfPCell summaryRight = new PdfPCell();
		summaryRight.setBorder(Rectangle.NO_BORDER);
		summaryRight.setPadding(5);
		summaryRight.setBackgroundColor(BLUE_50);
		summaryRight.setHorizontalAlignment(Element.ALIGN_CENTER);

		Paragraph totalPayable = new Paragraph("Total Amount Payable", poppinsSmall);
		totalPayable.setAlignment(Element.ALIGN_CENTER);
		Paragraph grandTotalAmount = new Paragraph(String.format("₹%.2f", booking.getGrandTotal()), poppinsBold);
		grandTotalAmount.setAlignment(Element.ALIGN_CENTER);
		grandTotalAmount.setSpacingBefore(3);

		summaryRight.addElement(totalPayable);
		summaryRight.addElement(grandTotalAmount);

		summaryTable.addCell(summaryLeft);
		summaryTable.addCell(summaryRight);

		PdfPCell summaryContainer = new PdfPCell(summaryTable);
		summaryContainer.setBorder(Rectangle.NO_BORDER);
		summaryContainer.setPadding(5);
		mainTable.addCell(summaryContainer);

		// ================= FOOTER =================
		Paragraph footer = new Paragraph("© 2024 Borewell Services. All rights reserved.", poppinsSmall);
		footer.setAlignment(Element.ALIGN_CENTER);
		footer.setSpacingBefore(10);

		Paragraph contact = new Paragraph("Customer Support: 1800-XXX-XXXX | support@borewellservices.com",
				poppinsSmall);
		contact.setAlignment(Element.ALIGN_CENTER);
		contact.setSpacingBefore(2);

		PdfPCell footerCell = new PdfPCell();
		footerCell.setBorder(Rectangle.NO_BORDER);
		footerCell.addElement(footer);
		footerCell.addElement(contact);
		mainTable.addCell(footerCell);

		document.add(mainTable);
		document.close();
		out.close();
		return out.toByteArray();
	}

	private PdfPCell createInfoCard(String title, String mainText, String[] details, Color bgColor, Font regularFont,
			Font mediumFont, Font smallFont) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.BOX);
		cell.setBorderWidth(1);
		cell.setBorderColor(GRAY_300);
		cell.setPadding(8);
		cell.setBackgroundColor(bgColor);

		Font titleFont = new Font(smallFont.getBaseFont(), smallFont.getSize(), Font.BOLD);

		Paragraph titlePara = new Paragraph(title, titleFont);
		Paragraph mainPara = new Paragraph(mainText, mediumFont);
		mainPara.setSpacingBefore(3);

		cell.addElement(titlePara);
		cell.addElement(mainPara);

		for (String detail : details) {
			Paragraph detailPara = new Paragraph(detail, smallFont);
			detailPara.setSpacingBefore(3);
			cell.addElement(detailPara);
		}

		return cell;
	}

	private PdfPCell createSpacerCell(float height) {
		PdfPCell spacer = new PdfPCell();
		spacer.setBorder(Rectangle.NO_BORDER);
		spacer.setFixedHeight(height);
		return spacer;
	}

	private void addBillingRow(PdfPTable table, String description, Double unitPrice, Integer units, Double amount,
			Font font, Color bgColor) {
		if (amount == null || amount <= 0)
			return;

		PdfPCell descCell = new PdfPCell(new Phrase(description, font));
		descCell.setPadding(6);
		descCell.setBorderWidth(1);
		descCell.setBorderColor(bgColor);

		PdfPCell priceCell = new PdfPCell(
				new Phrase(String.format("₹%.2f", unitPrice != null ? unitPrice : 0.0), font));
		priceCell.setPadding(6);
		priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		priceCell.setBorderWidth(1);
		priceCell.setBorderColor(bgColor);

		PdfPCell unitCell = new PdfPCell(new Phrase(units != null ? units.toString() : "0", font));
		unitCell.setPadding(6);
		unitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		unitCell.setBorderWidth(1);
		unitCell.setBorderColor(bgColor);

		PdfPCell amountCell = new PdfPCell(new Phrase(String.format("₹%.2f", amount != null ? amount : 0.0), font));
		amountCell.setPadding(6);
		amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		amountCell.setBorderWidth(1);
		amountCell.setBorderColor(bgColor);

		table.addCell(descCell);
		table.addCell(priceCell);
		table.addCell(unitCell);
		table.addCell(amountCell);
	}

	private void addTaxRow(PdfPTable table, String label, Double value, Font font, Color bgColor) {
		PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
		labelCell.setColspan(3);
		labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		labelCell.setBorder(Rectangle.NO_BORDER);
		labelCell.setPadding(5);
		labelCell.setBackgroundColor(bgColor);

		PdfPCell valueCell = new PdfPCell(new Phrase(String.format("₹%.2f", value != null ? value : 0.0), font));
		valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		valueCell.setBorder(Rectangle.NO_BORDER);
		valueCell.setPadding(5);
		valueCell.setBackgroundColor(bgColor);

		table.addCell(labelCell);
		table.addCell(valueCell);
	}
}
