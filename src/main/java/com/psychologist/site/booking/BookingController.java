package com.psychologist.site.booking;

import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** Receives the booking form. The page checks the fields first; the server checks them again. */
@RestController
public class BookingController {

    private final BookingNotifier notifier;

    public BookingController(BookingNotifier notifier) {
        this.notifier = notifier;
    }

    @PostMapping("/api/booking")
    public Map<String, Object> book(@Valid @RequestBody BookingRequest request) {
        notifier.send(request);
        return Map.of("ok", true);
    }

    /** Returns {"ok": false, "errors": {"name": "...", "contact": "...", "consent": "..."}} with status 400. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> invalid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(formField(error.getField()), error.getCode());
        }
        return ResponseEntity.badRequest().body(Map.of("ok", false, "errors", errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> unreadable() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("ok", false));
    }

    private static String formField(String field) {
        return switch (field) {
            case "contactValid", "method" -> "contact";
            case "consentGiven" -> "consent";
            default -> field;
        };
    }
}
