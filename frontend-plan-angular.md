# Angular Frontend Plan - Covoiturage Application

## 1. Project Overview

### Backend Capabilities Summary

The backend is a comprehensive carpool management system built with Spring Boot that provides:

**Core Entities:**
- **Utilisateur**: Base user entity with authentication
- **Conducteur** (Driver): Extends Utilisateur, manages trips and vehicles
- **Passager** (Passenger): Extends Utilisateur, manages reservations
- **Trajet** (Trip): Journey information with departure/arrival cities, date/time, available seats, and price
- **Reservation**: Links passengers to trips with confirmation status
- **Voiture** (Vehicle): Car details linked to drivers

**Authentication & Authorization:**
- JWT-based authentication with access tokens and refresh tokens
- Role-based access control (ROLE_USER, ROLE_ADMIN, ROLE_DRIVER, ROLE_PASSENGER)
- Secure endpoints for login, signup, logout, and token refresh
- User type differentiation (DRIVER vs PASSENGER)

**Key Business Capabilities:**
- **Drivers can:**
  - Create, update, and delete their profile
  - Add and manage vehicles
  - Create and manage trips
  - View reservations for their trips
  
- **Passengers can:**
  - Create, update, and delete their profile
  - Search for available trips
  - Make and cancel reservations
  - View their reservation history
  
- **Admin can:**
  - Manage all users, trips, and reservations
  - Full CRUD operations on all entities

### Frontend Goals

The Angular frontend application will provide a modern, responsive, and user-friendly interface to:

1. **Enable seamless authentication** with JWT token management using HTTP interceptors
2. **Provide differentiated experiences** for Drivers and Passengers using Angular guards
3. **Facilitate trip discovery** with powerful search and filtering using reactive forms
4. **Streamline booking process** with clear reservation management
5. **Offer real-time updates** using RxJS observables and reactive programming
6. **Ensure responsive design** for mobile and desktop users with Bootstrap 5
7. **Maintain security** through Angular route guards and role-based UI rendering

---

## 2. Angular Technology Stack

### Existing Configuration

**Current Setup (from package.json):**
- **Angular Version**: 20.3.0 (Latest)
- **UI Framework**: Bootstrap 5.3.8
- **State Management**: RxJS 7.8.0
- **Build Tool**: Angular CLI 20.3.10
- **TypeScript**: 5.9.2

### Recommended Additional Libraries

```bash
npm install --save @angular/material @angular/cdk @angular/animations
npm install --save @ng-bootstrap/ng-bootstrap
npm install --save ngx-toastr
npm install --save date-fns
npm install --save @fortawesome/fontawesome-free
```

**Justifications:**

#### **Angular Material** (Optional - Can use Bootstrap instead)
- Rich component library (dialogs, snackbars, date pickers)
- Accessibility built-in
- Consistent Material Design
- **Note**: You can use Bootstrap exclusively if preferred

#### **@ng-bootstrap/ng-bootstrap**
- Native Angular components for Bootstrap
- No jQuery dependency
- Type-safe component API
- Modal, dropdown, tooltip components

#### **ngx-toastr**
- Toast notifications for success/error messages
- Highly customizable
- Works well with Bootstrap

#### **date-fns**
- Lightweight date manipulation (alternative to moment.js)
- Tree-shakeable
- Immutable & pure functions

#### **Font Awesome**
- Icon library for UI elements
- Alternative to Material Icons

---

## 3. Angular Folder Structure

### Complete Project Structure

```
FrontEnd/
├── src/
│   ├── app/
│   │   ├── core/                           # Core module (singleton services, guards, interceptors)
│   │   │   ├── guards/
│   │   │   │   ├── auth.guard.ts          # Authentication guard
│   │   │   │   └── role.guard.ts          # Role-based guard
│   │   │   ├── interceptors/
│   │   │   │   ├── auth.interceptor.ts    # JWT token injection
│   │   │   │   ├── error.interceptor.ts   # Global error handling
│   │   │   │   └── loading.interceptor.ts # Loading state management
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts        # Authentication service
│   │   │   │   ├── token.service.ts       # Token management
│   │   │   │   ├── toast.service.ts       # Toast notifications
│   │   │   │   └── loading.service.ts     # Loading state service
│   │   │   ├── models/                    # Core interfaces/models
│   │   │   │   ├── user.model.ts
│   │   │   │   ├── jwt-response.model.ts
│   │   │   │   └── api-response.model.ts
│   │   │   └── core.module.ts             # Core module file
│   │   │
│   │   ├── shared/                        # Shared module (components, directives, pipes)
│   │   │   ├── components/
│   │   │   │   ├── header/
│   │   │   │   │   ├── header.component.ts
│   │   │   │   │   ├── header.component.html
│   │   │   │   │   └── header.component.css
│   │   │   │   ├── footer/
│   │   │   │   ├── navbar/
│   │   │   │   ├── sidebar/
│   │   │   │   ├── loading-spinner/
│   │   │   │   ├── confirm-dialog/
│   │   │   │   └── error-display/
│   │   │   ├── directives/
│   │   │   │   └── role-visibility.directive.ts  # Show/hide based on role
│   │   │   ├── pipes/
│   │   │   │   ├── date-format.pipe.ts
│   │   │   │   └── currency-format.pipe.ts
│   │   │   └── shared.module.ts           # Shared module file
│   │   │
│   │   ├── features/                      # Feature modules
│   │   │   ├── auth/                      # Authentication feature
│   │   │   │   ├── components/
│   │   │   │   │   ├── login/
│   │   │   │   │   │   ├── login.component.ts
│   │   │   │   │   │   ├── login.component.html
│   │   │   │   │   │   └── login.component.css
│   │   │   │   │   ├── signup/
│   │   │   │   │   ├── forgot-password/
│   │   │   │   │   └── user-type-selector/
│   │   │   │   ├── auth-routing.module.ts
│   │   │   │   └── auth.module.ts
│   │   │   │
│   │   │   ├── dashboard/                 # Dashboard feature
│   │   │   │   ├── components/
│   │   │   │   │   ├── dashboard-home/
│   │   │   │   │   ├── driver-dashboard/
│   │   │   │   │   ├── passenger-dashboard/
│   │   │   │   │   ├── stats-card/        # Reusable stats card
│   │   │   │   │   └── quick-actions/
│   │   │   │   ├── dashboard-routing.module.ts
│   │   │   │   └── dashboard.module.ts
│   │   │   │
│   │   │   ├── trips/                     # Trips feature
│   │   │   │   ├── components/
│   │   │   │   │   ├── trip-list/
│   │   │   │   │   ├── trip-card/
│   │   │   │   │   ├── trip-details/
│   │   │   │   │   ├── trip-form/
│   │   │   │   │   ├── trip-search/
│   │   │   │   │   └── trip-filter/
│   │   │   │   ├── services/
│   │   │   │   │   └── trip.service.ts
│   │   │   │   ├── models/
│   │   │   │   │   └── trip.model.ts
│   │   │   │   ├── trips-routing.module.ts
│   │   │   │   └── trips.module.ts
│   │   │   │
│   │   │   ├── reservations/              # Reservations feature
│   │   │   │   ├── components/
│   │   │   │   │   ├── reservation-list/
│   │   │   │   │   ├── reservation-card/
│   │   │   │   │   ├── reservation-details/
│   │   │   │   │   └── reservation-status/
│   │   │   │   ├── services/
│   │   │   │   │   └── reservation.service.ts
│   │   │   │   ├── models/
│   │   │   │   │   └── reservation.model.ts
│   │   │   │   ├── reservations-routing.module.ts
│   │   │   │   └── reservations.module.ts
│   │   │   │
│   │   │   ├── vehicles/                  # Vehicles feature
│   │   │   │   ├── components/
│   │   │   │   │   ├── vehicle-list/
│   │   │   │   │   ├── vehicle-card/
│   │   │   │   │   └── vehicle-form/
│   │   │   │   ├── services/
│   │   │   │   │   └── vehicle.service.ts
│   │   │   │   ├── models/
│   │   │   │   │   └── vehicle.model.ts
│   │   │   │   ├── vehicles-routing.module.ts
│   │   │   │   └── vehicles.module.ts
│   │   │   │
│   │   │   ├── profile/                   # Profile feature
│   │   │   │   ├── components/
│   │   │   │   │   ├── profile-view/
│   │   │   │   │   ├── profile-edit/
│   │   │   │   │   └── change-password/
│   │   │   │   ├── services/
│   │   │   │   │   └── profile.service.ts
│   │   │   │   ├── profile-routing.module.ts
│   │   │   │   └── profile.module.ts
│   │   │   │
│   │   │   └── home/                      # Landing page feature
│   │   │       ├── components/
│   │   │       │   ├── hero-section/
│   │   │       │   ├── featured-trips/
│   │   │       │   └── how-it-works/
│   │   │       ├── home-routing.module.ts
│   │   │       └── home.module.ts
│   │   │
│   │   ├── app.ts                         # Root component
│   │   ├── app.html                       # Root template
│   │   ├── app.css                        # Root styles
│   │   ├── app.config.ts                  # App configuration
│   │   └── app.routes.ts                  # Root routing
│   │
│   ├── assets/                            # Static assets
│   │   ├── images/
│   │   ├── icons/
│   │   └── i18n/                          # Translation files (future)
│   │
│   ├── environments/                      # Environment configurations
│   │   ├── environment.ts                 # Development
│   │   └── environment.prod.ts            # Production
│   │
│   ├── styles/                            # Global styles
│   │   ├── _variables.scss                # SCSS variables
│   │   ├── _mixins.scss                   # SCSS mixins
│   │   └── _utilities.scss                # Utility classes
│   │
│   ├── styles.css                         # Global stylesheet
│   ├── index.html                         # HTML entry point
│   └── main.ts                            # TypeScript entry point
│
├── angular.json                           # Angular CLI configuration
├── package.json                           # Dependencies
├── tsconfig.json                          # TypeScript configuration
└── README.md                              # Project documentation
```

---

## 4. Angular Module Architecture

### Module Organization Strategy

Angular uses a modular architecture. Here's the breakdown:

#### **Core Module** (Singleton)
- **Purpose**: Services and components that should be instantiated once
- **Contents**: Guards, interceptors, auth service, token service
- **Import**: Only in `AppModule` or `main.ts` providers

#### **Shared Module** (Reusable)
- **Purpose**: Components, directives, pipes used across multiple features
- **Contents**: UI components (header, footer), directives, pipes
- **Import**: In any feature module that needs them
- **Export**: All components, directives, pipes

#### **Feature Modules** (Lazy Loaded)
- **Purpose**: Self-contained features with their own routing
- **Contents**: Feature-specific components, services, models
- **Lazy Loading**: Loaded on-demand when route is accessed
- **Examples**: Auth, Dashboard, Trips, Reservations, Vehicles, Profile

---

## 5. Component-by-Component Blueprint

### 5.1 Authentication Module (`features/auth`)

#### **LoginComponent**

**Purpose:** User authentication with email and password

**Template Structure:**
```html
<div class="container">
  <div class="card login-card">
    <h2>Login to Covoiturage</h2>
    <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
      <div class="mb-3">
        <label for="email">Email</label>
        <input type="email" id="email" formControlName="email" class="form-control">
        <div *ngIf="email.invalid && email.touched" class="text-danger">
          Email is required and must be valid
        </div>
      </div>
      <div class="mb-3">
        <label for="password">Password</label>
        <input [type]="showPassword ? 'text' : 'password'" id="password" formControlName="password">
        <button type="button" (click)="togglePassword()">Show/Hide</button>
      </div>
      <button type="submit" [disabled]="loginForm.invalid || loading">
        {{ loading ? 'Logging in...' : 'Login' }}
      </button>
    </form>
    <div class="mt-3">
      <a routerLink="/auth/signup">Don't have an account? Sign up</a>
    </div>
  </div>
</div>
```

**Component Logic:**
```typescript
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  showPassword = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          this.toastService.success('Login successful!');
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.toastService.error(error.message || 'Login failed');
          this.loading = false;
        }
      });
    }
  }
}
```

**API Calls:**
- `POST /api/auth/login` via `AuthService.login()`

---

#### **SignupComponent**

**Purpose:** User registration with user type selection

**Form Structure:**
- User type selector (radio buttons or dropdown)
- Personal information (nom, prenom, email, telephone)
- Password with confirmation
- Terms acceptance checkbox

**Reactive Form:**
```typescript
this.signupForm = this.fb.group({
  userType: ['PASSENGER', Validators.required],
  nom: ['', Validators.required],
  prenom: ['', Validators.required],
  email: ['', [Validators.required, Validators.email]],
  telephone: ['', [Validators.required, Validators.pattern(/^\\d{10}$/)]],
  password: ['', [Validators.required, Validators.minLength(6)]],
  confirmPassword: ['', Validators.required],
  termsAccepted: [false, Validators.requiredTrue]
}, { validators: this.passwordMatchValidator });
```

**Custom Validator:**
```typescript
passwordMatchValidator(group: FormGroup): ValidationErrors | null {
  const password = group.get('password')?.value;
  const confirmPassword = group.get('confirmPassword')?.value;
  return password === confirmPassword ? null : { passwordMismatch: true };
}
```

**API Calls:**
- `POST /api/auth/signup` via `AuthService.signup()`

---

### 5.2 Dashboard Module (`features/dashboard`)

#### **DashboardHomeComponent**

**Purpose:** Main dashboard that conditionally renders driver or passenger dashboard

**Template:**
```html
<div class="container dashboard">
  <h1>Welcome, {{ currentUser?.prenom }}</h1>
  
  <app-driver-dashboard *ngIf="isDriver()"></app-driver-dashboard>
  <app-passenger-dashboard *ngIf="isPassenger()"></app-passenger-dashboard>
</div>
```

**Component Logic:**
```typescript
export class DashboardHomeComponent implements OnInit {
  currentUser: User | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
  }

  isDriver(): boolean {
    return this.currentUser?.userType === 'DRIVER';
  }

  isPassenger(): boolean {
    return this.currentUser?.userType === 'PASSENGER';
  }
}
```

---

#### **DriverDashboardComponent**

**Purpose:** Driver-specific dashboard with stats and quick actions

**Data Display:**
- Stats cards (total trips, active trips, total reservations, earnings)
- Upcoming trips list
- Recent reservations
- Quick action buttons (Create Trip, Manage Vehicle)

**Template Example:**
```html
<div class="row">
  <div class="col-md-3" *ngFor="let stat of stats">
    <app-stats-card [title]="stat.title" [value]="stat.value" [icon]="stat.icon"></app-stats-card>
  </div>
</div>

<div class="row mt-4">
  <div class="col-md-8">
    <h3>Upcoming Trips</h3>
    <app-trip-card *ngFor="let trip of upcomingTrips" [trip]="trip"></app-trip-card>
  </div>
  <div class="col-md-4">
    <h3>Quick Actions</h3>
    <button class="btn btn-primary" routerLink="/trips/create">Create New Trip</button>
    <button class="btn btn-secondary" routerLink="/vehicles">Manage Vehicle</button>
  </div>
</div>
```

**API Calls:**
- `GET /conducteurs/{id}` - Fetch driver data
- `GET /trajets/conducteur/{conducteurId}` - Fetch driver's trips

---

#### **PassengerDashboardComponent**

**Purpose:** Passenger-specific dashboard with reservations

**Data Display:**
- Stats cards (total reservations, active bookings, completed trips)
- Upcoming reservations with trip details
- Quick search form

**API Calls:**
- `GET /passagers/{id}` - Fetch passenger data
- `GET /passagers/{id}/reservations` - Fetch reservations

---

### 5.3 Trips Module (`features/trips`)

#### **TripSearchComponent**

**Purpose:** Search for available trips

**Search Form:**
```typescript
this.searchForm = this.fb.group({
  villeDepart: [''],
  villeArrivee: [''],
  dateHeure: [''],
  seatsNeeded: [1, [Validators.min(1)]]
});
```

**Template:**
```html
<form [formGroup]="searchForm" (ngSubmit)="search()">
  <div class="row">
    <div class="col-md-3">
      <input type="text" formControlName="villeDepart" placeholder="Departure City" class="form-control">
    </div>
    <div class="col-md-3">
      <input type="text" formControlName="villeArrivee" placeholder="Arrival City" class="form-control">
    </div>
    <div class="col-md-3">
      <input type="date" formControlName="dateHeure" class="form-control">
    </div>
    <div class="col-md-2">
      <input type="number" formControlName="seatsNeeded" min="1" class="form-control">
    </div>
    <div class="col-md-1">
      <button type="submit" class="btn btn-primary">Search</button>
    </div>
  </div>
</form>

<div class="results mt-4">
  <app-trip-card *ngFor="let trip of trips$ | async" [trip]="trip" (book)="onBook(trip)"></app-trip-card>
</div>
```

**Component Logic:**
```typescript
export class TripSearchComponent implements OnInit {
  searchForm!: FormGroup;
  trips$!: Observable<Trip[]>;

  constructor(
    private fb: FormBuilder,
    private tripService: TripService
  ) {}

  search(): void {
    const { villeDepart, villeArrivee } = this.searchForm.value;
    this.trips$ = this.tripService.searchTrips(villeDepart, villeArrivee);
  }
}
```

**API Calls:**
- `GET /trajets/rechercher?villeDepart={ville}&villeArrivee={ville}`

---

#### **TripCardComponent** (Reusable)

**Purpose:** Display trip information in a card format

**Input Properties:**
```typescript
@Input() trip!: Trip;
@Input() showBookButton = true;
@Output() book = new EventEmitter<Trip>();
```

**Template:**
```html
<div class="card trip-card">
  <div class="card-body">
    <h5 class="card-title">{{ trip.villeDepart }} → {{ trip.villeArrivee }}</h5>
    <p class="card-text">
      <i class="fa fa-calendar"></i> {{ trip.dateHeure | date:'short' }}<br>
      <i class="fa fa-users"></i> {{ trip.placesDisponibles }} seats available<br>
      <i class="fa fa-euro"></i> {{ trip.prixParPlace | currency:'EUR' }} per seat
    </p>
    <button *ngIf="showBookButton" class="btn btn-primary" (click)="book.emit(trip)">
      Book Now
    </button>
  </div>
</div>
```

---

#### **TripFormComponent**

**Purpose:** Create or edit a trip (Driver only)

**Form:**
```typescript
this.tripForm = this.fb.group({
  villeDepart: ['', Validators.required],
  villeArrivee: ['', Validators.required],
  dateHeure: ['', Validators.required],
  placesDisponibles: [1, [Validators.required, Validators.min(1), Validators.max(8)]],
  prixParPlace: [0, [Validators.required, Validators.min(0)]]
});
```

**Submit Logic:**
```typescript
onSubmit(): void {
  if (this.tripForm.valid) {
    const driverId = this.authService.getCurrentUser()?.id;
    this.tripService.createTrip(driverId!, this.tripForm.value).subscribe({
      next: () => {
        this.toastService.success('Trip created successfully!');
        this.router.navigate(['/dashboard']);
      },
      error: (error) => this.toastService.error(error.message)
    });
  }
}
```

**API Calls:**
- `POST /conducteurs/{id}/trajets` - Create trip

---

#### **TripDetailsComponent**

**Purpose:** Detailed view of a trip with booking functionality

**Template:**
```html
<div class="container" *ngIf="trip$ | async as trip">
  <div class="row">
    <div class="col-md-8">
      <div class="card">
        <div class="card-body">
          <h2>{{ trip.villeDepart }} → {{ trip.villeArrivee }}</h2>
          <p><strong>Date:</strong> {{ trip.dateHeure | date:'full' }}</p>
          <p><strong>Available Seats:</strong> {{ trip.placesDisponibles }}</p>
          <p><strong>Price per Seat:</strong> {{ trip.prixParPlace | currency:'EUR' }}</p>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card" *ngIf="isPassenger()">
        <div class="card-body">
          <h4>Book this trip</h4>
          <button class="btn btn-success btn-block" (click)="bookTrip()">
            Reserve Seat
          </button>
        </div>
      </div>
    </div>
  </div>
</div>
```

**API Calls:**
- `GET /trajets/{id}` - Fetch trip details
- `POST /passagers/{passagerId}/reservations?trajetId={trajetId}` - Make reservation

---

### 5.4 Reservations Module (`features/reservations`)

#### **ReservationListComponent**

**Purpose:** Display all reservations for a passenger

**Template:**
```html
<div class="container">
  <h2>My Reservations</h2>
  
  <ul class="nav nav-tabs mb-3">
    <li class="nav-item">
      <a class="nav-link" [class.active]="filter === 'upcoming'" (click)="filter = 'upcoming'">Upcoming</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" [class.active]="filter === 'past'" (click)="filter = 'past'">Past</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" [class.active]="filter === 'cancelled'" (click)="filter = 'cancelled'">Cancelled</a>
    </li>
  </ul>

  <app-reservation-card 
    *ngFor="let reservation of filteredReservations$ | async" 
    [reservation]="reservation"
    (cancel)="cancelReservation($event)">
  </app-reservation-card>
</div>
```

**Component Logic:**
```typescript
export class ReservationListComponent implements OnInit {
  reservations$ !: Observable<Reservation[]>;
  filteredReservations$!: Observable<Reservation[]>;
  filter = 'upcoming';

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    const passagerId = this.authService.getCurrentUser()?.id;
    this.reservations$ = this.reservationService.getReservationsByPassenger(passagerId!);
    
    this.filteredReservations$ = this.reservations$.pipe(
      map(reservations => this.filterReservations(reservations))
    );
  }

  filterReservations(reservations: Reservation[]): Reservation[] {
    // Filter logic based on this.filter
  }

  cancelReservation(reservationId: number): void {
    // Confirm dialog then cancel
  }
}
```

**API Calls:**
- `GET /passagers/{id}/reservations` - Fetch reservations
- `DELETE /passagers/{id}/reservations/{reservationId}` - Cancel reservation

---

### 5.5 Vehicles Module (`features/vehicles`)

#### **VehicleListComponent**

**Purpose:** Display and manage driver's vehicle

**Template:**
```html
<div class="container">
  <h2>My Vehicle</h2>
  
  <div *ngIf="vehicle$ | async as vehicle; else noVehicle">
    <app-vehicle-card [vehicle]="vehicle" (edit)="editVehicle()" (delete)="deleteVehicle()"></app-vehicle-card>
  </div>

  <ng-template #noVehicle>
    <div class="alert alert-info">
      <p>You haven't added a vehicle yet.</p>
      <button class="btn btn-primary" routerLink="/vehicles/add">Add Vehicle</button>
    </div>
  </ng-template>
</div>
```

---

#### **VehicleFormComponent**

**Purpose:** Add or edit vehicle

**Form:**
```typescript
this.vehicleForm = this.fb.group({
  marque: ['', Validators.required],
  modele: ['', Validators.required],
  places: [4, [Validators.required, Validators.min(1), Validators.max(8)]]
});
```

**API Calls:**
- `POST /conducteurs/{id}/voiture` - Add vehicle
- `PUT /voitures/{id}` - Update vehicle

---

### 5.6 Profile Module (`features/profile`)

#### **ProfileViewComponent**

**Purpose:** Display and edit user profile

**Template:**
```html
<div class="container">
  <div class="card">
    <div class="card-body">
      <h2>Profile</h2>
      <div *ngIf="!editMode">
        <p><strong>Name:</strong> {{ user?.nom }} {{ user?.prenom }}</p>
        <p><strong>Email:</strong> {{ user?.email }}</p>
        <p><strong>Phone:</strong> {{ user?.telephone }}</p>
        <p><strong>User Type:</strong> <span class="badge">{{ user?.userType }}</span></p>
        <button class="btn btn-primary" (click)="editMode = true">Edit Profile</button>
      </div>
      <div *ngIf="editMode">
        <form [formGroup]="profileForm" (ngSubmit)="saveProfile()">
          <!-- Editable fields -->
          <button type="submit">Save</button>
          <button type="button" (click)="editMode = false">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</div>
```

**API Calls:**
- `GET /conducteurs/{id}` or `GET /passagers/{id}` - Fetch profile
- `PUT /conducteurs/{id}` or `PUT /passagers/{id}` - Update profile

---

### 5.7 Shared Components

#### **HeaderComponent**

**Purpose:** Navigation header with user menu

**Features:**
- Logo and app name
- Navigation links (conditional based on auth state)
- User dropdown menu (Profile, Logout)
- Login/Signup buttons (when not authenticated)

---

#### **LoadingSpinnerComponent**

**Purpose:** Display loading state

**Usage:**
```html
<app-loading-spinner *ngIf="loading"></app-loading-spinner>
```

---

## 6. Services & API Integration

### 6.1 Core Services

#### **AuthService** (`core/services/auth.service.ts`)

**Purpose:** Handle authentication logic

**Methods:**
```typescript
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {
    // Load user from token on init
    const user = this.tokenService.getUser();
    if (user) {
      this.currentUserSubject.next(user);
    }
  }

  login(credentials: LoginRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>('/api/auth/login', credentials).pipe(
      tap(response => {
        this.tokenService.saveTokens(response.accessToken, response.refreshToken);
        this.tokenService.saveUser(response);
        this.currentUserSubject.next(this.mapToUser(response));
      })
    );
  }

  signup(signupData: SignupRequest): Observable<MessageResponse> {
    return this.http.post<MessageResponse>('/api/auth/signup', signupData);
  }

  logout(): void {
    const refreshToken = this.tokenService.getRefreshToken();
    this.http.post('/api/auth/logout', { refreshToken }).subscribe();
    this.tokenService.clearTokens();
    this.currentUserSubject.next(null);
  }

  refreshToken(): Observable<JwtResponse> {
    const refreshToken = this.tokenService.getRefreshToken();
    return this.http.post<JwtResponse>('/api/auth/refresh', { refreshToken }).pipe(
      tap(response => {
        this.tokenService.saveTokens(response.accessToken, response.refreshToken);
      })
    );
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.tokenService.getAccessToken();
  }

  isDriver(): boolean {
    return this.currentUserSubject.value?.userType === 'DRIVER';
  }

  isPassenger(): boolean {
    return this.currentUserSubject.value?.userType === 'PASSENGER';
  }
}
```

---

#### **TokenService** (`core/services/token.service.ts`)

**Purpose:** Manage JWT tokens in localStorage

**Methods:**
```typescript
export class TokenService {
  private readonly ACCESS_TOKEN_KEY = 'access_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly USER_KEY = 'user';

  saveTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem(this.ACCESS_TOKEN_KEY, accessToken);
    localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
  }

  getAccessToken(): string | null {
    return localStorage.getItem(this.ACCESS_TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  saveUser(jwtResponse: JwtResponse): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(jwtResponse));
  }

  getUser(): User | null {
    const userData = localStorage.getItem(this.USER_KEY);
    return userData ? JSON.parse(userData) : null;
  }

  clearTokens(): void {
    localStorage.removeItem(this.ACCESS_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }
}
```

---

### 6.2 Feature Services

#### **TripService** (`features/trips/services/trip.service.ts`)

**Methods:**
```typescript
export class TripService {
  private baseUrl = '/trajets';

  constructor(private http: HttpClient) {}

  getAllTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(this.baseUrl);
  }

  getTripById(id: number): Observable<Trip> {
    return this.http.get<Trip>(`${this.baseUrl}/${id}`);
  }

  searchTrips(villeDepart?: string, villeArrivee?: string): Observable<Trip[]> {
    let params = new HttpParams();
    if (villeDepart) params = params.set('villeDepart', villeDepart);
    if (villeArrivee) params = params.set('villeArrivee', villeArrivee);
    return this.http.get<Trip[]>(`${this.baseUrl}/rechercher`, { params });
  }

  createTrip(conducteurId: number, trip: Trip): Observable<any> {
    return this.http.post(`/conducteurs/${conducteurId}/trajets`, trip);
  }

  updateTrip(id: number, trip: Trip): Observable<Trip> {
    return this.http.put<Trip>(`${this.baseUrl}/${id}`, trip);
  }

  deleteTrip(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getTripsByConducteur(conducteurId: number): Observable<Trip[]> {
    return this.http.get<Trip[]>(`${this.baseUrl}/conducteur/${conducteurId}`);
  }
}
```

---

#### **ReservationService** (`features/reservations/services/reservation.service.ts`)

**Methods:**
```typescript
export class ReservationService {
  constructor(private http: HttpClient) {}

  makeReservation(passagerId: number, trajetId: number): Observable<Reservation> {
    return this.http.post<Reservation>(
      `/passagers/${passagerId}/reservations`,
      null,
      { params: { trajetId: trajetId.toString() } }
    );
  }

  getReservationsByPassenger(passagerId: number): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`/passagers/${passagerId}/reservations`);
  }

  cancelReservation(passagerId: number, reservationId: number): Observable<any> {
    return this.http.delete(`/passagers/${passagerId}/reservations/${reservationId}`);
  }

  getReservationById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`/reservations/${id}`);
  }
}
```

---

#### **VehicleService** (`features/vehicles/services/vehicle.service.ts`)

**Methods:**
```typescript
export class VehicleService {
  constructor(private http: HttpClient) {}

  addVehicle(conducteurId: number, vehicle: Vehicle): Observable<any> {
    return this.http.post(`/conducteurs/${conducteurId}/voiture`, vehicle);
  }

  updateVehicle(id: number, vehicle: Vehicle): Observable<Vehicle> {
    return this.http.put<Vehicle>(`/voitures/${id}`, vehicle);
  }

  deleteVehicle(id: number): Observable<any> {
    return this.http.delete(`/voitures/${id}`);
  }

  getVehicleById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`/voitures/${id}`);
  }
}
```

---

## 7. Guards & Interceptors

### 7.1 AuthGuard

**Purpose:** Protect routes that require authentication

```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  router.navigate(['/auth/login'], { queryParams: { returnUrl: state.url } });
  return false;
};
```

**Usage in routes:**
```typescript
{
  path: 'dashboard',
  canActivate: [authGuard],
  loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
}
```

---

### 7.2 RoleGuard

**Purpose:** Restrict routes based on user role

```typescript
export function roleGuard(allowedRoles: string[]): CanActivateFn {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const user = authService.getCurrentUser();
    
    if (!user) {
      router.navigate(['/auth/login']);
      return false;
    }

    const hasRole = user.roles.some(role => allowedRoles.includes(role));
    
    if (hasRole) {
      return true;
    }

    router.navigate(['/unauthorized']);
    return false;
  };
}
```

**Usage:**
```typescript
{
  path: 'trips/create',
  canActivate: [authGuard, roleGuard(['ROLE_DRIVER'])],
  component: TripFormComponent
}
```

---

### 7.3 AuthInterceptor

**Purpose:** Inject JWT token in all HTTP requests

```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.getAccessToken();

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req);
};
```

**Register in app.config.ts:**
```typescript
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([authInterceptor, errorInterceptor])
    )
  ]
};
```

---

### 7.4 ErrorInterceptor

**Purpose:** Handle API errors globally

```typescript
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const toastService = inject(ToastService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Attempt token refresh
        return authService.refreshToken().pipe(
          switchMap(() => {
            // Retry original request
            const token = inject(TokenService).getAccessToken();
            const clonedReq = req.clone({
              setHeaders: { Authorization: `Bearer ${token}` }
            });
            return next(clonedReq);
          }),
          catchError(() => {
            // Refresh failed, logout
            authService.logout();
            router.navigate(['/auth/login']);
            return throwError(() => error);
          })
        );
      }

      // Handle other errors
      let errorMessage = 'An error occurred';
      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      toastService.error(errorMessage);
      return throwError(() => error);
    })
  );
};
```

---

## 8. Routing Configuration

### Root Routes (`app.routes.ts`)

```typescript
export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadChildren: () => import('./features/home/home.module').then(m => m.HomeModule)
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: 'trips',
    loadChildren: () => import('./features/trips/trips.module').then(m => m.TripsModule)
  },
  {
    path: 'reservations',
    canActivate: [authGuard, roleGuard(['ROLE_PASSENGER'])],
    loadChildren: () => import('./features/reservations/reservations.module').then(m => m.ReservationsModule)
  },
  {
    path: 'vehicles',
    canActivate: [authGuard, roleGuard(['ROLE_DRIVER'])],
    loadChildren: () => import('./features/vehicles/vehicles.module').then(m => m.VehiclesModule)
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadChildren: () => import('./features/profile/profile.module').then(m => m.ProfileModule)
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];
```

---

## 9. Models & Interfaces

### Core Models (`core/models/`)

```typescript
// user.model.ts
export interface User {
  id: number;
  email: string;
  nom: string;
  prenom: string;
  telephone?: string;
  roles: string[];
  userType: 'DRIVER' | 'PASSENGER' | 'USER';
}

// jwt-response.model.ts
export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  type: string;
  id: number;
  email: string;
  nom: string;
  prenom: string;
  roles: string[];
  userType: string;
}

// login-request.model.ts
export interface LoginRequest {
  email: string;
  password: string;
}

// signup-request.model.ts
export interface SignupRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  telephone: string;
  userType: 'DRIVER' | 'PASSENGER';
  roles?: string[];
}
```

### Feature Models

```typescript
// trip.model.ts
export interface Trip {
  id?: number;
  villeDepart: string;
  villeArrivee: string;
  dateHeure: string; // ISO date string
  placesDisponibles: number;
  prixParPlace: number;
  conducteur?: Conducteur;
  voiture?: Vehicle;
}

// reservation.model.ts
export interface Reservation {
  id?: number;
  dateReservation: string;
  etat: 'Confirmée' | 'Annulée';
  passager?: Passager;
  trajet?: Trip;
}

// vehicle.model.ts
export interface Vehicle {
  id?: number;
  marque: string;
  modele: string;
  places: number;
}

// conducteur.model.ts
export interface Conducteur extends User {
  voiture?: Vehicle;
  trajets?: Trip[];
}

// passager.model.ts
export interface Passager extends User {
  reservations?: Reservation[];
}
```

---

## 10. Environment Configuration

### `environments/environment.ts` (Development)

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  apiVersion: 'v1'
};
```

### `environments/environment.prod.ts` (Production)

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.covoiturage.com',
  apiVersion: 'v1'
};
```

### Update `angular.json` to use environments:

```json
"fileReplacements": [
  {
    "replace": "src/environments/environment.ts",
    "with": "src/environments/environment.prod.ts"
  }
]
```

---

## 11. Styling with Bootstrap

### Global Styles (`styles.css`)

```css
/* Import Bootstrap */
@import '~bootstrap/dist/css/bootstrap.min.css';
@import '~@fortawesome/fontawesome-free/css/all.min.css';

/* Custom variables */
:root {
  --primary-color: #007bff;
  --secondary-color: #6c757d;
  --success-color: #28a745;
  --danger-color: #dc3545;
  --warning-color: #ffc107;
  --info-color: #17a2b8;
}

/* Global styles */
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f8f9fa;
}

.container {
  margin-top: 2rem;
}

/* Card styles */
.card {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 1.5rem;
}

/* Button styles */
.btn {
  border-radius: 4px;
  padding: 0.5rem 1.5rem;
}

/* Form styles */
.form-control:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

/* Utility classes */
.cursor-pointer {
  cursor: pointer;
}

.text-muted-light {
  color: #999;
}

/* Responsive */
@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }
}
```

---

## 12. Angular Best Practices

### 12.1 Component Design

#### **Smart vs Presentational Components**

**Smart Components (Container):**
- Interact with services
- Manage state
- Handle routing
- Example: `DashboardHomeComponent`

**Presentational Components (Dumb):**
- Receive data via `@Input()`
- Emit events via `@Output()`
- No service dependencies
- Pure, reusable
- Example: `TripCardComponent`

---

### 12.2 RxJS Best Practices

#### **Use Async Pipe**
```html
<!-- ✅ Good: Auto-unsubscribes -->
<div *ngFor="let trip of trips$ | async">{{ trip.villeDepart }}</div>

<!-- ❌ Bad: Manual subscription -->
<div *ngFor="let trip of trips">{{ trip.villeDepart }}</div>
```

#### **Unsubscribe from Observables**
```typescript
export class MyComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.tripService.getTrips()
      .pipe(takeUntil(this.destroy$))
      .subscribe(trips => { ... });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

#### **Avoid Nested Subscriptions**
```typescript
// ❌ Bad: Nested subscriptions
this.userService.getUser().subscribe(user => {
  this.tripService.getTrips(user.id).subscribe(trips => {
    // Do something
  });
});

// ✅ Good: Use switchMap
this.userService.getUser().pipe(
  switchMap(user => this.tripService.getTrips(user.id))
).subscribe(trips => {
  // Do something
});
```

---

### 12.3 Performance Optimization

#### **Lazy Loading Modules**
Already configured in routes with `loadChildren`.

#### **OnPush Change Detection**
```typescript
@Component({
  selector: 'app-trip-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '...'
})
export class TripCardComponent {
  @Input() trip!: Trip;
}
```

#### **TrackBy in ngFor**
```typescript
// Component
trackByFn(index: number, trip: Trip): number {
  return trip.id!;
}

// Template
<div *ngFor="let trip of trips; trackBy: trackByFn">
  {{ trip.villeDepart }}
</div>
```

---

### 12.4 Accessibility

#### **ARIA Labels**
```html
<button aria-label="Search for trips" (click)="search()">
  <i class="fa fa-search"></i>
</button>
```

#### **Semantic HTML**
```html
<nav>...</nav>
<main>...</main>
<footer>...</footer>
```

#### **Keyboard Navigation**
Ensure all interactive elements are accessible via keyboard.

---

## 13. Testing Strategy

### Unit Tests (Jasmine + Karma)

```typescript
describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, ReactiveFormsModule, HttpClientTestingModule],
      providers: [AuthService]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should invalidate form when email is empty', () => {
    component.loginForm.patchValue({ email: '', password: '123456' });
    expect(component.loginForm.valid).toBeFalsy();
  });

  it('should call authService.login on submit', () => {
    spyOn(authService, 'login').and.returnValue(of({} as JwtResponse));
    component.loginForm.patchValue({ email: 'test@test.com', password: '123456' });
    component.onSubmit();
    expect(authService.login).toHaveBeenCalled();
  });
});
```

### E2E Tests (Optional - Protractor/Cypress)

```typescript
describe('Login Flow', () => {
  it('should login successfully', () => {
    cy.visit('/auth/login');
    cy.get('#email').type('driver@test.com');
    cy.get('#password').type('password123');
    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/dashboard');
  });
});
```

---

## 14. Implementation Roadmap

### Phase 1: Core Setup (Week 1)
- [x] Angular project initialized
- [ ] Install additional dependencies (Angular Material/ngx-toastr)
- [ ] Configure environments
- [ ] Set up folder structure (core, shared, features)
- [ ] Create core module with services (AuthService, TokenService)
- [ ] Implement HTTP interceptors
- [ ] Create guards (AuthGuard, RoleGuard)

### Phase 2: Authentication (Week 1-2)
- [ ] Login component with reactive form
- [ ] Signup component with user type selection
- [ ] Password validation
- [ ] JWT token management
- [ ] Logout functionality

### Phase 3: Layout & Shared Components (Week 2)
- [ ] Header component with navigation
- [ ] Footer component
- [ ] Loading spinner component
- [ ] Toast notifications setup
- [ ] Error handling components

### Phase 4: Dashboard Module (Week 2-3)
- [ ] Dashboard home component
- [ ] Driver dashboard with stats
- [ ] Passenger dashboard with reservations
- [ ] Stats card component
- [ ] Quick actions component

### Phase 5: Trips Module (Week 3-4)
- [ ] Trip search component with filters
- [ ] Trip list component
- [ ] Trip card (reusable)
- [ ] Trip details component
- [ ] Trip form (create/edit) for drivers
- [ ] Trip service with all API methods

### Phase 6: Reservations Module (Week 4)
- [ ] Reservation list component
- [ ] Reservation card component
- [ ] Reservation filtering (upcoming/past/cancelled)
- [ ] Booking functionality
- [ ] Cancel reservation with confirmation

### Phase 7: Vehicles Module (Week 4-5)
- [ ] Vehicle list component
- [ ] Vehicle card component
- [ ] Vehicle form (add/edit)
- [ ] Vehicle service
- [ ] Delete vehicle with warning

### Phase 8: Profile Module (Week 5)
- [ ] Profile view/edit component
- [ ] Change password form
- [ ] Profile update service

### Phase 9: Polish & Testing (Week 5-6)
- [ ] Responsive design testing
- [ ] Accessibility improvements
- [ ] Unit tests for critical components
- [ ] E2E tests for main flows
- [ ] Performance optimization
- [ ] Code review and refactoring

### Phase 10: Deployment (Week 6)
- [ ] Build for production
- [ ] Environment configuration
- [ ] Deploy to hosting service

---

## 15. Deployment Guide

### Build for Production

```bash
ng build --configuration production
```

Output will be in `dist/front-end/browser/`.

### Environment Variables

Update `environment.prod.ts` with production API URL.

### Hosting Options
- **Vercel**: `vercel --prod`
- **Netlify**: Drag & drop `dist/front-end/browser` folder
- **Firebase Hosting**: `firebase deploy`
- **AWS S3 + CloudFront**

### Angular Universal (SSR) - Optional

For better SEO, consider implementing server-side rendering:

```bash
ng add @angular/ssr
```

---

## 16. Summary

This Angular-specific frontend plan provides:

✅ **Modern Angular Stack**: Angular 20.3 + Bootstrap 5 + RxJS  
✅ **Complete Module Architecture**: Core, Shared, and Feature modules with lazy loading  
✅ **Detailed Component Specifications**: 20+ components with templates and logic  
✅ **Full Service Layer**: Services for Auth, Trips, Reservations, Vehicles, Profile  
✅ **Security Implementation**: Guards, Interceptors, JWT management  
✅ **Best Practices**: RxJS patterns, change detection, accessibility  
✅ **Testing Strategy**: Unit and E2E testing guidelines  
✅ **Implementation Roadmap**: 6-week phased development plan  

---

## 17. Next Steps

1. **Review this plan** for approval
2. **Install additional dependencies** as needed
3. **Create folder structure** following the blueprint
4. **Start with Phase 1** - Core setup
5. **Implement authentication** as the foundation
6. **Build feature modules** incrementally
7. **Test and iterate** throughout development

---

## 18. Additional Notes

### Differences from React Plan

- **No Redux**: Angular uses Services + RxJS for state management
- **Modules Instead of Components**: Angular's modular architecture
- **Dependency Injection**: Services are injected, not imported
- **RxJS Observables**: Async operations handled with Observables instead of Promises
- **Template-driven & Reactive Forms**: Angular's powerful form handling
- **Guards Instead of Route Wrappers**: Angular's built-in route protection

### Angular Advantages

- **Complete Framework**: Everything included (routing, forms, HTTP, etc.)
- **TypeScript First**: Built with TypeScript from the ground up
- **Dependency Injection**: Powerful DI system for services
- **RxJS Integration**: Reactive programming built-in
- **CLI Generators**: Generate components, services, modules via CLI

---

**This plan is production-ready and tailored specifically for Angular!** 🚀
