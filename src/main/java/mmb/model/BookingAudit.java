package mmb.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_audit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;
    
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;
    
    @Column(name = "changed_by", length = 100)
    private String changedBy;
    
    @Column(name = "changed_date")
    private LocalDateTime changedDate;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_audit_changes", 
                     joinColumns = @JoinColumn(name = "audit_id"))
    @Column(name = "change_description", length = 500)
    private List<String> changes = new ArrayList<>();
    
    @Column(name = "change_summary", length = 1000)
    private String changeSummary;
    
    @Lob
    @Column(name = "old_booking_state", columnDefinition = "TEXT")
    private String oldBookingState;
    
    @Lob
    @Column(name = "new_booking_state", columnDefinition = "TEXT")
    private String newBookingState;
    
    @Column(name = "action", length = 50)
    private String action;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    @Column(name = "entity_version")
    private Integer entityVersion;
    
    @Column(name = "previous_version_id")
    private Long previousVersionId;
    
    @Version
    @Column(name = "version")
    private Integer version;

}
