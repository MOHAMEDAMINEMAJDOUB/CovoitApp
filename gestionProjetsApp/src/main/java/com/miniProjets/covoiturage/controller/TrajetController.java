package com.miniProjets.covoiturage.controller;

import com.miniProjets.covoiturage.dto.TrajetDTO;
import com.miniProjets.covoiturage.model.Trajet;
import com.miniProjets.covoiturage.security.UserDetailsImpl;
import com.miniProjets.covoiturage.service.TrajetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trajets")
public class TrajetController {

    @Autowired
    private TrajetService trajetService;

    // Create trip - requires authentication (any role)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Trajet> createTrajet(
            @Valid @RequestBody TrajetDTO trajetDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Trajet trajet = trajetService.createTrajet(trajetDTO, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(trajet);
    }

    // Get all trips - public endpoint
    @GetMapping
    public ResponseEntity<List<Trajet>> getAllTrajets() {
        List<Trajet> list = trajetService.getAllTrajets();
        return ResponseEntity.ok(list);
    }

    // Get trip by ID - public endpoint
    @GetMapping("/{id}")
    public ResponseEntity<Trajet> getTrajetById(@PathVariable Long id) {
        Trajet trajet = trajetService.getTrajetById(id);
        return ResponseEntity.ok(trajet);
    }

    // Update trip - requires authentication
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Trajet> updateTrajet(
            @PathVariable Long id,
            @Valid @RequestBody TrajetDTO trajetDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // TODO: Verify trip belongs to authenticated user
        Trajet trajet = new Trajet();
        trajet.setVilleDepart(trajetDTO.getVilleDepart());
        trajet.setVilleArrivee(trajetDTO.getVilleArrivee());
        trajet.setDateHeure(trajetDTO.getDateHeure());
        trajet.setPlacesDisponibles(trajetDTO.getPlacesDisponibles());
        trajet.setPrixParPlace(trajetDTO.getPrixParPlace());

        Trajet updated = trajetService.updateTrajet(id, trajet);
        return ResponseEntity.ok(updated);
    }

    // Delete trip - requires authentication
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteTrajet(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // TODO: Verify trip belongs to authenticated user
        trajetService.deleteTrajet(id);
        return ResponseEntity.noContent().build();
    }

    // Search trips - public endpoint
    @GetMapping("/rechercher")
    public ResponseEntity<List<Trajet>> rechercherTrajets(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee) {
        List<Trajet> resultats;
        if (villeDepart != null && villeArrivee != null) {
            resultats = trajetService.rechercherTrajets(villeDepart, villeArrivee);
        } else {
            resultats = trajetService.getAllTrajets();
        }
        return ResponseEntity.ok(resultats);
    }

    // Search by keyword - public endpoint
    @GetMapping("/search")
    public ResponseEntity<List<Trajet>> rechercherParMotCle(@RequestParam String motCle) {
        List<Trajet> resultats = trajetService.rechercherTrajetsParMotCle(motCle);
        return ResponseEntity.ok(resultats);
    }

    // Get trips by driver - public endpoint
    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<Trajet>> getTrajetsByConducteur(@PathVariable Long conducteurId) {
        System.out.println("Fetching trips for driver ID: " + conducteurId);
        List<Trajet> trajets = trajetService.getTrajetsByConducteur(conducteurId);
        System.out.println("Found " + trajets.size() + " trips");
        return ResponseEntity.ok(trajets);
    }
}
