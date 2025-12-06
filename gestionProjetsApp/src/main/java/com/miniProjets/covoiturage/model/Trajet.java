package com.miniProjets.covoiturage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Data
public class Trajet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String villeDepart;
    private String villeArrivee;
    private LocalDateTime dateHeure;
    private int placesDisponibles;
    private float prixParPlace;

    @Enumerated(EnumType.STRING)
    private EtatTrajet statut = EtatTrajet.PLANIFIE;

    // Relation ManyToOne avec Utilisateur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id", nullable = false)
    // @JsonIgnore
    @JsonBackReference
    private Utilisateur utilisateur;

    // Relation ManyToOne avec Voiture (une voiture peut être utilisée pour
    // plusieurs trajets)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voiture_id", nullable = true)
    @JsonIgnore
    private Voiture voiture;

    // Relation OneToMany avec Reservation
    @OneToMany(mappedBy = "trajet", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    // Constructeurs
    public Trajet() {
    }

    public Trajet(String villeDepart, String villeArrivee, LocalDateTime dateHeure,
            int placesDisponibles, float prixParPlace) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateHeure = dateHeure;
        this.placesDisponibles = placesDisponibles;
        this.prixParPlace = prixParPlace;
    }

    // Méthodes
    public void reserverPlace(Utilisateur utilisateur) {
        if (placesDisponibles > 0) {
            placesDisponibles--;
        } else {
            throw new RuntimeException("Plus de places disponibles sur ce trajet");
        }
    }

    public void annulerTrajet() {
        this.statut = EtatTrajet.ANNULE;
        // Logique d'annulation
        if (reservations != null && !reservations.isEmpty()) {
            for (Reservation r : reservations) {
                r.setEtat(EtatReservation.CANCELLED);
            }
        }
    }

    public void modifierTrajet(String villeDepart, String villeArrivee,
            LocalDateTime dateHeure, float prixParPlace,
            int placesDisponibles) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateHeure = dateHeure;
        this.prixParPlace = prixParPlace;
        this.placesDisponibles = placesDisponibles;
    }

    // Getter/Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(String villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public float getPrixParPlace() {
        return prixParPlace;
    }

    public void setPrixParPlace(float prixParPlace) {
        this.prixParPlace = prixParPlace;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Voiture getVoiture() {
        return voiture;
    }

    public void setVoiture(Voiture voiture) {
        this.voiture = voiture;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("conducteur")
    public Utilisateur getConducteur() {
        return utilisateur;
    }

    public EtatTrajet getStatut() {
        return statut;
    }

    public void setStatut(EtatTrajet statut) {
        this.statut = statut;
    }
}
