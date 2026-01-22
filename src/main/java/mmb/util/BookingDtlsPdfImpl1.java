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
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletContext;
import mmb.dto.BookingDTO;

@Service
public class BookingDtlsPdfImpl1 {
    
    @Autowired
    private ServletContext servletContext;

    // Define all color constants
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
        
        // Create PDF document
        Document document = new Document(PageSize.A4, 40, 40, 20, 40);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        
        // Create fonts
        Font poppinsBold = new Font(Font.HELVETICA, 20, Font.BOLD);
        Font poppinsRegular = new Font(Font.HELVETICA, 12, Font.NORMAL);
        Font poppinsMedium = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font poppinsSmall = new Font(Font.HELVETICA, 10, Font.NORMAL);
        
        // Try to load custom fonts if available
        try {
            String fontPath = servletContext.getRealPath("/WEB-INF/fonts/Poppins-Regular.ttf");
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            poppinsRegular = new Font(baseFont, 12);
            poppinsMedium = new Font(baseFont, 14, Font.BOLD);
            poppinsBold = new Font(baseFont, 20, Font.BOLD);
            poppinsSmall = new Font(baseFont, 10);
        } catch (Exception e) {
            // Fallback to default fonts
            System.out.println("Custom font not found, using default fonts");
        }
        
        // Create main table for layout
        PdfPTable mainTable = new PdfPTable(1);
        mainTable.setWidthPercentage(100);
        
        // HEADER SECTION
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{0.5f, 4.5f});
        
        // Logo/Icon
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        // Water icon using Wingdings font
//        Font symbolFont = new Font(Font.SYMBOL, 24);
//        Paragraph water = new Paragraph("W", symbolFont);
//        water.setAlignment(Element.ALIGN_CENTER);
//        logoCell.addElement(water);
        
        // Company Name
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Paragraph companyName = new Paragraph("MMB Borewell Services", poppinsBold);
        companyName.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph tagline = new Paragraph("Drilling Solutions You Can Trust", poppinsRegular);
        tagline.setAlignment(Element.ALIGN_CENTER);
        
        Chunk receiptBadge = new Chunk("Booking Receipt", poppinsMedium);
        receiptBadge.setBackground(BLUE_100);
        
        Paragraph badgePara = new Paragraph();
        badgePara.add(receiptBadge);
        badgePara.setAlignment(Element.ALIGN_CENTER);
        badgePara.setSpacingBefore(10);
        
        titleCell.addElement(companyName);
        titleCell.addElement(tagline);
        titleCell.addElement(badgePara);
        
        headerTable.addCell(logoCell);
        headerTable.addCell(titleCell);
        
        PdfPCell headerContainer = new PdfPCell(headerTable);
        headerContainer.setBorder(Rectangle.NO_BORDER);
        headerContainer.setPadding(10);
        mainTable.addCell(headerContainer);
        
        // Add spacing
        mainTable.addCell(createSpacerCell(20));
        
        // CUSTOMER INFO SECTION
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{1, 1});
        
        // Customer Details Card
        PdfPCell customerCard = createInfoCard("Customer Details", 
                booking.getCustomerName(),
                new String[]{
                    "Phone: " + booking.getPhoneNumber(),
                    "Address: " + booking.getCity().getCityName() + ", " + booking.getArea().getLocationAreaName()
                },
                BLUE_100, poppinsRegular, poppinsMedium, poppinsSmall);
        
        // Booking Details Card
		String formattedDate = booking.getBookingDate() != null
				? booking.getBookingDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy"))
				: "";
        
        PdfPCell bookingCard = createInfoCard("Booking Date", 
                formattedDate,
                new String[]{
                    "Borewell Type: " + booking.getBorewellType().getName(),
                    "Drilling Size: " + booking.getDrillingSize() + " inch"
                },
                GREEN_100, poppinsRegular, poppinsMedium, poppinsSmall);
        
        infoTable.addCell(customerCard);
        infoTable.addCell(bookingCard);
        
        PdfPCell infoContainer = new PdfPCell(infoTable);
        infoContainer.setBorder(Rectangle.NO_BORDER);
        infoContainer.setPadding(10);
        mainTable.addCell(infoContainer);
        
        // Add spacing
        mainTable.addCell(createSpacerCell(20));
        
        // BILLING DETAILS HEADER
        Paragraph billingHeader = new Paragraph("Billing Details", poppinsMedium);
        billingHeader.setAlignment(Element.ALIGN_CENTER);
        billingHeader.setSpacingBefore(20);
        billingHeader.setSpacingAfter(20);
        
        PdfPCell billingHeaderCell = new PdfPCell(billingHeader);
        billingHeaderCell.setBorder(Rectangle.NO_BORDER);
        mainTable.addCell(billingHeaderCell);
        
        // BILLING TABLE
        PdfPTable billingTable = new PdfPTable(4);
        billingTable.setWidthPercentage(100);
        billingTable.setWidths(new float[]{3, 1, 1, 1});
        
        // Table Header
        String[] headers = {"Service Description", "Unit Price (₹)", "Units", "Amount (₹)"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, poppinsMedium));
            headerCell.setBackgroundColor(DARK_BLUE);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setPadding(10);
            headerCell.setBorderColor(WHITE);
            headerCell.setBorderWidth(1);
            billingTable.addCell(headerCell);
        }
        
        // Add rows
        addBillingRow(billingTable, "Drilling Service", 
                     booking.getPrice(), 
                     booking.getTotalDrillingUnit(), 
                     booking.getTotalDrillingPrice(),
                     poppinsRegular, GRAY_200);
        
        addBillingRow(billingTable, "Casing Pipe", 
                     booking.getCasingPrice(), 
                     booking.getTotalUnitCasing(), 
                     booking.getTotalCasingPrice(),
                     poppinsRegular, GRAY_200);
        
        if (booking.getTotalMasterCasingPrice() != null && booking.getTotalMasterCasingPrice() > 0) {
            addBillingRow(billingTable, "Master Casing", 
                         booking.getMasterCasingPricePerUnit(), 
                         booking.getTotalUnitMasterCasing(), 
                         booking.getTotalMasterCasingPrice(),
                         poppinsRegular, GRAY_200);
        }
        
        if (booking.getTotalSlotingPrice() != null && booking.getTotalSlotingPrice() > 0) {
            addBillingRow(billingTable, "Casing Pipe Slotting", 
                         booking.getCasingSlotingPerUnit(), 
                         booking.getTotalUnitSloting(), 
                         booking.getTotalSlotingPrice(),
                         poppinsRegular, GRAY_200);
        }
        
        if (booking.getTotalWashingPrice() != null && booking.getTotalWashingPrice() > 0) {
            addBillingRow(billingTable, "Bore Washing Service", 
                         booking.getWashingPricePerUnit(), 
                         booking.getTotalWashingUnit(), 
                         booking.getTotalWashingPrice(),
                         poppinsRegular, GRAY_200);
        }
        
        if (booking.getCasingTransporting() != null && booking.getCasingTransporting() > 0) {
            addBillingRow(billingTable, "Transportation Charges", 
                         booking.getCasingTransporting(), 
                         1, 
                         booking.getCasingTransporting(),
                         poppinsRegular, GRAY_200);
        }
        
        if (booking.getTotalUnitGravelPrice() != null && booking.getTotalUnitGravelPrice() > 0) {
            addBillingRow(billingTable, "Gravel Packing", 
                         booking.getPrice(), 
                         booking.getTotalUnitGravel(), 
                         booking.getTotalUnitGravelPrice(),
                         poppinsRegular, GRAY_200);
        }
        
        if (booking.getTotalModPowderPrice() != null && booking.getTotalModPowderPrice() > 0) {
            addBillingRow(billingTable, "Mod-Powder", 
                         booking.getPrice(), 
                         booking.getTotalUnitModPowder(), 
                         booking.getTotalModPowderPrice(),
                         poppinsRegular, GRAY_200);
        }
        
        // Add subtotal
        PdfPCell subtotalLabel = new PdfPCell(new Phrase("Subtotal", poppinsMedium));
        subtotalLabel.setColspan(3);
        subtotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subtotalLabel.setBorder(Rectangle.NO_BORDER);
        subtotalLabel.setPadding(10);
        subtotalLabel.setBackgroundColor(BLUE_50);
        
        PdfPCell subtotalValue = new PdfPCell(new Phrase(
                String.format("₹%.2f", booking.getTotAmtBeforeDiscount()), 
                poppinsMedium));
        subtotalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        subtotalValue.setBorder(Rectangle.NO_BORDER);
        subtotalValue.setPadding(10);
        subtotalValue.setBackgroundColor(BLUE_50);
        
        billingTable.addCell(subtotalLabel);
        billingTable.addCell(subtotalValue);
        
        // Add discount
        addTaxRow(billingTable, "Discount (15%)", booking.getTotDiscountAmt(), poppinsRegular, GRAY_50);
        
        // Add taxable amount
        PdfPCell taxableLabel = new PdfPCell(new Phrase("Taxable Amount", poppinsMedium));
        taxableLabel.setColspan(3);
        taxableLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        taxableLabel.setBorder(Rectangle.NO_BORDER);
        taxableLabel.setPadding(10);
        taxableLabel.setBackgroundColor(BLUE_50);
        
        PdfPCell taxableValue = new PdfPCell(new Phrase(
                String.format("₹%.2f", booking.getTotalAmtAfterDiscount()), 
                poppinsMedium));
        taxableValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        taxableValue.setBorder(Rectangle.NO_BORDER);
        taxableValue.setPadding(10);
        taxableValue.setBackgroundColor(BLUE_50);
        
        billingTable.addCell(taxableLabel);
        billingTable.addCell(taxableValue);
        
        // Add taxes
        addTaxRow(billingTable, "CGST (6%)", booking.getCgst(), poppinsRegular, GRAY_50);
        addTaxRow(billingTable, "SGST (6%)", booking.getSgst(), poppinsRegular, GRAY_50);
        
        // Grand Total  poppinsBold
        PdfPCell grandTotalLabel = new PdfPCell(new Phrase("GRAND TOTAL", poppinsMedium));
        grandTotalLabel.setColspan(3);
        grandTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        grandTotalLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
        grandTotalLabel.setBorder(Rectangle.NO_BORDER);
        grandTotalLabel.setPaddingTop(8);
        grandTotalLabel.setPaddingBottom(8);
        grandTotalLabel.setPaddingRight(15); 
        grandTotalLabel.setBackgroundColor(GREEN_100);
        grandTotalLabel.setNoWrap(true);

        
        PdfPCell grandTotalValue = new PdfPCell(new Phrase(
                String.format("₹ %.2f", booking.getGrandTotal()),
                poppinsMedium));

        grandTotalValue.setHorizontalAlignment(Element.ALIGN_LEFT); 
        grandTotalValue.setVerticalAlignment(Element.ALIGN_MIDDLE);  
        grandTotalValue.setBorder(Rectangle.NO_BORDER);
        grandTotalValue.setPaddingTop(8);
        grandTotalValue.setPaddingBottom(8);
        grandTotalValue.setPaddingLeft(15);     
        grandTotalValue.setBackgroundColor(GREEN_100);
        grandTotalValue.setNoWrap(true);
        
        billingTable.addCell(grandTotalLabel);
        billingTable.addCell(grandTotalValue);
        
        PdfPCell billingContainer = new PdfPCell(billingTable);
        billingContainer.setBorder(Rectangle.NO_BORDER);
        billingContainer.setPadding(10);
        mainTable.addCell(billingContainer);
        
        // Add spacing
        mainTable.addCell(createSpacerCell(20));
        
        // PAYMENT SUMMARY
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        
        PdfPCell summaryLeft = new PdfPCell();
        summaryLeft.setBorder(Rectangle.NO_BORDER);
        summaryLeft.setPadding(10);
        summaryLeft.setBackgroundColor(BLUE_50);
        
        Paragraph summaryTitle = new Paragraph("Payment Summary", poppinsMedium);
        Paragraph summarySub = new Paragraph("All prices are in Indian Rupees (₹)", poppinsSmall);
        summarySub.setSpacingBefore(5);
        
        summaryLeft.addElement(summaryTitle);
        summaryLeft.addElement(summarySub);
        
        PdfPCell summaryRight = new PdfPCell();
        summaryRight.setBorder(Rectangle.NO_BORDER);
        summaryRight.setPadding(10);
        summaryRight.setBackgroundColor(BLUE_50);
        summaryRight.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Paragraph totalPayable = new Paragraph("Total Amount Payable", poppinsSmall);
        totalPayable.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph grandTotalAmount = new Paragraph(
                String.format("₹%.2f", booking.getGrandTotal()), 
                poppinsBold);
        grandTotalAmount.setAlignment(Element.ALIGN_CENTER);
        grandTotalAmount.setSpacingBefore(5);
        
        summaryRight.addElement(totalPayable);
        summaryRight.addElement(grandTotalAmount);
        
        summaryTable.addCell(summaryLeft);
        summaryTable.addCell(summaryRight);
        
        PdfPCell summaryContainer = new PdfPCell(summaryTable);
        summaryContainer.setBorder(Rectangle.NO_BORDER);
        summaryContainer.setPadding(10);
        mainTable.addCell(summaryContainer);
        
        // FOOTER
        Paragraph footer = new Paragraph("© 2024 Borewell Services. All rights reserved.", poppinsSmall);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(30);
        
        Paragraph contact = new Paragraph(
                "Customer Support: 1800-XXX-XXXX | support@borewellservices.com", 
                poppinsSmall);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingBefore(5);
        
        PdfPCell footerCell = new PdfPCell();
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerCell.addElement(footer);
        footerCell.addElement(contact);
        mainTable.addCell(footerCell);
        
        // Add main table to document
        document.add(mainTable);
        document.close();
        out.close();
        
        return out.toByteArray();
    }
    
    private PdfPCell createInfoCard(String title, String mainText, String[] details, 
                                   Color bgColor, Font regularFont, Font mediumFont, Font smallFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOX);
        cell.setBorderWidth(1);
        cell.setBorderColor(GRAY_300);
        cell.setPadding(15);
        cell.setBackgroundColor(bgColor);
        
        // Create title font (small but bold)
        Font titleFont = new Font(smallFont.getBaseFont(), smallFont.getSize(), Font.BOLD);
        
        Paragraph titlePara = new Paragraph(title, titleFont);
        Paragraph mainPara = new Paragraph(mainText, mediumFont);
        mainPara.setSpacingBefore(5);
        
        cell.addElement(titlePara);
        cell.addElement(mainPara);
        
        for (String detail : details) {
            Paragraph detailPara = new Paragraph(detail, smallFont);
            detailPara.setSpacingBefore(5);
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
    
    private void addBillingRow(PdfPTable table, String description, Double unitPrice, 
                              Integer units, Double amount, Font font, Color borderColor) {
        if (amount == null || amount <= 0) return;
        
        PdfPCell descCell = new PdfPCell(new Phrase(description, font));
        descCell.setPadding(10);
        descCell.setBorderWidth(1);
        descCell.setBorderColor(borderColor);
        
        PdfPCell priceCell = new PdfPCell(new Phrase(
                String.format("₹%.2f", unitPrice != null ? unitPrice : 0.0), 
                font));
        priceCell.setPadding(10);
        priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        priceCell.setBorderWidth(1);
        priceCell.setBorderColor(borderColor);
        
        PdfPCell unitCell = new PdfPCell(new Phrase(
                units != null ? units.toString() : "0", 
                font));
        unitCell.setPadding(10);
        unitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        unitCell.setBorderWidth(1);
        unitCell.setBorderColor(borderColor);
        
        PdfPCell amountCell = new PdfPCell(new Phrase(
                String.format("₹%.2f", amount != null ? amount : 0.0), 
                font));
        amountCell.setPadding(10);
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amountCell.setBorderWidth(1);
        amountCell.setBorderColor(borderColor);
        
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
        labelCell.setPadding(10);
        labelCell.setBackgroundColor(bgColor);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(
                String.format("₹%.2f", value != null ? value : 0.0), 
                font));
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(10);
        valueCell.setBackgroundColor(bgColor);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}