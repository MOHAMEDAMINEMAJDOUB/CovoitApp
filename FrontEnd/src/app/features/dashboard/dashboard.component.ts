import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  myTrips: any[] = [];
  myReservations: any[] = [];
  driverStats: any = { totalReservations: 0 };
  loading = false;
  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.isDriver() && this.currentUser) {
      this.loadMyTrips();
      this.loadDriverStats();
    } else if (this.isPassenger() && this.currentUser) {
      this.loadMyReservations();
    }
  }

  loadMyTrips(): void {
    if (!this.currentUser?.id) return;

    this.loading = true;
    const apiUrl = 'http://localhost:8081';

    this.http.get<any[]>(`${apiUrl}/trajets/conducteur/${this.currentUser.id}`).subscribe({
      next: (trips) => {
        this.myTrips = trips;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading trips:', err);
        this.errorMessage = 'Failed to load trips';
        this.loading = false;
      }
    });
  }

  loadDriverStats(): void {
    if (!this.currentUser?.id) return;

    const apiUrl = 'http://localhost:8081';
    this.http.get<any>(`${apiUrl}/conducteurs/${this.currentUser.id}/stats`).subscribe({
      next: (stats) => {
        this.driverStats = stats;
      },
      error: (err) => {
        console.error('Error loading driver stats:', err);
      }
    });
  }

  getTotalTrips(): number {
    return this.myTrips.length;
  }

  getActiveTrips(): number {
    return this.myTrips.filter(t => t.statut !== 'ANNULE' && new Date(t.dateHeure) > new Date()).length;
  }

  getDriverReservations(): number {
    return this.driverStats?.totalReservations || 0;
  }

  isDriver(): boolean {
    return this.currentUser?.userType === 'DRIVER';
  }

  isPassenger(): boolean {
    return this.currentUser?.userType === 'PASSENGER';
  }

  createNewTrip(): void {
    this.router.navigate(['/trips/create']);
  }

  manageVehicle(): void {
    this.router.navigate(['/my-vehicles']);
  }

  searchTrips(): void {
    this.router.navigate(['/trips']);
  }

  viewMyReservations(): void {
    this.router.navigate(['/my-reservations']);
  }

  loadMyReservations(): void {
    if (!this.currentUser?.id) return;

    this.loading = true;
    const apiUrl = 'http://localhost:8081';

    this.http.get<any[]>(`${apiUrl}/passagers/${this.currentUser.id}/reservations`).subscribe({
      next: (reservations) => {
        this.myReservations = reservations;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading reservations:', err);
        this.errorMessage = 'Failed to load reservations';
        this.loading = false;
      }
    });
  }

  getTotalReservations(): number {
    return this.myReservations.length;
  }

  getUpcomingTrips(): number {
    const now = new Date();
    return this.myReservations.filter(r =>
      r.etat !== 'CANCELLED' &&
      r.trajet?.dateHeure &&
      new Date(r.trajet.dateHeure) > now
    ).length;
  }

  getCompletedTrips(): number {
    const now = new Date();
    return this.myReservations.filter(r =>
      r.etat !== 'CANCELLED' &&
      r.trajet?.dateHeure &&
      new Date(r.trajet.dateHeure) <= now
    ).length;
  }
}
