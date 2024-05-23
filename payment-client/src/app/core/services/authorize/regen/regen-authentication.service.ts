import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class RegenAuthenticationService {
  constructor(private httpClient: HttpClient) {}

  authenticationRegen(payload) {
    console.log('hererer');
    return this.httpClient
      .post(`${environment.apiUrl}/genFile/regen`, payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1)
      )
      .toPromise();
  }
}
