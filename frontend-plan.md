# Frontend Plan - Covoiturage Application

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

The frontend application will provide a modern, responsive, and user-friendly interface to:

1. **Enable seamless authentication** with JWT token management
2. **Provide differentiated experiences** for Drivers and Passengers
3. **Facilitate trip discovery** with powerful search and filtering
4. **Streamline booking process** with clear reservation management
5. **Offer real-time updates** on trip availability and booking status
6. **Ensure responsive design** for mobile and desktop users
7. **Maintain security** through route guards and role-based UI

---

## 2. Recommended Frontend Stack

### Framework: **React 18+ with TypeScript**

**Justification:**
- **Type Safety**: TypeScript provides compile-time type checking, reducing runtime errors
- **Component Reusability**: React's component model fits well with the multi-entity structure
- **Large Ecosystem**: Extensive library support for HTTP clients, state management, and UI components
- **Performance**: Virtual DOM and concurrent rendering for smooth user experience
- **Developer Experience**: Hot reload, excellent debugging tools, and strong community support

### State Management: **Redux Toolkit (RTK) + RTK Query**

**Justification:**
- **Centralized State**: Manage authentication, user profile, and application state globally
- **API Integration**: RTK Query simplifies API calls with automatic caching and refetching
- **Type-Safe**: Excellent TypeScript integration
- **DevTools**: Redux DevTools for debugging state changes
- **Normalized Data**: Handle complex relationships between entities (trips, reservations, users)

### Routing: **React Router v6**

**Justification:**
- **Declarative Routing**: Clean, component-based routing
- **Nested Routes**: Support for complex layouts (dashboard with sub-pages)
- **Route Guards**: Easy implementation of authentication-based route protection
- **Lazy Loading**: Code splitting for performance optimization

### UI Library: **Material-UI (MUI) v5**

**Justification:**
- **Rich Component Library**: Pre-built components for forms, tables, dialogs, etc.
- **Theming**: Consistent design system with customizable theme
- **Responsive**: Built-in responsive design utilities
- **Accessibility**: ARIA-compliant components out of the box
- **Icons**: Comprehensive icon library included
- **French Localization**: Support for multi-language applications

### HTTP Client: **Axios**

**Justification:**
- **Interceptors**: Easy JWT token injection and refresh logic
- **Request/Response Transformation**: Handle data formatting centrally
- **Error Handling**: Global error handling configuration
- **TypeScript Support**: Type-safe API calls
- **Browser Compatibility**: Works across all modern browsers

### Form Management: **React Hook Form**

**Justification:**
- **Performance**: Minimal re-renders, uncontrolled components
- **Validation**: Built-in validation with schema support (Yup/Zod)
- **TypeScript**: Full type safety for form data
- **Developer Experience**: Simple API, less boilerplate

### Additional Tools:
- **Yup**: Schema validation for forms
- **date-fns**: Date/time manipulation (for trip dates)
- **React Query DevTools**: Debug API calls and cache
- **ESLint + Prettier**: Code quality and formatting

---

## 3. Folder Structure

```
src/
├── app/
│   ├── store.ts                    # Redux store configuration
│   └── hooks.ts                    # Typed Redux hooks (useAppDispatch, useAppSelector)
│
├── assets/
│   ├── images/                     # Static images, logos
│   ├── icons/                      # Custom SVG icons
│   └── fonts/                      # Custom fonts (if needed)
│
├── components/
│   ├── common/                     # Shared UI components
│   │   ├── Button/
│   │   │   ├── Button.tsx
│   │   │   ├── Button.test.tsx
│   │   │   └── Button.styles.ts
│   │   ├── Input/
│   │   ├── Card/
│   │   ├── Dialog/
│   │   ├── Toast/
│   │   ├── Loader/
│   │   ├── ErrorBoundary/
│   │   └── index.ts               # Export barrel
│   │
│   ├── layout/                     # Layout components
│   │   ├── Header/
│   │   │   ├── Header.tsx
│   │   │   └── Header.styles.ts
│   │   ├── Footer/
│   │   ├── Sidebar/
│   │   └── MainLayout/
│   │
│   └── features/                   # Feature-specific components
│       ├── auth/
│       │   ├── LoginForm/
│       │   ├── SignupForm/
│       │   └── UserTypeSelector/
│       ├── trips/
│       │   ├── TripCard/
│       │   ├── TripList/
│       │   ├── TripSearchForm/
│       │   ├── TripForm/
│       │   └── TripDetailsModal/
│       ├── reservations/
│       │   ├── ReservationCard/
│       │   ├── ReservationList/
│       │   └── ReservationStatus/
│       ├── vehicles/
│       │   ├── VehicleCard/
│       │   ├── VehicleForm/
│       │   └── VehicleList/
│       └── profile/
│           ├── ProfileCard/
│           ├── ProfileEditForm/
│           └── PasswordChangeForm/
│
├── pages/
│   ├── auth/
│   │   ├── LoginPage.tsx
│   │   ├── SignupPage.tsx
│   │   └── ForgotPasswordPage.tsx
│   ├── home/
│   │   └── HomePage.tsx
│   ├── dashboard/
│   │   ├── DashboardPage.tsx
│   │   ├── DriverDashboard.tsx
│   │   └── PassengerDashboard.tsx
│   ├── trips/
│   │   ├── TripsListPage.tsx
│   │   ├── TripDetailsPage.tsx
│   │   ├── CreateTripPage.tsx
│   │   ├── EditTripPage.tsx
│   │   └── SearchTripsPage.tsx
│   ├── reservations/
│   │   ├── MyReservationsPage.tsx
│   │   └── ReservationDetailsPage.tsx
│   ├── vehicles/
│   │   ├── VehiclesPage.tsx
│   │   ├── AddVehiclePage.tsx
│   │   └── EditVehiclePage.tsx
│   ├── profile/
│   │   └── ProfilePage.tsx
│   └── error/
│       ├── NotFoundPage.tsx
│       └── UnauthorizedPage.tsx
│
├── services/
│   ├── api/
│   │   ├── axios.config.ts        # Axios instance with interceptors
│   │   ├── authApi.ts             # Auth endpoints
│   │   ├── conducteurApi.ts       # Driver endpoints
│   │   ├── passagerApi.ts         # Passenger endpoints
│   │   ├── trajetApi.ts           # Trip endpoints
│   │   ├── reservationApi.ts      # Reservation endpoints
│   │   ├── voitureApi.ts          # Vehicle endpoints
│   │   └── index.ts
│   │
│   └── rtk/                        # RTK Query slices
│       ├── authSlice.ts
│       ├── tripsSlice.ts
│       ├── reservationsSlice.ts
│       └── userSlice.ts
│
├── hooks/
│   ├── useAuth.ts                 # Authentication hook
│   ├── useRole.ts                 # Role checking hook
│   ├── useToast.ts                # Toast notifications hook
│   ├── useDebounce.ts             # Debounce hook for search
│   └── useLocalStorage.ts         # localStorage wrapper
│
├── utils/
│   ├── constants.ts               # App constants (API URLs, roles, etc.)
│   ├── storage.ts                 # localStorage/sessionStorage helpers
│   ├── validation.ts              # Validation schemas (Yup)
│   ├── formatters.ts              # Date, currency formatters
│   ├── errorHandlers.ts           # Error handling utilities
│   └── helpers.ts                 # General helper functions
│
├── routes/
│   ├── AppRoutes.tsx              # Main routing configuration
│   ├── PrivateRoute.tsx           # Protected route wrapper
│   └── RoleBasedRoute.tsx         # Role-based route wrapper
│
├── types/
│   ├── auth.types.ts              # Authentication types
│   ├── user.types.ts              # User, Conducteur, Passager types
│   ├── trip.types.ts              # Trajet types
│   ├── reservation.types.ts       # Reservation types
│   ├── vehicle.types.ts           # Voiture types
│   ├── api.types.ts               # API request/response types
│   └── common.types.ts            # Shared types
│
├── styles/
│   ├── theme.ts                   # MUI theme configuration
│   ├── global.css                 # Global styles
│   └── variables.css              # CSS variables
│
├── App.tsx                        # Root component
├── main.tsx                       # Entry point
└── vite-env.d.ts                  # Vite type definitions
```

### Folder Explanations:

- **`app/`**: Redux store setup and typed hooks
- **`assets/`**: Static resources (images, icons, fonts)
- **`components/common/`**: Reusable UI components used across the app
- **`components/layout/`**: Structural components (header, footer, sidebar)
- **`components/features/`**: Feature-specific, reusable components
- **`pages/`**: Page-level components corresponding to routes
- **`services/api/`**: API service layer with Axios configuration
- **`services/rtk/`**: Redux Toolkit slices for state management
- **`hooks/`**: Custom React hooks for business logic
- **`utils/`**: Helper functions, constants, validation schemas
- **`routes/`**: Routing configuration and route guards
- **`types/`**: TypeScript type definitions
- **`styles/`**: Global styles and theme configuration

---

## 4. Page-by-Page Blueprint

### 4.1 Authentication Pages

#### **LoginPage**

**Purpose:** Allow users to authenticate with email and password

**UI Requirements:**
- Email input field with validation
- Password input field with show/hide toggle
- "Remember Me" checkbox
- Login button
- Link to "Forgot Password"
- Link to "Sign Up"
- Error message display area
- Loading state during authentication

**API Calls:**
- `POST /api/auth/login` - Authenticate user
  - Request: `{ email: string, password: string }`
  - Response: `JwtResponse` with tokens and user info

**Data Flow:**
1. User enters credentials
2. Form validation (client-side)
3. Submit to backend API
4. On success: Save tokens to localStorage, redirect to dashboard
5. On error: Display error message

---

#### **SignupPage**

**Purpose:** Register new users as either Driver or Passenger

**UI Requirements:**
- User type selector (Driver/Passenger) - prominent
- First name and last name fields
- Email field with validation
- Phone number field
- Password field with strength indicator
- Confirm password field
- Terms & conditions checkbox
- Sign up button
- Link to "Already have an account? Log in"
- Success/error message display

**API Calls:**
- `POST /api/auth/signup` - Register new user
  - Request: `SignupRequest { nom, prenom, email, password, telephone, userType, roles }`
  - Response: Success message

**Data Flow:**
1. User selects user type (DRIVER/PASSENGER)
2. User fills registration form
3. Client-side validation
4. Submit to backend
5. On success: Show success message, redirect to login
6. On error: Display validation errors

---

#### **ForgotPasswordPage** (Optional - Future Enhancement)

**Purpose:** Allow users to reset their password

**UI Requirements:**
- Email input field
- Submit button
- Back to login link
- Success message

**API Calls:**
- `POST /api/auth/forgot-password` (to be implemented)

---

### 4.2 Home Page

#### **HomePage**

**Purpose:** Landing page for unauthenticated users

**UI Requirements:**
- Hero section with app description
- Quick trip search form (departure, arrival, date)
- Featured trips preview
- How it works section (3 steps: Search → Book → Ride)
- Call-to-action buttons (Sign Up as Driver, Sign Up as Passenger)
- Testimonials section
- Footer with links

**API Calls:**
- `GET /trajets?limit=6` - Fetch featured trips

**Data Flow:**
- Page loads → Fetch featured trips
- User searches → Navigate to SearchTripsPage with query params

---

### 4.3 Dashboard Pages

#### **DashboardPage**

**Purpose:** Main hub after login, shows user-specific content

**UI Requirements:**
- Welcome message with user name
- Quick stats cards (based on user type)
- Quick actions buttons
- Recent activity feed
- Conditional rendering based on user role

**Conditional Rendering:**
- If DRIVER: Show `DriverDashboard`
- If PASSENGER: Show `PassengerDashboard`

---

#### **DriverDashboard**

**Purpose:** Driver's control panel

**UI Requirements:**
- Stats cards:
  - Total trips created
  - Active trips
  - Total reservations
  - Earnings (total)
- Quick actions:
  - Create new trip
  - Manage vehicles
  - View all trips
- Upcoming trips list (next 5)
- Recent reservations (last 10)
- Vehicle status card (if vehicle added)

**API Calls:**
- `GET /conducteurs/{id}` - Fetch driver profile
- `GET /trajets/conducteur/{conducteurId}` - Fetch driver's trips
- `GET /conducteurs/{id}/voiture` - Fetch driver's vehicle (if exists)

---

#### **PassengerDashboard**

**Purpose:** Passenger's control panel

**UI Requirements:**
- Stats cards:
  - Total reservations made
  - Active reservations
  - Completed trips
- Quick actions:
  - Search for trips
  - View my reservations
- Upcoming reservations (next 5) with trip details
- Search form (quick access)

**API Calls:**
- `GET /passagers/{id}` - Fetch passenger profile
- `GET /passagers/{id}/reservations` - Fetch passenger's reservations

---

### 4.4 Trip Pages

#### **SearchTripsPage**

**Purpose:** Search and filter available trips

**UI Requirements:**
- Search form:
  - Departure city (autocomplete or dropdown)
  - Arrival city (autocomplete or dropdown)
  - Date picker (optional)
  - Number of seats needed
  - Search button
- Filters panel:
  - Price range slider
  - Departure time range
  - Sort by (price, date, available seats)
- Results grid/list:
  - Trip cards showing:
    - Departure → Arrival
    - Date and time
    - Driver name and rating (future)
    - Available seats
    - Price per seat
    - Vehicle info
    - "Book Now" button
- Pagination or infinite scroll
- No results state

**API Calls:**
- `GET /trajets/rechercher?villeDepart={ville}&villeArrivee={ville}` - Search trips
- `GET /trajets/search?motCle={keyword}` - Keyword search

**Data Flow:**
1. User enters search criteria
2. Submit search → API call
3. Display results in grid
4. User clicks "Book Now" → Navigate to TripDetailsPage

---

#### **TripsListPage**

**Purpose:** View all trips (for admins or general browsing)

**UI Requirements:**
- Similar to SearchTripsPage but without search form
- Display all available trips
- Filters for active/past trips

**API Calls:**
- `GET /trajets` - Fetch all trips

---

#### **TripDetailsPage**

**Purpose:** Detailed view of a specific trip with booking option

**UI Requirements:**
- Trip information card:
  - Route map (optional - use static map or Google Maps embed)
  - Departure city, time
  - Arrival city, time
  - Price per seat
  - Available seats remaining
  - Total duration (calculated)
- Driver information card:
  - Name, photo (placeholder)
  - Rating (future)
  - Vehicle details
- Booking section:
  - Number of seats selector
  - Total price calculation
  - "Reserve" button (only for passengers)
  - Login prompt if not authenticated
- Cancellation policy info

**API Calls:**
- `GET /trajets/{id}` - Fetch trip details
- `GET /conducteurs/{conducteurId}` - Fetch driver info
- `POST /passagers/{passagerId}/reservations?trajetId={trajetId}` - Make reservation

**Data Flow:**
1. Page loads with trip ID
2. Fetch trip details + driver info
3. User selects seats and clicks Reserve
4. Submit reservation → Success message → Redirect to MyReservationsPage

---

#### **CreateTripPage** (Driver Only)

**Purpose:** Allow drivers to create new trips

**UI Requirements:**
- Trip creation form:
  - Departure city (text input or autocomplete)
  - Arrival city (text input or autocomplete)
  - Date and time picker
  - Available seats (number input)
  - Price per seat (number input)
  - Vehicle selector (dropdown of user's vehicles)
    - If no vehicle: Prompt to add one first
  - Submit button
- Form validation
- Success/error messages

**API Calls:**
- `POST /conducteurs/{id}/trajets` - Create trip
  - Request: `Trajet` object

**Data Flow:**
1. Check if driver has a vehicle
2. If no vehicle: Show message + link to add vehicle
3. If vehicle exists: Show form
4. User fills form
5. Submit → Create trip
6. On success: Redirect to DriverDashboard

---

#### **EditTripPage** (Driver Only)

**Purpose:** Allow drivers to edit their trips

**UI Requirements:**
- Pre-filled form with existing trip data
- Same fields as CreateTripPage
- Save and Cancel buttons
- Can't edit if reservations exist (or show warning)

**API Calls:**
- `GET /trajets/{id}` - Fetch trip to edit
- `PUT /trajets/{id}` - Update trip

---

### 4.5 Reservation Pages

#### **MyReservationsPage** (Passenger Only)

**Purpose:** View all reservations made by the passenger

**UI Requirements:**
- Tabs or filters:
  - Upcoming reservations
  - Past reservations
  - Cancelled reservations
- Reservation cards:
  - Trip route (Departure → Arrival)
  - Date and time
  - Reservation status (Confirmée/Annulée)
  - Total price
  - Reservation date
  - "View Details" button
  - "Cancel Reservation" button (for upcoming only)
- Empty state if no reservations

**API Calls:**
- `GET /passagers/{id}/reservations` - Fetch all reservations
- `GET /reservations/passager/{passagerId}` - Alternative endpoint

**Data Flow:**
1. Fetch all reservations on page load
2. Filter by status
3. User clicks "Cancel" → Confirm dialog → API call → Refresh list

---

#### **ReservationDetailsPage** (Optional)

**Purpose:** Detailed view of a specific reservation

**UI Requirements:**
- Reservation info card
- Trip details
- Driver contact info
- Payment info (if implemented)
- Cancel button

**API Calls:**
- `GET /reservations/{id}` - Fetch reservation details
- `DELETE /passagers/{passagerId}/reservations/{reservationId}` - Cancel reservation

---

### 4.6 Vehicle Pages (Driver Only)

#### **VehiclesPage**

**Purpose:** View and manage driver's vehicle

**UI Requirements:**
- Vehicle card (if exists):
  - Make, model
  - Number of seats
  - Edit button
  - Delete button (with warning if trips exist)
- "Add Vehicle" button (if no vehicle)
- Empty state if no vehicle

**API Calls:**
- `GET /conducteurs/{id}` - Fetch driver data (includes vehicle)
- `DELETE /voitures/{id}` - Delete vehicle

---

#### **AddVehiclePage**

**Purpose:** Add a new vehicle

**UI Requirements:**
- Vehicle form:
  - Make (text input)
  - Model (text input)
  - Number of seats (number input, min: 1, max: 8)
  - Submit button

**API Calls:**
- `POST /conducteurs/{id}/voiture` - Add vehicle
  - Request: `Voiture { marque, modele, places }`

**Data Flow:**
1. User fills form
2. Validate inputs
3. Submit → Create vehicle
4. On success: Redirect to VehiclesPage

---

#### **EditVehiclePage**

**Purpose:** Edit existing vehicle

**UI Requirements:**
- Pre-filled form with vehicle data
- Save and Cancel buttons

**API Calls:**
- `PUT /voitures/{id}` - Update vehicle

---

### 4.7 Profile Page

#### **ProfilePage**

**Purpose:** View and edit user profile

**UI Requirements:**
- Profile information card:
  - Profile photo (placeholder)
  - Name, email, phone
  - User type badge (Driver/Passenger)
  - "Edit Profile" button
- Edit mode:
  - Editable fields: nom, prenom, telephone
  - Email read-only
  - Save and Cancel buttons
- Change password section:
  - Current password
  - New password
  - Confirm new password
  - Change password button
- Account actions:
  - Logout button
  - Delete account button (with confirmation)

**API Calls:**
- `GET /conducteurs/{id}` or `GET /passagers/{id}` - Fetch user data
- `PUT /conducteurs/{id}` or `PUT /passagers/{id}` - Update profile
- (Future) `PUT /api/auth/change-password` - Change password

---

### 4.8 Error Pages

#### **NotFoundPage (404)**

**Purpose:** Display when route doesn't exist

**UI Requirements:**
- 404 message
- Illustration or icon
- Back to home button

---

#### **UnauthorizedPage (403)**

**Purpose:** Display when user tries to access forbidden route

**UI Requirements:**
- 403 message
- Explanation text
- Back to dashboard button

---

### 4.9 Shared UI Components

#### **TripCard**
- Compact trip display for lists
- Shows: route, date, price, seats, driver
- Click → Navigate to details

#### **ReservationCard**
- Compact reservation display
- Shows: trip info, status, date
- Cancel button

#### **VehicleCard**
- Shows: make, model, seats
- Edit/Delete actions

#### **SearchForm**
- Departure/Arrival inputs
- Date picker
- Submit button

#### **Dialog/Modal**
- Confirmation dialogs
- Detail modals

#### **Toast/Snackbar**
- Success/Error notifications

#### **Loader**
- Loading spinners for async operations

#### **DataTable**
- For admin panels
- Sortable, filterable

---

## 5. API Integration Guide

### 5.1 Complete Backend Endpoints

#### **Authentication Endpoints (`/api/auth`)**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/auth/login` | User login | `LoginRequest { email, password }` | `JwtResponse` |
| POST | `/api/auth/signup` | User registration | `SignupRequest { nom, prenom, email, password, telephone, userType, roles }` | `MessageResponse` |
| POST | `/api/auth/refresh` | Refresh access token | `RefreshTokenRequest { refreshToken }` | `JwtResponse` |
| POST | `/api/auth/logout` | User logout | `RefreshTokenRequest { refreshToken }` | `MessageResponse` |

---

#### **Conducteur Endpoints (`/conducteurs`)**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/conducteurs` | Create driver | `Conducteur` | `Conducteur` or message |
| GET | `/conducteurs` | Get all drivers | - | `Conducteur[]` or message |
| GET | `/conducteurs/{id}` | Get driver by ID | - | `Conducteur` |
| PUT | `/conducteurs/{id}` | Update driver | `Conducteur` | `Conducteur` |
| DELETE | `/conducteurs/{id}` | Delete driver | - | Message |
| POST | `/conducteurs/connexion` | Driver login (legacy) | `LoginRequest` | Message |
| POST | `/conducteurs/{id}/trajets` | Create trip for driver | `Trajet` | Success message |
| DELETE | `/conducteurs/{id}/trajets/{trajetId}` | Cancel trip | - | Message |
| POST | `/conducteurs/{id}/voiture` | Add vehicle to driver | `Voiture` | Success message or `Voiture` |

---

#### **Passager Endpoints (`/passagers`)**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/passagers` | Create passenger | `Passager` | `Passager` or message |
| GET | `/passagers` | Get all passengers | - | `Passager[]` or message |
| GET | `/passagers/{id}` | Get passenger by ID | - | `Passager` |
| PUT | `/passagers/{id}` | Update passenger | `Passager` | `Passager` |
| DELETE | `/passagers/{id}` | Delete passenger | - | Message |
| POST | `/passagers/connexion` | Passenger login (legacy) | `LoginRequest` | Message |
| POST | `/passagers/{id}/reservations?trajetId={trajetId}` | Make reservation | - | `Reservation` |
| DELETE | `/passagers/{id}/reservations/{reservationId}` | Cancel reservation | - | Message |
| GET | `/passagers/{id}/reservations` | Get passenger's reservations | - | `Reservation[]` |

---

#### **Trajet Endpoints (`/trajets`)**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/trajets` | Create trip | `Trajet` | `Trajet` |
| GET | `/trajets` | Get all trips | - | `Trajet[]` or message |
| GET | `/trajets/{id}` | Get trip by ID | - | `Trajet` |
| PUT | `/trajets/{id}` | Update trip | `Trajet` | `Trajet` |
| DELETE | `/trajets/{id}` | Delete trip | - | Message |
| GET | `/trajets/rechercher?villeDepart={ville}&villeArrivee={ville}` | Search trips | - | `Trajet[]` |
| GET | `/trajets/search?motCle={keyword}` | Search by keyword | - | `Trajet[]` |
| GET | `/trajets/conducteur/{conducteurId}` | Get trips by driver | - | `Trajet[]` |

---

#### **Reservation Endpoints (`/reservations`)**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/reservations` | Create reservation | `Reservation` | `Reservation` |
| GET | `/reservations` | Get all reservations | - | `Reservation[]` or message |
| GET | `/reservations/{id}` | Get reservation by ID | - | `Reservation` |
| PUT | `/reservations/{id}` | Update reservation | `Reservation` | `Reservation` |
| DELETE | `/reservations/{id}` | Delete reservation | - | Message |
| GET | `/reservations/passager/{passagerId}` | Get reservations by passenger | - | `Reservation[]` |
| GET | `/reservations/trajet/{trajetId}` | Get reservations by trip | - | `Reservation[]` |

---

#### **Voiture Endpoints (`/voitures`)**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/voitures` | Create vehicle | `Voiture` | Success message or `Voiture` |
| POST | `/voitures/conducteur/{conducteurId}` | Associate vehicle with driver | `Voiture` | `Voiture` |
| GET | `/voitures` | Get all vehicles | - | `Voiture[]` or message |
| GET | `/voitures/{id}` | Get vehicle by ID | - | `Voiture` |
| PUT | `/voitures/{id}` | Update vehicle | `Voiture` | `Voiture` |
| DELETE | `/voitures/{id}` | Delete vehicle | - | Success or error message |

---

### 5.2 Request/Response Formats

#### **JwtResponse**
```typescript
{
  accessToken: string;
  refreshToken: string;
  type: "Bearer";
  id: number;
  email: string;
  nom: string;
  prenom: string;
  roles: string[]; // e.g., ["ROLE_DRIVER"]
  userType: "DRIVER" | "PASSENGER" | "USER";
}
```

#### **LoginRequest**
```typescript
{
  email: string;
  password: string;
}
```

#### **SignupRequest**
```typescript
{
  nom: string;
  prenom: string;
  email: string;
  password: string;
  telephone: string;
  userType: "DRIVER" | "PASSENGER";
  roles?: string[]; // Optional, defaults based on userType
}
```

#### **Trajet**
```typescript
{
  id?: number;
  villeDepart: string;
  villeArrivee: string;
  dateHeure: string; // ISO 8601 format
  placesDisponibles: number;
  prixParPlace: number;
  conducteur?: Conducteur; // Populated when fetching
  voiture?: Voiture; // Populated when fetching
}
```

#### **Reservation**
```typescript
{
  id?: number;
  dateReservation: string; // ISO 8601 format
  etat: "Confirmée" | "Annulée";
  passager?: Passager;
  trajet?: Trajet;
}
```

#### **Voiture**
```typescript
{
  id?: number;
  marque: string;
  modele: string;
  places: number;
  conducteur?: Conducteur;
}
```

#### **Conducteur/Passager**
```typescript
{
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  motDePasse?: string; // Only for creation
  roles?: Role[];
  // Conducteur specific:
  voiture?: Voiture;
  trajets?: Trajet[];
  // Passager specific:
  reservations?: Reservation[];
}
```

---

### 5.3 JWT Handling

#### **Token Storage**
- **Access Token**: Store in memory (React state or Redux) for security
- **Refresh Token**: Store in `httpOnly` cookie (if backend supports) OR in `localStorage` with security considerations

#### **Axios Interceptor - Request**
```typescript
// Add JWT to all requests
axios.interceptors.request.use(
  (config) => {
    const token = getAccessToken(); // From Redux or localStorage
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
```

#### **Axios Interceptor - Response (Token Refresh)**
```typescript
axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    // If 401 and not already retried
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        const refreshToken = getRefreshToken();
        const response = await axios.post('/api/auth/refresh', { refreshToken });
        const { accessToken } = response.data;
        
        // Update stored token
        setAccessToken(accessToken);
        
        // Retry original request with new token
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return axios(originalRequest);
      } catch (refreshError) {
        // Refresh failed → logout user
        logout();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);
```

---

### 5.4 Error Handling Strategy

#### **Global Error Handler**
```typescript
export const handleApiError = (error: any) => {
  if (error.response) {
    // Server responded with error status
    const { status, data } = error.response;
    
    switch (status) {
      case 400:
        return { message: data.message || 'Invalid request', type: 'error' };
      case 401:
        return { message: 'Unauthorized. Please login.', type: 'error' };
      case 403:
        return { message: 'Access forbidden', type: 'error' };
      case 404:
        return { message: 'Resource not found', type: 'error' };
      case 500:
        return { message: 'Server error. Please try again later.', type: 'error' };
      default:
        return { message: 'An error occurred', type: 'error' };
    }
  } else if (error.request) {
    // Request made but no response
    return { message: 'Network error. Please check your connection.', type: 'error' };
  } else {
    // Other errors
    return { message: error.message || 'An unexpected error occurred', type: 'error' };
  }
};
```

#### **Usage in Components**
```typescript
try {
  const response = await createTrip(tripData);
  showToast('Trip created successfully!', 'success');
} catch (error) {
  const errorInfo = handleApiError(error);
  showToast(errorInfo.message, errorInfo.type);
}
```

---

## 6. Frontend Security Rules

### 6.1 Route Guards

#### **PrivateRoute Component**
```typescript
// Only allow authenticated users
const PrivateRoute = ({ children }: { children: React.ReactNode }) => {
  const { isAuthenticated } = useAuth();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  return <>{children}</>;
};
```

#### **RoleBasedRoute Component**
```typescript
// Only allow users with specific roles
const RoleBasedRoute = ({ 
  children, 
  allowedRoles 
}: { 
  children: React.ReactNode;
  allowedRoles: string[];
}) => {
  const { user, isAuthenticated } = useAuth();
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  const hasRequiredRole = user?.roles.some(role => allowedRoles.includes(role));
  
  if (!hasRequiredRole) {
    return <Navigate to="/unauthorized" replace />;
  }
  
  return <>{children}</>;
};
```

#### **Usage Example**
```typescript
<Route path="/dashboard" element={
  <PrivateRoute>
    <DashboardPage />
  </PrivateRoute>
} />

<Route path="/create-trip" element={
  <RoleBasedRoute allowedRoles={['ROLE_DRIVER']}>
    <CreateTripPage />
  </RoleBasedRoute>
} />
```

---

### 6.2 Token Expiration Logic

#### **Check Token on App Load**
```typescript
// In App.tsx or root component
useEffect(() => {
  const token = getAccessToken();
  if (token) {
    // Verify token is still valid
    const decoded = jwtDecode(token);
    const isExpired = decoded.exp * 1000 < Date.now();
    
    if (isExpired) {
      // Try to refresh
      refreshAccessToken().catch(() => {
        logout();
      });
    }
  }
}, []);
```

#### **Auto-refresh Before Expiration**
```typescript
// Set up timer to refresh token before it expires
useEffect(() => {
  const token = getAccessToken();
  if (token) {
    const decoded = jwtDecode(token);
    const expiresIn = decoded.exp * 1000 - Date.now();
    const refreshTime = expiresIn - 60000; // Refresh 1 min before expiry
    
    const timer = setTimeout(() => {
      refreshAccessToken();
    }, refreshTime);
    
    return () => clearTimeout(timer);
  }
}, [accessToken]);
```

---

### 6.3 Role-Based UI Blocking

#### **Conditional Rendering by Role**
```typescript
const { user } = useAuth();

// Example: Only show "Create Trip" button to drivers
{user?.userType === 'DRIVER' && (
  <Button onClick={navigateToCreateTrip}>
    Create New Trip
  </Button>
)}

// Example: Only show "My Reservations" to passengers
{user?.userType === 'PASSENGER' && (
  <Button onClick={navigateToReservations}>
    My Reservations
  </Button>
)}
```

#### **Custom Hook for Role Checking**
```typescript
export const useRole = () => {
  const { user } = useAuth();
  
  const hasRole = (role: string) => {
    return user?.roles?.includes(role) || false;
  };
  
  const isDriver = () => user?.userType === 'DRIVER';
  const isPassenger = () => user?.userType === 'PASSENGER';
  const isAdmin = () => hasRole('ROLE_ADMIN');
  
  return { hasRole, isDriver, isPassenger, isAdmin };
};
```

---

### 6.4 Input Validation Principles

#### **Client-Side Validation with Yup**
```typescript
import * as Yup from 'yup';

export const loginSchema = Yup.object({
  email: Yup.string()
    .email('Invalid email address')
    .required('Email is required'),
  password: Yup.string()
    .min(6, 'Password must be at least 6 characters')
    .required('Password is required'),
});

export const tripSchema = Yup.object({
  villeDepart: Yup.string()
    .required('Departure city is required'),
  villeArrivee: Yup.string()
    .required('Arrival city is required'),
  dateHeure: Yup.date()
    .min(new Date(), 'Date must be in the future')
    .required('Date and time are required'),
  placesDisponibles: Yup.number()
    .min(1, 'At least 1 seat required')
    .max(8, 'Maximum 8 seats')
    .required('Number of seats is required'),
  prixParPlace: Yup.number()
    .min(0, 'Price cannot be negative')
    .required('Price per seat is required'),
});
```

#### **Sanitize User Input**
- Trim whitespace from string inputs
- Escape HTML characters to prevent XSS
- Validate data types before sending to API
- Use React Hook Form's built-in validation

#### **Never Trust Client Input**
- Always validate on both client and server
- Client validation is for UX; server validation is for security

---

### 6.5 Secure Storage

#### **Don't Store Sensitive Data**
- Never store passwords in localStorage
- Be cautious with tokens in localStorage (XSS risk)
- Consider using sessionStorage for sensitive data (cleared on tab close)

#### **Encrypt Sensitive Data**
If storing sensitive data locally, consider encryption:
```typescript
import CryptoJS from 'crypto-js';

const encryptData = (data: string, key: string) => {
  return CryptoJS.AES.encrypt(data, key).toString();
};

const decryptData = (ciphertext: string, key: string) => {
  const bytes = CryptoJS.AES.decrypt(ciphertext, key);
  return bytes.toString(CryptoJS.enc.Utf8);
};
```

---

## 7. UX & Best Practices

### 7.1 Responsive Design Rules

#### **Mobile-First Approach**
- Design for mobile screens first (320px+)
- Progressively enhance for tablets (768px+) and desktops (1024px+)

#### **Breakpoints**
```typescript
// theme.ts
export const breakpoints = {
  xs: '0px',      // Mobile
  sm: '600px',    // Small tablets
  md: '960px',    // Tablets
  lg: '1280px',   // Desktops
  xl: '1920px',   // Large desktops
};
```

#### **Responsive Component Example**
```typescript
import { useMediaQuery, useTheme } from '@mui/material';

const TripCard = () => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  
  return (
    <Card sx={{ 
      flexDirection: isMobile ? 'column' : 'row',
      padding: isMobile ? 2 : 3 
    }}>
      {/* Content */}
    </Card>
  );
};
```

#### **Touch-Friendly**
- Buttons and clickable areas: minimum 44x44px
- Adequate spacing between interactive elements
- Swipe gestures for mobile (carousels, modals)

---

### 7.2 Component Reusability Guidelines

#### **Single Responsibility**
Each component should do one thing well.

```typescript
// ❌ Bad: Component does too much
const UserDashboard = () => {
  // Fetch data, UI rendering, form handling all in one
};

// ✅ Good: Separate concerns
const UserDashboard = () => {
  const { data } = useUserData(); // Custom hook for data
  return (
    <>
      <StatsCards stats={data.stats} />
      <RecentActivity activities={data.activities} />
      <QuickActions />
    </>
  );
};
```

#### **Props Interface**
Always define TypeScript interfaces for props.

```typescript
interface TripCardProps {
  trip: Trip;
  onBook?: () => void;
  showBookButton?: boolean;
}

const TripCard = ({ trip, onBook, showBookButton = true }: TripCardProps) => {
  // Component logic
};
```

#### **Composition Over Inheritance**
Use composition to build complex UIs.

```typescript
// Reusable Card component
const Card = ({ children, title }) => (
  <div className="card">
    <h3>{title}</h3>
    <div>{children}</div>
  </div>
);

// Compose specific cards
const TripCard = ({ trip }) => (
  <Card title={trip.villeDepart + ' → ' + trip.villeArrivee}>
    <TripDetails trip={trip} />
    <BookButton tripId={trip.id} />
  </Card>
);
```

---

### 7.3 Naming Conventions

#### **Files and Folders**
- **Components**: PascalCase (e.g., `TripCard.tsx`, `LoginForm.tsx`)
- **Utilities**: camelCase (e.g., `formatDate.ts`, `validateEmail.ts`)
- **Constants**: UPPER_SNAKE_CASE files (e.g., `API_URLS.ts`)

#### **Variables and Functions**
- **Variables**: camelCase (e.g., `userName`, `tripList`)
- **Functions**: camelCase with verb prefix (e.g., `getUser`, `handleSubmit`, `isAuthenticated`)
- **Boolean variables**: Prefix with `is`, `has`, `should` (e.g., `isLoading`, `hasError`, `shouldRender`)

#### **Components**
- **Component names**: PascalCase (e.g., `UserProfile`, `TripSearchForm`)
- **Props**: camelCase (e.g., `userId`, `onSubmit`)

#### **Types and Interfaces**
- **Interfaces**: PascalCase with `I` prefix (optional) or just PascalCase
  - Example: `interface User` or `interface IUser`
- **Types**: PascalCase (e.g., `type Trip`, `type ApiResponse`)
- **Enums**: PascalCase for name, UPPER_SNAKE_CASE for values
  - Example: `enum UserType { DRIVER = 'DRIVER', PASSENGER = 'PASSENGER' }`

---

### 7.4 Accessibility Checklist

#### **Semantic HTML**
- Use proper HTML5 tags: `<header>`, `<nav>`, `<main>`, `<article>`, `<footer>`
- Use `<button>` for actions, `<a>` for navigation

#### **ARIA Labels**
```typescript
<button aria-label="Search for trips">
  <SearchIcon />
</button>

<input 
  type="text" 
  aria-label="Departure city" 
  aria-required="true"
/>
```

#### **Keyboard Navigation**
- Ensure all interactive elements are keyboard accessible (Tab, Enter, Space)
- Provide focus indicators (visible outline on focus)
- Logical tab order

#### **Color Contrast**
- Text: Minimum 4.5:1 contrast ratio (WCAG AA)
- Large text: Minimum 3:1 ratio
- Don't rely on color alone to convey information

#### **Alt Text for Images**
```typescript
<img src="/logo.png" alt="Covoiturage App Logo" />
```

#### **Form Labels**
```typescript
<label htmlFor="email">Email Address</label>
<input id="email" type="email" name="email" />
```

#### **Screen Reader Support**
- Use `aria-live` for dynamic content updates
- Announce loading states
- Provide meaningful error messages

---

### 7.5 Performance Optimization

#### **Code Splitting**
```typescript
import { lazy, Suspense } from 'react';

const DashboardPage = lazy(() => import('./pages/dashboard/DashboardPage'));

const App = () => (
  <Suspense fallback={<Loader />}>
    <DashboardPage />
  </Suspense>
);
```

#### **Memoization**
```typescript
import { memo, useMemo, useCallback } from 'react';

// Memoize expensive calculations
const ExpensiveComponent = ({ data }) => {
  const processedData = useMemo(() => {
    return heavyProcessing(data);
  }, [data]);
  
  return <div>{processedData}</div>;
};

// Memoize callbacks to prevent re-renders
const Parent = () => {
  const handleClick = useCallback(() => {
    console.log('Clicked');
  }, []);
  
  return <Child onClick={handleClick} />;
};

// Memoize components
export default memo(ExpensiveComponent);
```

#### **Image Optimization**
- Use WebP format with fallbacks
- Lazy load images below the fold
- Use responsive images (`srcset`)

```typescript
<img 
  src="trip-small.webp"
  srcSet="trip-small.webp 480w, trip-medium.webp 768w, trip-large.webp 1200w"
  sizes="(max-width: 600px) 480px, (max-width: 960px) 768px, 1200px"
  alt="Trip image"
  loading="lazy"
/>
```

#### **Debounce Search**
```typescript
import { useDebounce } from '../hooks/useDebounce';

const SearchForm = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearch = useDebounce(searchTerm, 500);
  
  useEffect(() => {
    if (debouncedSearch) {
      searchTrips(debouncedSearch);
    }
  }, [debouncedSearch]);
  
  return (
    <input 
      value={searchTerm}
      onChange={(e) => setSearchTerm(e.target.value)}
    />
  );
};
```

#### **Virtualization for Long Lists**
Use libraries like `react-window` for rendering large lists efficiently.

```typescript
import { FixedSizeList } from 'react-window';

const TripsList = ({ trips }) => (
  <FixedSizeList
    height={600}
    itemCount={trips.length}
    itemSize={100}
    width="100%"
  >
    {({ index, style }) => (
      <div style={style}>
        <TripCard trip={trips[index]} />
      </div>
    )}
  </FixedSizeList>
);
```

#### **API Caching with RTK Query**
RTK Query handles caching automatically:
```typescript
export const tripsApi = createApi({
  reducerPath: 'tripsApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/api' }),
  tagTypes: ['Trip'],
  endpoints: (builder) => ({
    getTrips: builder.query<Trip[], void>({
      query: () => '/trajets',
      providesTags: ['Trip'],
      // Cache for 60 seconds
      keepUnusedDataFor: 60,
    }),
  }),
});
```

---

### 7.6 Testing Strategy (Recommended)

#### **Unit Tests**
- Test individual components with Jest and React Testing Library
- Test utility functions
- Test custom hooks

#### **Integration Tests**
- Test user flows (login → search → book)
- Test API integration with mock responses

#### **E2E Tests**
- Use Cypress or Playwright for critical user journeys
- Test authentication flow
- Test booking flow

---

### 7.7 Internationalization (i18n) - Future Enhancement

Consider adding French translations using `react-i18next`:

```typescript
import { useTranslation } from 'react-i18next';

const LoginPage = () => {
  const { t } = useTranslation();
  
  return (
    <div>
      <h1>{t('login.title')}</h1>
      <Button>{t('login.submit')}</Button>
    </div>
  );
};
```

---

## 8. Implementation Roadmap

### Phase 1: Foundation (Week 1-2)
- ✅ Set up project with Vite + React + TypeScript
- ✅ Configure Redux Toolkit and RTK Query
- ✅ Set up React Router
- ✅ Configure MUI theme
- ✅ Create base folder structure
- ✅ Set up Axios with interceptors
- ✅ Implement authentication (login/signup)
- ✅ Create layout components (Header, Footer, Sidebar)

### Phase 2: Core Features (Week 3-4)
- ✅ Implement user dashboards (Driver & Passenger)
- ✅ Build trip search and listing
- ✅ Create trip details page
- ✅ Implement booking flow for passengers
- ✅ Build trip creation for drivers
- ✅ Implement reservations management

### Phase 3: Additional Features (Week 5)
- ✅ Vehicle management for drivers
- ✅ Profile management
- ✅ Implement role-based route guards
- ✅ Add form validation
- ✅ Implement error handling and toasts

### Phase 4: Polish & Optimization (Week 6)
- ✅ Responsive design refinement
- ✅ Accessibility improvements
- ✅ Performance optimization
- ✅ Code splitting
- ✅ Testing
- ✅ Documentation
- ✅ Deployment preparation

---

## 9. Deployment Guidelines

### Build for Production
```bash
npm run build
```

### Environment Variables
Create `.env` files for different environments:

**.env.development**
```
VITE_API_BASE_URL=http://localhost:8080
VITE_APP_NAME=Covoiturage Dev
```

**.env.production**
```
VITE_API_BASE_URL=https://api.yourapp.com
VITE_APP_NAME=Covoiturage
```

### Hosting Options
- **Frontend**: Vercel, Netlify, AWS S3 + CloudFront
- **Backend**: AWS EC2, Heroku, Railway, DigitalOcean

### CI/CD
Set up GitHub Actions or GitLab CI for automated testing and deployment.

---

## 10. Additional Recommendations

### Security
- Implement Content Security Policy (CSP)
- Use HTTPS in production
- Sanitize all user inputs
- Implement rate limiting on API (backend)
- Add CAPTCHA for forms (signup/login)

### Monitoring
- Use Sentry for error tracking
- Implement Google Analytics or similar for usage tracking
- Set up performance monitoring (Web Vitals)

### Documentation
- Maintain a component storybook (Storybook.js)
- Document API integration in a shared space
- Create user guides and help sections

---

## 11. Summary

This comprehensive frontend plan provides:

✅ **Modern Technology Stack**: React 18 + TypeScript + Redux Toolkit + MUI  
✅ **Complete Folder Structure**: Industry-standard organization  
✅ **Detailed Page Blueprints**: Every page with UI requirements and API calls  
✅ **Full API Integration Guide**: All endpoints documented with request/response formats  
✅ **Security Best Practices**: JWT handling, route guards, role-based access  
✅ **UX Guidelines**: Responsive design, accessibility, performance optimization  
✅ **Implementation Roadmap**: Phased approach for systematic development  

This plan is **production-ready** and can be handed to a frontend development team for implementation. It covers all aspects of building a robust, secure, and user-friendly carpool application that seamlessly integrates with your Spring Boot backend.

---

**Next Steps:**
1. Review and approve this plan
2. Set up the development environment
3. Initialize the React project with the recommended stack
4. Start Phase 1 implementation
5. Iterate based on user feedback and testing

Good luck with your frontend development! 🚀
