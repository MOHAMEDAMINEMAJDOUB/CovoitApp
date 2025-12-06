package com.miniProjets.covoiturage.config;

import com.miniProjets.covoiturage.model.ERole;
import com.miniProjets.covoiturage.model.Role;
import com.miniProjets.covoiturage.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        for (ERole role : ERole.values()) {
            if (roleRepository.findByName(role).isEmpty()) {
                roleRepository.save(new Role(role));
                System.out.println("Initialized role: " + role);
            }
        }
    }
}
