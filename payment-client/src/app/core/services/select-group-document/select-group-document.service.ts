import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SelectGroupDocumentService {
  constructor(private httpClient: HttpClient) {}

  create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/selectGroupDocument/save', payload)
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
      .put(`${environment.apiUrl}` + '/selectGroupDocument/update/' + id, payload)
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

  preview(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/selectGroupDocument/preview', payload)
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
