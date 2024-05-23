import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LocalStorageService } from '@core/services';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private localstorageService: LocalStorageService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    if (!request.url.includes('getUserPrivileges')) {
      if (this.localstorageService.getJWT()) {
        // console.log(this.localstorageService.getJWT());
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${this.localstorageService.getJWT()}`,
          },
        });
      }
    }
    return next.handle(request);
  }
}
