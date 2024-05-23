import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PayMethodCurrencyConfigService {
  constructor(private httpClient: HttpClient) {}

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payMethodCurrencyConfig/save', payload)
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

  delete(id): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}` + '/payMethodCurrencyConfig/deleteById/' + id)
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

  getAllByID(id): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/payMethodCurrencyConfig/getAllByPayMethodConfigId/' + id)
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
