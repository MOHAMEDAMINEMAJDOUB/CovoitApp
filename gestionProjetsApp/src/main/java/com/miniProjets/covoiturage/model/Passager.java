package com.miniProjets.covoiturage.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passagers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Passager extends Utilisateur {

    public Passager(String nom, String prenom, String email, String motDePasse, String telephone) {
        super(nom, prenom, email, motDePasse, telephone);
    }
}
