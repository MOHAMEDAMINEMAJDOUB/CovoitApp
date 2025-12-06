# Guide : Ajouter une voiture avec Postman

## Prérequis
- L'application Spring Boot doit être démarrée (port 8080 par défaut)
- Postman installé

---

## Méthode 1 : Créer une voiture directement (sans conducteur)

### Endpoint
```
POST http://localhost:8080/voitures
```

### Configuration dans Postman :
1. **Méthode** : `POST`
2. **URL** : `http://localhost:8080/voitures`
3. **Headers** :
   - `Content-Type: application/json`
4. **Body** (raw, JSON) :
```json
{
  "marque": "Renault",
  "modele": "Clio",
  "places": 5
}
```

### Réponse attendue (201 Created) :
```json
{
  "id": 1,
  "marque": "Renault",
  "modele": "Clio",
  "places": 5
}
```

---

## Méthode 2 : Créer une voiture avec un conducteur (via l'ID du conducteur)

### Endpoint
```
POST http://localhost:8080/voitures/conducteur/{conducteurId}
```

### Configuration dans Postman :
1. **Méthode** : `POST`
2. **URL** : `http://localhost:8080/voitures/conducteur/1`
   - Remplacez `1` par l'ID du conducteur existant
3. **Headers** :
   - `Content-Type: application/json`
4. **Body** (raw, JSON) :
```json
{
  "marque": "Peugeot",
  "modele": "208",
  "places": 4
}
```

### Réponse attendue (201 Created) :
```json
{
  "id": 2,
  "marque": "Peugeot",
  "modele": "208",
  "places": 4
}
```

---

## Méthode 3 : Créer une voiture avec un conducteur (via l'objet conducteur)

### Endpoint
```
POST http://localhost:8080/voitures
```

### Configuration dans Postman :
1. **Méthode** : `POST`
2. **URL** : `http://localhost:8080/voitures`
3. **Headers** :
   - `Content-Type: application/json`
4. **Body** (raw, JSON) :
```json
{
  "marque": "Citroën",
  "modele": "C3",
  "places": 5,
  "conducteur": {
    "id": 1
  }
}
```

### Réponse attendue (201 Created) :
```json
{
  "id": 3,
  "marque": "Citroën",
  "modele": "C3",
  "places": 5
}
```

---

## Méthode 4 : Associer une voiture à un conducteur existant

### Endpoint
```
POST http://localhost:8080/conducteurs/{id}/voiture
```

### Configuration dans Postman :
1. **Méthode** : `POST`
2. **URL** : `http://localhost:8080/conducteurs/1/voiture`
   - Remplacez `1` par l'ID du conducteur
3. **Headers** :
   - `Content-Type: application/json`
4. **Body** (raw, JSON) :
```json
{
  "marque": "BMW",
  "modele": "Serie 3",
  "places": 5
}
```

### Réponse attendue (200 OK) :
```json
{
  "id": 4,
  "marque": "BMW",
  "modele": "Serie 3",
  "places": 5
}
```

---

## Exemples d'utilisation complète

### Étape 1 : Créer un conducteur
```
POST http://localhost:8080/conducteurs
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@email.com",
  "motDePasse": "password123",
  "telephone": "0123456789"
}
```

### Étape 2 : Créer une voiture pour ce conducteur
```
POST http://localhost:8080/voitures/conducteur/1
Content-Type: application/json

{
  "marque": "Renault",
  "modele": "Megane",
  "places": 5
}
```

---

## Vérifier que la voiture a été créée

### Endpoint
```
GET http://localhost:8080/voitures
```

### Ou récupérer une voiture spécifique
```
GET http://localhost:8080/voitures/{id}
```

---

## Erreurs courantes

### Erreur 400 - Bad Request
- **Cause** : Marque ou modèle manquant
- **Solution** : Vérifiez que `marque` et `modele` sont présents dans le JSON

### Erreur 400 - Conducteur non trouvé
- **Cause** : L'ID du conducteur n'existe pas
- **Solution** : Vérifiez que le conducteur existe d'abord avec `GET /conducteurs`

### Erreur 500 - Constraint violation
- **Cause** : Tentative d'associer une voiture à un conducteur qui en a déjà une
- **Solution** : Un conducteur ne peut avoir qu'une seule voiture (relation OneToOne)


