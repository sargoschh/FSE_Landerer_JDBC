package dataaccess;

import domain.Booking;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class MySqlBookingRepository implements MyBookingRepository {
    @Override
    public Optional<Booking> insert(Booking entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Booking> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Booking> getAll() {
        return null;
    }

    @Override
    public Optional<Booking> update(Booking entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Booking> findAllBookingsByStudentId(Long studentID) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsByCourseId(Long courseID) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsByBookingDate(Date bookingDate) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsBeforeDate(Date date) {
        return null;
    }

    @Override
    public List<Booking> findAllBookingsByApproval(Boolean approved) {
        return null;
    }
}
