import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';
import { ArrangeReques } from '@core/models/arrange/arrange-reques';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class ArrangeService {
  constructor(private httpClient: HttpClient, private snackBar: MatSnackBar) {}

  save(body: ArrangeReques): Promise<any> {
    return this.httpClient
      .post(`${environment.apiUrl}/arrange/save`, body)
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

  update(body: ArrangeReques, id: any): Promise<any> {
    return this.httpClient
      .put(`${environment.apiUrl}/arrange/edit/${id}`, body)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        }),
        take(1)
      )
      .toPromise();
  }

  delete(id: any): Promise<any> {
    return this.httpClient
      .delete(`${environment.apiUrl}/arrange/delete/${id}`)
      .pipe(
        map((data) => {
          return data;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        }),
        take(1)
      )
      .toPromise();
  }

  findByArrangeCode(arrageCode): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/arrange/findByArrangeCode/${arrageCode}`)
      .pipe(
        map((response) => {
          return response;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        })
        // take(1)
      )
      .toPromise();
  }

  findByArrangeCodeLastRow(arrageCode): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/arrange/findByArrangeCodeDefaultArrange/${arrageCode}`)
      .pipe(
        map((response) => {
          return response;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        })
        // take(1)
      )
      .toPromise();
  }

  findByArrangeCodeAndArrangeName(arrangeCode, arrangeName): Promise<any> {
    return this.httpClient
      .get(
        `${environment.apiUrl}/arrange/findByArrangeCodeAndArrangeName/${arrangeCode}/${arrangeName}`
      )
      .pipe(
        map((response) => {
          return response;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        })
        // take(1)
      )
      .toPromise();
  }

  findByArrangeId(arrangeId): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/arrange/findByArrange/${arrangeId}`)
      .pipe(
        map((response) => {
          return response;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        })
        // take(1)
      )
      .toPromise();
  }

  editDefaultArrange(arrangeId): Promise<any> {
    return this.httpClient
      .get(`${environment.apiUrl}/arrange/editDefaultArrange/${arrangeId}`)
      .pipe(
        map((response) => {
          return response;
        }),
        catchError((err) => {
          this.snackBar.open('Something went wrong. Please type again', 'close', {
            duration: 5000,
            panelClass: ['error-snackbar'],
          });
          return of(err);
        })
        // take(1)
      )
      .toPromise();
  }
}
