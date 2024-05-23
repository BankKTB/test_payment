import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { SearchBatchRequest } from '@core/models/batch-job/search-batch-request';
import { CreateBatchJobRequest } from '@core/models/batch-job/create-batch-job-request';

@Injectable({
  providedIn: 'root',
})
export class BatchJobService {
  constructor(private httpClient: HttpClient) {}

  searchAll(): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/scheduler/getAll`)
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

  searchAllWithCondition(request: SearchBatchRequest): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}/scheduler/getAllByCondition`, request)
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

  startJob(id: string) {
    return this.httpClient
      .patch(`${environment.apiUrl}/scheduler/run/${id}`, {})
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

  pauseJob(id: string) {
    return this.httpClient
      .patch(`${environment.apiUrl}/scheduler/pause/${id}`, {})
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

  stopJob(id: string) {
    return this.httpClient
      .patch(`${environment.apiUrl}/scheduler/stop/${id}`, {})
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

  deleteJob(id: string) {
    return this.httpClient
      .delete(`${environment.apiUrl}/scheduler/delete/${id}`)
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

  addJob(request: CreateBatchJobRequest) {
    return this.httpClient
      .post(`${environment.apiUrl}/scheduler/add/`, request)
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

  findCreateJobByCondition(value: string) {
    return this.httpClient
      .get(`${environment.apiUrl}/scheduler/findCreateJobByCondition/${value}`)
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
  findCreateJobByDateAndName(paymentDate: string, paymentName: string) {
    return this.httpClient
      .get(`${environment.apiUrl}/scheduler/findCreateJobByCondition/${paymentDate}/${paymentName}`)
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
  findSearchJobByCondition(value: string) {
    return this.httpClient
      .get(`${environment.apiUrl}/scheduler/findSearchJobByCondition/${value}`)
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
