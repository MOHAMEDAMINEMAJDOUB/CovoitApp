# 🚗 CovoitApp - Application de Covoiturage

Application web complète de covoiturage développée avec **Spring Boot** (Backend) et **Angular** (Frontend).

## 📋 Description

CovoitApp est une plateforme moderne de covoiturage permettant aux conducteurs de publier leurs trajets et aux passagers de réserver des places. L'application offre une gestion complète des utilisateurs, des véhicules, des trajets et des réservations avec un système d'authentification sécurisé basé sur JWT.

## 🏗️ Architecture

### Backend - Spring Boot
- **Framework**: Spring Boot 3.x
- **Sécurité**: JWT (JSON Web Tokens)
- **Base de données**: JPA/Hibernate
- **Architecture**: MVC en couches (Controller, Service, Repository)

### Frontend - Angular
- **Framework**: Angular 17+
- **Architecture**: Standalone Components
- **Routing**: Angular Router avec Guards
- **HTTP**: Intercepteurs pour JWT

## 🚀 Fonctionnalités

### Pour les Conducteurs
- ✅ Gestion des véhicules personnels
- ✅ Publication et modification de trajets
- ✅ Gestion des réservations
- ✅ Statistiques et tableau de bord

### Pour les Passagers
- ✅ Recherche de trajets disponibles
- ✅ Réservation de places
- ✅ Historique des réservations
- ✅ Profil utilisateur

### Sécurité
- 🔐 Authentification JWT
- 🔐 Gestion des rôles (ADMIN, DRIVER, PASSENGER)
- 🔐 Protection des routes (Guards)
- 🔐 Intercepteurs HTTP automatiques

## 📂 Structure du Projet

```
CovoitApp/
├── gestionProjetsApp/          # Backend Spring Boot
│   └── src/main/java/com/miniProjets/covoiturage/
│       ├── config/             # Configuration (Security, CORS)
│       ├── controller/         # REST Controllers
│       ├── dto/               # Data Transfer Objects
│       ├── model/             # Entités JPA
│       ├── repository/        # Repositories
│       ├── security/          # JWT & Security
│       └── service/           # Logique métier
│
└── FrontEnd/                   # Frontend Angular
    └── src/app/
        ├── core/              # Services & Guards
        │   ├── guards/
        │   ├── interceptors/
        │   ├── models/
        │   └── services/
        └── features/          # Composants par fonctionnalité
            ├── auth/
            ├── dashboard/
            ├── home/
            ├── reservations/
            ├── trips/
            └── vehicles/
```

## 🛠️ Installation

### Prérequis
- Java 17+
- Node.js 18+
- Maven 3.8+
- MySQL/PostgreSQL

### Backend

```bash
cd gestionProjetsApp
mvn clean install
mvn spring-boot:run
```

Le backend sera accessible sur `http://localhost:8080`

### Frontend

```bash
cd FrontEnd
npm install
npm start
```

Le frontend sera accessible sur `http://localhost:4200`

## 🔧 Configuration

### Backend - application.properties

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/covoiturage_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT
jwt.secret=your_secret_key
jwt.expiration=86400000
```

### Frontend - environment.ts

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## 📚 Documentation

Pour plus de détails sur l'architecture et la structure du projet, consultez :
- [README_PROJECT_STRUCTURE.md](./README_PROJECT_STRUCTURE.md) - Documentation complète de l'architecture

## 🔗 API Endpoints

### Authentification
- `POST /api/auth/login` - Connexion
- `POST /api/auth/signup` - Inscription

### Trajets
- `GET /api/trajets` - Liste des trajets
- `POST /api/trajets` - Créer un trajet (DRIVER)
- `PUT /api/trajets/{id}` - Modifier un trajet (DRIVER)
- `DELETE /api/trajets/{id}` - Supprimer un trajet (DRIVER)

### Réservations
- `GET /api/reservations` - Mes réservations
- `POST /api/reservations` - Créer une réservation
- `PUT /api/reservations/{id}` - Modifier une réservation

### Véhicules
- `GET /api/voitures` - Mes véhicules
- `POST /api/voitures` - Ajouter un véhicule
- `PUT /api/voitures/{id}` - Modifier un véhicule
- `DELETE /api/voitures/{id}` - Supprimer un véhicule

## 👥 Rôles Utilisateurs

- **ADMIN**: Accès complet à l'administration
- **DRIVER**: Peut publier des trajets et gérer ses véhicules
- **PASSENGER**: Peut rechercher et réserver des trajets

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou une pull request.

## 📝 Licence

Ce projet est développé dans un cadre éducatif.

## 👨‍💻 Auteur

**Mohamed Amine Majdoub**

---

⭐ N'oubliez pas de mettre une étoile si ce projet vous a été utile !