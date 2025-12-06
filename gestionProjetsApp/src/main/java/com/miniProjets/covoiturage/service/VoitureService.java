package com.miniProjets.covoiturage.service;

import com.miniProjets.covoiturage.model.Conducteur;
import com.miniProjets.covoiturage.model.Voiture;
import com.miniProjets.covoiturage.repository.ConducteurRepository;
import com.miniProjets.covoiturage.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoitureService {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ConducteurRepository conducteurRepository;

    @Autowired
    private com.miniProjets.covoiturage.repository.TrajetRepository trajetRepository;

    // CRUD Voiture
    public Voiture createVoiture(Voiture voiture) {
        if (voiture.getModele() == null || voiture.getMarque() == null) {
            throw new RuntimeException("Marque et modèle sont requis pour créer une voiture");
        }

        // Si un conducteur est associé, vérifier qu'il existe
        if (voiture.getConducteur() != null && voiture.getConducteur().getId() != null) {
            Conducteur conducteur = conducteurRepository.findById(voiture.getConducteur().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Conducteur non trouvé avec l'ID: " + voiture.getConducteur().getId()));

            voiture.setConducteur(conducteur);
        }

        return voitureRepository.save(voiture);
    }

    public Voiture associerVoitureWithConducteur(Voiture voiture, Long conducteurId) {
        if (voiture.getModele() == null || voiture.getMarque() == null) {
            throw new RuntimeException("Marque et modèle sont requis pour créer une voiture");
        }

        Conducteur conducteur = conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé avec l'ID: " + conducteurId));

        voiture.setConducteur(conducteur);
        return voitureRepository.save(voiture);
    }

    public List<Voiture> getAllVoitures() {
        return voitureRepository.findAll();
    }

    public Voiture getVoitureById(Long id) {
        return voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée"));
    }

    public Voiture updateVoiture(Long id, Voiture voiture) {
        Voiture existing = getVoitureById(id);
        existing.setMarque(voiture.getMarque());
        existing.setModele(voiture.getModele());
        existing.setImmatriculation(voiture.getImmatriculation());
        existing.setCouleur(voiture.getCouleur());
        existing.setPlaces(voiture.getPlaces());
        return voitureRepository.save(existing);
    }

    public boolean deleteVoiture(Long id) {
        if (id == null)
            return false;

        Optional<Voiture> v = voitureRepository.findById(id);

        if (v.isPresent()) {
            List<com.miniProjets.covoiturage.model.Trajet> trajets = trajetRepository.findByVoitureId(id);
            if (trajets != null) {
                for (com.miniProjets.covoiturage.model.Trajet trajet : trajets) {
                    // Check if trip is in the future AND NOT CANCELLED
                    // If it is future and active, we block deletion
                    if (trajet.getDateHeure().isAfter(LocalDateTime.now()) && !"ANNULE".equals(trajet.getStatut().name())) {
                        throw new RuntimeException("Impossible de supprimer : cette voiture est associée à des trajets futurs actifs !");
                    }
                    // For past trips or cancelled future trips, unlink the car instead of deleting the trip
                    trajet.setVoiture(null);
                    trajetRepository.save(trajet);
                }
            }

            voitureRepository.deleteById(id);
            return true;
        }
        return false;
    }
}