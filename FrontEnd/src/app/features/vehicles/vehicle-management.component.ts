import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { VehicleService, Vehicle } from '../../core/services/vehicle.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-vehicle-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicle-management.component.html',
  styleUrls: ['./vehicle-management.component.css']
})
export class VehicleManagementComponent implements OnInit {
  vehicleForm!: FormGroup;
  vehicles = signal<Vehicle[]>([]);
  loading = signal(false);
  saving = signal(false);
  deleting = signal(false);
  errorMessage = signal('');
  successMessage = signal('');
  editingVehicle = signal<Vehicle | null>(null);

  constructor(
    private fb: FormBuilder,
    private vehicleService: VehicleService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.loadVehicles();
  }

  initForm(): void {
    this.vehicleForm = this.fb.group({
      marque: ['', Validators.required],
      modele: ['', Validators.required],
      immatriculation: ['', Validators.required],
      nombrePlaces: [4, [Validators.required, Validators.min(1), Validators.max(50)]],
      couleur: ['']
    });
  }

  get marque() { return this.vehicleForm.get('marque')!; }
  get modele() { return this.vehicleForm.get('modele')!; }
  get immatriculation() { return this.vehicleForm.get('immatriculation')!; }
  get nombrePlaces() { return this.vehicleForm.get('nombrePlaces')!; }

  loadVehicles(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.vehicleService.getAllVehicles().subscribe({
      next: (vehicles) => {
        const user = this.authService.getCurrentUser();
        // Filter to show only current user's vehicles
        const myVehicles = vehicles.filter(v => v.conducteur?.id === user?.id);
        this.vehicles.set(myVehicles);
        this.loading.set(false);
      },
      error: (error) => {
        this.loading.set(false);
        this.errorMessage.set(error.error?.message || error.error || 'Failed to load vehicles');
        console.error('Load vehicles error:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.vehicleForm.invalid) return;

    const user = this.authService.getCurrentUser();
    if (!user) {
      this.errorMessage.set('User not authenticated');
      return;
    }

    this.saving.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const vehicleData: Vehicle = this.vehicleForm.value;

    if (this.editingVehicle()?.id) {
      // Update existing vehicle
      this.vehicleService.updateVehicle(this.editingVehicle()!.id!, vehicleData).subscribe({
        next: () => {
          this.saving.set(false);
          this.successMessage.set('Vehicle updated successfully');
          this.vehicleForm.reset({ nombrePlaces: 4 });
          this.editingVehicle.set(null);
          this.loadVehicles();
        },
        error: (error) => {
          this.saving.set(false);
          this.errorMessage.set(error.error?.message || error.error || 'Failed to update vehicle');
          console.error('Update vehicle error:', error);
        }
      });
    } else {
      // Add new vehicle
      this.vehicleService.addVehicle(user.id, vehicleData).subscribe({
        next: () => {
          this.saving.set(false);
          this.successMessage.set('Vehicle added successfully');
          this.vehicleForm.reset({ nombrePlaces: 4 });
          this.loadVehicles();
        },
        error: (error) => {
          this.saving.set(false);
          let msg = 'Failed to add vehicle';
          if (error.error) {
            if (typeof error.error === 'string') msg = error.error;
            else if (error.error.message) msg = error.error.message;
            else msg = JSON.stringify(error.error);
          }
          this.errorMessage.set(msg);
          console.error('Add vehicle error:', error);
        }
      });
    }
  }

  editVehicle(vehicle: Vehicle): void {
    this.editingVehicle.set(vehicle);
    this.vehicleForm.patchValue({
      marque: vehicle.marque,
      modele: vehicle.modele,
      immatriculation: vehicle.immatriculation,
      nombrePlaces: vehicle.nombrePlaces,
      couleur: vehicle.couleur || ''
    });
    // Scroll to form
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelEdit(): void {
    this.editingVehicle.set(null);
    this.vehicleForm.reset({ nombrePlaces: 4 });
  }

  deleteVehicle(vehicle: Vehicle): void {
    if (!vehicle.id) return;

    if (!confirm(`Are you sure you want to delete ${vehicle.marque} ${vehicle.modele}?`)) {
      return;
    }

    this.deleting.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.vehicleService.deleteVehicle(vehicle.id).subscribe({
      next: () => {
        this.deleting.set(false);
        this.successMessage.set('Vehicle deleted successfully');
        this.loadVehicles();
      },
      error: (error) => {
        this.deleting.set(false);
        this.errorMessage.set(error.error?.message || error.error || 'Failed to delete vehicle. It may be linked to existing trips.');
        console.error('Delete vehicle error:', error);
      }
    });
  }
}
