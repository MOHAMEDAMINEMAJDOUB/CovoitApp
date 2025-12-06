import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TripService } from '../../../../core/services/trip.service';
import { ReservationService } from '../../../../core/services/reservation.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import { Trip } from '../../../../core/models/trip.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-trips-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './trips-list.component.html',
  styleUrls: ['./trips-list.component.css']
})
export class TripsListComponent implements OnInit {
  trips = signal<Trip[]>([]);
  loading = signal(false);
  searchDepart = '';
  searchArrivee = '';
  myReservations = signal<any[]>([]);

  constructor(
    private tripService: TripService,
    private reservationService: ReservationService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.loadTrips();
    this.loadMyReservations();
  }

  loadTrips() {
    this.loading.set(true);
    this.tripService.getAllTrips().subscribe({
      next: (data) => {
        // Filter out cancelled and past trips
        const now = new Date();
        const activeTrips = data.filter(t => t.statut !== 'ANNULE' && new Date(t.dateHeure) > now);
        this.trips.set(activeTrips);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading trips', err);
        this.loading.set(false);
      }
    });
  }

  search() {
    if (!this.searchDepart && !this.searchArrivee) {
      this.loadTrips();
      return;
    }

    this.loading.set(true);
    // Allow search with either departure or arrival (or both)
    this.tripService.searchTrips(
      this.searchDepart || undefined,
      this.searchArrivee || undefined
    ).subscribe({
      next: (data) => {
        // Filter out cancelled and past trips
        const now = new Date();
        const activeTrips = data.filter(t => t.statut !== 'ANNULE' && new Date(t.dateHeure) > now);
        this.trips.set(activeTrips);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error searching trips', err);
        this.loading.set(false);
      }
    });
  }

  reserve(trip: Trip) {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      if (confirm('You need to be logged in to reserve a trip. Would you like to go to the login page?')) {
        this.router.navigate(['/auth/login'], { queryParams: { returnUrl: '/trips' } });
      }
      return;
    }

    if (confirm('Do you want to reserve a seat for ' + trip.villeDepart + ' to ' + trip.villeArrivee + '?')) {
      this.reservationService.bookTrip(currentUser.id, trip.id!).subscribe({
        next: () => {
          alert('Reservation successful!');
          this.loadTrips();
          this.loadMyReservations();
        },
        error: (err) => {
          console.error('Reservation failed', err);
          const errorMessage = err.error?.message || err.message || JSON.stringify(err.error) || 'Unknown error';
          alert('Reservation failed: ' + errorMessage);
        }
      });
    }
  }

  loadMyReservations() {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return;

    this.reservationService.getMyReservations(currentUser.id).subscribe({
      next: (data) => {
        this.myReservations.set(data);
      },
      error: (err) => {
        console.error('Error loading reservations', err);
      }
    });
  }

  isDriver(trip: Trip): boolean {
    const currentUser = this.authService.getCurrentUser();
    return currentUser?.id === trip.conducteur?.id;
  }

  isReserved(trip: Trip): boolean {
    return this.myReservations().some(res => res.trajet?.id === trip.id && res.etat !== 'CANCELLED');
  }

  getReservationId(trip: Trip): number | undefined {
    const reservation = this.myReservations().find(res => res.trajet?.id === trip.id && res.etat !== 'CANCELLED');
    return reservation?.id;
  }

  cancelReservation(trip: Trip) {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.router.navigate(['/auth/login']);
      return;
    }

    const reservationId = this.getReservationId(trip);
    if (!reservationId) {
      alert('Reservation not found');
      return;
    }

    if (confirm('Do you want to cancel your reservation for ' + trip.villeDepart + ' to ' + trip.villeArrivee + '?')) {
      this.reservationService.cancelReservation(currentUser.id, reservationId).subscribe({
        next: () => {
          alert('Reservation cancelled successfully!');
          this.loadTrips();
          this.loadMyReservations();
        },
        error: (err) => {
          console.error('Cancellation failed', err);
          const errorMessage = err.error?.message || err.message || JSON.stringify(err.error) || 'Unknown error';
          alert('Cancellation failed: ' + errorMessage);
        }
      });
    }
  }

  isCurrentUserDriver(): boolean {
    const currentUser = this.authService.getCurrentUser();
    return currentUser?.userType === 'DRIVER';
  }
}
