import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProposalTr1Service {
  constructor(private httpClient: HttpClient) {}

  getSummaryTR1(request: any) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/proposalLogTR1/getSummaryTR1', request)
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
