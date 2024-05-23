import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class CompanyPayingPayMethodConfigService {
  constructor(private httpClient: HttpClient) {}

  update(payload, id): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/companyPayingPayMethodConfig/update/' + id, payload)
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
      .get(`${environment.apiUrl}` + '/companyPayingPayMethodConfig/findBy/' + id)
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

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/companyPayingPayMethodConfig/save', payload)
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

  searchAllParameter(companyPayingId): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/companyPayingPayMethodConfig/getAll/' + companyPayingId)
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
      .delete(`${environment.apiUrl}` + '/companyPayingPayMethodConfig/delete/' + id)
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
