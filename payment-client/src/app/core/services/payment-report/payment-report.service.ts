import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentReportService {
  constructor(private httpClient: HttpClient) {}

  findVendorReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/vendor/' + id + '/' + type)
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

  findErrorReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/error/' + id + '/' + type)
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
  findErrorDetailByPaymentAliasId(id): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentRunError/findErrorDetailByPaymentAliasId/' + id)
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

  findAreaWithPaymentMethodReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/areaWithPaymentMethod/' + id + '/' + type)
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

  findCountryWithPaymentMethodReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/countryWithPaymentMethod/' + id + '/' + type)
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

  findCurrencyWithPaymentMethodReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/currencyWithPaymentMethod/' + id + '/' + type)
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

  findPaymentMethodReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/paymentMethod/' + id + '/' + type)
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

  findBankReportByAlias(id, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentReport/bank/' + id + '/' + type)
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

  findOneVendor(item: {paymentAliasId: string, vendor: string, backAccount: string}, type, page, size): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/paymentReport/search/${item.paymentAliasId}/${item.vendor}/${item.backAccount}/${type}/${page}/${size}`)
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

  findPaymentReportCompanyCode(paymentAliasId: string) {
    return this.httpClient
      .get(`${environment.apiUrl}/paymentReport/compCode/${paymentAliasId}`)
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
