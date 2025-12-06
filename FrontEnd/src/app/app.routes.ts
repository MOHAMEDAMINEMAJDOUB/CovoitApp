import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
    },
    {
        path: 'auth',
        loadChildren: () => import('./features/auth/auth.routes').then(m => m.authRoutes)
    },
    {
        path: 'dashboard',
        canActivate: [authGuard],
        loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent)
    },
    {
        path: 'my-vehicles',
        canActivate: [authGuard],
        loadComponent: () => import('./features/vehicles/vehicle-management.component').then(m => m.VehicleManagementComponent)
    },
    {
        path: 'my-reservations',
        canActivate: [authGuard],
        loadComponent: () => import('./features/reservations/components/my-reservations/my-reservations.component').then(m => m.MyReservationsComponent)
    },
    {
        path: 'trips',
        loadComponent: () => import('./features/trips/components/trips-list/trips-list.component').then(m => m.TripsListComponent)
    },
    {
        path: 'trips/create',
        canActivate: [authGuard],
        loadComponent: () => import('./features/trips/components/create-trip.component').then(m => m.CreateTripComponent)
    },
    {
        path: 'trips/my-trips',
        canActivate: [authGuard],
        loadComponent: () => import('./features/trips/components/my-trips/my-trips.component').then(m => m.MyTripsComponent)
    },
    {
        path: 'trips/edit/:id',
        canActivate: [authGuard],
        loadComponent: () => import('./features/trips/components/create-trip.component').then(m => m.CreateTripComponent)
    },
    {
        path: '**',
        redirectTo: '/home'
    }
];
