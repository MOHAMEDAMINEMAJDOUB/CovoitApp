import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReservationService, Reservation } from '../../../../core/services/reservation.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-reservations',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-reservations.component.html',
  styleUrls: ['./my-reservations.component.css']
})
export class MyReservationsComponent implements OnInit {
  reservations = signal<Reservation[]>([]);
  loading = signal(false);

  constructor(
    private reservationService: ReservationService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.loadMyReservations();
  }

  loadMyReservations() {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.router.navigate(['/auth/login']);
      return;
    }

    this.loading.set(true);
    this.reservationService.getMyReservations(currentUser.id).subscribe({
      next: (data) => {
        // Filter out cancelled reservations
        const activeReservations = data.filter(r => r.etat !== 'CANCELLED');
        this.reservations.set(activeReservations);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading reservations', err);
        this.loading.set(false);
      }
    });
  }

  getStatusClass(status?: string): string {
    return status || 'CONFIRMED';
  }

  cancelReservation(reservation: Reservation) {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.router.navigate(['/auth/login']);
      return;
    }

    if (confirm(`Are you sure you want to cancel your reservation from ${reservation.trajet?.villeDepart} to ${reservation.trajet?.villeArrivee}?`)) {
      this.reservationService.cancelReservation(currentUser.id, reservation.id!).subscribe({
        next: () => {
          alert('Reservation cancelled successfully');
          this.loadMyReservations();
        },
        error: (err) => {
          console.error('Error cancelling reservation', err);
          alert('Failed to cancel reservation. Please try again.');
        }
      });
    }
  }
  isTripPassed(dateHeure: string): boolean {
    return new Date(dateHeure) < new Date();
  }
}
