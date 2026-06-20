import { AuthService } from 'src/app/services/auth.service';
import { ApiService } from 'src/app/services/api.service';

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-userprofilecard',
  templateUrl: './userprofilecard.component.html',
  styleUrls: ['./userprofilecard.component.css'],
})
export class UserprofilecardComponent implements OnInit {
  userProfileData: any;
  accountDetails: any;

  constructor(
    private authService: AuthService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.getUserProfileData();
    this.getAccountDetails();
  }

  get avatarInitial(): string {
    return this.userProfileData && this.userProfileData.name 
      ? this.userProfileData.name.charAt(0).toUpperCase() 
      : 'U';
  }

  getUserProfileData(): void {
    this.authService.getUserDetails().subscribe({
      next: (data: any) => {
        this.userProfileData = data;
      },
      error: (error: any) => {
        console.error('Error fetching user profile data:', error);
      },
    });
  }

  getAccountDetails(): void {
    this.apiService.getAccountDetails().subscribe({
      next: (data: any) => {
        this.accountDetails = data;
      },
      error: (error: any) => {
        console.error('Error fetching account details:', error);
      },
    });
  }
}
