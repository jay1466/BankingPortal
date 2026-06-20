import { ToastService } from 'angular-toastify';
import { ICountry } from 'ngx-countries-dropdown';
import { AuthService } from 'src/app/services/auth.service';
import { invalidPhoneNumber } from 'src/app/services/country-code.service';

import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit {
  userProfile: any;
  profileForm!: FormGroup;
  passwordForm!: FormGroup;
  showUpdateForm: boolean = false;

  constructor(
    private authService: AuthService,
    private _toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.getUserProfileData();
    this.profileForm = new FormGroup(
      {
        name: new FormControl('', Validators.required),
        email: new FormControl('', [Validators.required, Validators.email]),
        countryCode: new FormControl('', Validators.required),
        phoneNumber: new FormControl('', Validators.required),
        address: new FormControl('', Validators.required),
        password: new FormControl('', [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(127),
        ]),
      },
      {
        validators: invalidPhoneNumber(),
      }
    );

    this.passwordForm = new FormGroup({
      oldPassword: new FormControl('', Validators.required),
      newPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(127),
      ]),
      confirmPassword: new FormControl('', Validators.required),
    });
  }

  // Convenience getter for easy access to form fields
  get f() {
    return this.profileForm.controls;
  }

  onCountryChange(country: ICountry) {
    this.profileForm.patchValue({ countryCode: country.code });
  }

  getUserProfileData(): void {
    this.authService.getUserDetails().subscribe({
      next: (data: any) => {
        this.userProfile = data;

        this.profileForm.patchValue(data);
      },
      error: (error: any) => {
        console.error('Error fetching user profile data:', error);
      },
    });
  }

  toggleUpdateForm(): void {
    this.profileForm.patchValue(this.userProfile);
    this.showUpdateForm = !this.showUpdateForm;
  }

  updateProfile(): void {
    console.log(this.profileForm.value);

    this.authService.updateUserProfile(this.profileForm.value).subscribe({
      next: (data: any) => {
        this.userProfile = data;
        console.log('Profile updated successfully:', data);
        this.showUpdateForm = false;
      },
      error: (error: any) => {
        const errorMessage = error.error?.error || error.error?.message || 'Failed to load profile';
        this._toastService.error(errorMessage);
        console.error('Error updating user profile:', error);
      },
    });
  }

  changePassword(): void {
    if (this.passwordForm.invalid) return;

    if (this.passwordForm.value.newPassword !== this.passwordForm.value.confirmPassword) {
      this._toastService.error("Passwords do not match");
      return;
    }

    const payload = {
      oldPassword: this.passwordForm.value.oldPassword,
      newPassword: this.passwordForm.value.newPassword
    };

    this.authService.changePassword(payload).subscribe({
      next: (res: any) => {
        this._toastService.success("Password changed successfully!");
        this.passwordForm.reset();
      },
      error: (error: any) => {
        const errorMessage = error.error?.error || error.error?.message || 'Failed to change password';
        this._toastService.error(errorMessage);
      }
    });
  }
}
