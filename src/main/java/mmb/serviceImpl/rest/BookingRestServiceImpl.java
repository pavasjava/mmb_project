package mmb.serviceImpl.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mmb.dto.BookingDTO;
import mmb.model.Booking;
import mmb.repository.BookingRepository;
import mmb.service.rest.BookingRestService;

@Service
public class BookingRestServiceImpl implements BookingRestService {
	
	@Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ModelMapper modelMapper; // Make sure to define this as a @Bean in config

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO getById(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        return bookingOpt.map(booking -> modelMapper.map(booking, BookingDTO.class)).orElse(null);
    }

    @Override
    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        Booking savedBooking = bookingRepository.save(booking);
        return modelMapper.map(savedBooking, BookingDTO.class);
    }

    @Override
    public void updateBooking(BookingDTO bookingDTO) {
        if (bookingDTO.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID cannot be null for update");
        }

        Optional<Booking> existingOpt = bookingRepository.findById(bookingDTO.getBookingId());
        if (existingOpt.isPresent()) {
            Booking existing = existingOpt.get();

            // Update relevant fields
            existing.setCustomerName(bookingDTO.getCustomerName());
            existing.setPhoneNumber(bookingDTO.getPhoneNumber());
            existing.setCity(bookingDTO.getCity());
            existing.setWorkLocationArea(bookingDTO.getArea());
            existing.setBorewellType(bookingDTO.getBorewellType());
            existing.setDrillingSize(bookingDTO.getDrillingSize());
            existing.setPrice(bookingDTO.getPrice());
            existing.setBookingDate(bookingDTO.getBookingDate());
            existing.setStatus(bookingDTO.getStatus());

            bookingRepository.save(existing);
        } else {
            throw new RuntimeException("Booking not found with ID: " + bookingDTO.getBookingId());
        }
    }

    @Override
    public void deleteById(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

}
