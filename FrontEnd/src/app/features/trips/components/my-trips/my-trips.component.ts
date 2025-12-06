import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { TripService } from '../../../../core/services/trip.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Trip } from '../../../../core/models/trip.model';

@Component({
  selector: 'app-my-trips',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-trips.component.html',
  styleUrls: ['./my-trips.component.css']
})
export class MyTripsComponent implements OnInit {
  trips = signal<Trip[]>([]);
  loading = signal(false);

  constructor(
    private tripService: TripService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.loadMyTrips();
  }

  loadMyTrips() {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) return;

    this.loading.set(true);
    this.tripService.getTripsByDriver(currentUser.id).subscribe({
      next: (data) => {
        this.trips.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error loading my trips', err);
        this.loading.set(false);
      }
    });
  }

  getStatusClass(status?: string): string {
    return status || 'PENDING';
  }

  editTrip(trip: Trip) {
    this.router.navigate(['/trips/edit', trip.id]);
  }

  cancelTrip(trip: Trip) {
    if (confirm(`Are you sure you want to cancel the trip from ${trip.villeDepart} to ${trip.villeArrivee}? This action cannot be undone.`)) {
      this.tripService.deleteTrip(trip.id!).subscribe({
        next: () => {
          alert('Trip cancelled successfully');
          this.loadMyTrips();
        },
        error: (err) => {
          console.error('Error cancelling trip', err);
          alert('Failed to cancel trip. Please try again.');
        }
      });
    }
  }
  isTripPassed(dateHeure: string): boolean {
    return new Date(dateHeure) < new Date();
  }
}
