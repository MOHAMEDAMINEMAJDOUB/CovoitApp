package com.miniProjets.covoiturage.controller;

import com.miniProjets.covoiturage.model.Voiture;
import com.miniProjets.covoiturage.service.VoitureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voitures")
public class VoitureController {

    @Autowired
    private VoitureService voitureService;

    // CRUD Voiture
    @PostMapping
    public ResponseEntity<?> createVoiture(@RequestBody Voiture voiture) {
        try {
            Voiture v = voitureService.createVoiture(voiture);
            if (v != null) {
                return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Voiture Ajoutée avec Succès"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(v);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }

    // Créer une voiture avec un conducteur
    @PostMapping("/conducteur/{conducteurId}")
    public ResponseEntity<?> associerVoitureWithConducteur(@PathVariable Long conducteurId,
            @RequestBody Voiture voiture) {
        try {
            Voiture v = voitureService.associerVoitureWithConducteur(voiture, conducteurId);
            return ResponseEntity.status(HttpStatus.CREATED).body(v);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllVoitures() {
        try {
            List<Voiture> list = voitureService.getAllVoitures();
            // Always return the list (even if empty) for consistent JSON response
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVoitureById(@PathVariable Long id) {
        try {
            Voiture v = voitureService.getVoitureById(id);
            return ResponseEntity.ok(v);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        try {
            Voiture updated = voitureService.updateVoiture(id, voiture);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoiture(@PathVariable Long id) {
        try {
            boolean deleted = voitureService.deleteVoiture(id);

            if (deleted) {
                return ResponseEntity.ok(java.util.Collections.singletonMap("message", "✅ Voiture supprimée avec succès !"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(java.util.Collections.singletonMap("message", "❌ Impossible de supprimer : la voiture est liée à un conducteur !"));
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Collections.singletonMap("message", "❌ Voiture introuvable !"));
        }
    }

}
