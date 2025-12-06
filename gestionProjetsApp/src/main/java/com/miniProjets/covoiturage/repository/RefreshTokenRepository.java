package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.RefreshToken;
import com.miniProjets.covoiturage.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(Utilisateur user);

    int deleteByUser(Utilisateur user);
}
