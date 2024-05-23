import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentAliasService {
  constructor(private httpClient: HttpClient) {}

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentAlias/save', payload)
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
      .put(`${environment.apiUrl}` + '/paymentAlias/update/' + id, payload)
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

  deleteParameter(payload, id): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/paymentAlias/deleteParameter/' + id, payload)
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

  updateSchedule(payload, id, type): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/paymentAlias/update/schedule/' + id + '/' + type, payload)
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

  search(date, name): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentAlias/search/' + date + '/' + name)
      .pipe(
        map((data) => {
          ////console.log(data);
          return data;
        }),
        catchError((err) => {
          ////console.log(err);
          return of(err);
        }),
        take(1)
      )
      .toPromise();
  }

  searchAllParameter(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentAlias/getAll')
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

  searchByValue(key): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentAlias/findByCondition/' + key)
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
  searchJobByValue(key): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentAlias/findJobByCondition/' + key)
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
      .delete(`${environment.apiUrl}` + '/paymentAlias/delete/' + id)
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

  checkValidate(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentAlias/validate', payload)
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

  deletePaymentDocumentAll(id): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}` + '/paymentAlias/deletePaymentDocumentAll/' + id)
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
