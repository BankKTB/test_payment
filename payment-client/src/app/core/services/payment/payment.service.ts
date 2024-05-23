import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  constructor(private httpClient: HttpClient) {}

  createProposal(id, webInfo): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/proposal/run/' + id, webInfo)
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

  real(id, webInfo): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/real/run/' + id, webInfo)
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

  searchProposalDocument(id, webInfo): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/proposal/document/' + id, webInfo)
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

  blockDocument(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/block/document', payload)
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

  reversePayment(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/reverse/paymentDocument', payload)
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
      .post(`${environment.apiUrl}` + '/payment/reverse/invoiceDocument', payload)
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

  repairDocument(paymentProcessId, webInfo): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/repair/document/' + paymentProcessId, webInfo)
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

  reversePaymentAll(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/reversePaymentAll', payload)
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

  pullReversePaymentDocument(paymentAliasId, webInfo): Promise<any> {
    return this.httpClient
      .post(
        `${environment.apiUrl}` + '/payment/pullReversePaymentDocument/' + paymentAliasId,
        webInfo
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

  massChangeDocument(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/mass/changeDocument', object)
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
  massReverseDocument(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/mass/reverseDocument', object)
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
  pullNewMassReverseDocument(uuid): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/payment/mass/pullMassReverseDocument/' + uuid)
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
  pullMassReverseDocument(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/mass/pullMassReverseDocument', object)
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
  pullMassStep4ReverseDocument(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/mass/pullMassStep4ReverseDocument', object)
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
  pullMassChangeDocument(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/mass/pullMassChangeDocument', object)
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
  pullNewMassChangeDocument(uuid): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/payment/mass/pullMassChangeDocument/' + uuid)
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
  changeDocument(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/changeDocument', object)
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
  checkOriginalDocument(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/financial/checkOriginalDocument', payload)
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

  validateMarkComplete(object): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/payment/validateMarkComplete', object)
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
