import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { LocalStorageService } from './local-storage.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebInfoService {
  constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService) {}

  public getIPAddress(): Observable<any> {
    return this.httpClient.get('http://api.ipify.org/?format=json').pipe(
      map((data) => {
        console.log(data);
        return data;
      })
    );
  }
}
