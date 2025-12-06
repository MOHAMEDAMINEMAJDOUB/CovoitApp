# Application de Covoiturage - Mini Projet

Application de covoiturage développée en Spring Boot avec MySQL permettant la gestion des conducteurs, passagers, trajets, voitures et réservations.

## Architecture

### Entités (Modèles)

#### Utilisateur (Classe abstraite)
- Classe de base pour Conducteur et Passager
- Attributs : `id`, `nom`, `prenom`, `email`, `motDePasse`, `telephone`
- Méthodes abstraites : `seConnecter()`, `seDeconnecter()`, `modifierProfil()`

#### Conducteur (Hérite de Utilisateur)
- Peut proposer des trajets
- Peut gérer sa voiture
- Peut consulter ses trajets
- Relation OneToMany avec Trajet
- Relation OneToOne avec Voiture

#### Passager (Hérite de Utilisateur)
- Peut réserver des trajets
- Peut consulter ses réservations
- Peut annuler des réservations
- Relation OneToMany avec Reservation

#### Trajet
- Attributs : `villeDepart`, `villeArrivee`, `dateHeure`, `placesDisponibles`, `prixParPlace`
- Relation ManyToOne avec Conducteur
- Relation Optional OneToOne avec Voiture
- Relation OneToMany avec Reservation

#### Voiture
- Attributs : `marque`, `modele`, `places`
- Relation OneToOne avec Conducteur

#### Reservation
- Attributs : `dateReservation`, `etat` (enum : Confirmée, Annulée)
- Relation ManyToOne avec Passager
- Relation ManyToOne avec Trajet

## Endpoints API

### Conducteurs (`/conducteurs`)
- `POST /conducteurs` - Créer un conducteur
- `GET /conducteurs` - Liste de tous les conducteurs
- `GET /conducteurs/{id}` - Détails d'un conducteur
- `PUT /conducteurs/{id}` - Modifier un conducteur
- `DELETE /conducteurs/{id}` - Supprimer un conducteur
- `POST /conducteurs/connexion` - Connexion d'un conducteur
- `POST /conducteurs/{id}/trajets` - Proposer un trajet
- `DELETE /conducteurs/{id}/trajets/{trajetId}` - Annuler un trajet
- `POST /conducteurs/{id}/voiture` - Associer une voiture

### Passagers (`/passagers`)
- `POST /passagers` - Créer un passager
- `GET /passagers` - Liste de tous les passagers
- `GET /passagers/{id}` - Détails d'un passager
- `PUT /passagers/{id}` - Modifier un passager
- `DELETE /passagers/{id}` - Supprimer un passager
- `POST /passagers/connexion` - Connexion d'un passager
- `POST /passagers/{id}/reservations?trajetId=X` - Réserver un trajet
- `DELETE /passagers/{id}/reservations/{reservationId}` - Annuler une réservation
- `GET /passagers/{id}/reservations` - Consulter mes réservations

### Trajets (`/trajets`)
- `POST /trajets` - Créer un trajet
- `GET /trajets` - Liste de tous les trajets
- `GET /trajets/{id}` - Détails d'un trajet
- `PUT /trajets/{id}` - Modifier un trajet
- `DELETE /trajets/{id}` - Supprimer un trajet
- `GET /trajets/rechercher?villeDepart=X&villeArrivee=Y` - Rechercher des trajets
- `GET /trajets/search?motCle=X` - Recherche par mot-clé
- `GET /trajets/conducteur/{conducteurId}` - Trajets d'un conducteur

### Voitures (`/voitures`)
- `POST /voitures` - Créer une voiture
- `GET /voitures` - Liste de toutes les voitures
- `GET /voitures/{id}` - Détails d'une voiture
- `PUT /voitures/{id}` - Modifier une voiture
- `DELETE /voitures/{id}` - Supprimer une voiture

### Réservations (`/reservations`)
- `POST /reservations` - Créer une réservation
- `GET /reservations` - Liste de toutes les réservations
- `GET /reservations/{id}` - Détails d'une réservation
- `PUT /reservations/{id}` - Modifier une réservation
- `DELETE /reservations/{id}` - Supprimer une réservation
- `GET /reservations/passager/{passagerId}` - Réservations d'un passager
- `GET /reservations/trajet/{trajetId}` - Réservations d'un trajet

## Configuration

### Base de données
- URL : `jdbc:mysql://localhost:3306/BDCovoiturage`
- Le schéma est créé automatiquement au démarrage (JPA/Hibernate)
- Configuration dans `application.properties`

### Serveur
- Port : 8080
- URL : http://localhost:8080

## Technologies utilisées
- Spring Boot 3.4.10
- Spring Data JPA
- MySQL
- Lombok
- Java 17

## Cas d'utilisation implémentés

### Pour le Conducteur
1. Créer un compte / Se connecter
2. Proposer un trajet
3. Modifier un trajet
4. Annuler un trajet
5. Consulter mes trajets
6. Ajouter / Modifier une voiture

### Pour le Passager
1. Créer un compte / Se connecter
2. Rechercher un trajet
3. Réserver un trajet
4. Annuler une réservation
5. Consulter mes réservations

## Structure du projet

```
src/main/java/com/miniProjets/covoiturage/
├── model/
│   ├── Utilisateur.java (abstraite)
│   ├── Conducteur.java
│   ├── Passager.java
│   ├── Trajet.java
│   ├── Voiture.java
│   ├── Reservation.java
│   └── EtatReservation.java (enum)
├── repository/
│   ├── ConducteurRepository.java
│   ├── PassagerRepository.java
│   ├── TrajetRepository.java
│   ├── VoitureRepository.java
│   └── ReservationRepository.java
├── service/
│   ├── ConducteurService.java
│   ├── PassagerService.java
│   ├── TrajetService.java
│   ├── VoitureService.java
│   └── ReservationService.java
├── controller/
│   ├── ConducteurController.java
│   ├── PassagerController.java
│   ├── TrajetController.java
│   ├── VoitureController.java
│   └── ReservationController.java
└── CovoiturageApplication.java
```

## Démarrage de l'application

```bash
# Compiler le projet
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

L'application sera accessible sur http://localhost:8080

## Exemples d'utilisation

### Créer un conducteur
```bash
POST http://localhost:8080/conducteurs
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@example.com",
  "motDePasse": "password123",
  "telephone": "0612345678"
}
```

### Créer un trajet
```bash
POST http://localhost:8080/trajets
Content-Type: application/json

{
  "villeDepart": "Paris",
  "villeArrivee": "Lyon",
  "dateHeure": "2024-12-25T10:00:00",
  "placesDisponibles": 4,
  "prixParPlace": 25.50
}
```

### Réserver un trajet
```bash
POST http://localhost:8080/passagers/1/reservations?trajetId=1
```

