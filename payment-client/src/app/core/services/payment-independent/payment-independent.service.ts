import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentIndependentService {
  constructor(private httpClient: HttpClient) {}

  search(value): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentIndependentConfig/searchGroup/' + value)
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1)
      )
      .toPromise();
  }

  searchStandard(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentIndependentConfig/searchStandard')
      .pipe(
        map((data) => {
          console.log(data);
          return data;
        }),
        catchError((err) => {
          console.log(err);
          return of(err);
        }),
        take(1)
      )
      .toPromise();
  }
}
