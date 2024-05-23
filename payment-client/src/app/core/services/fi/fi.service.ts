import { catchError, map, take } from 'rxjs/operators';
import { environment } from '@env/environment';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class FiService {
  constructor(private httpClient: HttpClient) {}

  searchDetail(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/searchDetail', payload)
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
  searchAutoDoc(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/searchAutoDoc', payload)
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

  paymentBlockDetail(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/paymentBlockDetail', payload)
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

  updateLineVendor(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/updateLineVendor', payload)
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
  validateUpdateLineVendor(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/validateUpdateLineVendor', payload)
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

  searchRef(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/search', payload)
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
