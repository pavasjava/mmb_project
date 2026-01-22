package mmb.service;

import java.util.List;

import mmb.dto.BookingDTO;
import mmb.dto.BorewellTypeDTO;

public interface BookingService {
	
	BookingDTO saveBooking(BookingDTO dto);
	public List<BookingDTO> getAllBookings();
	
	BookingDTO getById(Long id);
    void deleteById(Long id);
    public void updateBooking(BookingDTO dto);
    public BookingDTO getConfirmBookingDetails(BookingDTO bookingDTO);
    public BookingDTO getBookingDtlsById(Long bookingId);

}
