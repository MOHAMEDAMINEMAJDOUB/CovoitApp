export interface Trip {
    id?: number;
    villeDepart: string;
    villeArrivee: string;
    dateHeure: string; // ISO 8601 format
    placesDisponibles: number;
    prixParPlace: number;
    conducteur?: {
        id: number;
        nom: string;
        prenom: string;
        email: string;
    };
    voiture?: {
        id: number;
        marque?: string;
        modele?: string;
    };
    statut?: string;
}

export interface CreateTripRequest {
    villeDepart: string;
    villeArrivee: string;
    dateHeure: string;  // ISO 8601: "2025-11-26T10:00:00"
    placesDisponibles: number;  // 1-50
    prixParPlace: number;  // >= 0.0
    // Note: conducteurId and voitureId not needed - backend uses authenticated user
}
