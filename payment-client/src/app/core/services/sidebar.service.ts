import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';
import { UserProfile } from '@core/models/user-profile';

@Injectable({
  providedIn: 'root',
})
export class SidebarService {
  isSidebarVisible = true;

  userProfile: UserProfile;

  nowPage: string;

  pageType: string;

  isDisplayMobile: boolean;

  sidebarVisibilityChange: Subject<boolean> = new Subject<boolean>();

  sidebarUserProfileChange: Subject<UserProfile> = new Subject<UserProfile>();

  sidebarNowPageChange: Subject<string> = new Subject<string>();

  sidebarPageTypeChange: Subject<string> = new Subject<string>();

  sidebarToggleChange: Subject<boolean> = new Subject<boolean>();

  constructor() {
    this.sidebarVisibilityChange.subscribe((value) => {
      this.isSidebarVisible = value;
    });

    this.sidebarUserProfileChange.subscribe((value) => {
      this.userProfile = value;
    });

    this.sidebarNowPageChange.subscribe((value) => {
      this.nowPage = value;
    });

    this.sidebarPageTypeChange.subscribe((value) => {
      this.pageType = value;
    });

    this.sidebarToggleChange.subscribe((value) => {
      this.isDisplayMobile = value;
    });
  }

  toggleSidebarVisibility(flag) {
    this.sidebarVisibilityChange.next(flag);
  }

  updateUserProfile(userProfile) {
    this.sidebarUserProfileChange.next(userProfile);
  }

  updateNowPage(nowPage) {
    this.sidebarNowPageChange.next(nowPage);
  }

  updatePageType(pageType) {
    this.sidebarPageTypeChange.next(pageType);
  }

  toggleSidebar(isDisplayMobile) {
    this.sidebarToggleChange.next(isDisplayMobile);
  }
}
