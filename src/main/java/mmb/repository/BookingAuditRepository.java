package mmb.repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mmb.dto.AuditSummaryDTO;
import mmb.model.BookingAudit;

@Repository
public interface BookingAuditRepository extends JpaRepository<BookingAudit, Long> {

    List<BookingAudit> findByBookingIdOrderByChangedDateDesc(Long bookingId);
    
    /**
     * Find all audit records for a specific booking with pagination
     * 
     * @param bookingId The ID of the booking
     * @param pageable Pagination information
     * @return Page of audit records
     */
    Page<BookingAudit> findByBookingId(Long bookingId, Pageable pageable);
    
    /**
     * Find the most recent audit record for a booking
     * 
     * @param bookingId The ID of the booking
     * @return Optional containing the latest audit record
     */
    Optional<BookingAudit> findTopByBookingIdOrderByChangedDateDesc(Long bookingId);
    
    /**
     * Find audit records by booking ID and date range
     * 
     * @param bookingId The ID of the booking
     * @param startDate Start date
     * @param endDate End date
     * @return List of audit records within the date range
     */
    @Query("SELECT ba FROM BookingAudit ba WHERE ba.bookingId = :bookingId " +
           "AND ba.changedDate BETWEEN :startDate AND :endDate " +
           "ORDER BY ba.changedDate DESC")
    List<BookingAudit> findByBookingIdAndDateRange(@Param("bookingId") Long bookingId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find audit records by booking ID and action type
     * 
     * @param bookingId The ID of the booking
     * @param action The action type (CREATE, UPDATE, DELETE, etc.)
     * @return List of audit records with specific action
     */
    @Query("SELECT ba FROM BookingAudit ba WHERE ba.bookingId = :bookingId " +
           "AND ba.action = :action ORDER BY ba.changedDate DESC")
    List<BookingAudit> findByBookingIdAndAction(@Param("bookingId") Long bookingId,
                                               @Param("action") String action);
    
    /**
     * Find audit records by booking ID and changed by user
     * 
     * @param bookingId The ID of the booking
     * @param changedBy Username of the user who made the change
     * @return List of audit records by specific user
     */
    @Query("SELECT ba FROM BookingAudit ba WHERE ba.bookingId = :bookingId " +
           "AND ba.changedBy = :changedBy ORDER BY ba.changedDate DESC")
    List<BookingAudit> findByBookingIdAndChangedBy(@Param("bookingId") Long bookingId,
                                                  @Param("changedBy") String changedBy);
    
    /**
     * Count total number of changes for a booking
     * 
     * @param bookingId The ID of the booking
     * @return Total number of audit records
     */
    @Query("SELECT COUNT(ba) FROM BookingAudit ba WHERE ba.bookingId = :bookingId")
    Long countByBookingId(@Param("bookingId") Long bookingId);
    
    /**
     * Find audit records by booking ID and version
     * 
     * @param bookingId The ID of the booking
     * @param entityVersion The version number
     * @return List of audit records with specific version
     */
    List<BookingAudit> findByBookingIdAndEntityVersion(Long bookingId, Integer entityVersion);
    
    /**
     * Find audit records with specific change description
     * 
     * @param bookingId The ID of the booking
     * @param keyword Keyword to search in change descriptions
     * @return List of audit records containing the keyword
     */
    @Query("SELECT ba FROM BookingAudit ba JOIN ba.changes c " +
           "WHERE ba.bookingId = :bookingId AND LOWER(c) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY ba.changedDate DESC")
    List<BookingAudit> findByBookingIdAndChangeContaining(@Param("bookingId") Long bookingId,
                                                         @Param("keyword") String keyword);
    
    /**
     * Find audit records by booking ID with custom projection
     * Returns only selected fields for better performance
     * 
     * @param bookingId The ID of the booking
     * @return List of audit summaries
     */
    @Query("SELECT new mmb.dto.AuditSummaryDTO(" +
    	       "ba.auditId, ba.changedBy, ba.changedDate, ba.action, ba.changeSummary, ba.entityVersion) " +
    	       "FROM BookingAudit ba WHERE ba.bookingId = :bookingId ORDER BY ba.changedDate DESC")
    	List<AuditSummaryDTO> findAuditSummariesByBookingId(@Param("bookingId") Long bookingId);
    
    /**
     * Find audit records within date range for all bookings
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return List of audit records within date range
     */
    List<BookingAudit> findByChangedDateBetweenOrderByChangedDateDesc(LocalDateTime startDate, 
                                                                     LocalDateTime endDate);
    
    /**
     * Find audit records by IP address
     * 
     * @param ipAddress The IP address
     * @return List of audit records from specific IP
     */
    List<BookingAudit> findByIpAddressOrderByChangedDateDesc(String ipAddress);
    
    /**
     * Find audit records by user agent
     * 
     * @param userAgent The user agent string
     * @return List of audit records with specific user agent
     */
    List<BookingAudit> findByUserAgentContainingOrderByChangedDateDesc(String userAgent);
}
