import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PayMethodConfigService {
  constructor(private httpClient: HttpClient) {}

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payMethodConfig/save', payload)
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

  copy(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payMethodConfig/copy', payload)
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

  update(payload, id): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/payMethodConfig/update/' + id, payload)
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

  searchAllParameter(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/payMethodConfig/getAll')
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
      .delete(`${environment.apiUrl}` + '/payMethodConfig/delete/' + id)
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

  searchByID(id): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/payMethodConfig/findBy/' + id)
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
