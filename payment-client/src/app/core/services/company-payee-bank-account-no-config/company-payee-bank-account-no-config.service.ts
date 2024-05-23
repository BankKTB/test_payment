import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class CompanyPayeeBankAccountNoConfigService {
  constructor(private httpClient: HttpClient) {}

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/companyPayeeBankAccountNoConfig/save', payload)
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
      .put(`${environment.apiUrl}` + '/companyPayeeBankAccountNoConfig/update/' + id, payload)
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

  searchAllParameter(id): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/companyPayeeBankAccountNoConfig/getAll/' + id)
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
      .delete(`${environment.apiUrl}` + '/companyPayeeBankAccountNoConfig/delete/' + id)
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
      .get(`${environment.apiUrl}` + '/companyPayeeBankAccountNoConfig/findBy/' + id)
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
