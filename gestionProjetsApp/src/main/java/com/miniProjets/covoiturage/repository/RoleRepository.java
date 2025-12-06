package com.miniProjets.covoiturage.repository;

import com.miniProjets.covoiturage.model.ERole;
import com.miniProjets.covoiturage.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
