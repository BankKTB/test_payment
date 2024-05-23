import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError } from 'rxjs/operators';

@Injectable()
export class JwtErrorInterceptor implements HttpInterceptor {
  constructor(private _snackBar: MatSnackBar) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((err) => {
        if (err.status === 9999) {
          this._snackBar.open('ท่านไม่ได้ทำรายการในเวลาที่กำหนด กรุณาเข้าสู่ระบบใหม่อีกครั้ง', '', {
            panelClass: '_warning',
          });
          return throwError(err.error.message);
        }
        if (err.status === 401) {
          if (confirm('กรุณาเข้าสู่ระบบใหม่อีกครั้ง')) {
            window.close();
            return throwError(err);
          }
        }

        return throwError(err);
      })
    );
  }
}
