package mmb.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import mmb.dto.BookingDTO;
import mmb.model.Booking;
import mmb.repository.BookingRepository;
import mmb.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService{
	
	private final BookingRepository bookingRepo;

    public BookingServiceImpl(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    // ✅ This is the method your controller expects
    public BookingDTO saveBooking(BookingDTO dto) {
        Booking booking = mapToEntity(dto);
        booking.setStatus("Pending");
        Booking saved = bookingRepo.save(booking);
        return mapToDTO(saved);
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ---------- Mappers ----------
    private BookingDTO mapToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setCustomerName(booking.getCustomerName());
        dto.setPhoneNumber(booking.getPhoneNumber());
        dto.setAddress(booking.getAddress());
        dto.setLocation(booking.getLocation());
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    private Booking mapToEntity(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setBookingId(dto.getBookingId());
        booking.setCustomerName(dto.getCustomerName());
        booking.setPhoneNumber(dto.getPhoneNumber());
        booking.setAddress(dto.getAddress());
        booking.setLocation(dto.getLocation());
        booking.setBookingDate(dto.getBookingDate());
        booking.setStatus(dto.getStatus());
        return booking;
    }

}
