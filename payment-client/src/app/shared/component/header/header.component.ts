import { Component, OnInit } from '@angular/core';
import { SidebarService } from '@core/services/sidebar.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  menuClassMobile = '';
  isDisplayMenu;
  menuClassActive = '';

  constructor(private sidebarService: SidebarService, private router: Router) {}

  ngOnInit() {
    this.sidebarService.sidebarToggleChange.subscribe((value) => {
      this.isDisplayMenu = value;
      this.menuClassActive = value ? 'active' : '';
    });
  }

  toggleMenu() {
    this.sidebarService.toggleSidebar(!this.isDisplayMenu);
    this.menuClassMobile = this.menuClassMobile === 'menu_opened' ? '' : 'menu_opened';
  }

  mainPage() {
    this.router.navigate(['/menu']);
  }
}
