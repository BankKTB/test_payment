import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentBlockService {
  constructor(private httpClient: HttpClient) {}

  search(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/search', payload)
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

  changePaymentBlock(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/changePaymentBlock', payload)
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
  pullChangePaymentBlock(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/pullChangePaymentBlock', payload)
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

  pullChangePaymentBlockGET(uuid: string): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentBlock/pullChangePaymentBlock/' + uuid)
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

  findParent(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/findParent', payload)
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

  // searchDetail(companyCode, docNo, year): Promise<any> {
  //   return this.httpClient
  //     .get(
  //       `${environment.apiUrl}` +
  //         '/paymentBlock/searchDetail/' +
  //         companyCode +
  //         '/' +
  //         docNo +
  //         '/' +
  //         year
  //     )
  //     .pipe(
  //       map((data) => {
  //         return data;
  //       }),
  //       catchError((err) => {
  //         return of(err);
  //       }),
  //       take(1)
  //     )
  //     .toPromise();
  // }

  createCriteria(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/save/search/criteria', payload)
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
  updateCriteria(payload): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/paymentBlock/update/search/criteria', payload)
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
  deleteCriteriaById(criteriaId): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}` + '/paymentBlock/delete/criteria/' + criteriaId)
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

  searchCriteriaById(criteriaId): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentBlock/search/criteria/' + criteriaId)
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

  searchCriteriaAllByRole(role, user, value): Promise<any> {
    return this.httpClient
      .get(
        `${environment.apiUrl}` +
          '/paymentBlock/search/criteriaAll/' +
          role +
          '/' +
          user +
          '/' +
          value
      )
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

  createColumn(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/save/column', payload)
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

  searchColumnAllByRole(role, user, value): Promise<any> {
    return this.httpClient
      .get(
        `${environment.apiUrl}` +
          '/paymentBlock/search/columnAll/' +
          role +
          '/' +
          user +
          '/' +
          value
      )
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

  searchColumnByRoleAndName(role, name): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentBlock/search/column/' + role + '/' + name)
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
  updateColumn(payload): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/paymentBlock/update/column', payload)
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
  deleteColumnById(criteriaId): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}` + '/paymentBlock/delete/column/' + criteriaId)
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

  getPaymentBlockLog(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/log', payload)
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

  report(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/report', payload)
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

  findLogDetail(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentBlock/findLogDetail', payload)
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

  searchPaymentBlockLogDetail(originalDoc): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/paymentBlock/searchPaymentBlockLogDetail/' + originalDoc)
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
