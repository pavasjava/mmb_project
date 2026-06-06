package mmb.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import mmb.dto.AreaWiseItemRequirementDTO;
import mmb.dto.BookingAuditDTO;
import mmb.dto.BookingDTO;
import mmb.dto.RawMaterialDTO;
import mmb.model.Booking;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.model.MaterialCompanyName;
import mmb.model.UserInfo;
import mmb.model.WorkLocationArea;
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
import mmb.util.BookingDtlsPdf;

@Controller
@RequestMapping("/bookings")
@SessionAttributes("token")
public class BookingController {

    private final UserDetailsService userDetails;

	private final BookingService bookingService;

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
    private BookingDtlsPdf bookingDtlsPdf;

	public BookingController(BookingService bookingService, UserDetailsService userDetails) {
		this.bookingService = bookingService;
		this.userDetails = userDetails;
	}

	@GetMapping("/showBookingForm")
	public String showBookingForm(@RequestParam(required = false) String cityId,
	        @RequestParam(required = false) String areaId,@RequestParam(required = false) String drillingSize, Model model) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		System.out.println("username -> "+ username);

        BookingDTO booking = new BookingDTO();
	    Optional<UserInfo> userInfoOpt = userService.findByEmail(username);
	    if (userInfoOpt.isPresent()) {
	        UserInfo userInfo = userInfoOpt.get();

	        String customerName = userInfo.getFname() + " " + userInfo.getMname() + " " +userInfo.getLname();
	        String phoneNumber = userInfo.getMobile();

	        booking.setCustomerName(customerName);
	        booking.setPhoneNumber(phoneNumber);
	    }
        
        booking.setPipeQuality("80"); 
		model.addAttribute("booking", booking);

		// Load all cities
		List<City> cities = cityRepository.findAll();
		model.addAttribute("cities", cities);

		// Load all borewellTypes only
//        List<BorewellType> borewellTypes = borewellTypeRepository.findAll();
		List<BorewellType> borewellTypes = borewellTypeRepository.getBorewellTypeByAreaWise(cityId, areaId, drillingSize);
        model.addAttribute("borewellTypes", borewellTypes);
        
        List<RawMaterialDTO> rawMaterials = rawMaterialService.getAllMaterials();
        model.addAttribute("rawMaterials", rawMaterials);
        
        List<MaterialCompanyName> companyNames = materialCompanyNameRepo.findAll();
        model.addAttribute("companyNames", companyNames);

		return "booking/booking-form";
	}
	
	@PostMapping("/confirmBooking")
	public String confirmBooking(@ModelAttribute("booking") BookingDTO bookingDto, Model model) {
		
		BookingDTO bookingDtls = bookingService.getConfirmBookingDetails(bookingDto);
	    model.addAttribute("booking", bookingDtls);

	    return "booking/confirm-booking-page";
	}
	


//    else if((bookingDto.getDrillingSize().equalsIgnoreCase("5") || bookingDto.getDrillingSize().equalsIgnoreCase("6") || bookingDto.getDrillingSize().equalsIgnoreCase("4")
//    		|| bookingDto.getDrillingSize().equalsIgnoreCase("3")) && (bw.getName().equalsIgnoreCase("ROTTARY") || bw.getName().equalsIgnoreCase("CALLIX") || bw.getName().equalsIgnoreCase("DTH-ROTTARY"))) {
//    	bookingDto.setTotalDrillingUnit(200);
//    	Double totDrillingPrice = bookingDto.getTotalDrillingUnit()*bookingDto.getPrice();
//    	bookingDto.setTotalDrillingPrice(totDrillingPrice);
//    	bookingDto.setTotalUnitCasing(12);
//    	Double totCasingPrice = bookingDto.getCasingPrice()*bookingDto.getTotalUnitCasing();
//    	bookingDto.setTotalCasingPrice(totCasingPrice);
//    	bookingDto.setTotalUnitMasterCasing(0);
//    	Double totMasterCasingPrice = 0.0;
//    	bookingDto.setTotalMasterCasingPrice(totMasterCasingPrice);
//    	bookingDto.setCasingTransporting(1000.00);
//    	bookingDto.setTotalUnitSloting(5);
//    	bookingDto.setTotalSlotingPrice(500.00*bookingDto.getTotalUnitSloting());
//    	bookingDto.setTotalUnitGravel(2);
//    	bookingDto.setTotalGravelPrice(2500.0*2);
//    	bookingDto.setTotalWashingUnit(2);
//    	bookingDto.setTotalWashingPrice(3000.00*2);
//    	bookingDto.setTotalUnitModPowder(10);
//    	bookingDto.setTotalModPowderPrice(400.00*10);
//    	bookingDto.setOtherItemDetails("NA");
//    	bookingDto.setOtherItemPrice(0.0);
//    }
//    else {
//    	bookingDto.setTotalDrillingUnit(350);
//    }

    // ---- SEND FINAL DTO TO THYMELEAF ----
	
	
	
//	model.addAttribute("totalAmount", totalAmount);
//	model.addAttribute("lessDiscount", lessDiscount);
//	model.addAttribute("totalAmtAfterDiscount", totalAmtAfterDiscount);
//	model.addAttribute("cgst", cgst);
//	model.addAttribute("sgst", sgst);
//	model.addAttribute("grandTotal", grandTotal);

	// Add these methods to your BookingController

	@GetMapping("/edit/{bookingId}")
	public String showEditBookingForm(@PathVariable("bookingId") Long bookingId, Model model) {
	    // Fetch the existing booking
	    BookingDTO bookingDto = bookingService.getBookingById(bookingId);
	    model.addAttribute("booking", bookingDto);
	    
	    // Load all required data for dropdowns
	    List<City> cities = cityRepository.findAll();
	    model.addAttribute("cities", cities);
	    
	    // Load areas for the booking's city
	    if (bookingDto.getCity() != null) {
	        List<WorkLocationArea> areas = workLocationAreaService.getAreasByCityId(bookingDto.getCity().getCityId());
	        model.addAttribute("areas", areas);
	    }
	    
	    List<MaterialCompanyName> companyNames = materialCompanyNameRepo.findAll();
	    model.addAttribute("companyNames", companyNames);
	    
	    return "booking/edit-booking-page";
	}

	@PostMapping("/updateBookingDtls")
	public String updateBooking(@ModelAttribute("booking") BookingDTO bookingDto, Model model) {
	    try {
	        BookingDTO updatedBooking = bookingService.updateBookingDtls(bookingDto);
	        model.addAttribute("booking", updatedBooking);
	        model.addAttribute("success", "Booking updated successfully!");
	        return "booking/confirm-booking-page";
	    } catch (Exception e) {
	        model.addAttribute("error", "Failed to update booking: " + e.getMessage());
	        return "booking/edit-booking-page";
	    }
	}

	@GetMapping("/history/{bookingId}")
	public String viewBookingHistory(@PathVariable("bookingId") Long bookingId, Model model) {
	    BookingDTO bookingDto = bookingService.getBookingById(bookingId);
	    List<BookingAuditDTO> auditHistory = bookingService.getBookingAuditHistory(bookingId);
	    
	    model.addAttribute("booking", bookingDto);
	    model.addAttribute("auditHistory", auditHistory);
	    
	    return "booking/booking-history";
	}
	
	@GetMapping("/getAllBookings")
    public String getAllBookingDetails(Model model) {
		List<BookingDTO> bookings = bookingService.getAllBookings();
		bookings.forEach(b -> System.out.println(
		        "BookingID: " + b.getBookingId() +
		        ", Customer: " + b.getCustomerName() +
		        ", Phone: " + b.getPhoneNumber() +
		        ", City: " + (b.getCity() != null ? b.getCity().getCityName() : "N/A") +
		        ", Area: " + (b.getArea() != null ? b.getArea().getLocationAreaName() : "N/A") +
		        ", BorewellType: " + (b.getBorewellType() != null ? b.getBorewellType().getName() : "N/A") +
		        ", Drilling Size: " + b.getDrillingSize() +
		        ", Price: " + b.getPrice() +
		        ", Booking Date: " + b.getBookingDate() +
		        ", Status: " + b.getStatus()
		    ));
        model.addAttribute("bookings", bookings);
        return "booking/booking_list";
    }

	@PostMapping("/saveBooking")
	public String saveBooking(@ModelAttribute("booking") BookingDTO bookingDto) {
		bookingService.saveBooking(bookingDto);
		
		System.out.println("City name and city id -> "+bookingDto.getCity().getCityId()+" "+bookingDto.getCity().getCityName());
		System.out.println("Area name and Area id -> "+bookingDto.getArea().getLocationAreaId()+" "+bookingDto.getArea().getLocationAreaName());
		System.out.println("Borewell Type id -> "+bookingDto.getBorewellType().getBorewelTypeid()+" "+bookingDto.getBorewellType().getName());
		System.out.println("Borewell Size -> "+bookingDto.getDrillingSize());
		System.out.println("Other Size -> "+bookingDto.getDrillingSize());
		
		return "redirect:/bookings/success";
	}

	@GetMapping("/success")
	public String successPage() {
		return "booking/booking-success";
	}
	
	@GetMapping("/payment")
    public String paymentForm(HttpSession session, Model model) {
        UserInfo loggedInUser = (UserInfo) session.getAttribute("loggedInUser");
        System.out.println("loggedInUser -> "+loggedInUser);
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        BookingDTO booking = (BookingDTO) session.getAttribute("booking");
        if (booking != null) {
//            double amount = booking.get
//            model.addAttribute("amount", amount);
        }
        return "booking/payment"; 
    }
	
//	@GetMapping("/editBookingDetails/{id}")
//    public String editBookingDetails(@PathVariable Long id, Model model) {
//		BookingDTO dto = bookingService.getById(id);
//        model.addAttribute("bookings", dto);
//        return "booking/booking-form";
//    }
	
	@GetMapping("/editBookingDetails/{bookingId}")
	public String editBookingDetails(@PathVariable("bookingId") Long bookingId, Model model) {
		System.out.println("bookingId -> "+bookingId);
	    BookingDTO bookingDTO = bookingService.getById(bookingId);
	    System.out.println("booking date -> "+bookingDTO.getBookingDate());
	    model.addAttribute("booking", bookingDTO);
	    List<City> cities = cityRepository.findAll();
	    model.addAttribute("cities", cities);
	    model.addAttribute("borewellTypes", borewellTypeService.getAllTypes());
	 // ✅ Load areas for the selected city
	    if (bookingDTO.getCity() != null && bookingDTO.getCity().getCityId() != null) {
	        model.addAttribute("areas", workLocationAreaService.getAreasByCityId(bookingDTO.getCity().getCityId()));
	    } else {
	        model.addAttribute("areas", Collections.emptyList());
	    }
//	    model.addAttribute("areas", workLocationAreaRepository.findByCity_CityId(cityId));
	    
//	    return "booking/editBookingDetails";
	    return "booking/edit-booking-page";
	}
	@GetMapping("/borewellType/image/{id}")
	public ResponseEntity<byte[]> getBorewellImage(@PathVariable Long id) {

	    BorewellType type = borewellTypeRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Not found"));

	    return ResponseEntity.ok()
	            .contentType(MediaType.IMAGE_JPEG) // or detect dynamically
	            .body(type.getImageData());
	}

//    @PostMapping("/updateBooking")
//    public String updateBookingDetails(@ModelAttribute BookingDTO dto) throws IOException {
//    	bookingService.saveBooking(dto);
//        return "redirect:/bookings/getAllBookings";
//    }
	
//	@PostMapping("/updateBooking")
//	public String updateBooking(@ModelAttribute("booking") BookingDTO bookingDTO) {
//	    bookingService.updateBooking(bookingDTO);
//	    return "redirect:/bookings"; // Redirect back to booking list
//	}

    @GetMapping("/deleteBookingDetails/{id}")
    public String deleteBookingDetails(@PathVariable Long id) {
    	bookingService.deleteById(id);
        return "redirect:/bookings/getAllBookings";
    }
    
    @GetMapping("/downloadPdf/{bookingId}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long bookingId) {
        try {
            // Get booking details
            BookingDTO booking = bookingService.getBookingDtlsById(bookingId);
            
            if (booking == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Generate PDF as byte array
            byte[] pdfBytes = bookingDtlsPdf.generateBookingPdf(booking);
            
            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "booking-receipt-" + booking.getCustomerName() + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/generatePdf")
    public ResponseEntity<byte[]> generatePdf(@ModelAttribute("booking") BookingDTO bookingDto) {
        try {
            // Generate PDF directly from booking data
            byte[] pdfBytes = bookingDtlsPdf.generateBookingPdf(bookingDto);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                "booking-receipt-" + bookingDto.getCustomerName() + ".pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private double safeMultiply(Number a, Number b) {
        double val1 = (a == null) ? 0 : a.doubleValue();
        double val2 = (b == null) ? 0 : b.doubleValue();
        return val1 * val2;
    }
    
    @GetMapping("/editBooking/{id}")
    public String editBooking(@PathVariable Long id, Model model) {
        // Fetch the booking by ID
        BookingDTO bookingDTO = bookingService.getBookingById(id);
        
        System.out.println("Total drilling"+bookingDTO.getTotalDrillingUnit() );
        System.out.println("Total drilling"+bookingDTO.getTotal2_5kgPrice() );
        System.out.println("Total drilling"+bookingDTO.getTotal6kgPrice() );
        bookingDTO.setTotalCasingPrice(safeMultiply(bookingDTO.getCasingPrice(), bookingDTO.getTotalUnitCasing()));
        bookingDTO.setTotalMasterCasingPrice(safeMultiply(bookingDTO.getMasterCasingPricePerUnit(), bookingDTO.getTotalUnitMasterCasing()));
        bookingDTO.setTotalSlotingPrice(safeMultiply(bookingDTO.getCasingSlotingPerUnit(), bookingDTO.getTotalUnitSloting()));
        bookingDTO.setTotalDrillingPrice(safeMultiply(bookingDTO.getTotalDrillingUnit(), bookingDTO.getPrice()));
        bookingDTO.setTotalWashingPrice(safeMultiply(bookingDTO.getTotalWashingUnit(), bookingDTO.getWashingPricePerUnit()));
        bookingDTO.setTotalMaster10CasingPrice(safeMultiply(bookingDTO.getMc10PricePerUnit(), bookingDTO.getTotalUnitMC10()));
        bookingDTO.setTotalMaster12CasingPrice(safeMultiply(bookingDTO.getMc12PricePerUnit(), bookingDTO.getTotalUnitMC12()));
        bookingDTO.setTotalMasterCasing14Price(safeMultiply(bookingDTO.getMc14PricePerUnit(), bookingDTO.getTotalUnitMC14()));
        
        // Add booking to model
        model.addAttribute("booking", bookingDTO);
        
        // Load all required dropdown data
        List<City> cities = cityRepository.findAll();
        model.addAttribute("cities", cities);
        
        // Load areas for the booking's city
        if (bookingDTO.getCity() != null && bookingDTO.getCity().getCityId() != null) {
            List<WorkLocationArea> areas = workLocationAreaRepository.findByCity_CityId(bookingDTO.getCity().getCityId());
            model.addAttribute("areas", areas);
        } else {
            model.addAttribute("areas", new ArrayList<>());
        }
        
        // Load company names for casing pipe
        List<MaterialCompanyName> companyNames = materialCompanyNameRepo.findAll();
        model.addAttribute("companyNames", companyNames);
        
        // Add page title
        model.addAttribute("pageTitle", "Edit Booking");
        
        return "booking/edit-booking"; // You'll create this template
    }

    @PostMapping("/updateBooking")
    public String updateBooking(@ModelAttribute("booking") BookingDTO bookingDTO) {
        bookingService.updateBooking(bookingDTO);
        
        System.out.println("Updated Booking - City: " + bookingDTO.getCity().getCityId() + " " + bookingDTO.getCity().getCityName());
        System.out.println("Updated Booking - Area: " + bookingDTO.getArea().getLocationAreaId() + " " + bookingDTO.getArea().getLocationAreaName());
        System.out.println("Updated Booking - Borewell Type: " + bookingDTO.getBorewellType().getBorewelTypeid() + " " + bookingDTO.getBorewellType().getName());
        
     // Print 2.5 kg Casing Details
        System.out.println("\n--- 2.5 kg Master Casing Details ---");
        System.out.println("Total Unit 2.5 kg Master Casing: " + bookingDTO.getTotalUnit2_5kg());
        System.out.println("2.5 kg Master Casing Price/Unit: " + bookingDTO.getPricePerUnit2_5kg());
        System.out.println("Total 2.5 kg Master Casing Price: " + bookingDTO.getTotal2_5kgPrice());
        
        // Print 6 kg Casing Details
        System.out.println("\n--- 6 kg Master Casing Details ---");
        System.out.println("Total Unit 6 kg Master Casing: " + bookingDTO.getTotalUnit6kg());
        System.out.println("6 kg Master Casing Price/Unit: " + bookingDTO.getPricePerUnit6kg());
        System.out.println("Total 6 kg Master Casing Price: " + bookingDTO.getTotal6kgPrice());
        
        System.out.println("=====================================\n");
        
        return "redirect:/bookings/getAllBookings?editSuccess=true";
    }

}
