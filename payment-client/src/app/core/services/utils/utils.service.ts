import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UtilsService {
  constructor(private httpClient: HttpClient) {}

  reportHeaderData(payload, url) {
    return this.httpClient
      .post(`${environment.apiUrl}` + url, payload)
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

  exportExcelHeaderData(payload, url) {
    return this.httpClient
      .post(`${environment.apiUrl}` + url, payload)
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

  exportExcelHeaderZipfile(payload, url) {
    return this.httpClient
      .post(`${environment.apiUrl}` + url, payload, { responseType: 'arraybuffer' })
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

  exportExcelHeaderSaver(payload, url) {
    return this.httpClient
      .post(`${environment.apiUrl}` + url, payload)
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
