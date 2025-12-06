package com.miniProjets.covoiturage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conducteurs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Conducteur extends Utilisateur {

    // Changed from OneToOne to OneToMany to allow multiple vehicles
    @OneToMany(mappedBy = "conducteur", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Completely ignore in JSON - prevents circular reference
    private List<Voiture> voitures = new ArrayList<>();

    public Conducteur(String nom, String prenom, String email, String motDePasse, String telephone) {
        super(nom, prenom, email, motDePasse, telephone);
    }

    public void proposerTrajet(Trajet trajet) {
        if (this.getTrajets() == null) {
            this.setTrajets(new ArrayList<>());
        }
        trajet.setUtilisateur(this);
        this.getTrajets().add(trajet);
    }

    public void annulerTrajet(Trajet trajet) {
        if (this.getTrajets() != null) {
            this.getTrajets().remove(trajet);
        }
    }

    public void gérerVoiture(Voiture voiture) {
        if (this.voitures == null) {
            this.voitures = new ArrayList<>();
        }
        if (!this.voitures.contains(voiture)) {
            this.voitures.add(voiture);
            voiture.setConducteur(this);
        }
    }

    // For backward compatibility - returns the first vehicle or null
    @JsonIgnore
    public Voiture getVoiture() {
        return (voitures != null && !voitures.isEmpty()) ? voitures.get(0) : null;
    }
}
