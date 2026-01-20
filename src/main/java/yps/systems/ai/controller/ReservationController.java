package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yps.systems.ai.model.Reservation;
import yps.systems.ai.repository.IReservationRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservationService") 
public class ReservationController {

    private final IReservationRepository reservationRepository;

    @Autowired
    public ReservationController(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @GetMapping("/{elementId}")
    ResponseEntity<Reservation> getByElementId(@PathVariable String elementId) {
        return reservationRepository.findById(elementId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<Reservation>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(reservationRepository.findAllByUserId(userId));
    }

    @PostMapping
    ResponseEntity<String> save(@RequestBody Reservation reservation) {
        // Validación básica de conflicto (Opcional, pero recomendada usar el repositorio)
        boolean isOccupied = reservationRepository.checkSpotAvailability(
                reservation.getSpotId(), 
                reservation.getStartTime(), 
                reservation.getEndTime()
        );

        if (isOccupied) {
            return new ResponseEntity<>("Spot is already reserved for this timeframe", HttpStatus.CONFLICT);
        }

        Reservation saved = reservationRepository.save(reservation);
        return new ResponseEntity<>("Reservation created ID: " + saved.getElementId(), HttpStatus.CREATED);
    }

    @PutMapping("/{elementId}")
    ResponseEntity<String> update(@PathVariable String elementId, @RequestBody Reservation reservation) {
        Optional<Reservation> existing = reservationRepository.findById(elementId);
        if (existing.isPresent()) {
            reservation.setElementId(existing.get().getElementId());
            reservationRepository.save(reservation);
            return new ResponseEntity<>("Reservation updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{elementId}")
    ResponseEntity<String> delete(@PathVariable String elementId) {
        if (reservationRepository.existsById(elementId)) {
            reservationRepository.deleteById(elementId);
            return new ResponseEntity<>("Reservation deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
    }
}
