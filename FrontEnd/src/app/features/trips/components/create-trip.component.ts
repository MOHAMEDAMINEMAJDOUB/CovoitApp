import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { TripService } from '../../../core/services/trip.service';
import { VehicleService, Vehicle } from '../../../core/services/vehicle.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-create-trip',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './create-trip.component.html',
  styleUrls: ['./create-trip.component.css']
})
export class CreateTripComponent implements OnInit {
  tripForm!: FormGroup;
  loading = signal(false);
  loadingVehicles = signal(false);
  vehicles = signal<Vehicle[]>([]);
  currentUser = signal<any>(null);
  errorMessage = signal('');
  successMessage = signal('');
  minDateTime: string = '';
  isEditing = signal(false);
  tripId = signal<number | null>(null);

  constructor(
    private fb: FormBuilder,
    private tripService: TripService,
    private vehicleService: VehicleService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // Get current user for role check
    const user = this.authService.getCurrentUser();
    this.currentUser.set(user);

    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    this.minDateTime = now.toISOString().slice(0, 16);

    this.tripForm = this.fb.group({
      villeDepart: ['', Validators.required],
      villeArrivee: ['', Validators.required],
      dateHeure: ['', Validators.required],
      voitureId: ['', Validators.required],
      placesDisponibles: [1, [Validators.required, Validators.min(1)]],
      prixParPlace: [0, [Validators.required, Validators.min(0)]]
    });

    this.loadUserVehicles();

    // Check for edit mode
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditing.set(true);
      this.tripId.set(+id);
      this.loadTripDetails(+id);
    }
  }

  loadTripDetails(id: number): void {
    this.loading.set(true);
    this.tripService.getTripById(id).subscribe({
      next: (trip: any) => {
        this.tripForm.patchValue({
          villeDepart: trip.villeDepart,
          villeArrivee: trip.villeArrivee,
          dateHeure: trip.dateHeure,
          placesDisponibles: trip.placesDisponibles,
          prixParPlace: trip.prixParPlace,
          voitureId: trip.voiture?.id
        });
        this.loading.set(false);
      },
      error: (err: any) => {
        console.error('Error loading trip', err);
        this.errorMessage.set('Failed to load trip details');
        this.loading.set(false);
      }
    });
  }

  loadUserVehicles(): void {
    this.loadingVehicles.set(true);
    const currentUser = this.authService.getCurrentUser();

    if (!currentUser) {
      this.loadingVehicles.set(false);
      return;
    }

    this.vehicleService.getAllVehicles().subscribe({
      next: (vehicles: Vehicle[]) => {
        const myVehicles = vehicles.filter(v => v.conducteur?.id === currentUser.id);
        this.vehicles.set(myVehicles);
        this.loadingVehicles.set(false);
      },
      error: (error: any) => {
        console.error('Load vehicles error:', error);
        this.loadingVehicles.set(false);
      }
    });
  }

  get villeDepart() { return this.tripForm.get('villeDepart')!; }
  get villeArrivee() { return this.tripForm.get('villeArrivee')!; }
  get dateHeure() { return this.tripForm.get('dateHeure')!; }
  get voitureId() { return this.tripForm.get('voitureId')!; }
  get placesDisponibles() { return this.tripForm.get('placesDisponibles')!; }
  get prixParPlace() { return this.tripForm.get('prixParPlace')!; }

  onSubmit(): void {
    if (this.tripForm.invalid) {
      Object.keys(this.tripForm.controls).forEach(key => {
        this.tripForm.get(key)?.markAsTouched();
      });
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.errorMessage.set('User not authenticated');
      this.loading.set(false);
      return;
    }

    const formValue = this.tripForm.value;
    const tripData: any = {
      villeDepart: formValue.villeDepart,
      villeArrivee: formValue.villeArrivee,
      dateHeure: formValue.dateHeure.length === 16 ? formValue.dateHeure + ':00' : formValue.dateHeure,
      placesDisponibles: formValue.placesDisponibles,
      prixParPlace: formValue.prixParPlace,
      voitureId: parseInt(formValue.voitureId)
    };

    const request = this.isEditing()
      ? this.tripService.updateTrip(this.tripId()!, tripData)
      : this.tripService.createTrip(tripData);

    request.subscribe({
      next: (trip: any) => {
        this.loading.set(false);
        this.successMessage.set(this.isEditing() ? 'Trip updated successfully!' : 'Trip created successfully!');
        setTimeout(() => {
          this.router.navigate(['/trips/my-trips']);
        }, 2000);
      },
      error: (error: any) => {
        this.loading.set(false);
        const message = error.error?.message || error.error || (this.isEditing() ? 'Failed to update trip.' : 'Failed to create trip.');
        this.errorMessage.set(message);
        console.error('Trip operation error:', error);
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/dashboard']);
  }
}
