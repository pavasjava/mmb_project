package mmb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mmb.dto.BookingDTO;
import mmb.service.BookingService;

@Controller
@RequestMapping("/bookings")
public class BookingController {
	
	private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/showBookingForm")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new BookingDTO());
        return "booking/booking-form";
    }

    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute("booking") BookingDTO bookingDto) {
        bookingService.saveBooking(bookingDto); 
        return "redirect:/bookings/success";
    }

    @GetMapping("/success")
    public String successPage() {
        return "booking-success";
    }

}
