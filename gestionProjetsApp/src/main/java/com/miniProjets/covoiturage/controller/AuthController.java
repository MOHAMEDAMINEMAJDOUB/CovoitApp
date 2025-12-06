package com.miniProjets.covoiturage.controller;

import com.miniProjets.covoiturage.dto.*;
import com.miniProjets.covoiturage.model.*;
import com.miniProjets.covoiturage.repository.*;
import com.miniProjets.covoiturage.security.JwtUtils;
import com.miniProjets.covoiturage.security.UserDetailsImpl;
import com.miniProjets.covoiturage.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ConducteurRepository conducteurRepository;

    @Autowired
    private PassagerRepository passagerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        // Determine user type
        String userType = "USER";
        if (conducteurRepository.findById(userDetails.getId()).isPresent()) {
            userType = "DRIVER";
        } else if (passagerRepository.findById(userDetails.getId()).isPresent()) {
            userType = "PASSENGER";
        }

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getNom(),
                userDetails.getPrenom(),
                roles,
                userType));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check if email already exists
        if (utilisateurRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Determine user type and create appropriate entity
        Utilisateur user;

        if ("DRIVER".equalsIgnoreCase(signUpRequest.getUserType())) {
            user = new Conducteur(
                    signUpRequest.getNom(),
                    signUpRequest.getPrenom(),
                    signUpRequest.getEmail(),
                    passwordEncoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getTelephone());
        } else if ("PASSENGER".equalsIgnoreCase(signUpRequest.getUserType())) {
            user = new Passager(
                    signUpRequest.getNom(),
                    signUpRequest.getPrenom(),
                    signUpRequest.getEmail(),
                    passwordEncoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getTelephone());
        } else {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Invalid user type. Must be DRIVER or PASSENGER"));
        }

        // Assign roles
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Default role based on user type
            if ("DRIVER".equalsIgnoreCase(signUpRequest.getUserType())) {
                Role driverRole = roleRepository.findByName(ERole.ROLE_DRIVER)
                        .orElseThrow(() -> new RuntimeException("Error: Role DRIVER is not found."));
                roles.add(driverRole);
            } else {
                Role passengerRole = roleRepository.findByName(ERole.ROLE_PASSENGER)
                        .orElseThrow(() -> new RuntimeException("Error: Role PASSENGER is not found."));
                roles.add(passengerRole);
            }
        } else {
            strRoles.forEach(role -> {
                switch (role.toUpperCase()) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                        roles.add(adminRole);
                        break;
                    case "DRIVER":
                        Role driverRole = roleRepository.findByName(ERole.ROLE_DRIVER)
                                .orElseThrow(() -> new RuntimeException("Error: Role DRIVER is not found."));
                        roles.add(driverRole);
                        break;
                    case "PASSENGER":
                        Role passengerRole = roleRepository.findByName(ERole.ROLE_PASSENGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role PASSENGER is not found."));
                        roles.add(passengerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        // Save user (will cascade to Conducteur or Passager table)
        if (user instanceof Conducteur) {
            conducteurRepository.save((Conducteur) user);
        } else {
            passagerRepository.save((Passager) user);
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return ResponseEntity.ok(new JwtResponse(
                            token,
                            requestRefreshToken,
                            user.getId(),
                            user.getEmail(),
                            user.getNom(),
                            user.getPrenom(),
                            user.getRoles().stream()
                                    .map(role -> role.getName().name())
                                    .collect(Collectors.toList()),
                            determineUserType(user)));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.findByToken(request.getRefreshToken())
                .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUser().getId()));

        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    private String determineUserType(Utilisateur user) {
        if (user instanceof Conducteur) {
            return "DRIVER";
        } else if (user instanceof Passager) {
            return "PASSENGER";
        }
        return "USER";
    }
}
