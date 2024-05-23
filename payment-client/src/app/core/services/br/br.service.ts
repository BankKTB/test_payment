import { catchError, map, take } from 'rxjs/operators';
import { environment } from '@env/environment';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class BrService {
  constructor(private httpClient: HttpClient) {
  }

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/create', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  change(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/change', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  search(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/search', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  searchDetail(payload) {
    console.log('SSSSSSSSSSSSSSSSS', payload);
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/searchDetail', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  consumption(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/consumption', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  close(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/close', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  selectList(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/selectList', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  selection(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/selection', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  cancelList(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/cancelList', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  cancel(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/cancel', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  approveList(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/approveList', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }

  approve(payload) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/budgetReserve/approve', payload)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          return of(err);
        }),
        take(1),
      )
      .toPromise();
  }
}
