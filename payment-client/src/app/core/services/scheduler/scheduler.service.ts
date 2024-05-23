import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SchedulerService {
  constructor(private httpClient: HttpClient) {}

  createJob(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/scheduler/schedule', payload)
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

  createJobRunNow(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/scheduler/addJobRunNow', payload)
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

  deleteJob(payload): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/scheduler/unschedule', payload)
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

  deleteJobLog(id): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}` + '/scheduler/deleteJobLog/' + id)
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
