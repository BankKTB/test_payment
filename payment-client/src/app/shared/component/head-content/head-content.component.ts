import { UserProfile } from '@core/models/user-profile';
import { Router } from '@angular/router';
import { LocalStorageService } from '@core/services';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-head-content',
  templateUrl: './head-content.component.html',
  styleUrls: ['./head-content.component.scss'],
})
export class HeadContentComponent implements OnInit {
  userProfile: UserProfile;

  constructor(private router: Router, private localStorageService: LocalStorageService) {}

  ngOnInit() {
    this.userProfile = this.localStorageService.getUserProfile();
    console.log('userProfile ', this.userProfile);

    if (this.userProfile === null && this.userProfile === undefined) {
      // this.router.navigate(['/']);
    }
  }
}
