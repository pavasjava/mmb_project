package mmb.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking { 
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
    private Long bookingId;

	@Column(name = "customer_name")
    private String customerName;
	@Column(name = "phone_number")
    private String phoneNumber;
	@Column(name = "address")
    private String address;
	@Column(name = "location")
    private String location;

	@Column(name = "booking_date")
    private LocalDate bookingDate;

	@Column(name = "status")
    private String status;

}
