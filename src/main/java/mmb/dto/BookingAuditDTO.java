package mmb.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingAuditDTO {

	private Long auditId;
    private Long bookingId;
    private String changedBy;
    private LocalDateTime changedDate;
    private List<String> changes;
    private String changeSummary;
    private String oldBookingState;
    private String action;
    private String ipAddress;
    private String userAgent;
}
