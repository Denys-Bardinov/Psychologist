package com.psychologist.site.booking;

/**
 * Delivers a booking request to the psychologist.
 * Replace LoggingBookingNotifier with a real one (email, Telegram bot, Google Sheet) once the destination is chosen.
 */
public interface BookingNotifier {

    void send(BookingRequest request);
}
