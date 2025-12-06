package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
    Passager findByEmail(String email);
}

