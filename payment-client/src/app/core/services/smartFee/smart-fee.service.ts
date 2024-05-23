import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SmartFeeService {
  constructor(private httpClient: HttpClient) {}

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/smartFee/save', payload)
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

  update(payload, id): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/smartFee/update/' + id, payload)
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

  searchAll(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/smartFee/getAll')
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

  delete(id): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}` + '/smartFee/delete/' + id)
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

  searchByID(id): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/smartFee/findBy/' + id)
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
