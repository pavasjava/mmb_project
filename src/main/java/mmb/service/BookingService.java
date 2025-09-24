package mmb.service;

import java.util.List;

import mmb.dto.BookingDTO;

public interface BookingService {
	
	BookingDTO saveBooking(BookingDTO dto);
	public List<BookingDTO> getAllBookings();

}
