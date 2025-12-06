package com.miniProjets.covoiturage.controller;

import com.miniProjets.covoiturage.model.Conducteur;
import com.miniProjets.covoiturage.model.Trajet;
import com.miniProjets.covoiturage.model.Voiture;
import com.miniProjets.covoiturage.service.ConducteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conducteurs")
public class ConducteurController {

    @Autowired
    private ConducteurService conducteurService;

    // CRUD Conducteur
    @PostMapping
    public ResponseEntity<?> createConducteur(@RequestBody Conducteur conducteur) {
        try {
            Conducteur c = conducteurService.createConducteur(conducteur);
            if (c == null) {
                return ResponseEntity.ok("Conducteur null ou il déjà existe");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(c);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllConducteurs() {
        try {
            List<Conducteur> list = conducteurService.getAllConducteurs();
            if (list.isEmpty()) {
                return ResponseEntity.ok("Aucune resultat oubtenu");
            }
            return ResponseEntity.ok(list);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConducteurById(@PathVariable Long id) {
        try {
            Conducteur c = conducteurService.getConducteurById(id);
            return ResponseEntity.ok(c);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateConducteur(@PathVariable Long id, @RequestBody Conducteur conducteur) {
        try {
            Conducteur updated = conducteurService.updateConducteur(id, conducteur);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConducteur(@PathVariable Long id) {
        try {
            conducteurService.deleteConducteur(id);
            return ResponseEntity.ok("Conducteur supprimé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Proposer un trajet
    @PostMapping("/{id}/trajets")
    public ResponseEntity<?> proposerTrajet(@PathVariable Long id, @RequestBody Trajet trajet) {
        try {
            Trajet t = conducteurService.proposerTrajet(id, trajet);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("✅ Trajet proposé avec succès (ID: " + t.getId() + ")");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("❌ Erreur : " + e.getMessage());
        }
    }

    // Annuler un trajet
    @DeleteMapping("/{id}/trajets/{trajetId}")
    public ResponseEntity<?> annulerTrajet(@PathVariable Long id, @PathVariable Long trajetId) {
        try {
            conducteurService.annulerTrajet(id, trajetId);
            return ResponseEntity.ok("Trajet annulé avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Associer une voiture - FIXED: Now returns JSON object instead of plain text
    @PostMapping("/{id}/voiture")
    public ResponseEntity<?> associerVoiture(@PathVariable Long id, @RequestBody Voiture voiture) {
        try {
            Voiture v = conducteurService.associerVoiture(id, voiture);
            return ResponseEntity.ok(v);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

    

    

    