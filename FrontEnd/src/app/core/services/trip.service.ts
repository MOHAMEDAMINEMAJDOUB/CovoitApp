import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Trip, CreateTripRequest } from '../models/trip.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class TripService {
    private apiUrl = `${environment.apiUrl}/trajets`;

    constructor(private http: HttpClient) { }

    createTrip(tripData: CreateTripRequest): Observable<Trip> {
        return this.http.post<Trip>(this.apiUrl, tripData);
    }

    getAllTrips(): Observable<Trip[]> {
        return this.http.get<Trip[]>(this.apiUrl);
    }

    getTripById(id: number): Observable<Trip> {
        return this.http.get<Trip>(`${this.apiUrl}/${id}`);
    }

    searchTrips(villeDepart?: string, villeArrivee?: string): Observable<Trip[]> {
        const params: any = {};
        if (villeDepart) params.villeDepart = villeDepart;
        if (villeArrivee) params.villeArrivee = villeArrivee;
        return this.http.get<Trip[]>(`${this.apiUrl}/rechercher`, { params });
    }

    getTripsByDriver(conducteurId: number): Observable<Trip[]> {
        return this.http.get<Trip[]>(`${this.apiUrl}/conducteur/${conducteurId}`);
    }

    updateTrip(id: number, tripData: any): Observable<Trip> {
        return this.http.put<Trip>(`${this.apiUrl}/${id}`, tripData);
    }

    deleteTrip(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
