import { AfterViewInit, Component, OnInit } from '@angular/core';
import { SidebarService } from '@core/services/sidebar.service';
import { LocalStorageService } from '@core/services';
import { UserProfile } from '@core/models/user-profile';
import { WebInfo } from '@core/models/web-info';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { BrService } from '@core/services/br/br.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit, AfterViewInit {
  userProfile: UserProfile;
  private webInfo: WebInfo;

  constructor(
    private localStorageService: LocalStorageService,
    private sidebarService: SidebarService,
    private brService: BrService
  ) {}

  ngAfterViewInit(): void {
    // setTimeout(() => {
    //   this.globalObject.menuPage = 'menu';
    //   this.globalObject.nowpage = 'menu';
    // }, 100);
    // this.globalObject.isShowMenu = true;
  }

  ngOnInit() {
    this.userProfile = this.localStorageService.getUserProfile();
    this.webInfo = this.localStorageService.getWebInfo();

    this.sidebarService.updatePageType('');
    this.sidebarService.updateNowPage('');
  }
}
