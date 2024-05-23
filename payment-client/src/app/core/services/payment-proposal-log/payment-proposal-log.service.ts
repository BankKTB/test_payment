import { Injectable } from '@angular/core';

import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentProposalLogService {
  constructor(private httpClient: HttpClient) {}

  findProposalLogByPaymentAliasId(paymentAliasId, type): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentProposalLog/search/' + paymentAliasId + '/' + type)
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

  findProposalLogByPaymentAliasIdPagination(paymentAliasId, type, page: number, size: number): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/paymentProposalLog/search/${paymentAliasId}/${type}/${page}/${size}`)
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

  findRealRunLogByPaymentAliasId(paymentAliasId): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentProposalLog/searchRealRun/' + paymentAliasId)
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

  findRealRunLogByPaymentAliasIdPagination(paymentAliasId, type, page: number, size: number): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/paymentProposalLog/searchRealRun/${paymentAliasId}/${type}/${page}/${size}`)
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
