import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Vehicle {
    id?: number;
    marque: string;
    modele: string;
    immatriculation: string;
    nombrePlaces: number;
    couleur?: string;
    conducteur?: {
        id: number;
        nom: string;
        prenom: string;
    };
}

@Injectable({
    providedIn: 'root'
})
export class VehicleService {
    private apiUrl = `${environment.apiUrl}`;

    constructor(private http: HttpClient) { }

    addVehicle(conducteurId: number, vehicle: Vehicle): Observable<Vehicle> {
        return this.http.post<Vehicle>(
            `${this.apiUrl}/conducteurs/${conducteurId}/voiture`,
            vehicle
        );
    }

    getAllVehicles(): Observable<Vehicle[]> {
        return this.http.get<Vehicle[]>(`${this.apiUrl}/voitures`);
    }

    getVehicleById(id: number): Observable<Vehicle> {
        return this.http.get<Vehicle>(`${this.apiUrl}/voitures/${id}`);
    }

    updateVehicle(id: number, vehicle: Vehicle): Observable<Vehicle> {
        return this.http.put<Vehicle>(
            `${this.apiUrl}/voitures/${id}`,
            vehicle
        );
    }

    deleteVehicle(id: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/voitures/${id}`);
    }
}
