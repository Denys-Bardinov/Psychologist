package com.psychologist.site.booking;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** A booking request as sent by the form on the page (JSON). */
public record BookingRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull ContactMethod method,
        @NotBlank @Size(max = 200) String contact,
        @Size(max = 100) String sessionType,
        @Size(max = 200) String preferredTime,
        @Size(max = 2000) String message,
        boolean consent,
        String lang) {

    /** Checked by Bean Validation; the error is reported on the "contact" field. */
    @AssertTrue
    public boolean isContactValid() {
        return method == null || contact == null || method.isValid(contact.trim());
    }

    @AssertTrue
    public boolean isConsentGiven() {
        return consent;
    }
}
