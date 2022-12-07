package dataaccess;

import domain.Booking;

import java.sql.Date;
import java.util.List;

public interface MyBookingRepository extends BaseRepository<Booking, Long> {

    List<Booking> findAllBookingsByStudentId(Long studentID);
    List<Booking> findAllBookingsByCourseId(Long courseID);
    List<Booking> findAllBookingsByBookingDate(Date bookingDate);
    List<Booking> findAllBookingsBeforeDate(Date date);
    List<Booking> findAllBookingsByApproval(Boolean approved);
}
