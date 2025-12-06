package com.miniProjets.covoiturage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    private String email;

    @JsonIgnore
    private String motDePasse;

    private String telephone;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore // Don't serialize roles in API responses
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JsonIgnore // Completely ignore in JSON - not needed in vehicle/user responses
    private java.util.List<Trajet> trajets = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JsonIgnore // Completely ignore in JSON - not needed in vehicle/user responses
    private java.util.List<Reservation> reservations = new java.util.ArrayList<>();

    public Utilisateur(String nom, String prenom, String email, String motDePasse, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
    }

    public void seConnecter() {
        // Méthode appelée lors de la connexion
        // Peut être étendue pour ajouter de la logique supplémentaire
        // comme la gestion de session, la journalisation, etc.
    }

    // Explicit getters/setters (Lombok @Data may not be working)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
