import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class ReturnService {
  constructor(private httpClient: HttpClient) {}

  getReturnPropLogList(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/getPropLogReturn/', payload)
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

  saveFileStatusReturn(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/saveStatus/', payload)
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

  getPropLogReverseDocPayment(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/getPropLogReverseDocPayment/', payload)
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

  saveReversePayment(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/reverse/payment/', payload)
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

  getPropLogReverseDocInvoice(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/getPropLogReverseDocInvoice/', payload)
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

  reverseInvoice(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/reverse/invoice/', payload)
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

  getAutoUpdateFileReturn(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/return/autoUpdateFile', payload)
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
