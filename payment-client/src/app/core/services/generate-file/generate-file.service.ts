import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class GenerateFileService {
  constructor(private httpClient: HttpClient) {}

  async create(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/genFile/save', payload)
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
      .delete(`${environment.apiUrl}` + '/genFile/delete/' + id)
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

  search(date, name): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/genFile/search/' + date + '/' + name)
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

  generateFormat(id, payload): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/genFile/' + id, payload)
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

  generateFormatPac(id, payload): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}` + '/genFile/pac/' + id, payload)
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

  searchAllParameter(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/genFile/getAll/')
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
      .get(`${environment.apiUrl}` + '/genFile/getAll/' + key)
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

  searchByReturn(key): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/genFile/getReturn/' + key)
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

  getLog(logId: string): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/genFile/report/' + logId)
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

  getDocumentJu(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}` + '/genFile/generate/document/ju/')
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
