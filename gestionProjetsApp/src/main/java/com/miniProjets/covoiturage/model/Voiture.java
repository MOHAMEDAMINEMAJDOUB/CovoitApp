package com.miniProjets.covoiturage.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "voiture")
@Data
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Voiture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marque;
    private String modele;
    private String immatriculation; // License plate

    @JsonProperty("nombrePlaces") // Map frontend "nombrePlaces" to backend "places"
    private int places;

    private String couleur; // Color

    // Relation ManyToOne avec Conducteur (many vehicles can belong to one driver)
    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = true)
    @JsonIgnoreProperties({ "voitures", "trajets", "reservations", "roles", "motDePasse", "telephone" }) // Prevent
                                                                                                         // circular
                                                                                                         // reference
    private Conducteur conducteur;

    // Constructeurs
    public Voiture() {
    }

    public Voiture(String marque, String modele, String immatriculation, int places, String couleur) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.places = places;
        this.couleur = couleur;
    }

    // Méthodes
    public void modifierVoiture(String marque, String modele, String immatriculation, int places, String couleur) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.places = places;
        this.couleur = couleur;
    }

    public void afficherDetails() {
        System.out.println("Voiture: " + marque + " " + modele + " (" + immatriculation + ") " +
                "avec " + places + " places, couleur: " + couleur);
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Conducteur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }
}
