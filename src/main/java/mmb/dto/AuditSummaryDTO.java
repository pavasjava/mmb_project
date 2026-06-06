package mmb.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuditSummaryDTO {

	private Long auditId;
    private String changedBy;
    private LocalDateTime changedDate;
    private String action;
    private String changeSummary;
    private Integer entityVersion;
    public AuditSummaryDTO(
            Long auditId,
            String changedBy,
            LocalDateTime changedDate,
            String action,
            String changeSummary,
            Integer entityVersion) {

        this.auditId = auditId;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
        this.action = action;
        this.changeSummary = changeSummary;
        this.entityVersion = entityVersion;
    }
}
