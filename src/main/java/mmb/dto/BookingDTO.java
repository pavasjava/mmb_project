package mmb.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
	
	private Long bookingId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String location;
    private LocalDate bookingDate;
    private String borewellDepth;
    private String status;

}
