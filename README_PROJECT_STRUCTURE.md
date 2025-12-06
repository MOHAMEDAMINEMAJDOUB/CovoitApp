# Documentation de la Structure du Projet

Ce document explique l'architecture et la structure des répertoires de l'application de Covoiturage. Le projet est divisé en deux parties principales : le Backend (Spring Boot) et le Frontend (Angular).

## 💡 Concept Clé : Différence entre Service et DTO

Il est important de comprendre la distinction entre ces deux composants fondamentaux de l'architecture backend :

### 📦 DTO (Data Transfer Object)
*   **C'est quoi ?** Un objet simple (POJO) utilisé uniquement pour transporter des données d'un point A à un point B (par exemple, du client vers le serveur).
*   **Rôle :** Il définit la structure des données attendues ou renvoyées par l'API. Il ne contient **aucune logique métier**.
*   **Exemple :** `LoginRequest` contient juste `email` et `password`. Il ne vérifie pas si le mot de passe est correct, il transporte juste l'information.
*   **Pourquoi ?** Pour découpler la base de données (Entités) de ce que voit l'utilisateur (API). Cela permet de cacher certains champs (comme le mot de passe haché) ou d'agréger des données.

### ⚙️ Service
*   **C'est quoi ?** Une classe qui contient la **logique métier** de l'application.
*   **Rôle :** C'est le "cerveau" de l'application. Il effectue les calculs, applique les règles de gestion, valide les données complexes et appelle les Repositories pour accéder à la base de données.
*   **Exemple :** `AuthService` reçoit le DTO `LoginRequest`, vérifie si l'email existe dans la base (via Repository), compare les mots de passe, et génère un token JWT si tout est correct.

---

## 📂 Backend : `gestionProjetsApp`

Le backend est construit avec **Spring Boot** et suit une architecture en couches standard.

### Structure des Répertoires (`src/main/java/com/miniProjets/covoiturage`)

*   **`config/`** : Classes de configuration.
    *   Contient la configuration pour CORS (Cross-Origin Resource Sharing), la documentation Swagger/OpenAPI, et d'autres configurations globales.
*   **`controller/`** : Contrôleurs REST.
    *   **Objectif** : Gérer les requêtes HTTP entrantes et définir les points de terminaison de l'API (Endpoints).
    *   **Fichiers Clés** : `AuthController.java` (Connexion/Inscription), `TrajetController.java` (Opérations sur les trajets), `ReservationController.java`, `ConducteurStatsController.java`.
*   **`dto/`** (Data Transfer Objects) :
    *   **Objectif** : Objets utilisés pour transférer les données entre le client et le serveur.
    *   **Fichiers Clés** : `LoginRequest.java`, `TrajetDTO.java`, `UserDTO.java`.
*   **`exception/`** : Gestion Globale des Exceptions.
    *   **Objectif** : Centraliser la gestion des erreurs pour renvoyer des réponses d'erreur cohérentes au frontend.
    *   **Fichiers Clés** : `GlobalExceptionHandler.java`.
*   **`model/`** : Entités JPA.
    *   **Objectif** : Représente les tables de la base de données.
    *   **Fichiers Clés** : `Utilisateur.java`, `Trajet.java`, `Reservation.java`, `Voiture.java`.
*   **`repository/`** : Couche d'Accès aux Données.
    *   **Objectif** : Interfaces étendant `JpaRepository` pour les opérations CRUD (Create, Read, Update, Delete) en base de données.
    *   **Fichiers Clés** : `UserRepository.java`, `TrajetRepository.java`.
*   **`security/`** : Configuration de la Sécurité.
    *   **Objectif** : Implémente l'authentification JWT (JSON Web Token) et la logique d'autorisation.
    *   **Fichiers Clés** : `JwtUtils.java`, `UserDetailsServiceImpl.java`, `WebSecurityConfig.java`.
*   **`service/`** : Couche de Logique Métier.
    *   **Objectif** : Contient les règles métier, faisant le pont entre les Contrôleurs et les Repositories.
    *   **Fichiers Clés** : `AuthService.java`, `TrajetService.java`, `ReservationService.java`.

---

## 📂 Frontend : `FrontEnd`

Le frontend est construit avec **Angular** (v17+) en utilisant une architecture basée sur des composants (Standalone Components).

### Structure des Répertoires (`src/app`)

*   **`core/`** : Module Core/Partagé.
    *   Contient les services singletons et les utilitaires globaux utilisés dans toute l'application.
    *   **`guards/`** : Gardes de route pour protéger les pages (ex: `AuthGuard` pour empêcher l'accès au tableau de bord sans connexion).
    *   **`interceptors/`** : Intercepteurs HTTP (ex: `JwtInterceptor` pour ajouter le token à chaque requête).
    *   **`models/`** : Interfaces TypeScript définissant les structures de données (ex: `User`, `Trip`, `Vehicle`).
    *   **`services/`** : Services pour la communication avec l'API.
        *   `auth.service.ts` : Connexion, inscription, gestion du token.
        *   `trip.service.ts` : Opérations CRUD pour les trajets.
        *   `vehicle.service.ts` : Gestion des véhicules.
*   **`features/`** : Modules de Fonctionnalités.
    *   Organisé par domaine fonctionnel.
    *   **`auth/`** : Pages d'authentification.
        *   `login/` : Composant formulaire de connexion.
        *   `signup/` : Composant formulaire d'inscription.
    *   **`dashboard/`** : Tableau de Bord Utilisateur.
        *   Affiche les statistiques et les actions rapides pour les conducteurs et passagers.
    *   **`home/`** : Page d'Accueil.
        *   Page publique avec fonctionnalité de recherche.
    *   **`reservations/`** : Gestion des Réservations.
        *   `my-reservations/` : Liste des réservations de l'utilisateur.
    *   **`trips/`** : Gestion des Trajets.
        *   `create-trip/` : Formulaire pour publier ou modifier un trajet.
        *   `trips-list/` : Recherche et liste des trajets disponibles.
        *   `my-trips/` : Vue conducteur de ses trajets publiés.
    *   **`vehicles/`** : Gestion des Véhicules.
        *   `my-vehicles/` : CRUD pour les voitures du conducteur.

### Fichiers de Configuration Clés
*   **`app.routes.ts`** : Définit les routes de navigation de l'application.
*   **`app.config.ts`** : Configuration globale de l'application (fournisseurs, configuration client HTTP).

---

## 📂 Backend : Packages `config` et `security`

### Package `config` - Configuration de l'Application

#### 1. **`SecurityConfig.java`** - Configuration de la Sécurité Principale

**Objectif** : Configure Spring Security pour l'authentification JWT et les autorisations.

**Méthodes Clés** :

*   **`authenticationJwtTokenFilter()`** : Crée le filtre JWT qui intercepte chaque requête.
*   **`authenticationProvider()`** : Configure le fournisseur d'authentification avec `CustomUserDetailsService` et l'encodeur de mot de passe.
*   **`authenticationManager()`** : Fournit le gestionnaire d'authentification utilisé pour valider les identifiants.
*   **`passwordEncoder()`** : Retourne un encodeur BCrypt pour hacher les mots de passe.
*   **`filterChain(HttpSecurity http)`** : **Configuration principale de sécurité** :
    *   Désactive CSRF (car JWT est stateless)
    *   Active CORS
    *   Configure la gestion des sessions en mode STATELESS
    *   **Règles d'autorisation** :
        *   `/api/auth/**` : Public (login, register)
        *   `GET /trajets`, `/trajets/**` : Public (consultation des trajets)
        *   `POST/PUT/DELETE /trajets` : Réservé aux `DRIVER`
        *   `/voitures/**`, `/reservations/**` : Authentification requise
        *   `/conducteurs`, `/passagers` : Réservé aux `ADMIN`
*   **`corsConfigurationSource()`** : Configure CORS pour autoriser les requêtes du frontend Angular.

---

#### 2. **`WebConfig.java`** - Configuration Web MVC

**Objectif** : Configuration supplémentaire pour CORS au niveau MVC.

**Méthodes Clés** :

*   **`corsConfigurer()`** : Retourne un `WebMvcConfigurer` qui ajoute les mappings CORS.
*   **`addCorsMappings(CorsRegistry registry)`** : Configure CORS pour toutes les routes (`/**`) :
    *   Origine autorisée : `http://localhost:4200`
    *   Méthodes autorisées : GET, POST, PUT, DELETE, OPTIONS
    *   Autorise les credentials (cookies, headers d'authentification)

---

#### 3. **`RoleInitializer.java`** - Initialisation des Rôles

**Objectif** : Initialise automatiquement les rôles dans la base de données au démarrage de l'application.

**Méthodes Clés** :

*   **`run(String... args)`** : Implémente `CommandLineRunner` pour s'exécuter au démarrage.
    *   Parcourt tous les rôles définis dans `ERole` (ROLE_ADMIN, ROLE_DRIVER, ROLE_PASSENGER)
    *   Vérifie si chaque rôle existe déjà dans la base de données
    *   Crée et sauvegarde les rôles manquants

---

### Package `security` - Sécurité et Authentification JWT

#### 1. **`JwtUtils.java`** - Utilitaire JWT

**Objectif** : Gère la génération, validation et extraction d'informations des tokens JWT.

**Propriétés** :
*   `jwtSecret` : Clé secrète pour signer les tokens (depuis `application.properties`)
*   `jwtExpiration` : Durée de validité du token en millisecondes

**Méthodes Clés** :

*   **`generateJwtToken(Authentication authentication)`** : Génère un token JWT après une authentification réussie.
    *   Extrait l'email de l'utilisateur
    *   Crée un token signé avec HS512
    *   Définit la date d'expiration
*   **`generateTokenFromEmail(String email)`** : Génère un token directement à partir d'un email (utilisé lors de l'inscription).
*   **`getUserNameFromJwtToken(String token)`** : Extrait l'email (username) du token JWT.
*   **`validateJwtToken(String authToken)`** : Valide un token JWT.
    *   Vérifie la signature
    *   Vérifie l'expiration
    *   Gère les exceptions (token malformé, expiré, non supporté)

---

#### 2. **`JwtAuthenticationFilter.java`** - Filtre d'Authentification JWT

**Objectif** : Intercepte chaque requête HTTP pour vérifier et valider le token JWT.

**Méthodes Clés** :

*   **`doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)`** : Méthode principale du filtre.
    *   Extrait le token JWT du header `Authorization`
    *   Valide le token avec `JwtUtils`
    *   Si valide :
        *   Extrait l'email de l'utilisateur
        *   Charge les détails de l'utilisateur via `CustomUserDetailsService`
        *   Crée un objet `Authentication` et le place dans le `SecurityContext`
    *   Passe la requête au filtre suivant
*   **`parseJwt(HttpServletRequest request)`** : Extrait le token du header `Authorization`.
    *   Vérifie que le header commence par "Bearer "
    *   Retourne le token sans le préfixe "Bearer "

---

#### 3. **`CustomUserDetailsService.java`** - Service de Chargement des Utilisateurs

**Objectif** : Implémente `UserDetailsService` de Spring Security pour charger les utilisateurs depuis la base de données.

**Méthodes Clés** :

*   **`loadUserByUsername(String email)`** : Charge un utilisateur par son email.
    *   Recherche l'utilisateur dans `UtilisateurRepository`
    *   Si trouvé : Convertit l'entité `Utilisateur` en `UserDetailsImpl`
    *   Si non trouvé : Lance `UsernameNotFoundException`

---

#### 4. **`UserDetailsImpl.java`** - Implémentation de UserDetails

**Objectif** : Adapte l'entité `Utilisateur` au format requis par Spring Security (`UserDetails`).

**Propriétés** :
*   `id`, `email`, `password`, `authorities` (rôles)

**Méthodes Clés** :

*   **`build(Utilisateur user)`** : Méthode statique qui convertit un `Utilisateur` en `UserDetailsImpl`.
    *   Mappe les rôles de l'utilisateur en `GrantedAuthority`
*   **`getAuthorities()`** : Retourne les rôles de l'utilisateur sous forme de `Collection<GrantedAuthority>`.
*   **`getUsername()`** : Retourne l'email (utilisé comme username).
*   **`isAccountNonExpired()`, `isAccountNonLocked()`, etc.** : Retournent `true` (pas de gestion de verrouillage de compte pour le moment).

---

#### 5. **`JwtAuthenticationEntryPoint.java`** - Gestion des Erreurs d'Authentification

**Objectif** : Gère les erreurs d'authentification (token invalide, manquant, expiré).

**Méthodes Clés** :

*   **`commence(HttpServletRequest, HttpServletResponse, AuthenticationException)`** : Appelée quand une requête non authentifiée tente d'accéder à une ressource protégée.
    *   Retourne une réponse HTTP 401 (Unauthorized)
    *   Envoie un message d'erreur JSON au client

---

## 🔐 Flux d'Authentification JWT

1.  **Connexion** : L'utilisateur envoie email + mot de passe à `/api/auth/login`
2.  **Validation** : `AuthController` utilise `AuthenticationManager` pour valider les identifiants
3.  **Génération du Token** : Si valide, `JwtUtils.generateJwtToken()` crée un token JWT
4.  **Réponse** : Le token est renvoyé au client
5.  **Requêtes Suivantes** : Le client inclut le token dans le header `Authorization: Bearer <token>`
6.  **Validation** : `JwtAuthenticationFilter` intercepte chaque requête, valide le token et authentifie l'utilisateur
7.  **Autorisation** : `SecurityConfig` vérifie si l'utilisateur a les rôles requis pour accéder à la ressource

