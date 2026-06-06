package mmb.service;

import java.util.List;

import mmb.dto.BookingAuditDTO;
import mmb.dto.BookingDTO;
import mmb.dto.BorewellTypeDTO;

public interface BookingService {
	
	BookingDTO saveBooking(BookingDTO dto);
	public List<BookingDTO> getAllBookings();
	
	BookingDTO getById(Long id);
    void deleteById(Long id);
    public BookingDTO updateBooking(BookingDTO dto);
    public BookingDTO getConfirmBookingDetails(BookingDTO bookingDTO);
    public BookingDTO getBookingDtlsById(Long bookingId);
    public BookingDTO getBookingById(Long bookingId);
    public BookingDTO updateBookingDtls(BookingDTO bookingDto);
    public List<BookingAuditDTO> getBookingAuditHistory(Long bookingId);

}
