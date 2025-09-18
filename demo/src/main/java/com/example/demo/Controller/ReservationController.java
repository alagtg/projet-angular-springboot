// src/main/java/com/example/demo/controller/ReservationController.java
package com.example.demo.controller;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.AuditReservation;
import com.example.demo.repository.AuditReservationRepository;
import com.example.demo.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final ReservationService reservationService;
    private final AuditReservationRepository auditReservationRepository;

    public ReservationController(ReservationService reservationService,
                                 AuditReservationRepository auditReservationRepository) {
        this.reservationService = reservationService;
        this.auditReservationRepository = auditReservationRepository;
    }

    //  Créer une réservation (201 si OK)
    @PostMapping
    public ResponseEntity<Reservation> reserver(@Valid @RequestBody Reservation reservation) {
        Reservation saved = reservationService.reserverVol(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }




    @ExceptionHandler(ReservationService.VolNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleVolNotFound(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ReservationService.PlacesInsuffisantesException.class)
    public ResponseEntity<Map<String,Object>> handlePlaces(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), req.getRequestURI());
    }

    @ExceptionHandler(ReservationService.ConcurrencyException.class)
    public ResponseEntity<Map<String,Object>> handleConcurrency(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.CONFLICT, "Conflict", "Conflit concurrent, réessayez.", req.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("Requête invalide");
        return error(HttpStatus.BAD_REQUEST, "Bad Request", msg, req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleOther(Exception ex, HttpServletRequest req) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), req.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String error, String message, String path) {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }
}
