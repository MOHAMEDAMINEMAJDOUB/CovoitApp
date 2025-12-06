package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
}

