package mmb.restController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mmb.dto.BookingDTO;
import mmb.model.BorewellType;
import mmb.model.City;
import mmb.repository.BorewellTypeRepository;
import mmb.repository.CityRepository;
import mmb.repository.DrillingPriceChartRepository;
import mmb.repository.RawMaterialRepo;
import mmb.repository.WorkLocationAreaRepository;
import mmb.service.BorewellTypeService;
import mmb.service.WorkLocationAreaService;
import mmb.service.rest.BookingRestService;

@RestController
@RequestMapping("/api/bookings")
//@CrossOrigin(origins = "*")
public class BookingRestController {

    private final BookingRestService bookingService;

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
    private DrillingPriceChartRepository drillingPriceChartRepository;

	@Autowired
	private RawMaterialRepo rawMaterialRepo;

    @Autowired
    public BookingRestController(BookingRestService bookingService) {
        this.bookingService = bookingService;
    }

    // ✅ 1. Get all cities
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return ResponseEntity.ok(cities);
    }

    // ✅ 2. Get all borewell types
    @GetMapping("/borewellTypes")
    public ResponseEntity<List<BorewellType>> getAllBorewellTypes() {
        List<BorewellType> borewellTypes = borewellTypeRepository.findAll();
        return ResponseEntity.ok(borewellTypes);
    }
    
//    @GetMapping("/getBorewellTypeByAreaWise")
//    public ResponseEntity<List<BorewellType>> getBorewellTypeByAreaWise(
//            @RequestParam("cityId") String cityId,
//            @RequestParam("locationAreaId") String locationAreaId) {
//
//        List<BorewellType> list = borewellTypeService.getBorewellTypeByAreaWise(cityId, locationAreaId);
//
//        if (list == null || list.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(list);
//    }
    
    @GetMapping("/getBorewellTypeByAreaWise")
    public ResponseEntity<List<BorewellType>> getBorewellTypeByAreaWise(
            @RequestParam String cityId,
            @RequestParam String locationAreaId,@RequestParam String drillingSize) {
        return ResponseEntity.ok(
                borewellTypeService.getBorewellTypeByAreaWise(cityId, locationAreaId, drillingSize));
    }
    
    @GetMapping("/selection")
    @ResponseBody
    public String receiveSelection(
            @RequestParam String cityId,
            @RequestParam String areaId) {

        System.out.println("Selected City ID => " + cityId);
        System.out.println("Selected Area ID => " + areaId);

        return "OK";
    }

    // ✅ 3. Get all bookings
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // ✅ 4. Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.getById(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 5. Create new booking
    @PostMapping
    public ResponseEntity<BookingDTO> saveBooking(@RequestBody BookingDTO bookingDto) {
        BookingDTO saved = bookingService.saveBooking(bookingDto);
        return ResponseEntity.status(201).body(saved); // 201 Created
    }

    // ✅ 6. Update booking
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDto) {
        bookingDto.setBookingId(id);
        bookingService.updateBooking(bookingDto);
        return ResponseEntity.ok("Booking updated successfully");
    }

    // ✅ 7. Delete booking
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    // ✅ 8. Get all areas by city ID
    @GetMapping("/areas/{cityId}")
    public ResponseEntity<List<?>> getAreasByCity(@PathVariable Long cityId) {
        List<?> areas = workLocationAreaService.getAreasByCityId(cityId);
        return ResponseEntity.ok(areas);
    }
    
    @GetMapping("/findCasingPrice")
	public Double findPrice(
	        @RequestParam Integer companyId,
	        @RequestParam Long borewellTypeId,
	        @RequestParam String borewellSize,
	        @RequestParam String quality) {
    	
	    System.out.println("==== Debug Values ====");
	    System.out.println("companyId = " + companyId);
	    System.out.println("borewellTypeId = " + borewellTypeId);
	    System.out.println("borewellSize = " + borewellSize);
	    System.out.println("borewellSize = " + borewellSize);

	    System.out.println("companyId -> " + companyId + "borewellTypeId -> " + borewellTypeId + "borewellSize -> " + borewellSize);
	    Double materialPrice = rawMaterialRepo.findByCompanyAndTypeAndSizeAndQuality(companyId,borewellTypeId,borewellSize,quality);

	    System.out.println("price -> " + materialPrice);

	    return materialPrice != null ? materialPrice : 0.0;
	}
    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getBorewellTypeImage(@PathVariable Long id) {
        Optional<BorewellType> borewellTypeOpt = borewellTypeRepository.findById(id);
        if (borewellTypeOpt.isPresent()) {
            BorewellType borewellType = borewellTypeOpt.get();
            byte[] imageData = borewellType.getImageData();
            if (imageData != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(imageData);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
