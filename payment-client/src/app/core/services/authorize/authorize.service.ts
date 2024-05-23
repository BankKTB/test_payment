import { Injectable } from '@angular/core';
import { LocalStorageService } from '@core/services/local-storage.service';

@Injectable()
export class AuthorizeService {
  permissions: Array<string> = null;

  constructor(private localStorageService: LocalStorageService) {}

  hasPermission(auth: string) {
    if (auth.includes('|')) {
      let grant = false;
      auth.split('|').forEach((a) => {
        if (
          this.permissions &&
          this.permissions.find((permission) => {
            return permission === a;
          })
        ) {
          grant = true;
          return;
        }
      });
      return grant;
    } else {
      return !!(
        this.permissions &&
        this.permissions.find((permission) => {
          return permission === auth;
        })
      );
    }
  }

  initializePermissions() {
    return new Promise((resolve, reject) => {
      const userMatrix = this.localStorageService.getUserMatrix();
      if (userMatrix.privilege.screen) {
        this.permissions = ['MENU'];
      }
      console.log(userMatrix.privilege.screen);
      userMatrix.privilege.screen.forEach((screen, i) => {
        this.permissions.push(screen.pageWeb);
      });
      console.log(this.permissions);
      resolve();
    });
  }
}
