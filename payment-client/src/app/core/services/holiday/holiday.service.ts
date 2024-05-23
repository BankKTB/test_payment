import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { HolidayRequest } from '@core/models/holiday-request';

@Injectable({
  providedIn: 'root',
})
export class HolidayService {
  constructor(private httpClient: HttpClient) {}

  searchAll(fiscalYear?: string, date?: string): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}/masterIdem/searchHoliday`, { fiscalYear, date })
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

  create(r: HolidayRequest): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}/masterIdem/createHoliday`, r)
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

  update(r: HolidayRequest): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}/masterIdem/updateHoliday`, r)
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
