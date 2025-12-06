package com.miniProjets.covoiturage.service;

import com.miniProjets.covoiturage.model.Passager;
import com.miniProjets.covoiturage.model.Reservation;
import com.miniProjets.covoiturage.model.Trajet;
import com.miniProjets.covoiturage.model.EtatReservation;
import com.miniProjets.covoiturage.repository.PassagerRepository;
import com.miniProjets.covoiturage.repository.ReservationRepository;
import com.miniProjets.covoiturage.repository.TrajetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassagerService {

    @Autowired
    private PassagerRepository passagerRepository;

    @Autowired
    private com.miniProjets.covoiturage.repository.UtilisateurRepository utilisateurRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // CRUD Passager
    public Passager createPassager(Passager passager) {
        if (passager == null) {
            System.out.println("Passager null");
            return null;
        }

        if (passagerRepository.findByEmail(passager.getEmail()) != null) {
            System.out.println("Passager déjà existe");
            return null;
        }

        return passagerRepository.save(passager);
    }

    public List<Passager> getAllPassagers() {
        return passagerRepository.findAll();
    }

    public Passager getPassagerById(Long id) {
        return passagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passager non trouvé"));
    }

    public Passager updatePassager(Long id, Passager passager) {
        Passager existing = getPassagerById(id);
        existing.setNom(passager.getNom());
        existing.setPrenom(passager.getPrenom());
        existing.setEmail(passager.getEmail());
        existing.setMotDePasse(passager.getMotDePasse());
        existing.setTelephone(passager.getTelephone());
        return passagerRepository.save(existing);
    }

    public void deletePassager(Long id) {
        passagerRepository.deleteById(id);
    }

    // Réserver un trajet
    public Reservation reserverTrajet(Long utilisateurId, Long trajetId) {
        com.miniProjets.covoiturage.model.Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

        if (trajet.getPlacesDisponibles() <= 0) {
            throw new RuntimeException("Plus de places disponibles");
        }

        // utilisateur.reserverTrajet(trajet); // Logic moved here
        // utilisateurRepository.save(utilisateur); // Not strictly needed if we save
        // reservation and cascade, but safe

        Reservation reservation = new Reservation(EtatReservation.CONFIRMED);
        reservation.setUtilisateur(utilisateur);
        reservation.setTrajet(trajet);

        trajet.reserverPlace(utilisateur);
        trajetRepository.save(trajet);

        return reservationRepository.save(reservation);
    }

    // Annuler une réservation
    public void annulerReservation(Long utilisateurId, Long reservationId) {
        // Passager passager = getPassagerById(passagerId); // Not needed for check if
        // we check reservation owner
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (!reservation.getUtilisateur().getId().equals(utilisateurId)) {
            throw new RuntimeException("Vous ne pouvez pas annuler cette réservation");
        }

        reservation.annulerReservation();
        reservationRepository.save(reservation);

        // Remettre une place disponible
        Trajet trajet = reservation.getTrajet();
        trajet.setPlacesDisponibles(trajet.getPlacesDisponibles() + 1);
        trajetRepository.save(trajet);
    }

    // Consulter mes réservations
    public List<Reservation> getMesReservations(Long utilisateurId) {
        return reservationRepository.findByUtilisateurId(utilisateurId);
    }

    public Passager seConnecter(String email, String motDePasse) {
        Passager passager = passagerRepository.findByEmail(email);
        if (passager != null && passager.getMotDePasse().equals(motDePasse)) {
            passager.seConnecter();
            return passager;
        }
        throw new RuntimeException("Email ou mot de passe incorrect");
    }
}
