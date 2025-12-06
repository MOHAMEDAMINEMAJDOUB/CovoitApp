import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { SignupRequest } from '../../../../core/models/user.model';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;
  loading = signal(false);
  errorMessage = signal('');
  successMessage = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.signupForm = this.fb.group({
      userType: ['PASSENGER', Validators.required],
      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telephone: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required],
      termsAccepted: [false, Validators.requiredTrue]
    }, { validators: this.passwordMatchValidator });
  }


  get nom() { return this.signupForm.get('nom')!; }
  get prenom() { return this.signupForm.get('prenom')!; }
  get email() { return this.signupForm.get('email')!; }
  get telephone() { return this.signupForm.get('telephone')!; }
  get password() { return this.signupForm.get('password')!; }
  get confirmPassword() { return this.signupForm.get('confirmPassword')!; }
  get termsAccepted() { return this.signupForm.get('termsAccepted')!; }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit(): void {
    if (this.signupForm.valid) {
      this.loading.set(true);
      this.errorMessage.set('');
      this.successMessage.set('');

      const signupData: SignupRequest = {
        nom: this.signupForm.value.nom,
        prenom: this.signupForm.value.prenom,
        email: this.signupForm.value.email,
        telephone: this.signupForm.value.telephone,
        password: this.signupForm.value.password,
        userType: this.signupForm.value.userType
      };

      this.authService.signup(signupData).subscribe({
        next: () => {
          this.loading.set(false);
          this.successMessage.set('Account created! Redirecting...');
          setTimeout(() => {
            this.router.navigate(['/auth/login']);
          }, 2000);
        },
        error: (error: any) => {
          this.loading.set(false);
          const message = error.error?.message || 'Signup failed. Please try again.';
          this.errorMessage.set(message);
          console.error('Signup error:', error);
        }
      });
    }
  }
}
