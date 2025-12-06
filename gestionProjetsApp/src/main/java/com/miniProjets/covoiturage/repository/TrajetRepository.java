package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee);

    @Query("SELECT t FROM Trajet t WHERE t.utilisateur.id = :utilisateurId")
    List<Trajet> findByUtilisateurId(@Param("utilisateurId") Long utilisateurId);

    @Query("SELECT t FROM Trajet t WHERE t.villeDepart LIKE %:motCle% OR t.villeArrivee LIKE %:motCle%")
    List<Trajet> rechercherTrajets(@Param("motCle") String motCle);

    List<Trajet> findByVoitureId(Long voitureId);
}
