package mmb.serviceImpl;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import mmb.dto.AreaWiseItemRequirementDTO;
import mmb.dto.BookingDTO;
import mmb.dto.BorewellTypeDTO;
import mmb.dto.DrillingPriceChartDTO;
import mmb.dto.RawMaterialDTO;
import mmb.model.Booking;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.DrillingPriceChart;
import mmb.model.MaterialCompanyName;
import mmb.model.WorkLocationArea;
import mmb.repository.BookingRepository;
import mmb.repository.BorewellTypeRepository;
import mmb.repository.CityRepository;
import mmb.repository.DrillingPriceChartRepository;
import mmb.repository.MaterialCompanyNameRepo;
import mmb.repository.WorkLocationAreaRepository;
import mmb.service.AreaWiseItemRequirementService;
import mmb.service.BookingService;
import mmb.service.BorewellTypeService;
import mmb.service.RawMaterialService;
import mmb.service.UserService;
import mmb.service.WorkLocationAreaService;

@Service
public class BookingServiceImpl implements BookingService {
	
	@Autowired
	private EntityManager entityManager;

	private final BookingRepository bookingRepo;

	public BookingServiceImpl(BookingRepository bookingRepo) {
		this.bookingRepo = bookingRepo;
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
	
	@Override
	public void updateBooking(BookingDTO dto) {
	    Booking booking = bookingRepo.findById(dto.getBookingId())
	            .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + dto.getBookingId()));

	    booking.setCustomerName(dto.getCustomerName());
	    booking.setPhoneNumber(dto.getPhoneNumber());
	    booking.setCity(dto.getCity());
	    booking.setWorkLocationArea(dto.getArea());
	    booking.setBorewellType(dto.getBorewellType());
	    booking.setDrillingSize(dto.getDrillingSize());
	    booking.setPrice(dto.getPrice());
	    booking.setBookingDate(dto.getBookingDate());
	    booking.setStatus(dto.getStatus());

	    bookingRepo.save(booking);
	}
	

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
				RawMaterialDTO masterCasingDtls = null;
				RawMaterialDTO masterCasing12Dtls = null;
				RawMaterialDTO masterCasing10Dtls = null;
				if (bookingDto.getDrillingSize().equals("5") || bookingDto.getDrillingSize().equals("6")) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("8", "4kg");

					if (safeInt(itemRequirement.getReq10MasterCasingPipe()) > 0) {

						masterCasing10Dtls = rawMaterialService.findMasterCasingDetails("10");

						bookingDto.setTotalUnitMasterCasing10(safeInt(itemRequirement.getReq10MasterCasingPipe()));

						bookingDto.setTotalMaster10CasingPrice(
								safeDouble(masterCasing10Dtls != null ? masterCasing10Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMasterCasing10());
					} else {
						bookingDto.setTotalUnitMasterCasing10(0);
						bookingDto.setTotalMaster10CasingPrice(0.0);
					}
				}
				else if (bookingDto.getDrillingSize().equals("8")) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("10", "6kg");

					if (safeInt(itemRequirement.getReq12MasterCasingPipe()) > 0) {

						masterCasing12Dtls = rawMaterialService.findMasterCasingDetails("10");

						bookingDto.setTotalUnitMasterCasing12(safeInt(itemRequirement.getReq12MasterCasingPipe()));

						bookingDto.setTotalMaster12CasingPrice(
								safeDouble(masterCasing12Dtls != null ? masterCasing12Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMasterCasing12());
					} else {
						bookingDto.setTotalUnitMasterCasing12(0);
						bookingDto.setTotalMaster12CasingPrice(0.0);
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
				bookingDto
						.setTotalWashingPrice(safeDouble(boreWashing != null ? boreWashing.getMaterialPrice() : null));
			}

			bookingDto.setOtherItemDetails("NA");
			bookingDto.setOtherItemPrice(0.0);
		}
		
		Double totalAmount = bookingDto.getTotalDrillingPrice()+bookingDto.getTotalCasingPrice()+bookingDto.getTotalMasterCasingPrice()+bookingDto.getTotalSlotingPrice()+
				bookingDto.getCasingTransporting()+bookingDto.getTotalWashingPrice();
		
		Double lessDiscount = totalAmount * 15 / 100;;
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

					if (safeInt(itemRequirement.getReq10MasterCasingPipe()) > 0) {

						masterCasing10Dtls = rawMaterialService.findMasterCasingDetails("10");

						bookingDto.setTotalUnitMasterCasing10(safeInt(itemRequirement.getReq10MasterCasingPipe()));

						bookingDto.setTotalMaster10CasingPrice(
								safeDouble(masterCasing10Dtls != null ? masterCasing10Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMasterCasing10());
					} else {
						bookingDto.setTotalUnitMasterCasing10(0);
						bookingDto.setTotalMaster10CasingPrice(0.0);
					}
				}
				else if ("8".equals(drillingSize)) {
					masterCasingDtls = rawMaterialService.findMasterCasingDetails("10", "6kg");

					if (safeInt(itemRequirement.getReq12MasterCasingPipe()) > 0) {

						masterCasing12Dtls = rawMaterialService.findMasterCasingDetails("10");

						bookingDto.setTotalUnitMasterCasing12(safeInt(itemRequirement.getReq12MasterCasingPipe()));

						bookingDto.setTotalMaster12CasingPrice(
								safeDouble(masterCasing12Dtls != null ? masterCasing12Dtls.getMaterialPrice() : null)
										* bookingDto.getTotalUnitMasterCasing12());
					} else {
						bookingDto.setTotalUnitMasterCasing12(0);
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
				bookingDto
						.setTotalWashingPrice(safeDouble(boreWashing != null ? boreWashing.getMaterialPrice() : null));
			}

			bookingDto.setOtherItemDetails("NA");
			bookingDto.setOtherItemPrice(0.0);
		}
		
		Double totalAmount = bookingDto.getTotalDrillingPrice()+bookingDto.getTotalCasingPrice()+bookingDto.getTotalMasterCasingPrice()+bookingDto.getTotalSlotingPrice()+
				bookingDto.getCasingTransporting()+bookingDto.getTotalWashingPrice();
		
		Double lessDiscount = totalAmount * 15 / 100;;
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

}
