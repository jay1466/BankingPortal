import { ToastService } from 'angular-toastify';
import { ICountry } from 'ngx-countries-dropdown';
import { AuthService } from 'src/app/services/auth.service';
import { invalidPhoneNumber } from 'src/app/services/country-code.service';

import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { passwordMismatch, StrongPasswordRegx } from 'src/app/util/formutil';



@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  showRegistrationData = false;
  registrationData: any;
  otpSent = false;

  constructor(
    private authService: AuthService,
    private _toastService: ToastService
  ) { }

  ngOnInit() {
    this.registerForm = new FormGroup(
      {
        name: new FormControl('', Validators.required),
        email: new FormControl('', [Validators.required, Validators.email]),
        countryCode: new FormControl('', Validators.required),
        phoneNumber: new FormControl('', Validators.required),
        city: new FormControl('', Validators.required),
        address: new FormControl('', Validators.required),
        password: new FormControl('', [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(127),
          Validators.pattern(StrongPasswordRegx)
        ]),
        confirmPassword: new FormControl('', Validators.required),
        otp: new FormControl('', [Validators.minLength(6), Validators.maxLength(6)]),
      },
      {
        validators: [
          passwordMismatch('password', 'confirmPassword'),
          invalidPhoneNumber(),
        ],
      }
    );
  }

  // Convenience getter for easy access to form fields
  get f() {
    return this.registerForm.controls;
  }

  onCountryChange(country: ICountry) {
    this.registerForm.patchValue({ countryCode: country.code });
  }

  sendOtp() {
    if (this.registerForm.get('email')?.invalid || this.registerForm.get('name')?.invalid) {
      this._toastService.error("Please fill Name and Email first");
      return;
    }
    
    const email = this.registerForm.get('email')?.value;
    const name = this.registerForm.get('name')?.value;
    
    this.authService.generateRegistrationOtp(email, name).subscribe({
      next: (res: any) => {
         this.otpSent = true;
         this._toastService.success(res.msg || "OTP sent to your email!");
         this.registerForm.get('otp')?.setValidators([Validators.required, Validators.pattern('^[0-9]{6}$')]);
         this.registerForm.get('otp')?.updateValueAndValidity();
      },
      error: (error: any) => {
         console.error('Failed to send OTP:', error);
         const errorMessage = error.error?.error || error.error?.message || 'Failed to send OTP';
         this._toastService.error(errorMessage);
      }
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      return;
    }
    
    if (!this.otpSent) {
      this.sendOtp();
      return;
    }
    
    const otp = this.registerForm.get('otp')?.value;

    console.log(this.registerForm.value);
    // Call the API service to register the user
    this.authService.registerUser(this.registerForm.value, otp).subscribe({
      next: (response: any) => {
        // Store the registration data and show it on the page
        this.registrationData = response;
        this.showRegistrationData = true;
      },
      error: (error: any) => {
        console.error('Registration failed:', error);
        const errorMessage = error.error?.error || error.error?.message || 'Registration failed';
        this._toastService.error(errorMessage);
      },
    });
  }
}
