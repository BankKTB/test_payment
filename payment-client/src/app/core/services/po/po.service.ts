import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PoService {
  constructor(private httpClient: HttpClient) {}

  searchDetail(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/purchasingOrder/searchDetail', payload)
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

  history(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/purchasingOrder/history', payload)
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

  changeHistory(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/purchasingOrder/changeHistory', payload)
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
