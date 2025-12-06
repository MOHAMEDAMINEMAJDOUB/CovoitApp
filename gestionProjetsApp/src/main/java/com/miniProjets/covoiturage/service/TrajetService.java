package com.miniProjets.covoiturage.service;

import com.miniProjets.covoiturage.dto.TrajetDTO;
import com.miniProjets.covoiturage.exception.TrajetNotFoundException;
import com.miniProjets.covoiturage.exception.VehicleRequiredException;
import com.miniProjets.covoiturage.model.Utilisateur;
import com.miniProjets.covoiturage.model.Trajet;
import com.miniProjets.covoiturage.model.Voiture;
import com.miniProjets.covoiturage.repository.UtilisateurRepository;
import com.miniProjets.covoiturage.repository.TrajetRepository;
import com.miniProjets.covoiturage.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrajetService {

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private VoitureRepository voitureRepository;

    // Create trajet with DTO and authenticated user
    @Transactional
    public Trajet createTrajet(TrajetDTO trajetDTO, Long utilisateurId) {
        // Get authenticated user
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // For now, allow trips without vehicles (temporary workaround)
        // TODO: Uncomment this when vehicle management is implemented
        // if (conducteur.getVoiture() == null) {
        // throw new VehicleRequiredException();
        // }

        // Create trajet from DTO
        Trajet trajet = new Trajet();
        trajet.setVilleDepart(trajetDTO.getVilleDepart());
        trajet.setVilleArrivee(trajetDTO.getVilleArrivee());
        trajet.setDateHeure(trajetDTO.getDateHeure());
        trajet.setPlacesDisponibles(trajetDTO.getPlacesDisponibles());
        trajet.setPrixParPlace(trajetDTO.getPrixParPlace());
        trajet.setUtilisateur(utilisateur);

        if (trajetDTO.getVoitureId() != null) {
            Voiture voiture = voitureRepository.findById(trajetDTO.getVoitureId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            // Verify vehicle belongs to user
            if (voiture.getConducteur() != null && !voiture.getConducteur().getId().equals(utilisateurId)) {
                throw new RuntimeException("You can only use your own vehicles");
            }
            trajet.setVoiture(voiture);
        }

        return trajetRepository.save(trajet);
    }

    @Transactional(readOnly = true)
    public List<Trajet> getAllTrajets() {
        return trajetRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Trajet getTrajetById(Long id) {
        return trajetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
    }

    public Trajet updateTrajet(Long id, Trajet trajet) {
        Trajet existing = getTrajetById(id);
        existing.setVilleDepart(trajet.getVilleDepart());
        existing.setVilleArrivee(trajet.getVilleArrivee());
        existing.setDateHeure(trajet.getDateHeure());
        existing.setPlacesDisponibles(trajet.getPlacesDisponibles());
        existing.setPrixParPlace(trajet.getPrixParPlace());
        return trajetRepository.save(existing);
    }

    public void deleteTrajet(Long id) {
        Trajet trajet = getTrajetById(id);
        trajet.annulerTrajet();
        trajetRepository.save(trajet);
        // Soft delete: do not physically delete the record
        // trajetRepository.deleteById(id);
    }

    // Rechercher des trajets
    @Transactional(readOnly = true)
    public List<Trajet> rechercherTrajets(String villeDepart, String villeArrivee) {
        return trajetRepository.findByVilleDepartAndVilleArrivee(villeDepart, villeArrivee);
    }

    @Transactional(readOnly = true)
    public List<Trajet> rechercherTrajetsParMotCle(String motCle) {
        return trajetRepository.rechercherTrajets(motCle);
    }

    // Trajets d'un conducteur
    @Transactional(readOnly = true)
    public List<Trajet> getTrajetsByConducteur(Long utilisateurId) {
        return trajetRepository.findByUtilisateurId(utilisateurId);
    }
}
