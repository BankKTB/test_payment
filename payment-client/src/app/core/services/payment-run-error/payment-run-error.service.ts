import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PaymentRunErrorService {
  constructor(private httpClient: HttpClient) {}

  findAll(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentRunError/findAll')
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

  findErrorDetailByInvoice(invoiceCompanyCode, invoiceDocumentNo, invoiceFiscalYear): Promise<any> {
    return this.httpClient
      .get(
        `${environment.apiUrl}` +
          '/paymentRunError/findErrorDetailByInvoice/' +
          invoiceCompanyCode +
          '/' +
          invoiceDocumentNo +
          '/' +
          invoiceFiscalYear
      )
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
