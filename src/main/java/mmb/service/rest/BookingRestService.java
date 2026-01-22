package mmb.service.rest;

import java.util.List;

import mmb.dto.BookingDTO;

public interface BookingRestService {

	List<BookingDTO> getAllBookings();

    BookingDTO getById(Long id);

    BookingDTO saveBooking(BookingDTO bookingDTO);

    void updateBooking(BookingDTO bookingDTO);

    void deleteById(Long id);
}
