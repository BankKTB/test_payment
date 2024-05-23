import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthorizeService } from './authorize.service';
import { ParentPathService } from '@core/services';

@Injectable()
export class AuthGuardService implements CanActivate {
  parentPathValue = '';

  constructor(
    protected router: Router,
    protected authorizeService: AuthorizeService,
    private parentPathService: ParentPathService
  ) {}

  canActivate(route: ActivatedRouteSnapshot): Promise<boolean> | boolean {
    let auth = route.data.auth;
    console.log(route);
    console.log(auth);
    if (!auth) {
      auth = route.routeConfig.path;
      console.log(auth);
      return this.hasRequiredPermission(auth);
    } else {
      if (this.hasRequiredPermission(auth)) {
        return true;
      } else {
        const needPath = auth.split('.');
        this.parentPathService.parentPath.subscribe((value) => {
          this.parentPathValue = value;
        });
        const parentPath = this.parentPathValue.split('.');
        // จะมาหน้า Create ได้ parent path ต้องมาจาก search เท่านั้น
        if (
          needPath[0] === parentPath[0] &&
          needPath[1] === 'CREATE' &&
          parentPath[1] === 'SEARCH'
        ) {
          return true;
        } else if (
          needPath[0] === parentPath[0] &&
          needPath[1] === 'CREATE' &&
          parentPath[1] === 'EDIT'
        ) {
          return true;
        } else if (
          needPath[0] === parentPath[0] &&
          needPath[1] === 'CREATE' &&
          parentPath[1] === 'CANCEL'
        ) {
          return true;
        } else {
          return false;
        }
        // วิธีไปหาใน array user matrix
        // const matrix = this.authorizeService.getUserMatrix();
        // const create = matrix.find(item => item === path[0] + '.CREATE');
        // const edit = matrix.find(item => item === path[0] + '.EDIT');
        // const search = matrix.find(item => item === path[0] + '.SEARCH');
        // const cancel = matrix.find(item => item === path[0] + '.CANCEL');
        // if (path[1] === 'CREATE') {
        //   if (edit) {
        //     return true;
        //   } else if (search) {
        //     return true;
        //   } else if (cancel) {
        //     return true;
        //   } else {
        //     return false;
        //   }
        // } else if (path[1] === 'EDIT') {
        //   if (search) {
        //     return true;
        //   } else if (cancel) {
        //     return true;
        //   } else {
        //     return false;
        //   }
        // } else if (path[1] === 'SEARCH') {
        //   if (cancel) {
        //     return true;
        //   } else {
        //     return false;
        //   }
        // }
      }
    }
  }

  protected hasRequiredPermission(auth: string): Promise<boolean> | boolean {
    console.log(this.authorizeService.permissions);
    console.log(auth);
    if (this.authorizeService.permissions) {
      if (auth) {
        return this.authorizeService.hasPermission(auth.toUpperCase());
      } else {
        return this.authorizeService.hasPermission(null);
      }
    } else {
      return new Promise<boolean>((resolve, reject) => {
        this.authorizeService
          .initializePermissions()
          .then(() => {
            if (auth) {
              resolve(this.authorizeService.hasPermission(auth.toUpperCase()));
            } else {
              resolve(this.authorizeService.hasPermission(null));
            }
          })
          .catch(() => {
            resolve(false);
          });
      });
    }
  }
}
