import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, tap, catchError, take } from 'rxjs/operators';
import { environment } from '@env/environment';
let httpOptions = {};
@Injectable({
  providedIn: 'root',
})
export class IamService {
  constructor(private http: HttpClient) {
    httpOptions = {
      headers: new HttpHeaders({
        'content-type': 'application/json;charset=UTF-8',
      }),
    };
  }

  getUserprofile(token: string): Promise<any> {
    const url = `${environment.callUserProfile}`;
    console.log(url);
    const payload = {
      token
    };
    return this.http
      .post(url, payload)
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

  getToken(authCode: string): Promise<any> {
    const url = `${environment.callUserAuthCode}`;
    console.log(url);
    const payload = {
      authCode: authCode,
    };
    return this.http
      .post(url, payload)
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

  getUserMatrix(token: string, user: string): Promise<any> {
    const header = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const payload = {
      system: 'CENTRAL_PAYMENT',
      usercode: user,
    };
    return this.http
      .post(`${environment.callUserMatrix}`, payload, { headers: header })
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

  // getUserMatrix(token: string, user: string): Observable<any> {
  //   const url = `${environment.callUserMatrix}`;
  //   console.log(url);
  //   const header = new HttpHeaders().set(
  //     'Authorization',
  //     `Bearer ${token}`
  //   );
  //   const payload = {
  //     system: 'AGENCY',
  //     usercode: user
  //   }
  //   return this.http.post<any>(url, payload, {headers: header}).pipe(
  //     map(res => {
  //       return res;
  //     }),
  //     tap(_ => console.log('getUserMatrix')),
  //     catchError(this.handleError('getUserMatrix', null))
  //   );
  // }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}
