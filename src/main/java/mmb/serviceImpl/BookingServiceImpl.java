package mmb.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import mmb.dto.AreaWiseItemRequirementDTO;
import mmb.dto.BookingAuditDTO;
import mmb.dto.BookingDTO;
import mmb.dto.BorewellTypeDTO;
import mmb.dto.DrillingPriceChartDTO;
import mmb.dto.RawMaterialDTO;
import mmb.exception.ResourceNotFoundException;
import mmb.model.Booking;
import mmb.model.BookingAudit;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.Discount;
import mmb.model.DrillingPriceChart;
import mmb.model.MaterialCompanyName;
import mmb.model.WorkLocationArea;
import mmb.repository.*;
import mmb.service.AreaWiseItemRequirementService;
import mmb.service.BookingService;
import mmb.service.BorewellTypeService;
import mmb.service.RawMaterialService;
import mmb.service.UserService;
import mmb.service.WorkLocationAreaService;

@Service
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;

    private final UserDetailsService userDetails;
	
	@Autowired
	private EntityManager entityManager;

	private final BookingRepository bookingRepo;
	
	@Autowired
	private DiscountRepository discountRepo;

	public BookingServiceImpl(BookingRepository bookingRepo, UserDetailsService userDetails, UserRepository userRepository) {
		this.bookingRepo = bookingRepo;
		this.userDetails = userDetails;
		this.userRepository = userRepository;
	}

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DrillingPriceChartRepository drillingPriceChartRepositor;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
    private WorkLocationAreaRepository workLocationAreaRepository;
	@Autowired
	private WorkLocationAreaService workLocationAreaService;
	
	@Autowired
    private BorewellTypeRepository borewellTypeRepository;
	
	@Autowired
	private BorewellTypeService borewellTypeService;
	
	@Autowired
	private RawMaterialService rawMaterialService;
	
	@Autowired
	private MaterialCompanyNameRepo materialCompanyNameRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DrillingPriceChartRepository drillingPriceChartRepository;
	
	@Autowired
	private AreaWiseItemRequirementService areaWiseItemRequirementService;
	
	@Autowired
	private BookingAuditRepository auditRepository;

	// ✅ This is the method your controller expects
//    public BookingDTO saveBooking(BookingDTO dto) {
//        Booking booking = mapToEntity(dto);
//        booking.setStatus("Pending");
//        Booking saved = bookingRepo.save(booking);
//        return mapToDTO(saved);
//    }

	@Override
	public BookingDTO saveBooking(BookingDTO dto) {
		Booking booking = mapToEntity(dto);
		City city = entityManager.getReference(City.class, dto.getCity().getCityId());
	    WorkLocationArea area = entityManager.getReference(WorkLocationArea.class, dto.getArea().getLocationAreaId());
	    BorewellType borewellType = null;

	    if (dto.getBorewellType() != null && dto.getBorewellType().getBorewelTypeid() != null) {
	        borewellType = entityManager.getReference(BorewellType.class, dto.getBorewellType().getBorewelTypeid());
	    }

	    booking.setCity(city);
	    booking.setWorkLocationArea(area);
	    booking.setBorewellType(borewellType);
	    booking.setCasingPrice(dto.getCasingPrice());
	    booking.setPipeQuality(dto.getPipeQuality());
	    booking.setCompanyName(dto.getCompanyName());
	    
	    booking.setTotalDrillingUnit(dto.getTotalDrillingUnit());
	    booking.setTotalUnitCasing(dto.getTotalUnitCasing());
	    booking.setTotalUnitMasterCasing(dto.getTotalUnitMasterCasing());
	    booking.setMasterCasingPricePerUnit(dto.getMasterCasingPricePerUnit());
	    booking.setCasingTransportingPrice(dto.getCasingTransporting());
	    booking.setTotalUnitSloting(dto.getTotalUnitSloting());
	    booking.setCasingSlotingPricePerUnit(dto.getCasingSlotingPerUnit());
	    booking.setTotalUnitModPowder(dto.getTotalUnitModPowder());
	    
	    booking.setModPowderPricePerUnit(dto.getModPowderPerUnit());
	    booking.setTotalUnitGravel(dto.getTotalUnitGravel());
	    booking.setGravelPricePerUnit(dto.getGravelPricePerUnit());
	    booking.setTotalWashingUnit(dto.getTotalWashingUnit());
	    booking.setWashingPricePerUnit(dto.getWashingPricePerUnit());
	    booking.setTotalUnitMC10(dto.getTotalUnitMC10());
	    booking.setMc10PricePerUnit(dto.getMc10PricePerUnit());
	    booking.setTotalUnitMC12(dto.getTotalUnitMC12());
	    
	    booking.setMc12PricePerUnit(dto.getMc12PricePerUnit());
	    booking.setTotalUnitMC14(dto.getTotalUnitMC14());
	    booking.setMc14PricePerUnit(dto.getMc14PricePerUnit());
	    booking.setDiscount(dto.getDiscount());
	    System.out.println("Discount percent -> "+dto.getDiscount());
	    booking.setCgstPercent(dto.getCgstPercent());
	    booking.setSgstPercent(dto.getSgstPercent());
	    
	    booking.setStatus("Booked");
		Booking saved = bookingRepo.save(booking);
		return mapToDTO(saved);
	}

	public List<BookingDTO> getAllBookings() {
		List<BookingDTO> bookings = bookingRepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
		return bookings;
	}

	// ✅ Entity → DTO
	private BookingDTO mapToDTO(Booking booking) {
		BookingDTO dto = new BookingDTO();

		dto.setBookingId(booking.getBookingId());
		dto.setCustomerName(booking.getCustomerName());
		dto.setPhoneNumber(booking.getPhoneNumber());
		dto.setCity(booking.getCity());
		dto.setArea(booking.getWorkLocationArea());
		dto.setPrice(booking.getPrice());
		dto.setDrillingSize(booking.getDrillingSize());
		dto.setBookingDate(booking.getBookingDate());
		dto.setBorewellType(booking.getBorewellType());
		dto.setStatus(booking.getStatus());

		return dto;
	}

	// ✅ DTO → Entity (required when saving/updating)
	private Booking mapToEntity(BookingDTO dto) {
		Booking booking = new Booking();

		booking.setBookingId(dto.getBookingId());
		booking.setCustomerName(dto.getCustomerName());
		booking.setPhoneNumber(dto.getPhoneNumber());
		booking.setCity(dto.getCity());
		booking.setWorkLocationArea(dto.getArea());
		booking.setPrice(dto.getPrice());
		booking.setDrillingSize(dto.getDrillingSize());
		booking.setBookingDate(dto.getBookingDate());
		booking.setBorewellType(dto.getBorewellType());
		booking.setStatus(dto.getStatus());

		return booking;
	}
	
//	@Override
//    public BookingDTO getById(Long id) {
//		Booking booking = bookingRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Booking is not found"));
//             
//		BookingDTO dto = new BookingDTO();
//	    dto.setBookingId(booking.getBookingId());
//	    dto.setCustomerName(booking.getCustomerName());
//	    dto.setPhoneNumber(booking.getPhoneNumber());
//	    dto.setCity(booking.getCity());
//	    dto.setArea(booking.getWorkLocationArea());
//	    dto.setBorewellType(booking.getBorewellType());
//	    dto.setDrillingSize(booking.getDrillingSize());
//	    dto.setPrice(booking.getPrice());
//	    dto.setBookingDate(booking.getBookingDate());
//	    dto.setStatus(booking.getStatus());
//	    dto.setOtherSize(booking.getOtherSize());
//
//	    return dto;
//    }

	@Override
	public BookingDTO getById(Long id) {
	    Booking booking = bookingRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

	    BookingDTO dto = new BookingDTO();
	    dto.setBookingId(booking.getBookingId());
	    dto.setCustomerName(booking.getCustomerName());
	    dto.setPhoneNumber(booking.getPhoneNumber());
	    dto.setCity(booking.getCity());
	    dto.setArea(booking.getWorkLocationArea());
	    dto.setBorewellType(booking.getBorewellType());
	    dto.setDrillingSize(booking.getDrillingSize());
	    dto.setPrice(booking.getPrice());
	    dto.setBookingDate(booking.getBookingDate());
	    dto.setStatus(booking.getStatus());
	    return dto;
	}
	
//	@Override
//	public void updateBooking(BookingDTO dto) {
//	    Booking booking = bookingRepo.findById(dto.getBookingId())
//	            .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + dto.getBookingId()));
//
//	    booking.setCustomerName(dto.getCustomerName());
//	    booking.setPhoneNumber(dto.getPhoneNumber());
//	    booking.setCity(dto.getCity());
//	    booking.setWorkLocationArea(dto.getArea());
//	    booking.setBorewellType(dto.getBorewellType());
//	    booking.setDrillingSize(dto.getDrillingSize());
//	    booking.setPrice(dto.getPrice());
//	    booking.setBookingDate(dto.getBookingDate());
//	    booking.setStatus(dto.getStatus());
//
//	    bookingRepo.save(booking);
//	}
	

    @Override
    public void deleteById(Long id) {
    	bookingRepo.deleteById(id);
    }

	@Override
	public BookingDTO getConfirmBookingDetails(BookingDTO bookingDto) {
		
		System.out.println("Customer Name -> " + bookingDto.getCustomerName());
	    System.out.println("Phone Number -> " + bookingDto.getPhoneNumber());

	    // ---- CITY ----
	    Long cityId = bookingDto.getCity().getCityId();
	    City city = cityRepository.findById(cityId).orElse(null);
	    System.out.println("City ID -> " + cityId);
	    System.out.println("City Name -> " + (city != null ? city.getCityName() : "null"));

	    // ---- AREA ----
	    Long areaId = bookingDto.getArea().getLocationAreaId();
	    WorkLocationArea area = workLocationAreaRepository.findById(areaId).orElse(null);
	    System.out.println("Area ID -> " + areaId);
	    System.out.println("Area Name -> " + (area != null ? area.getLocationAreaName() : "null"));

	    // ---- BOREWELL TYPE ----
	    BorewellType bw = borewellTypeRepository.findById(
	            bookingDto.getBorewellType().getBorewelTypeid()
	    ).orElse(null);
	    System.out.println("Borewell Type -> " + (bw != null ? bw.getName() : "null"));

	    // ---- COMPANY NAME ----
	    MaterialCompanyName comp = materialCompanyNameRepo.findById(
	            bookingDto.getCompanyName().getCompanyId()
	    ).orElse(null);
	    System.out.println("Company Name -> " + (comp != null ? comp.getCompanyName() : "null"));

	    // ---- UPDATE DTO WITH FULL OBJECTS ----
	    bookingDto.setCity(city);
	    bookingDto.setArea(area);
	    bookingDto.setBorewellType(bw);
	    bookingDto.setCompanyName(comp);

	    System.out.println("Pipe Quality -> " + bookingDto.getPipeQuality());
	    System.out.println("Drilling Size -> " + bookingDto.getDrillingSize());
	    System.out.println("Price -> " + bookingDto.getPrice());
	    System.out.println("Casing Price -> " + bookingDto.getCasingPrice());
	    System.out.println("Booking Date -> " + bookingDto.getBookingDate());
		if ((bookingDto.getDrillingSize().equalsIgnoreCase("8") || bookingDto.getDrillingSize().equalsIgnoreCase("5") || bookingDto.getDrillingSize().equalsIgnoreCase("6")
				|| bookingDto.getDrillingSize().equalsIgnoreCase("4") || bookingDto.getDrillingSize().equalsIgnoreCase("3"))
				&& (bw.getName().equalsIgnoreCase("DTH") || bw.getName().equalsIgnoreCase("IN-WELL"))) {

			AreaWiseItemRequirementDTO itemRequirement = areaWiseItemRequirementService.getRequirementItemDetails(
					String.valueOf(cityId), String.valueOf(areaId), String.valueOf(bw.getBorewelTypeid()),
					bookingDto.getDrillingSize());

			bookingDto.setTotalDrillingUnit(safeInt(itemRequirement.getReqDrillingDepth()));
			bookingDto.setTotalDrillingPrice(bookingDto.getTotalDrillingUnit() * safeDouble(bookingDto.getPrice()));

			bookingDto.setTotalUnitCasing(safeInt(itemRequirement.getReqCasingPipe()));
			bookingDto.setTotalCasingPrice(safeDouble(bookingDto.getCasingPrice()) * bookingDto.getTotalUnitCasing());

			RawMaterialDTO casingTransporting = rawMaterialService.findMaterialDetailsByMaterialName("CASING TRANSPORTING");
			bookingDto.setCasingTransporting(casingTransporting != null && casingTransporting.getMaterialPrice() != null
					? casingTransporting.getMaterialPrice() : 0.0);

			RawMaterialDTO casingSlottingDtls = rawMaterialService
					.findMaterialDetailsByMaterialName("CASING PIPIE SLOTING");
			RawMaterialDTO boreWashing = rawMaterialService.findMaterialDetailsByMaterialName("BORE WASHING");

			if (eqIgnoreCase(bw.getName(), "ROTTARY") || eqIgnoreCase(bw.getName(), "CALLIX")
					|| eqIgnoreCase(bw.getName(), "DTH-ROTTARY")) {

				RawMaterialDTO gravelDtls = rawMaterialService.findMaterialDetailsByMaterialName("GRAVEL");
				RawMaterialDTO modPowderDtls = rawMaterialService.findMaterialDetailsByMaterialName("MOD POWDER");

				bookingDto.setTotalUnitGravel(safeInt(itemRequirement.getReqGravel()));
				bookingDto.setGravelPricePerUnit(gravelDtls.getMaterialPrice());
				bookingDto.setTotalUnitGravelPrice(safeDouble(gravelDtls != null ? gravelDtls.getMaterialPrice() : null)
						* bookingDto.getTotalUnitGravel());

				bookingDto.setTotalUnitModPowder(safeInt(itemRequirement.getReqModPowder()));
				bookingDto.setModPowderPerUnit(modPowderDtls.getMaterialPrice());
				bookingDto.setTotalModPowderPrice(
						safeDouble(modPowderDtls != null ? modPowderDtls.getMaterialPrice() : null)
								* bookingDto.getTotalUnitModPowder());

				bookingDto.setTotalWashingUnit(safeInt(itemRequirement.getWashingHours()));
				bookingDto.setTotalWashingPrice(safeDouble(boreWashing != null ? boreWashing.getMaterialPrice() : null)
						* bookingDto.getTotalWashingUnit());

				bookingDto.setTotalUnitSloting(safeInt(itemRequirement.getNoOfSloting()));
				bookingDto.setCasingSlotingPerUnit(casingSlottingDtls.getMaterialPrice());
				bookingDto.setTotalSlotingPrice(
						(safeDouble(casingSlottingDtls != null ? casingSlottingDtls.getMaterialPrice() : null) + 200)
								* bookingDto.getTotalUnitSloting());

			} else {
				RawMaterialDTO masterCasingDtls = null;
				RawMaterialDTO masterCasing10Dtls = null;
				RawMaterialDTO masterCasing12Dtls = null;
				RawMaterialDTO masterCasing14Dtls = null;
				if (bookingDto.getDrillingSize().equals("5") || bookingDto.getDrillingSize().equals("6")) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("8", "4kg");
					
					bookingDto.setTotalUnitMasterCasing(itemRequirement.getReqMasterCasingPipe());
					bookingDto.setMasterCasingPricePerUnit(masterCasingDtls.getMaterialPrice());
					

					if (safeInt(itemRequirement.getReq10MasterCasingPipe()) > 0) {

						masterCasing10Dtls = rawMaterialService.findMasterCasingDetails("10", "4kg");

						bookingDto.setTotalUnitMC10(safeInt(itemRequirement.getReq10MasterCasingPipe()));

						bookingDto.setTotalMaster10CasingPrice(
								safeDouble(masterCasing10Dtls != null ? masterCasing10Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMC10());
					} else {
						bookingDto.setTotalUnitMC10(0);
						bookingDto.setTotalMaster10CasingPrice(0.0);
					}
				}
				else if (bookingDto.getDrillingSize().equals("8") || bookingDto.getDrillingSize().equals("10")) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("10");
					bookingDto.setTotalUnitMC10(itemRequirement.getReq10MasterCasingPipe());
					bookingDto.setMc10PricePerUnit(masterCasingDtls.getMaterialPrice());
					

					if (safeInt(itemRequirement.getReq12MasterCasingPipe()) > 0) {

						masterCasing12Dtls = rawMaterialService.findMasterCasingDetails("12");

						bookingDto.setTotalUnitMC12(safeInt(itemRequirement.getReq12MasterCasingPipe()));
						bookingDto.setMc12PricePerUnit(masterCasing12Dtls.getMaterialPrice());

						bookingDto.setTotalMaster12CasingPrice(
								safeDouble(masterCasing12Dtls != null ? masterCasing12Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMC12());
					} else {
						bookingDto.setTotalUnitMC12(0);
						bookingDto.setTotalMaster12CasingPrice(0.0);
					}
					// 14 inch. Master casing 
					if (safeInt(itemRequirement.getReq14MasterCasingPipe()) > 0) {

						masterCasing14Dtls = rawMaterialService.findMasterCasingDetails("14");

						bookingDto.setTotalUnitMC14(safeInt(itemRequirement.getReq12MasterCasingPipe()));
						bookingDto.setMc14PricePerUnit(masterCasing14Dtls.getMaterialPrice());

						bookingDto.setTotalMasterCasing14Price(
								safeDouble(masterCasing14Dtls != null ? masterCasing14Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMC14());
					} else {
						bookingDto.setTotalUnitMC14(0);
						bookingDto.setTotalMasterCasing14Price(0.0);
					}
				}

				bookingDto.setTotalUnitMasterCasing(safeInt(itemRequirement.getReqMasterCasingPipe()));

				bookingDto.setMasterCasingPricePerUnit(masterCasingDtls.getMaterialPrice());
				bookingDto.setTotalMasterCasingPrice(
						safeDouble(masterCasingDtls != null ? masterCasingDtls.getMaterialPrice() : null)
								* bookingDto.getTotalUnitMasterCasing());

				bookingDto.setTotalUnitSloting(safeInt(itemRequirement.getNoOfSloting()));
				bookingDto.setCasingSlotingPerUnit(casingSlottingDtls.getMaterialPrice());
				bookingDto.setTotalSlotingPrice(
						safeDouble(casingSlottingDtls != null ? casingSlottingDtls.getMaterialPrice() : null)
								* bookingDto.getTotalUnitSloting());
                bookingDto.setWashingPricePerUnit(boreWashing.getMaterialPrice());
                bookingDto.setWashingPricePerUnit(boreWashing.getMaterialPrice());
				bookingDto
						.setTotalWashingPrice(safeDouble(boreWashing != null ? boreWashing.getMaterialPrice() : null));
			}

			bookingDto.setOtherItemDetails("NA");
			bookingDto.setOtherItemPrice(0.0);
		}
		
		Double totalAmount = bookingDto.getTotalDrillingPrice()+bookingDto.getTotalCasingPrice()+bookingDto.getTotalMasterCasingPrice()+bookingDto.getTotalSlotingPrice()+
				bookingDto.getCasingTransporting()+bookingDto.getTotalWashingPrice();
		Discount discountDetails = discountRepo.getDiscount("normal");
		Integer discount = Integer.parseInt(discountDetails.getDiscount());
		System.out.println("discount ->"+discount);
		Double lessDiscount = totalAmount * discount / 100;
		System.out.println("lessDiscount -> "+lessDiscount);
		Double totalAmtAfterDiscount = totalAmount-lessDiscount;
		Double cgst = totalAmtAfterDiscount * 6 / 100;
		Double sgst = totalAmtAfterDiscount * 6 / 100;
		Double grandTotal = totalAmtAfterDiscount+cgst+sgst;
		grandTotal = Math.round(grandTotal * 10.0) / 10.0;
		
		bookingDto.setTotAmtBeforeDiscount(totalAmount);
		bookingDto.setTotDiscountAmt(lessDiscount);
		bookingDto.setDiscount(discountDetails);
		bookingDto.setCgstPercent(6);
		bookingDto.setSgstPercent(6);
		bookingDto.setTotalAmtAfterDiscount(totalAmtAfterDiscount);
		bookingDto.setCgst(cgst);
		bookingDto.setSgst(sgst);
		
		bookingDto.setGrandTotal(grandTotal);
		
		return bookingDto;
	}
	

    private double safeDouble(Double val) {
        return val != null ? val : 0.0;
    }

    private int safeInt(Integer val) {
        return val != null ? val : 0;
    }

    private boolean eqIgnoreCase(String a, String b) {
        return a != null && b != null && a.equalsIgnoreCase(b);
    }

	@Override
	public BookingDTO getBookingDtlsById(Long bookingId) {
		
		Booking booking = bookingRepo.findById(bookingId)
		        .orElseThrow(() -> new RuntimeException("Booking not found with id " + bookingId));
		
		Discount discount = discountRepo.findById(booking.getDiscount().getDiscountId())
			    .orElseThrow(() -> new RuntimeException("Discount not found"));
		
		BookingDTO bookingDto = new BookingDTO();
		bookingDto.setCity(booking.getCity());
		bookingDto.setArea(booking.getWorkLocationArea());
		bookingDto.setBorewellType(booking.getBorewellType());
		bookingDto.setCompanyName(booking.getCompanyName());
		
		Long cityId = bookingDto.getCity().getCityId();
	    City city = cityRepository.findById(cityId).orElse(null);
	    System.out.println("City ID -> " + cityId);
	    System.out.println("City Name -> " + (city != null ? city.getCityName() : "null"));

	    // ---- AREA ----
	    Long areaId = bookingDto.getArea().getLocationAreaId();
	    WorkLocationArea area = workLocationAreaRepository.findById(areaId).orElse(null);
	    System.out.println("Area ID -> " + areaId);
	    System.out.println("Area Name -> " + (area != null ? area.getLocationAreaName() : "null"));

	    // ---- BOREWELL TYPE ----
	    BorewellType bw = borewellTypeRepository.findById(
	            bookingDto.getBorewellType().getBorewelTypeid()
	    ).orElse(null);
	    System.out.println("Borewell Type -> " + (bw != null ? bw.getName() : "null"));

	    // ---- COMPANY NAME ----
	    MaterialCompanyName comp = materialCompanyNameRepo.findById(
	            bookingDto.getCompanyName().getCompanyId()
	    ).orElse(null);
	    System.out.println("Company Name -> " + (comp != null ? comp.getCompanyName() : "null"));

	    // ---- UPDATE DTO WITH FULL OBJECTS ----
	    bookingDto.setCity(city);
	    bookingDto.setArea(area);
	    bookingDto.setBorewellType(bw);
	    bookingDto.setCompanyName(comp);

	    System.out.println("Pipe Quality -> " + booking.getPipeQuality());
	    System.out.println("Drilling Size -> " + booking.getDrillingSize());
	    System.out.println("Price -> " + booking.getPrice());
	    System.out.println("Casing Price -> " + booking.getCasingPrice());
	    System.out.println("Booking Date -> " + booking.getBookingDate());
	    String drillingSize = booking.getDrillingSize();
	    String boreTypeName = bw != null ? bw.getName() : null;
	    bookingDto.setBookingDate(booking.getBookingDate());
	    bookingDto.setPhoneNumber(booking.getPhoneNumber());
	    bookingDto.setCustomerName(booking.getCustomerName());
	    bookingDto.setDrillingSize(drillingSize);
	    if (drillingSize != null &&
	    	    ("8".equalsIgnoreCase(drillingSize)
	    	        || "5".equalsIgnoreCase(drillingSize)
	    	        || "6".equalsIgnoreCase(drillingSize)
	    	        || "4".equalsIgnoreCase(drillingSize)
	    	        || "3".equalsIgnoreCase(drillingSize))
	    	    && ("DTH".equalsIgnoreCase(boreTypeName)
	    	        || "IN-WELL".equalsIgnoreCase(boreTypeName))) {

			AreaWiseItemRequirementDTO itemRequirement = areaWiseItemRequirementService.getRequirementItemDetails(
					String.valueOf(cityId), String.valueOf(areaId), String.valueOf(bw.getBorewelTypeid()),
					booking.getDrillingSize());

			bookingDto.setTotalDrillingUnit(safeInt(itemRequirement.getReqDrillingDepth()));
			bookingDto.setPrice(booking.getPrice());
			bookingDto.setTotalDrillingPrice(itemRequirement.getReqDrillingDepth() * safeDouble(booking.getPrice()));

			bookingDto.setTotalUnitCasing(safeInt(itemRequirement.getReqCasingPipe()));
			bookingDto.setCasingPrice(booking.getCasingPrice());
			//=====
			//=======
			
			
			bookingDto.setTotalCasingPrice(safeDouble(bookingDto.getCasingPrice()) * bookingDto.getTotalUnitCasing());

//			RawMaterialDTO casingTransporting = rawMaterialService.findMaterialDetailsByMaterialName("CASING TRANSPORTING");
//			Double casingTransportingPrice = casingTransporting != null && casingTransporting.getMaterialPrice() != null
//			        ? casingTransporting.getMaterialPrice() : 0.0;
			bookingDto.setCasingTransporting(booking.getCasingTransportingPrice());

			RawMaterialDTO casingSlottingDtls = rawMaterialService
					.findMaterialDetailsByMaterialName("CASING PIPIE SLOTING");
			RawMaterialDTO boreWashing = rawMaterialService.findMaterialDetailsByMaterialName("BORE WASHING");

			if (eqIgnoreCase(bw.getName(), "ROTTARY") || eqIgnoreCase(bw.getName(), "CALLIX")
					|| eqIgnoreCase(bw.getName(), "DTH-ROTTARY")) {

				RawMaterialDTO gravelDtls = rawMaterialService.findMaterialDetailsByMaterialName("GRAVEL");
				RawMaterialDTO modPowderDtls = rawMaterialService.findMaterialDetailsByMaterialName("MOD POWDER");

				bookingDto.setTotalUnitGravel(safeInt(itemRequirement.getReqGravel()));
				bookingDto.setTotalUnitGravelPrice(safeDouble(gravelDtls != null ? gravelDtls.getMaterialPrice() : null)
						* bookingDto.getTotalUnitGravel());

				bookingDto.setTotalUnitModPowder(safeInt(itemRequirement.getReqModPowder()));
				bookingDto.setTotalModPowderPrice(
						safeDouble(modPowderDtls != null ? modPowderDtls.getMaterialPrice() : null)
								* bookingDto.getTotalUnitModPowder());

				bookingDto.setTotalWashingUnit(safeInt(itemRequirement.getWashingHours()));
				bookingDto.setTotalWashingPrice(safeDouble(boreWashing != null ? boreWashing.getMaterialPrice() : null)
						* bookingDto.getTotalWashingUnit());

				bookingDto.setTotalUnitSloting(safeInt(itemRequirement.getNoOfSloting()));
				bookingDto.setTotalSlotingPrice(
						(safeDouble(casingSlottingDtls != null ? casingSlottingDtls.getMaterialPrice() : null) + 200)
								* bookingDto.getTotalUnitSloting());

			} else {
				System.out.println("Execute else block");
				RawMaterialDTO masterCasingDtls = null;
				RawMaterialDTO masterCasing12Dtls = null;
				RawMaterialDTO masterCasing10Dtls = null;
				if ("5".equals(drillingSize) || "6".equals(drillingSize)) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("8", "4kg");
					System.out.println("masterCasingDtls -> "+masterCasingDtls.getMaterialPrice() * itemRequirement.getReqMasterCasingPipe());
					bookingDto.setMasterCasingPricePerUnit(masterCasingDtls.getMaterialPrice());
					bookingDto.setTotalMasterCasingPrice(masterCasingDtls.getMaterialPrice() * itemRequirement.getReqMasterCasingPipe());
					
					//2.5kg master casing
					bookingDto.setPricePerUnit2_5kg(booking.getPricePerUnit2_5kg());
					bookingDto.setTotalUnit2_5kg(booking.getTotalUnit2_5kg());
					bookingDto.setTotal2_5kgPrice(booking.getTotal2_5kgPrice());
					//6kg master casing
					bookingDto.setPricePerUnit6kg(booking.getPricePerUnit6kg());
					bookingDto.setTotalUnit6kg(booking.getTotalUnit6kg());
					bookingDto.setTotal6kgPrice(booking.getTotal6kgPrice());
					
					System.out.println("2.5 per unit price ->"+booking.getPricePerUnit2_5kg());
					System.out.println("2.5 total Unit  ->"+booking.getTotalUnit2_5kg());
					System.out.println("2.5 total Unit price  ->"+booking.getTotal2_5kgPrice());
					
					System.out.println("6 per unit price ->"+booking.getPricePerUnit6kg());
					System.out.println("6 total Unit  ->"+booking.getTotalUnit6kg());
					System.out.println("6 total Unit price  ->"+booking.getTotal6kgPrice());
					
					bookingDto.setMc10PricePerUnit(booking.getMc10PricePerUnit());
					bookingDto.setTotalUnitMC10(booking.getTotalUnitMC10());
					Double totalPriceOfMC10 = booking.getMc10PricePerUnit() * booking.getTotalUnitMC10();
					bookingDto.setTotalMaster10CasingPrice(totalPriceOfMC10);
					
					bookingDto.setMc12PricePerUnit(booking.getMc12PricePerUnit());
					bookingDto.setTotalUnitMC12(booking.getTotalUnitMC12());

					Double totalPriceOfMC12 = null;
					if (booking.getMc12PricePerUnit() != null && booking.getTotalUnitMC12() != null) {
					    totalPriceOfMC12 = booking.getMc12PricePerUnit() * booking.getTotalUnitMC12();
					}
					bookingDto.setTotalMaster12CasingPrice(totalPriceOfMC12);

//					if (safeInt(itemRequirement.getReq10MasterCasingPipe()) > 0) {
//
//						masterCasing10Dtls = rawMaterialService.findMasterCasingDetails("10");
//
//						bookingDto.setTotalUnitMC10(safeInt(itemRequirement.getReq10MasterCasingPipe()));
//
//						bookingDto.setTotalMaster10CasingPrice(
//								safeDouble(masterCasing10Dtls != null ? masterCasing10Dtls.getMaterialPrice() : null)
//										* bookingDto.getTotalUnitMC10());
//					} else {
//						bookingDto.setTotalUnitMC10(0);
//						bookingDto.setTotalMaster10CasingPrice(0.0);
//					}
				}
				else if ("8".equals(drillingSize)) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("10", "6kg");

					if (safeInt(itemRequirement.getReq12MasterCasingPipe()) > 0) {

						masterCasing12Dtls = rawMaterialService.findMasterCasingDetails("10");

						bookingDto.setTotalUnitMC12(safeInt(itemRequirement.getReq12MasterCasingPipe()));

						bookingDto.setTotalMaster12CasingPrice(
								safeDouble(masterCasing12Dtls != null ? masterCasing12Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMC12());
					} else {
						bookingDto.setTotalUnitMC12(0);
						bookingDto.setTotalMaster12CasingPrice(0.0);
					}
				}

				bookingDto.setTotalUnitMasterCasing(safeInt(itemRequirement.getReqMasterCasingPipe()));

				//===####
				bookingDto.setTotalMasterCasingPrice(
					    safeDouble(masterCasingDtls != null ? masterCasingDtls.getMaterialPrice() : null)
					        * bookingDto.getTotalUnitMasterCasing()
					);
				bookingDto.setTotalMasterCasingPrice(
						safeDouble(masterCasingDtls != null ? masterCasingDtls.getMaterialPrice() : null)
								* bookingDto.getTotalUnitMasterCasing());
               //===####
				bookingDto.setTotalUnitSloting(safeInt(itemRequirement.getNoOfSloting()));
				//===####
				bookingDto.setCasingSlotingPerUnit(
				        casingSlottingDtls != null && casingSlottingDtls.getMaterialPrice() != null
				                ? casingSlottingDtls.getMaterialPrice()
				                : 0.0
				);
				
				bookingDto.setTotalSlotingPrice(
						safeDouble(casingSlottingDtls != null ? casingSlottingDtls.getMaterialPrice() : null)
								* bookingDto.getTotalUnitSloting());
				
				bookingDto.setWashingPricePerUnit(
				        boreWashing != null && boreWashing.getMaterialPrice() != null
				                ? boreWashing.getMaterialPrice()
				                : 0.0
				);
				//===####
				bookingDto.setTotalWashingPrice(
					    safeDouble(boreWashing != null ? boreWashing.getMaterialPrice() : null) 
					    * (booking.getTotalWashingUnit() != null ? booking.getTotalWashingUnit() : 0)
					);
				bookingDto.setTotalWashingUnit(booking.getTotalWashingUnit());
			}

			bookingDto.setOtherItemDetails("NA");
			bookingDto.setOtherItemPrice(0.0);
		}
		
		Double totalAmount = bookingDto.getTotalDrillingPrice()+bookingDto.getTotalCasingPrice()+bookingDto.getTotalMasterCasingPrice()+bookingDto.getTotalSlotingPrice()+
				bookingDto.getCasingTransporting()+bookingDto.getTotalWashingPrice()+bookingDto.getTotal2_5kgPrice()+bookingDto.getTotal6kgPrice()+
				Optional.ofNullable(bookingDto.getTotalMaster10CasingPrice()).orElse(0.0) +
			    Optional.ofNullable(bookingDto.getTotalMaster12CasingPrice()).orElse(0.0) +
			    Optional.ofNullable(bookingDto.getTotalMasterCasing14Price()).orElse(0.0);
		
		Integer discountPer = Integer.parseInt(discount.getDiscount());
		bookingDto.setDiscountPer(discountPer);
		
		Double lessDiscount = totalAmount * discountPer / 100;
		System.out.println("lessDiscount -> "+lessDiscount);
		Double totalAmtAfterDiscount = totalAmount-lessDiscount;
		Double cgst = totalAmtAfterDiscount * 6 / 100;
		Double sgst = totalAmtAfterDiscount * 6 / 100;
		Double grandTotal = totalAmtAfterDiscount+cgst+sgst;
		grandTotal = Math.round(grandTotal * 10.0) / 10.0;
		
		bookingDto.setTotAmtBeforeDiscount(totalAmount);
		bookingDto.setTotDiscountAmt(lessDiscount);
		bookingDto.setTotalAmtAfterDiscount(totalAmtAfterDiscount);
		bookingDto.setCgst(cgst);
		bookingDto.setSgst(sgst);
		bookingDto.setGrandTotal(grandTotal);
		
		return bookingDto;
	}
	
	@Override
    public BookingDTO getBookingById(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return modelMapper.map(booking, BookingDTO.class);
    }
    
	@Override
	@Transactional
	public BookingDTO updateBookingDtls(BookingDTO bookingDto) {

	    // Fetch existing booking
	    Booking existingBooking = bookingRepo.findById(bookingDto.getBookingId())
	        .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

	    // Create audit record before updating
	    createAuditRecord(existingBooking, bookingDto);

	    // ✅ First recalculate using DTO
	    recalculateBookingPrices(bookingDto);

	    // ✅ Then map calculated values into entity
	    modelMapper.map(bookingDto, existingBooking);

	    // Set last updated date
	    existingBooking.setLastUpdatedDate(LocalDateTime.now());

	    // Save updated booking
	    Booking updatedBooking = bookingRepo.save(existingBooking);

	    return modelMapper.map(updatedBooking, BookingDTO.class);
	}

    
    @Override
    public List<BookingAuditDTO> getBookingAuditHistory(Long bookingId) {
        List<BookingAudit> auditRecords = auditRepository.findByBookingIdOrderByChangedDateDesc(bookingId);
        return auditRecords.stream()
            .map(audit -> modelMapper.map(audit, BookingAuditDTO.class))
            .collect(Collectors.toList());
    }
    
    private void createAuditRecord(Booking oldBooking, BookingDTO newBooking) {
        BookingAudit audit = new BookingAudit();
        audit.setBookingId(oldBooking.getBookingId());
        audit.setChangedBy(getCurrentUser()); // Implement user tracking
        audit.setChangedDate(LocalDateTime.now());
        
        // Capture changes
        List<String> changes = detectChanges(oldBooking, newBooking);
        audit.setChanges(changes);
        
        // Create summary
        String summary = String.format("Booking updated - %d change(s) made", changes.size());
        audit.setChangeSummary(summary);
        
        // Store old state
        audit.setOldBookingState(convertToJson(oldBooking));
        
        auditRepository.save(audit);
    }
    
    private List<String> detectChanges(Booking oldBooking, BookingDTO newBooking) {
        List<String> changes = new ArrayList<>();
        
        if (!oldBooking.getCustomerName().equals(newBooking.getCustomerName())) {
            changes.add(String.format("Customer name changed from '%s' to '%s'", 
                oldBooking.getCustomerName(), newBooking.getCustomerName()));
        }
        
        if (!oldBooking.getPhoneNumber().equals(newBooking.getPhoneNumber())) {
            changes.add(String.format("Phone number changed from '%s' to '%s'", 
                oldBooking.getPhoneNumber(), newBooking.getPhoneNumber()));
        }
        
        if (oldBooking.getCity() != null && newBooking.getCity() != null &&
            !oldBooking.getCity().getCityId().equals(newBooking.getCity().getCityId())) {
            changes.add(String.format("City changed from '%s' to '%s'",
                oldBooking.getCity().getCityName(), newBooking.getCity().getCityName()));
        }
        
        if (oldBooking.getWorkLocationArea() != null && newBooking.getArea() != null &&
            !oldBooking.getWorkLocationArea().getLocationAreaId().equals(newBooking.getArea().getLocationAreaId())) {
            changes.add(String.format("Area changed from '%s' to '%s'",
                oldBooking.getWorkLocationArea().getLocationAreaName(), newBooking.getArea().getLocationAreaName()));
        }
        
        if (oldBooking.getBorewellType() != null && newBooking.getBorewellType() != null &&
            !oldBooking.getBorewellType().getBorewelTypeid().equals(newBooking.getBorewellType().getBorewelTypeid())) {
            changes.add(String.format("Borewell type changed from '%s' to '%s'",
                oldBooking.getBorewellType().getName(), newBooking.getBorewellType().getName()));
        }
        
        if (oldBooking.getDrillingSize() != null && !oldBooking.getDrillingSize().equals(newBooking.getDrillingSize())) {
            changes.add(String.format("Drilling size changed from '%s' to '%s'",
                oldBooking.getDrillingSize(), newBooking.getDrillingSize()));
        }
        
        // Compare prices if changed
        if (oldBooking.getPrice() != null && newBooking.getPrice() != null &&
            !oldBooking.getPrice().equals(newBooking.getPrice())) {
            changes.add(String.format("Drilling price changed from ₹%.2f to ₹%.2f",
                oldBooking.getPrice(), newBooking.getPrice()));
        }
        
        if (oldBooking.getBookingDate() != null && newBooking.getBookingDate() != null &&
            !oldBooking.getBookingDate().equals(newBooking.getBookingDate())) {
            changes.add(String.format("Booking date changed from %s to %s",
                oldBooking.getBookingDate(), newBooking.getBookingDate()));
        }
        
        return changes;
    }
    
    private void recalculateBookingPrices(BookingDTO booking) {
        // Recalculate total drilling price
        if (booking.getPrice() != null && booking.getTotalDrillingUnit() != null) {
            booking.setTotalDrillingPrice(booking.getPrice() * booking.getTotalDrillingUnit());
        }
        
        // Recalculate casing price
        if (booking.getCasingPrice() != null && booking.getTotalUnitCasing() != null) {
            booking.setTotalCasingPrice(booking.getCasingPrice() * booking.getTotalUnitCasing());
        }
        
        // Recalculate total before discount
        double totalBeforeDiscount = calculateSubtotal(booking);
        booking.setTotAmtBeforeDiscount(totalBeforeDiscount);
        
        // Apply discount
        double discount = totalBeforeDiscount * 0.15; // 15% discount
        booking.setTotDiscountAmt(discount);
        
        // Amount after discount
        double afterDiscount = totalBeforeDiscount - discount;
        booking.setTotalAmtAfterDiscount(afterDiscount);
        
        // Apply taxes (CGST 6%, SGST 6%)
        double cgst = afterDiscount * 0.06;
        double sgst = afterDiscount * 0.06;
        booking.setCgst(cgst);
        booking.setSgst(sgst);
        
        // Grand total
        booking.setGrandTotal(afterDiscount + cgst + sgst);
    }
    
    private double calculateSubtotal(BookingDTO booking) {
        double subtotal = 0.0;
        
        subtotal += booking.getTotalDrillingPrice() != null ? booking.getTotalDrillingPrice() : 0;
        subtotal += booking.getTotalCasingPrice() != null ? booking.getTotalCasingPrice() : 0;
        subtotal += booking.getTotalMasterCasingPrice() != null ? booking.getTotalMasterCasingPrice() : 0;
        subtotal += booking.getTotalSlotingPrice() != null ? booking.getTotalSlotingPrice() : 0;
        subtotal += booking.getTotalWashingPrice() != null ? booking.getTotalWashingPrice() : 0;
        subtotal += booking.getCasingTransporting() != null ? booking.getCasingTransporting() : 0;
        subtotal += booking.getTotalUnitGravelPrice() != null ? booking.getTotalUnitGravelPrice() : 0;
        subtotal += booking.getTotalModPowderPrice() != null ? booking.getTotalModPowderPrice() : 0;
        
        return subtotal;
    }
    
    private String convertToJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "{}";
        }
    }
    
    private String getCurrentUser() {
        // Implement your security context to get current user
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null && context.getAuthentication() != null) {
            return context.getAuthentication().getName();
        }
        return "System";
    }
    
 // In your BookingService class
//    public BookingDTO getBookingById(Long id) {
//        Booking booking = bookingRepo.findById(id)
//            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
//        return mapToDTO(booking);
//    }

    public BookingDTO updateBooking(BookingDTO dto) {
        // Check if booking exists
        Booking existingBooking = bookingRepo.findById(dto.getBookingId())
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + dto.getBookingId()));
        
        // Map the DTO to entity (update all fields)
        Booking booking = mapToEntity(dto);
        
        // Get references for related entities
        City city = entityManager.getReference(City.class, dto.getCity().getCityId());
        WorkLocationArea area = entityManager.getReference(WorkLocationArea.class, dto.getArea().getLocationAreaId());
        BorewellType borewellType = null;
        
        if (dto.getBorewellType() != null && dto.getBorewellType().getBorewelTypeid() != null) {
            borewellType = entityManager.getReference(BorewellType.class, dto.getBorewellType().getBorewelTypeid());
        }
        
        // Set the relationships
        booking.setCity(city);
        booking.setWorkLocationArea(area);
        booking.setBorewellType(borewellType);
        
        // Preserve the original booking ID and status if needed
        booking.setBookingId(existingBooking.getBookingId());
        booking.setStatus(existingBooking.getStatus()); // Keep existing status or allow update
        
        booking.setCasingPrice(dto.getCasingPrice());
	    booking.setPipeQuality(dto.getPipeQuality());
	    booking.setCompanyName(dto.getCompanyName());
	    
	    booking.setTotalDrillingUnit(dto.getTotalDrillingUnit());
	    booking.setTotalUnitCasing(dto.getTotalUnitCasing());
	    booking.setTotalUnitMasterCasing(dto.getTotalUnitMasterCasing());
	    booking.setMasterCasingPricePerUnit(dto.getMasterCasingPricePerUnit());
	    booking.setCasingTransportingPrice(dto.getCasingTransporting());
	    booking.setTotalUnitSloting(dto.getTotalUnitSloting());
	    booking.setCasingSlotingPricePerUnit(dto.getCasingSlotingPerUnit());
	    booking.setTotalUnitModPowder(dto.getTotalUnitModPowder());
	    
	    booking.setModPowderPricePerUnit(dto.getModPowderPerUnit());
	    booking.setTotalUnitGravel(dto.getTotalUnitGravel());
	    booking.setGravelPricePerUnit(dto.getGravelPricePerUnit());
	    booking.setTotalWashingUnit(dto.getTotalWashingUnit());
	    booking.setWashingPricePerUnit(dto.getWashingPricePerUnit());
	    booking.setTotalUnitMC10(dto.getTotalUnitMC10());
	    booking.setMc10PricePerUnit(dto.getMc10PricePerUnit());
	    booking.setTotalUnitMC12(dto.getTotalUnitMC12());
	    
	    booking.setMc12PricePerUnit(dto.getMc12PricePerUnit());
	    booking.setTotalUnitMC14(dto.getTotalUnitMC14());
	    booking.setMc14PricePerUnit(dto.getMc14PricePerUnit());
	    booking.setDiscount(dto.getDiscount());
	    System.out.println("Discount percent -> "+dto.getDiscount());
	    booking.setCgstPercent(dto.getCgstPercent());
	    booking.setSgstPercent(dto.getSgstPercent());
	    
	    booking.setTotalUnit2_5kg(dto.getTotalUnit2_5kg());
	    booking.setPricePerUnit2_5kg(dto.getPricePerUnit2_5kg());
	    booking.setTotal2_5kgPrice(dto.getTotal2_5kgPrice());
	    
	    booking.setTotalUnit6kg(dto.getTotalUnit6kg());
	    booking.setPricePerUnit6kg(dto.getPricePerUnit6kg());
	    booking.setTotal6kgPrice(dto.getTotal6kgPrice());
        
        // Save the updated booking
        Booking updated = bookingRepo.save(booking);
        return mapToDTO(updated);
    }

}
