import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReservationService, Reservation } from '../../core/services/reservation.service';
import { AuthService } from '../../core/services/auth.service';

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
  cancelling = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  constructor(
    private reservationService: ReservationService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.errorMessage.set('User not authenticated');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.reservationService.getMyReservations(user.id).subscribe({
      next: (reservations) => {
        this.reservations.set(reservations);
        this.loading.set(false);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(error.error?.message || error.error || 'Failed to load reservations');
        console.error('Load reservations error:', error);
      }
    });
  }

  cancelReservation(reservation: Reservation): void {
    if (!reservation.id) return;

    if (!confirm('Are you sure you want to cancel this reservation?')) {
      return;
    }

    const user = this.authService.getCurrentUser();
    if (!user) return;

    this.cancelling.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.reservationService.cancelReservation(user.id, reservation.id).subscribe({
      next: () => {
        this.cancelling.set(false);
        this.successMessage.set('Reservation cancelled successfully');
        // Reload reservations to reflect changes
        setTimeout(() => {
          this.loadReservations();
          this.successMessage.set('');
        }, 2000);
      },
      error: (error) => {
        this.cancelling.set(false);
        this.errorMessage.set(error.error?.message || error.error || 'Failed to cancel reservation');
        console.error('Cancel reservation error:', error);
      }
    });
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
