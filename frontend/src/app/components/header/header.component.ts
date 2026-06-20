import { ToastService } from 'angular-toastify';
import { AuthService } from 'src/app/services/auth.service';
import { environment } from 'src/environment/environment';

import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  private authTokenName = environment.tokenName;
  userName: string = '';
  avatarInitial: string = 'U';

  constructor(
    private authService: AuthService,
    private router: Router,
    private _toastService: ToastService,
    private apiService: ApiService
  ) {}

  ngOnInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        if (this.isLoggedIn() && !this.userName) {
          this.fetchUserName();
        } else if (!this.isLoggedIn()) {
          this.userName = '';
          this.avatarInitial = 'U';
        }
      }
    });
  }

  fetchUserName() {
    this.authService.getUserDetails().subscribe({
      next: (res: any) => {
        if (res && res.name) {
          this.userName = res.name;
          this.avatarInitial = res.name.charAt(0).toUpperCase();
        }
      },
      error: () => {
        // Silently fail if we can't get the user profile
      }
    });
  }

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  checkScreenSize() {
    return window.innerWidth < 768;
  }

  logout(): void {
    this.authService.logOutUser().subscribe({
      next: () => {
        localStorage.removeItem(this.authTokenName);
        this.router.navigate(['/']);
      },
      error: (error: any) => {
        console.error('Logout error:', error);
        localStorage.removeItem(this.authTokenName);
        this.router.navigate(['/']);
        
        // Handle error display safely without showing [object Object]
        const errorMessage = error.error?.error || error.error?.message || 'Failed to fetch user';
        this._toastService.error(errorMessage);
      },
    });
  }
}
