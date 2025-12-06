package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur, Long> {
    Conducteur findByEmail(String email);

    Conducteur getConducteurById(Long conducteurId);
}

