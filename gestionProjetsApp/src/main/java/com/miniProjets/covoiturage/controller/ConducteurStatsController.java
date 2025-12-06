package com.miniProjets.covoiturage.controller;

import com.miniProjets.covoiturage.model.Reservation;
import com.miniProjets.covoiturage.model.Trajet;
import com.miniProjets.covoiturage.repository.ReservationRepository;
import com.miniProjets.covoiturage.service.TrajetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/conducteurs")
public class ConducteurStatsController {

    @Autowired
    private TrajetService trajetService;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/{conducteurId}/stats")
    public ResponseEntity<Map<String, Object>> getConducteurStats(@PathVariable Long conducteurId) {
        List<Trajet> trajets = trajetService.getTrajetsByConducteur(conducteurId);
        
        int totalReservations = 0;
        for (Trajet trajet : trajets) {
            // Count reservations only for active (not cancelled) and future trips
            if (!"ANNULE".equals(trajet.getStatut().name()) && trajet.getDateHeure().isAfter(LocalDateTime.now())) {
                List<Reservation> reservations = reservationRepository.findByTrajetId(trajet.getId());
                // Count only non-cancelled reservations
                totalReservations += (int) reservations.stream()
                    .filter(r -> !"CANCELLED".equals(r.getEtat().name()))
                    .count();
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReservations", totalReservations);
        
        return ResponseEntity.ok(stats);
    }
}
