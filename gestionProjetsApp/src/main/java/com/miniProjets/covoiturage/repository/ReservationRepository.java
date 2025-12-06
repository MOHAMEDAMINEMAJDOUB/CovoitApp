package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUtilisateurId(Long utilisateurId);

    List<Reservation> findByTrajetId(Long trajetId);
}
