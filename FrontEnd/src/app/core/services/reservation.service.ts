import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Reservation {
    id?: number;
    trajetId?: number;
    passagerId?: number;
    etat?: string;
    dateReservation?: string;
    trajet?: {
        id: number;
        villeDepart: string;
        villeArrivee: string;
        dateHeure: string;
        prixParPlace: number;
    };
}

@Injectable({
    providedIn: 'root'
})
export class ReservationService {
    private apiUrl = `${environment.apiUrl}/passagers`;

    constructor(private http: HttpClient) { }

    bookTrip(passagerId: number, trajetId: number): Observable<Reservation> {
        return this.http.post<Reservation>(
            `${this.apiUrl}/${passagerId}/reservations?trajetId=${trajetId}`,
            {}
        );
    }

    cancelReservation(passagerId: number, reservationId: number): Observable<any> {
        return this.http.delete(
            `${this.apiUrl}/${passagerId}/reservations/${reservationId}`
        );
    }

    getMyReservations(passagerId: number): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(
            `${this.apiUrl}/${passagerId}/reservations`
        );
    }
}
