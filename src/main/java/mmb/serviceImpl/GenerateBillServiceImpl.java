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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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
	    try {
//	        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
//	        PdfWriter.getInstance(document, out);
//	        document.open();
//
//	        // Fonts
//	        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
//	        Font headFont = new Font(Font.HELVETICA, 11, Font.BOLD);
//	        Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
//
//	        // Title / Company header
//	        Paragraph title = new Paragraph("MAA MANGALA BOREWELL", titleFont);
//	        title.setAlignment(Element.ALIGN_CENTER);
//	        document.add(title);
//	        document.add(new Paragraph("Address line 1, Address line 2", normalFont));
//	        document.add(Chunk.NEWLINE);
	    	Document document = new Document(PageSize.A4, 36, 36, 36, 36);
	    	PdfWriter writer = PdfWriter.getInstance(document, out);
	    	document.open();

	    	// Fonts
	    	Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
	    	Font headFont = new Font(Font.HELVETICA, 11, Font.BOLD);
	    	Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

	    	// ===== Custom Header =====

	    	// 1. Draw full-page border
	    	PdfContentByte canvas = writer.getDirectContent();
	    	float borderOffset = 0f; 
	    	float topborderOffset = 5f;
	    	float llx = document.left() - borderOffset;
	    	float lly = document.bottom() - borderOffset;
	    	float urx = document.right() + borderOffset;
	    	float ury = document.top() - topborderOffset;
	    	canvas.setLineWidth(1f);
	    	canvas.rectangle(llx, lly, urx - llx, ury - lly);
	    	canvas.stroke();

	    	// 2. Optional: background watermark
	    	Image bgImage = Image.getInstance("D:/Pravas/My practise project/MMB/src/main/resources/static/images/MMBLogo.jpg");
	    	float imgWidth = 300f;
	    	float imgHeight = 300f;
	    	float posX = (document.getPageSize().getWidth() - imgWidth) / 2;
	    	float posY = (document.getPageSize().getHeight() - imgHeight) / 2;
	    	bgImage.setAbsolutePosition(posX, posY);
	    	bgImage.scaleAbsolute(imgWidth, imgHeight);
	    	canvas.addImage(bgImage);

	    	// semi-transparent overlay
	    	canvas.saveState();
	    	PdfGState gState = new PdfGState();
	    	gState.setFillOpacity(0.9f);
	    	canvas.setGState(gState);
	    	canvas.setColorFill(Color.WHITE);
	    	canvas.rectangle(posX, posY, imgWidth, imgHeight);
	    	canvas.fill();
	    	canvas.restoreState();

	    	// 3. Header table
	    	float tableWidth = urx - llx;
	    	PdfPTable headerTable = new PdfPTable(1);
	    	headerTable.setTotalWidth(tableWidth);
	    	headerTable.setLockedWidth(true);
	    	headerTable.setSpacingBefore(10f);
	    	headerTable.setSpacingAfter(10f);

	    	// Nested table for logo + text
	    	PdfPTable nestedTable = new PdfPTable(2);
	    	nestedTable.setWidthPercentage(100);
	    	nestedTable.setWidths(new float[] { 1f, 4f });

	    	// Logo cell
	    	Image logo = Image.getInstance("D:/Pravas/My practise project/MMB/src/main/resources/static/images/mmimg.png");
	    	logo.scaleToFit(100f, 100f);
	    	PdfPCell imageCell = new PdfPCell(logo, false);
	    	imageCell.setBorder(Rectangle.NO_BORDER);
	    	imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    	nestedTable.addCell(imageCell);

	    	// Text cell
	    	PdfPCell textCell = new PdfPCell();
	    	textCell.setBorder(Rectangle.NO_BORDER);
	    	textCell.setPadding(0f);
	    	textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

	    	Paragraph companyName = new Paragraph("MAA MANGALA BOREWELL", titleFont);
	    	companyName.setAlignment(Element.ALIGN_LEFT);
	    	textCell.addElement(companyName);

	    	Paragraph services = new Paragraph("Deals with DTH Borewell, Inwell Borewell, Rotary of Callis Borewell,\n"
	    	        + "Compressor Bore Washing, All types of Motor Fitting", headFont);
	    	services.setAlignment(Element.ALIGN_LEFT);
	    	textCell.addElement(services);

	    	Paragraph address = new Paragraph(
	    	        "Dumuduma HB Colony, Bhubaneswar-751019, Mob : 7978609919, 9437001922, 7077931922", normalFont);
	    	address.setAlignment(Element.ALIGN_LEFT);
	    	textCell.addElement(address);

	    	nestedTable.addCell(textCell);

	    	// Wrap nested table inside main header cell
	    	PdfPCell headerCell = new PdfPCell(nestedTable);
	    	headerCell.setBorderWidth(2f);
	    	headerCell.setBackgroundColor(new Color(255, 255, 200));
	    	headerCell.setPadding(10f);
	    	headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

	    	headerTable.addCell(headerCell);
	    	document.add(headerTable);
	    	document.add(Chunk.NEWLINE);
	    	
	    	Font blackTitleFont = new Font(Font.HELVETICA, 5, Font.BOLD, Color.BLACK);
	    	Paragraph title = new Paragraph("", blackTitleFont);
	    	title.setAlignment(Element.ALIGN_CENTER);
	    	title.setSpacingAfter(0); // remove extra space
	    	document.add(title);
	    	
	    	PdfPTable billHeaderTbl = new PdfPTable(1); // single column
	    	billHeaderTbl.setWidthPercentage(100);

	    	Font bodyTitleFont = new Font(Font.HELVETICA, 15, Font.BOLD, Color.BLACK);
	    	PdfPCell billCell = new PdfPCell(new Phrase("BILL", bodyTitleFont));
	    	billCell.setBorder(Rectangle.NO_BORDER);
	    	billCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    	billCell.setPadding(2f); // very small padding to avoid extra space
	    	billHeaderTbl.addCell(billCell);

	    	document.add(billHeaderTbl);

	        // ===== Meta table (left: label, right: value) =====
	        PdfPTable meta = new PdfPTable(2);
	        meta.setWidthPercentage(100);
	        meta.setWidths(new float[]{1f, 2f});

//	        addbodyHeader(meta, "Bill", headFont, Element.ALIGN_CENTER);
	        addCellNoBorder(meta, "Bill ID:", headFont);
	        addCellNoBorder(meta, bill.getBillId() != null ? String.valueOf(bill.getBillId()) : "-", normalFont);

	        addCellNoBorder(meta, "Customer:", headFont);
	        addCellNoBorder(meta, safe(bill.getCustomerName()), normalFont);

	        addCellNoBorder(meta, "Mobile:", headFont);
	        addCellNoBorder(meta, safe(bill.getMobileNo()), normalFont);

	        addCellNoBorder(meta, "Work Address:", headFont);
	        addCellNoBorder(meta, safe(bill.getWorkAddress()), normalFont);

	        addCellNoBorder(meta, "Work Date:", headFont);
	        addCellNoBorder(meta, bill.getWorkDate() != null ? bill.getWorkDate().toString() : "-", normalFont);

	        document.add(meta);
	        document.add(Chunk.NEWLINE);

	        // ===== Drilling / Transport / Advance compact layout =====
	        PdfPTable metaCompact = new PdfPTable(4); // 2 columns for label/value, 2 items per row
	        metaCompact.setWidthPercentage(100);
	        metaCompact.setWidths(new float[]{2f, 2f, 2f, 2f}); // adjust widths

	        addCellNoBorder(metaCompact, "Boring Type / Dia:", headFont);
	        addCellNoBorder(metaCompact, safe(bill.getBoringType()) + " / " + safe(bill.getBoringDia()) + "inch.", normalFont);

//	        addCellNoBorder(metaCompact, "Price Qty Details:", headFont);
//	        addCellNoBorder(metaCompact, safe(bill.getPriceQntDtls()), normalFont);

//	        addCellNoBorder(metaCompact, "Drilling Price (per unit):", headFont);
//	        addCellNoBorder(metaCompact, bill.getDrillingPrice() != null ? format(bill.getDrillingPrice()) : "-", normalFont);
	        String drillingPriceText = "";
	        if (bill.getDrillingPrice() != null) {
	            drillingPriceText = format(bill.getDrillingPrice());
	            if (bill.getPriceQntDtls() != null && !bill.getPriceQntDtls().isEmpty()) {
	                drillingPriceText += " (" + safe(bill.getPriceQntDtls()) + ")";
	            }
	        } else {
	            drillingPriceText = "-";
	        }

	        addCellNoBorder(metaCompact, "Drilling Price:", headFont);
	        addCellNoBorder(metaCompact, drillingPriceText, normalFont);

	        addCellNoBorder(metaCompact, "Total Drilling (units):", headFont);
	        addCellNoBorder(metaCompact, bill.getTotalDrilling() != null ? String.valueOf(bill.getTotalDrilling()) : "-", normalFont);

	        double drillingAmt = 0.0;
	        if (bill.getTotalDrillingAmt() != null) {
	            drillingAmt = bill.getTotalDrillingAmt();
	        } else if (bill.getTotalDrilling() != null && bill.getDrillingPrice() != null) {
	            drillingAmt = bill.getTotalDrilling() * bill.getDrillingPrice();
	        }

	        addCellNoBorder(metaCompact, "Total Drilling Amount:", headFont);
	        addCellNoBorder(metaCompact, format(drillingAmt), normalFont);

//	        addCellNoBorder(metaCompact, "Transport Type:", headFont);
//	        addCellNoBorder(metaCompact, safe(bill.getTransportingVehicleType()), normalFont);

	        addCellNoBorder(metaCompact, "Material Transporting Price:", headFont);
	        addCellNoBorder(metaCompact, bill.getTransportingPrice() != null ? format(bill.getTransportingPrice()) : "-", normalFont);

	        addCellNoBorder(metaCompact, "Total Advance:", headFont);
	        addCellNoBorder(metaCompact, bill.getTotalAdvance() != null ? format(bill.getTotalAdvance()) : "-", normalFont);

	        document.add(metaCompact);
	        document.add(Chunk.NEWLINE);

	        // ===== Materials table (Sl.No, Material (company), Qty, Unit Price, Amount) =====
	        double totalMaterialAmt = 0.0;
	        boolean hasMaterials = (bill.getRequiredMaterialIds() != null && !bill.getRequiredMaterialIds().isEmpty())
	                || (bill.getRequiredMaterialwithCompanyName() != null && !bill.getRequiredMaterialwithCompanyName().isEmpty());

	        if (hasMaterials) {
	            PdfPTable matTbl = new PdfPTable(new float[]{0.6f, 4f, 1f, 1.2f, 1.2f});
	            matTbl.setWidthPercentage(100);

	            addTableHeader(matTbl, "Sl. No.", headFont);
	            addTableHeader(matTbl, "Material Description", headFont);
	            addTableHeader(matTbl, "Qty", headFont);
	            addTableHeader(matTbl, "Unit Price", headFont);
	            addTableHeader(matTbl, "Amount", headFont);

	            if (bill.getRequiredMaterialIds() != null && !bill.getRequiredMaterialIds().isEmpty()) {
	                int minSize = Math.min(bill.getRequiredMaterialIds().size(),
	                        bill.getRequiredMaterialQuantities() != null ? bill.getRequiredMaterialQuantities().size() : 0);

	                for (int i = 0; i < minSize; i++) {
	                    Integer materialId = bill.getRequiredMaterialIds().get(i);
	                    Integer qty = bill.getRequiredMaterialQuantities().get(i) != null ? bill.getRequiredMaterialQuantities().get(i) : 0;

	                    RawMaterial rm = null;
	                    try {
	                        rm = (materialId != null) ? rawMaterialRepo.findById(materialId).orElse(null) : null;
	                    } catch (Exception ex) {
	                        rm = null;
	                    }

	                    String matName;
	                    double unitPrice = 0.0;
	                    if (rm != null) {
	                        String typeName = rm.getMaterialType() != null ? safe(rm.getMaterialType().getMaterialName()) : "Material";
	                        String cmp = rm.getCompanyName() != null ? safe(rm.getCompanyName().getCompanyName()) : "";
	                        matName = typeName + (cmp.isEmpty() ? "" : " (" + cmp + ")");
	                        unitPrice = rm.getMaterialPrice() != null ? rm.getMaterialPrice() : 0.0;
	                    } else {
	                        if (bill.getRequiredMaterialwithCompanyName() != null && bill.getRequiredMaterialwithCompanyName().size() > i) {
	                            matName = bill.getRequiredMaterialwithCompanyName().get(i);
	                        } else {
	                            matName = "Material-" + (materialId != null ? materialId : (i + 1));
	                        }
	                        unitPrice = 0.0;
	                    }

	                    double amount = unitPrice * qty;
	                    totalMaterialAmt += amount;

	                    matTbl.addCell(new Phrase(String.valueOf(i + 1), normalFont));
	                    matTbl.addCell(new Phrase(matName, normalFont));
	                    matTbl.addCell(new Phrase(String.valueOf(qty), normalFont));
	                    matTbl.addCell(new Phrase(unitPrice > 0 ? format(unitPrice) : "-", normalFont));
	                    matTbl.addCell(new Phrase(amount > 0 ? format(amount) : "-", normalFont));
	                }
	            } else {
	                int minSize = Math.min(
	                        bill.getRequiredMaterialwithCompanyName() != null ? bill.getRequiredMaterialwithCompanyName().size() : 0,
	                        bill.getRequiredMaterialQuantities() != null ? bill.getRequiredMaterialQuantities().size() : 0
	                );
	                for (int i = 0; i < minSize; i++) {
	                    String matName = bill.getRequiredMaterialwithCompanyName().get(i);
	                    Integer qty = bill.getRequiredMaterialQuantities().get(i) != null ? bill.getRequiredMaterialQuantities().get(i) : 0;
	                    matTbl.addCell(new Phrase(String.valueOf(i + 1), normalFont));
	                    matTbl.addCell(new Phrase(safe(matName), normalFont));
	                    matTbl.addCell(new Phrase(String.valueOf(qty), normalFont));
	                    matTbl.addCell(new Phrase("-", normalFont));
	                    matTbl.addCell(new Phrase("-", normalFont));
	                }
	            }

	            matTbl.addCell(new Phrase("", normalFont));
	            PdfPCell totLabel = new PdfPCell(new Phrase("Total Material Amount", headFont));
	            totLabel.setColspan(3);
	            totLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            matTbl.addCell(totLabel);
	            matTbl.addCell(new Phrase(format(totalMaterialAmt), headFont));

	            document.add(matTbl);
	            document.add(Chunk.NEWLINE);
	        }

	        // ===== Other Works table (Sl.No, Description, Unit, Price) =====
	        double totalOtherWorkAmt = 0.0;
	        if (bill.getOtherWorks() != null && !bill.getOtherWorks().isEmpty()) {
	            PdfPTable otherTbl = new PdfPTable(new float[]{0.6f, 4f, 1f, 1.4f});
	            otherTbl.setWidthPercentage(100);

	            addTableHeader(otherTbl, "Sl. No.", headFont);
	            addTableHeader(otherTbl, "Other Work Description", headFont);
	            addTableHeader(otherTbl, "Unit", headFont);
	            addTableHeader(otherTbl, "Price", headFont);

	            int idx = 1;
	            for (OtherWorkDTO ow : bill.getOtherWorks()) {
	                String units = ow.getTotalUnit() != null ? ow.getTotalUnit() : "0";
	                double price = ow.getPrice() != null ? ow.getPrice() : 0.0;
	                totalOtherWorkAmt += price;

	                otherTbl.addCell(new Phrase(String.valueOf(idx++), normalFont));
	                otherTbl.addCell(new Phrase(safe(ow.getOthWorkName()), normalFont));
	                otherTbl.addCell(new Phrase(units, normalFont));
	                otherTbl.addCell(new Phrase(price > 0 ? format(price) : "-", normalFont));
	            }

	            otherTbl.addCell(new Phrase("", normalFont));
	            PdfPCell othLabel = new PdfPCell(new Phrase("Total Other Work Amount", headFont));
	            othLabel.setColspan(2);
	            othLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            otherTbl.addCell(othLabel);
	            otherTbl.addCell(new Phrase(format(totalOtherWorkAmt), headFont));

	            document.add(otherTbl);
	            document.add(Chunk.NEWLINE);
	        }

	        // ===== Totals block (right aligned) =====
	        PdfPTable totalTbl = new PdfPTable(2);
	        totalTbl.setWidths(new float[]{3f, 1f});
	        totalTbl.setWidthPercentage(50);
	        totalTbl.setHorizontalAlignment(Element.ALIGN_RIGHT);

	        double transport = bill.getTransportingPrice() != null ? bill.getTransportingPrice() : 0.0;
	        double advance = bill.getTotalAdvance() != null ? bill.getTotalAdvance() : 0.0;
	        double grandTotal = drillingAmt + totalMaterialAmt + totalOtherWorkAmt + transport;

	        addCellNoBorder(totalTbl, "Drilling Amount:", headFont);
	        addCellNoBorder(totalTbl, format(drillingAmt), normalFont);

	        addCellNoBorder(totalTbl, "Total Material Amount:", headFont);
	        addCellNoBorder(totalTbl, format(totalMaterialAmt), normalFont);

	        addCellNoBorder(totalTbl, "Total Other Work Amount:", headFont);
	        addCellNoBorder(totalTbl, format(totalOtherWorkAmt), normalFont);

	        addCellNoBorder(totalTbl, "Material Transporting:", headFont);
	        addCellNoBorder(totalTbl, format(transport), normalFont);

	        addCellNoBorder(totalTbl, "Grand Total:", headFont);
	        addCellNoBorder(totalTbl, format(grandTotal), headFont);

	        addCellNoBorder(totalTbl, "Advance Paid:", headFont);
	        addCellNoBorder(totalTbl, format(advance), normalFont);

	        addCellNoBorder(totalTbl, "Balance Due:", headFont);
	        addCellNoBorder(totalTbl, format(grandTotal - advance), headFont);

	        document.add(totalTbl);

	        document.close();
	    } catch (Exception e) {
	        throw new RuntimeException("Error while generating PDF", e);
	    }
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
	
//	private void addbodyHeader(PdfPTable table, String text, Font font, int alignment) {
//	    PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
//	    cell.setBorder(Rectangle.NO_BORDER);
//	    cell.setPadding(4f);
//	    cell.setHorizontalAlignment(alignment); // set alignment dynamically
//	    table.addCell(cell);
//	}
}
