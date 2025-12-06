package com.miniProjets.covoiturage.controller;

import com.miniProjets.covoiturage.model.Passager;
import com.miniProjets.covoiturage.model.Reservation;
import com.miniProjets.covoiturage.service.PassagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passagers")
public class PassagerController {

    @Autowired
    private PassagerService passagerService;

    // CRUD Passager
    @PostMapping
    public ResponseEntity<?> createPassager(@RequestBody Passager passager) {
        try {
            Passager p = passagerService.createPassager(passager);
            if (p == null) {
                return ResponseEntity
                        .ok("Impossible d'ajouter un passager, Vérifier qu'il soit non null et non existant!");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPassagers() {
        try {
            List<Passager> list = passagerService.getAllPassagers();
            if (list.isEmpty()) {
                return ResponseEntity.ok("Aucun resulats obtenu");
            }
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPassagerById(@PathVariable Long id) {
        try {
            Passager p = passagerService.getPassagerById(id);
            return ResponseEntity.ok(p);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePassager(@PathVariable Long id, @RequestBody Passager passager) {
        try {
            Passager updated = passagerService.updatePassager(id, passager);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePassager(@PathVariable Long id) {
        try {
            passagerService.deletePassager(id);
            return ResponseEntity.ok("Passager supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // NOTE: Login/authentication has been moved to AuthController (/api/auth/login)
    // This endpoint was redundant and has been removed to maintain a single
    // authentication flow

    // Réserver un trajet
    @PostMapping("/{id}/reservations")
    public ResponseEntity<?> reserverTrajet(@PathVariable Long id, @RequestParam Long trajetId) {
        try {
            Reservation reservation = passagerService.reserverTrajet(id, trajetId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Annuler une réservation
    @DeleteMapping("/{id}/reservations/{reservationId}")
    public ResponseEntity<?> annulerReservation(@PathVariable Long id, @PathVariable Long reservationId) {
        try {
            passagerService.annulerReservation(id, reservationId);
            return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Réservation annulée avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Consulter mes réservations
    @GetMapping("/{id}/reservations")
    public ResponseEntity<?> getMesReservations(@PathVariable Long id) {
        try {
            List<Reservation> reservations = passagerService.getMesReservations(id);
            return ResponseEntity.ok(reservations);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
