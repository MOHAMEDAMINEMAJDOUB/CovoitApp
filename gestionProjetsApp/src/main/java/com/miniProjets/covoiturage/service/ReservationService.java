package com.miniProjets.covoiturage.service;

import com.miniProjets.covoiturage.model.Reservation;
import com.miniProjets.covoiturage.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private com.miniProjets.covoiturage.repository.TrajetRepository trajetRepository;

    // CRUD Reservation
    public Reservation createReservation(Reservation reservation) {
        if (reservation != null) {
            // Validate that the user is not reserving their own trip
            if (reservation.getTrajet() != null && reservation.getTrajet().getId() != null) {
                Long trajetId = reservation.getTrajet().getId();
                com.miniProjets.covoiturage.model.Trajet trajet = trajetRepository
                        .findById(trajetId)
                        .orElseThrow(() -> new RuntimeException("Trajet non trouvé"));

                if (trajet.getUtilisateur() != null && reservation.getUtilisateur() != null &&
                        trajet.getUtilisateur().getId() != null && reservation.getUtilisateur().getId() != null &&
                        trajet.getUtilisateur().getId().equals(reservation.getUtilisateur().getId())) {
                    throw new RuntimeException("Vous ne pouvez pas réserver votre propre trajet");
                }

                // Check if there are available seats
                if (trajet.getPlacesDisponibles() <= 0) {
                    throw new RuntimeException("Aucune place disponible pour ce trajet");
                }

                // Decrement available seats
                trajet.setPlacesDisponibles(trajet.getPlacesDisponibles() - 1);
                trajetRepository.save(trajet);
            }

            System.out.println("Reservation Ajoutée!");
            return reservationRepository.save(reservation);
        } else {
            System.out.println("Reservation Invalide!");
            return null;

        }

    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existing = getReservationById(id);
        existing.setDateReservation(reservation.getDateReservation());
        existing.setEtat(reservation.getEtat());
        return reservationRepository.save(existing);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Consulter les réservations
    public List<Reservation> getReservationsByPassager(Long utilisateurId) {
        return reservationRepository.findByUtilisateurId(utilisateurId);
    }

    public List<Reservation> getReservationsByTrajet(Long trajetId) {
        return reservationRepository.findByTrajetId(trajetId);
    }
}
