package mmb.serviceImpl;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import mmb.dto.GenerateBillDTO;
import mmb.dto.MaterialWithCompanyProjection;
import mmb.dto.OtherWorkDTO;
import mmb.dto.SelectedMaterialDTO;
import mmb.model.GenerateBill;
import mmb.model.GenerateMaterialsBill;
import mmb.model.MaterialCompanyName;
import mmb.model.MaterialType;
import mmb.model.OtherWork;
import mmb.model.RawMaterial;
import mmb.repository.GenerateBillRepo;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.MaterialTypeRepo;
import mmb.repository.OtherWorkRepo;
import mmb.repository.RawMaterialRepo;
import mmb.service.GenerateBillService;

@Service
@Transactional
public class GenerateBillServiceImpl implements GenerateBillService {

	private final GenerateBillRepo generateBillRepo;
	private final RawMaterialRepo rawMaterialRepo;
	private final OtherWorkRepo otherWorkRepo;
	@Autowired
	private MaterialTypeRepo materialTypeRepository;
	@Autowired
	private MaterialCompanyNameRepo materialCompanyNameRepository;
	@Autowired
	private GenerateBillRepo generateBillRepository;
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	public GenerateBillServiceImpl(GenerateBillRepo generateBillRepository, RawMaterialRepo rawMaterialRepo,
			OtherWorkRepo otherWorkRepo) {
		this.generateBillRepo = generateBillRepository;
		this.rawMaterialRepo = rawMaterialRepo;
		this.otherWorkRepo = otherWorkRepo;
	}

	// Get all bills
	@Override
	public List<GenerateBill> getAllBills() {
		return generateBillRepo.findAllByOrderByDoeDesc();
	}

	// Get bill by ID
	@Override
	public GenerateBillDTO getBillById(Integer billId) {
		GenerateBill bill = generateBillRepo.findById(billId)
				.orElseThrow(() -> new RuntimeException("Bill not found with id " + billId));
		GenerateBillDTO dto = new GenerateBillDTO();

		dto.setBillId(bill.getBillId());
		dto.setCustomerName(bill.getCustomerName());
		dto.setMobileNo(bill.getMobileNo());
		dto.setWorkAddress(bill.getWorkAddress());
		dto.setWorkDate(bill.getWorkDate());
		dto.setBoringType(bill.getBoringType());
		dto.setBoringDia(bill.getBoringDia());
		dto.setPriceQntDtls(bill.getPriceQntDtls());
		dto.setDrillingPrice(bill.getDrillingPrice());
		dto.setTransportingVehicleType(bill.getTransportingVehicleType());
		dto.setTransportingPrice(bill.getTransportingPrice());
		dto.setTotalDrilling(bill.getTotalDrilling());
		dto.setTotalAdvance(bill.getTotalAdvance());

		// === Materials ===
		if (bill.getMaterialsBill() != null) {
			dto.setRequiredMaterialIds(
					bill.getMaterialsBill().stream().map(gmb -> gmb.getRawMaterial().getMaterialId()).toList());

			dto.setRequiredMaterialQuantities(
					bill.getMaterialsBill().stream().map(GenerateMaterialsBill::getTotalUnit).toList());

			List<SelectedMaterialDTO> selectedMaterials = bill.getMaterialsBill().stream().map(gmb -> {
				RawMaterial rm = gmb.getRawMaterial();
				String typeName = (rm.getMaterialType() != null) ? rm.getMaterialType().getMaterialName() : "N/A";
				String company = (rm.getCompanyName() != null) ? rm.getCompanyName().getCompanyName() : "N/A";
				return new SelectedMaterialDTO(rm.getMaterialId(), typeName, company, gmb.getTotalUnit());
			}).toList();

			dto.setSelectedMaterials(selectedMaterials);
		}

		// === Other Works ===
		if (bill.getOtherWorks() != null) {
			dto.setOtherWorks(bill.getOtherWorks().stream().map(
					ow -> new OtherWorkDTO(ow.getOthWorkId(), ow.getOthWorkName(), ow.getTotalUnit(), ow.getPrice()))
					.toList());
		}

		return dto;
	}

	public List<MaterialWithCompanyProjection> getAllMaterials() {
		return generateBillRepo.findAllMaterialsDtls();
	}

	public List<MaterialType> getAllMaterialTypes() {
		return materialTypeRepository.findAll();
	}

	public List<MaterialCompanyName> getAllCompanies() {
		return materialCompanyNameRepository.findAll();
	}

	// Save or update bill
//    @Override
//    @Transactional
//    public GenerateBill saveOrUpdate(GenerateBillDTO dto) {
//        GenerateBill bill;
//
//        // if edit, fetch existing
//        if (dto.getBillId() != null) {
//            bill = generateBillRepo.findById(dto.getBillId())
//                    .orElseThrow(() -> new RuntimeException("Bill not found with id " + dto.getBillId()));
//        } else {
//            bill = new GenerateBill();
//        }
//
//        bill.setCustomerName(dto.getCustomerName());
//        bill.setMobileNo(dto.getMobileNo());
//        bill.setWorkAddress(dto.getWorkAddress());
//        bill.setWorkDate(dto.getWorkDate());
//        bill.setBoringType(dto.getBoringType());
//        bill.setBoringDia(dto.getBoringDia());
//        bill.setPriceQntDtls(dto.getPriceQntDtls());
//        bill.setDrillingPrice(dto.getDrillingPrice());
//        bill.setTransportingVehicleType(dto.getTransportingVehicleType());
//        bill.setTransportingPrice(dto.getTransportingPrice());
//        bill.setTotalDrilling(dto.getTotalDrilling());
//        bill.setTotalAdvance(dto.getTotalAdvance());
//        double totalDrillingAmt = dto.getTotalDrilling()*dto.getDrillingPrice();
//        bill.setTotalDrillingAmt(totalDrillingAmt);
//        
//        double totalMaterialAmt = 0.0;
//        double totalOtherWorkAmt = 0.0;
//        
//        List<GenerateMaterialsBill> materialsBills = new ArrayList<>();
//        if (dto.getRequiredMaterialIds() != null && dto.getRequiredMaterialQuantities() != null) {
//            for (int i = 0; i < dto.getRequiredMaterialIds().size(); i++) {
//                Integer materialId = dto.getRequiredMaterialIds().get(i);
//                Integer qty = dto.getRequiredMaterialQuantities().get(i);
//
//                RawMaterial material = rawMaterialRepo.findById(materialId)
//                        .orElseThrow(() -> new RuntimeException("Material not found with id " + materialId));
//                
//                System.out.println("material price -> "+ material.getMaterialPrice());
//                System.out.println("Total material price -> "+ material.getMaterialPrice()*qty);
//
//                GenerateMaterialsBill gmb = new GenerateMaterialsBill();
//                gmb.setGenerateBill(bill);
//                gmb.setRawMaterial(material);
//                gmb.setTotalUnit(qty);
//                double totalMaterialPrice = material.getMaterialPrice()*qty;
//                totalMaterialAmt += totalMaterialPrice;
//                gmb.setTotalAmount(totalMaterialPrice);
//
//                materialsBills.add(gmb);
//            }
//        }
//        bill.setMaterialsBill(materialsBills);
//
//        List<OtherWork> works = new ArrayList<>();
//        if (dto.getOtherWorks() != null) {
//            for (OtherWorkDTO owDto : dto.getOtherWorks()) {
//                OtherWork work = new OtherWork();
//                work.setOthWorkName(owDto.getOthWorkName());
//                work.setTotalUnit(owDto.getTotalUnit());
//                work.setPrice(owDto.getPrice());
//                double otherWorkPrice = owDto.getPrice();
//                totalOtherWorkAmt += otherWorkPrice;
//                work.setGenerateBill(bill);
//                works.add(work);
//            }
//        }
//        bill.setOtherWorks(works);
//        bill.setTotalMatrialAmt(totalMaterialAmt);
//        bill.setTotalOthWorkAmt(totalOtherWorkAmt);
//
//        return generateBillRepo.save(bill);
//    }
	@Override
	@Transactional
	public GenerateBill saveOrUpdate(GenerateBillDTO dto) {
		GenerateBill bill;
		if (dto.getBillId() != null) {
			bill = generateBillRepo.findById(dto.getBillId())
					.orElseThrow(() -> new RuntimeException("Bill not found with id " + dto.getBillId()));
		} else {
			bill = new GenerateBill();
		}

		// --- map simple fields ---
		bill.setCustomerName(dto.getCustomerName());
		bill.setMobileNo(dto.getMobileNo());
		bill.setWorkAddress(dto.getWorkAddress());
		bill.setWorkDate(dto.getWorkDate());
		bill.setBoringType(dto.getBoringType());
		bill.setBoringDia(dto.getBoringDia());
		bill.setPriceQntDtls(dto.getPriceQntDtls());
		bill.setDrillingPrice(dto.getDrillingPrice());
		bill.setTransportingVehicleType(dto.getTransportingVehicleType());
		bill.setTransportingPrice(dto.getTransportingPrice());
		bill.setTotalDrilling(dto.getTotalDrilling());
		bill.setTotalAdvance(dto.getTotalAdvance());

		double totalDrillingAmt = 0.0;
		if (dto.getTotalDrilling() != null && dto.getDrillingPrice() != null) {
			totalDrillingAmt = dto.getTotalDrilling() * dto.getDrillingPrice();
		}
		bill.setTotalDrillingAmt(totalDrillingAmt);

		double totalMaterialAmt = 0.0;
		double totalOtherWorkAmt = 0.0;

		// --- update materialsBill correctly ---
		bill.getMaterialsBill().clear();
		if (dto.getRequiredMaterialIds() != null && dto.getRequiredMaterialQuantities() != null) {
			for (int i = 0; i < dto.getRequiredMaterialIds().size(); i++) {
				Integer materialId = dto.getRequiredMaterialIds().get(i);
				Integer qty = dto.getRequiredMaterialQuantities().get(i);

				RawMaterial material = rawMaterialRepo.findById(materialId)
						.orElseThrow(() -> new RuntimeException("Material not found with id " + materialId));

				GenerateMaterialsBill gmb = new GenerateMaterialsBill();
				gmb.setGenerateBill(bill);
				gmb.setRawMaterial(material);
				gmb.setTotalUnit(qty);

				double totalMaterialPrice = material.getMaterialPrice() * qty;
				totalMaterialAmt += totalMaterialPrice;
				gmb.setTotalAmount(totalMaterialPrice);

				bill.getMaterialsBill().add(gmb);
			}
		}

		// --- update otherWorks correctly ---
		bill.getOtherWorks().clear();
		if (dto.getOtherWorks() != null) {
			for (OtherWorkDTO owDto : dto.getOtherWorks()) {
				OtherWork work = new OtherWork();
				work.setOthWorkName(owDto.getOthWorkName());
				work.setTotalUnit(owDto.getTotalUnit());
				work.setPrice(owDto.getPrice());

				double otherWorkPrice = owDto.getPrice() != null ? owDto.getPrice() : 0.0;
				totalOtherWorkAmt += otherWorkPrice;

				work.setGenerateBill(bill);
				bill.getOtherWorks().add(work);
			}
		}

		bill.setTotalMatrialAmt(totalMaterialAmt);
		bill.setTotalOthWorkAmt(totalOtherWorkAmt);

		return generateBillRepo.save(bill);
	}

	@Override
	public void deleteBillById(Integer billId) {
		generateBillRepo.deleteById(billId);
	}

	@Override
	public void generateBillPdf(GenerateBillDTO bill, OutputStream out) {
	    Document document = null;
	    try {
	        // EXACT SAME MARGINS as first code: left:20, right:20, top:10, bottom:20
	        document = new Document(PageSize.A4, 20, 20, 10, 20);
	        PdfWriter writer = PdfWriter.getInstance(document, out);
	        document.open();

	        // Define colors (EXACT SAME as first code)
	        Color DARK_BLUE = new Color(0, 51, 102);
	        Color BLUE_50 = new Color(240, 248, 255);
	        Color BLUE_100 = new Color(173, 216, 230);
	        Color GREEN_100 = new Color(144, 238, 144);
	        Color GRAY_50 = new Color(248, 249, 250);
	        Color GRAY_200 = new Color(233, 236, 239);
	        Color WHITE = Color.WHITE;

	        // Fonts - with Poppins support (EXACT SAME approach as first code)
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

	        // ================= PAGE BORDER (EXACT SAME as first code) =================
	        PdfContentByte canvas = writer.getDirectContent();
	        float llx = document.left();
	        float lly = document.bottom();
	        float urx = document.right();
	        float ury = document.top();
	        canvas.setLineWidth(1f);
	        canvas.rectangle(llx, lly, urx - llx, ury - lly);
	        canvas.stroke();

	        // Create main table like first code
	        PdfPTable mainTable = new PdfPTable(1);
	        mainTable.setWidthPercentage(100);
	        mainTable.setKeepTogether(true);

	        // ================= HEADER (EXACT SAME as first code) =================
	        PdfPTable headerTable = new PdfPTable(1);
	        headerTable.setWidthPercentage(100);
	        headerTable.setSpacingBefore(5f);
	        headerTable.setSpacingAfter(5f);
	        headerTable.setKeepTogether(true);

	        PdfPTable nestedTable = new PdfPTable(2);
	        nestedTable.setWidthPercentage(100);
	        nestedTable.setWidths(new float[] { 1f, 4f });

	        // Logo - use the same path as first code or make it configurable
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
	                "Dumuduma HB Colony, Bhubaneswar-751019, Mob: 7978609919, 9437001922, 7077931922", 
	                new Font(Font.HELVETICA, 10));
	        address.setAlignment(Element.ALIGN_LEFT);
	        textCell.addElement(address);

	        nestedTable.addCell(textCell);

	        PdfPCell headerCell1 = new PdfPCell(nestedTable);
	        headerCell1.setBorderWidth(1f);
	        headerCell1.setBackgroundColor(new Color(255, 255, 200));
	        headerCell1.setPadding(5f);
	        headerTable.addCell(headerCell1);
	        mainTable.addCell(headerTable);

	        // Title - styled like first code
	        Paragraph title = new Paragraph("BILL RECEIPT", poppinsBold);
	        title.setAlignment(Element.ALIGN_CENTER);
	        title.setSpacingAfter(10);
	        mainTable.addCell(new PdfPCell(title) {
	            {
	                setBorder(Rectangle.NO_BORDER);
	                setHorizontalAlignment(Element.ALIGN_CENTER);
	                setPadding(5);
	            }
	        });

	        // ================= BILL INFO CARD (styled like first code) =================
	        PdfPTable infoTable = new PdfPTable(2);
	        infoTable.setWidthPercentage(100);
	        infoTable.setWidths(new float[] { 1, 1 });
	        infoTable.setKeepTogether(true);

	        // Customer Card
	        PdfPCell customerCard = createInfoCard("Customer Details", 
	                bill.getCustomerName() != null ? bill.getCustomerName() : "-",
	                new String[] { 
	                    "Phone: " + (bill.getMobileNo() != null ? bill.getMobileNo() : "-"),
	                    "Address: " + (bill.getWorkAddress() != null ? bill.getWorkAddress() : "-")
	                },
	                BLUE_100, poppinsRegular, poppinsMedium, poppinsSmall);

	        // Bill Info Card
	        String workDateStr = bill.getWorkDate() != null ? 
	                bill.getWorkDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "-";
	        
	        PdfPCell billCard = createInfoCard("Bill Information", 
	                "Bill ID: " + (bill.getBillId() != null ? bill.getBillId() : "-"),
	                new String[] { 
	                    "Work Date: " + workDateStr,
	                    "Boring Type: " + (bill.getBoringType() != null ? bill.getBoringType() : "-") + "(" + (bill.getBoringDia() != null ? bill.getBoringDia() : "-") + "inch.)"
	                },
	                GREEN_100, poppinsRegular, poppinsMedium, poppinsSmall);

	        infoTable.addCell(customerCard);
	        infoTable.addCell(billCard);

	        PdfPCell infoContainer = new PdfPCell(infoTable);
	        infoContainer.setBorder(Rectangle.NO_BORDER);
	        infoContainer.setPadding(5);
	        mainTable.addCell(infoContainer);

	        // ================= SERVICE DETAILS (styled like first code billing table) =================
	        Paragraph serviceHeader = new Paragraph("Service Details", poppinsMedium);
	        serviceHeader.setAlignment(Element.ALIGN_CENTER);
	        mainTable.addCell(new PdfPCell(serviceHeader) {
	            {
	                setBorder(Rectangle.NO_BORDER);
	                setHorizontalAlignment(Element.ALIGN_CENTER);
	                setPadding(5);
	            }
	        });

	        PdfPTable serviceTable = new PdfPTable(4);
	        serviceTable.setWidthPercentage(100);
	        serviceTable.setWidths(new float[] { 3, 1, 1, 1 });
	        serviceTable.setKeepTogether(true);

	        // Table headers (same style as first code)
	        String[] headers = { "Description", "Unit Price (₹)", "Units", "Amount (₹)" };
	        for (String h : headers) {
	            PdfPCell hc = new PdfPCell(new Phrase(h, poppinsMedium));
	            hc.setBackgroundColor(DARK_BLUE);
	            hc.setHorizontalAlignment(Element.ALIGN_CENTER);
	            hc.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            hc.setPadding(5);
	            hc.setBorderColor(WHITE);
	            hc.setBorderWidth(1);
	            serviceTable.addCell(hc);
	        }

	        // Drilling Service Row
	        addBillingRow(serviceTable, "Drilling Service", 
//	        		 + "("+
	                bill.getDrillingPrice() != null ? bill.getDrillingPrice() : 0.0,
	                bill.getTotalDrilling() != null ? bill.getTotalDrilling() : 0.0,
	                bill.getTotalDrillingAmt() != null ? bill.getTotalDrillingAmt() : 
	                    (bill.getDrillingPrice() != null && bill.getTotalDrilling() != null ? 
	                     bill.getDrillingPrice() * bill.getTotalDrilling() : 0.0),
	                poppinsRegular, GRAY_200);
	        
	        addBillingRow(serviceTable, "Casing Pipe", 
	                bill.getDrillingPrice() != null ? bill.getDrillingPrice() : 0.0,
	                bill.getTotalDrilling() != null ? bill.getTotalDrilling() : 0.0,
	                bill.getTotalDrillingAmt() != null ? bill.getTotalDrillingAmt() : 
	                    (bill.getDrillingPrice() != null && bill.getTotalDrilling() != null ? 
	                     bill.getDrillingPrice() * bill.getTotalDrilling() : 0.0),
	                poppinsRegular, GRAY_200);

	        // Transport Charges Row
	        if (bill.getTransportingPrice() != null && bill.getTransportingPrice() > 0) {
	            addBillingRow(serviceTable, "Transportation Charges", 
	                    bill.getTransportingPrice(), 1, bill.getTransportingPrice(), 
	                    poppinsRegular, GRAY_200);
	        }

	        // Add materials if available
	        double totalMaterialAmt = 0.0;
	        if (bill.getRequiredMaterialIds() != null && !bill.getRequiredMaterialIds().isEmpty()) {
	            // You can add individual material rows here or just show total
	            // For simplicity, showing total materials
	            totalMaterialAmt = calculateTotalMaterials(bill);
	            if (totalMaterialAmt > 0) {
	                addBillingRow(serviceTable, "Materials", totalMaterialAmt, 1, totalMaterialAmt, 
	                        poppinsRegular, GRAY_200);
	            }
	        }

	        // Add other works if available
	        double totalOtherWorkAmt = 0.0;
	        if (bill.getOtherWorks() != null && !bill.getOtherWorks().isEmpty()) {
	            totalOtherWorkAmt = bill.getOtherWorks().stream()
	                    .mapToDouble(ow -> ow.getPrice() != null ? ow.getPrice() : 0.0)
	                    .sum();
	            if (totalOtherWorkAmt > 0) {
	                addBillingRow(serviceTable, "Other Works", totalOtherWorkAmt, 1, totalOtherWorkAmt, 
	                        poppinsRegular, GRAY_200);
	            }
	        }

	        // Calculate totals
	        double drillingAmt = bill.getTotalDrillingAmt() != null ? bill.getTotalDrillingAmt() : 
	            (bill.getDrillingPrice() != null && bill.getTotalDrilling() != null ? 
	             bill.getDrillingPrice() * bill.getTotalDrilling() : 0.0);
	        double transportAmt = bill.getTransportingPrice() != null ? bill.getTransportingPrice() : 0.0;
	        double grandTotal = drillingAmt + transportAmt + totalMaterialAmt + totalOtherWorkAmt;
	        double advanceAmt = bill.getTotalAdvance() != null ? bill.getTotalAdvance() : 0.0;
	        double balanceDue = grandTotal - advanceAmt;

	        // Subtotal (same style as first code)
	        PdfPCell subtotalLabel = new PdfPCell(new Phrase("Subtotal", poppinsMedium));
	        subtotalLabel.setColspan(3);
	        subtotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        subtotalLabel.setBorder(Rectangle.NO_BORDER);
	        subtotalLabel.setPadding(5);
	        subtotalLabel.setBackgroundColor(BLUE_50);
	        
	        PdfPCell subtotalValue = new PdfPCell(
	                new Phrase(String.format("₹%.2f", grandTotal), poppinsMedium));
	        subtotalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        subtotalValue.setBorder(Rectangle.NO_BORDER);
	        subtotalValue.setPadding(5);
	        subtotalValue.setBackgroundColor(BLUE_50);
	        serviceTable.addCell(subtotalLabel);
	        serviceTable.addCell(subtotalValue);

	        // Advance Payment
	        if (advanceAmt > 0) {
	            addTaxRow(serviceTable, "Advance Paid", -advanceAmt, poppinsRegular, GRAY_50);
	        }

	        // Balance Due (styled like Grand Total in first code)
	        PdfPCell balanceLabel = new PdfPCell(new Phrase("BALANCE DUE", poppinsMedium));
	        balanceLabel.setColspan(3);
	        balanceLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        balanceLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        balanceLabel.setBorder(Rectangle.NO_BORDER);
	        balanceLabel.setPaddingTop(8);
	        balanceLabel.setPaddingBottom(8);
	        balanceLabel.setPaddingRight(20);
	        balanceLabel.setBackgroundColor(GREEN_100);
	        balanceLabel.setNoWrap(true);

	        PdfPCell balanceValue = new PdfPCell(
	                new Phrase(String.format("₹ %.2f", balanceDue), poppinsMedium));
	        balanceValue.setHorizontalAlignment(Element.ALIGN_LEFT);
	        balanceValue.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        balanceValue.setBorder(Rectangle.NO_BORDER);
	        balanceValue.setPaddingTop(8);
	        balanceValue.setPaddingBottom(8);
	        balanceValue.setPaddingLeft(35);
	        balanceValue.setBackgroundColor(GREEN_100);
	        balanceValue.setNoWrap(true);

	        serviceTable.addCell(balanceLabel);
	        serviceTable.addCell(balanceValue);

	        PdfPCell serviceContainer = new PdfPCell(serviceTable);
	        serviceContainer.setBorder(Rectangle.NO_BORDER);
	        serviceContainer.setPadding(10);
	        mainTable.addCell(serviceContainer);

	        // ================= FOOTER (same as first code) =================
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

	    } catch (Exception e) {
	        if (document != null && document.isOpen()) {
	            document.close();
	        }
	        throw new RuntimeException("Error while generating PDF", e);
	    }
	}

	// ================= HELPER METHODS (same as first code) =================

	private PdfPCell createInfoCard(String title, String mainInfo, String[] details, 
	        Color bgColor, Font regularFont, Font mediumFont, Font smallFont) {
	    PdfPCell card = new PdfPCell();
	    card.setBackgroundColor(bgColor);
	    card.setPadding(10);
	    card.setBorderWidth(1);
	    card.setBorderColor(Color.DARK_GRAY);
	    
	    Paragraph titlePara = new Paragraph(title, mediumFont);
	    titlePara.setAlignment(Element.ALIGN_CENTER);
	    card.addElement(titlePara);
	    
	    if (mainInfo != null && !mainInfo.isEmpty()) {
	        Paragraph mainPara = new Paragraph(mainInfo, regularFont);
	        mainPara.setAlignment(Element.ALIGN_CENTER);
	        mainPara.setSpacingBefore(5);
	        card.addElement(mainPara);
	    }
	    
	    for (String detail : details) {
	        Paragraph detailPara = new Paragraph(detail, smallFont);
	        detailPara.setAlignment(Element.ALIGN_LEFT);
	        detailPara.setSpacingBefore(3);
	        card.addElement(detailPara);
	    }
	    
	    return card;
	}

	private void addBillingRow(PdfPTable table, String desc, double unitPrice, double units, 
	        double amount, Font font, Color bgColor) {
	    PdfPCell descCell = new PdfPCell(new Phrase(desc, font));
	    descCell.setBackgroundColor(bgColor);
	    descCell.setPadding(5);
	    
	    PdfPCell priceCell = new PdfPCell(new Phrase(String.format("₹%.2f", unitPrice), font));
	    priceCell.setBackgroundColor(bgColor);
	    priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    priceCell.setPadding(5);
	    
	    PdfPCell unitCell = new PdfPCell(new Phrase(String.valueOf(units), font));
	    unitCell.setBackgroundColor(bgColor);
	    unitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    unitCell.setPadding(5);
	    
	    PdfPCell amtCell = new PdfPCell(new Phrase(String.format("₹%.2f", amount), font));
	    amtCell.setBackgroundColor(bgColor);
	    amtCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    amtCell.setPadding(5);
	    
	    table.addCell(descCell);
	    table.addCell(priceCell);
	    table.addCell(unitCell);
	    table.addCell(amtCell);
	}

	private void addTaxRow(PdfPTable table, String label, double amount, Font font, Color bgColor) {
	    PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
	    labelCell.setColspan(3);
	    labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    labelCell.setBorder(Rectangle.NO_BORDER);
	    labelCell.setPadding(5);
	    labelCell.setBackgroundColor(bgColor);
	    
	    PdfPCell amountCell = new PdfPCell(new Phrase(String.format("₹%.2f", amount), font));
	    amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    amountCell.setBorder(Rectangle.NO_BORDER);
	    amountCell.setPadding(5);
	    amountCell.setBackgroundColor(bgColor);
	    
	    table.addCell(labelCell);
	    table.addCell(amountCell);
	}

	private double calculateTotalMaterials(GenerateBillDTO bill) {
	    double total = 0.0;
	    if (bill.getRequiredMaterialIds() != null && !bill.getRequiredMaterialIds().isEmpty()) {
	        for (int i = 0; i < bill.getRequiredMaterialIds().size(); i++) {
	            Integer materialId = bill.getRequiredMaterialIds().get(i);
	            Integer qty = bill.getRequiredMaterialQuantities() != null && 
	                         i < bill.getRequiredMaterialQuantities().size() ? 
	                         bill.getRequiredMaterialQuantities().get(i) : 0;
	            
	            // You would need to fetch material price from repository
	            // For now, assuming price is 0 or you can modify this
	            double unitPrice = 0.0;
	            total += unitPrice * qty;
	        }
	    }
	    return total;
	}

	/* ---------- small helper methods ---------- */

	private void addCellNoBorder(PdfPTable table, String text, Font font) {
	    PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setPadding(4f);
	    table.addCell(cell);
	}

	private void addTableHeader(PdfPTable table, String header, Font font) {
	    PdfPCell cell = new PdfPCell(new Phrase(header, font));
	    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    cell.setPadding(6f);
	    table.addCell(cell);
	}

	private String safe(Object o) {
	    return o == null ? "" : o.toString();
	}

	private String format(double value) {
	    return String.format("%.2f", value);
	}
	
	private void addbodyHeader(PdfPTable table, String text, Font font, int alignment) {
	    PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
	    cell.setBorder(Rectangle.NO_BORDER);
	    cell.setPadding(4f);
	    cell.setHorizontalAlignment(alignment); // set alignment dynamically
	    table.addCell(cell);
	}
}
