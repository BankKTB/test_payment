import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class GenerateJuService {
  constructor(private httpClient: HttpClient) {}
  async generateJu(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/genJu/document', payload)
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

  getJuDocument(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/genJu/getJuDocument/detail', payload)
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
