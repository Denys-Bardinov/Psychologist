package com.psychologist.site.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Temporary notifier: only writes the request to the application log. */
@Component
public class LoggingBookingNotifier implements BookingNotifier {

    private static final Logger log = LoggerFactory.getLogger(LoggingBookingNotifier.class);

    @Override
    public void send(BookingRequest request) {
        log.info("New booking request: name={}, method={}, contact={}, type={}, time={}, lang={}",
                request.name(), request.method(), request.contact(), request.sessionType(),
                request.preferredTime(), request.lang());
    }
}
