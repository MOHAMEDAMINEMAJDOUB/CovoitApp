package com.miniProjets.covoiturage.service;

import com.miniProjets.covoiturage.model.Conducteur;
import com.miniProjets.covoiturage.model.Trajet;
import com.miniProjets.covoiturage.model.Voiture;
import com.miniProjets.covoiturage.repository.ConducteurRepository;
import com.miniProjets.covoiturage.repository.TrajetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConducteurService {

    @Autowired
    private ConducteurRepository conducteurRepository;
    @Autowired
    private TrajetRepository trajetRepository;

    // CRUD Conducteur
    public Conducteur createConducteur(Conducteur conducteur) {
        if (conducteur == null) {
            System.out.println("Passager null");
            return null;
        }

        if (conducteurRepository.findByEmail(conducteur.getEmail()) != null) {
            System.out.println("Passager déjà existe");
            return null;
        }
        return conducteurRepository.save(conducteur);
    }

    public List<Conducteur> getAllConducteurs() {
        return conducteurRepository.findAll();
    }

    public Conducteur getConducteurById(Long id) {
        return conducteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));
    }

    public Conducteur updateConducteur(Long id, Conducteur conducteur) {
        Conducteur existing = getConducteurById(id);
        existing.setNom(conducteur.getNom());
        existing.setPrenom(conducteur.getPrenom());
        existing.setEmail(conducteur.getEmail());
        existing.setMotDePasse(conducteur.getMotDePasse());
        existing.setTelephone(conducteur.getTelephone());
        return conducteurRepository.save(existing);
    }

    public void deleteConducteur(Long id) {
        conducteurRepository.deleteById(id);
    }

    // Gestion des trajets
    public Trajet proposerTrajet(Long conducteurId, Trajet trajet) {
        if (trajet != null) {
            Conducteur conducteur = conducteurRepository.findById(conducteurId)
                    .orElseThrow(() -> new RuntimeException("Conducteur non trouvé"));

            if (conducteur.getVoiture() == null) {
                throw new RuntimeException("Ce conducteur n'a pas encore de voiture.");
            }

            // ✅ Lier conducteur + voiture au trajet
            trajet.setUtilisateur(conducteur);
            trajet.setVoiture(conducteur.getVoiture());

            // ✅ Ajouter dans la liste du conducteur
            conducteur.getTrajets().add(trajet);

            // ✅ Sauvegarder le trajet dans la BD
            trajetRepository.save(trajet);
        }
        return trajet;
    }

    public void annulerTrajet(Long conducteurId, Long trajetId) {
        Conducteur conducteur = getConducteurById(conducteurId);
        Trajet trajet = conducteur.getTrajets().stream()
                .filter(t -> t.getId().equals(trajetId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));
        conducteur.annulerTrajet(trajet);
        conducteurRepository.save(conducteur);
    }

    // Gestion de la voiture
    public Voiture associerVoiture(Long conducteurId, Voiture voiture) {
        Conducteur conducteur = getConducteurById(conducteurId);

        // Valider que la voiture a les champs requis
        if (voiture.getMarque() == null || voiture.getModele() == null) {
            throw new RuntimeException("Marque et modèle sont requis pour créer une voiture");
        }

        conducteur.gérerVoiture(voiture);
        conducteurRepository.save(conducteur);
        return voiture;
    }

    public Conducteur seConnecter(String email, String motDePasse) {
        Conducteur conducteur = conducteurRepository.findByEmail(email);
        if (conducteur != null && conducteur.getMotDePasse().equals(motDePasse)) {
            return conducteur;
        }
        throw new RuntimeException("Email ou mot de passe incorrect");
    }
}
