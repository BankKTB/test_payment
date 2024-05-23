import { Injectable } from '@angular/core';
import { saveAs } from 'file-saver';
import { HttpClient } from '@angular/common/http';
import { environment } from '@env/environment';
import { catchError, map, take } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  constructor(private httpClient: HttpClient) {}

  reportTail(payload) {
    return this.httpClient
      .post('/report/tail', payload, {
        responseType: 'arraybuffer',
      })
      .subscribe((res) => {
        if (payload.format === 'pdf') {
          const blob = new Blob([res], { type: 'application/pdf' });
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL);
          // saveAs(blob, new Date().getTime() + '.pdf');
        } else if (payload.format === 'xls') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.xls');
        } else if (payload.format === 'doc') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.doc');
        }
      });
  }

  reportLog(payload) {
    return this.httpClient
      .post('/report/log', payload, {
        responseType: 'arraybuffer',
      })
      .subscribe((res) => {
        if (payload.format === 'pdf') {
          const blob = new Blob([res], { type: 'application/pdf' });
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL);
          // saveAs(blob, new Date().getTime() + '.pdf');
        } else if (payload.format === 'xls') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.xls');
        } else if (payload.format === 'doc') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.doc');
        }
      });
  }

  reportSng(payload) {
    return this.httpClient
      .post('/report/sng', payload, {
        responseType: 'arraybuffer',
      })
      .subscribe((res) => {
        if (payload.format === 'pdf') {
          const blob = new Blob([res], { type: 'application/pdf' });
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL);
          // saveAs(blob, new Date().getTime() + '.pdf');
        } else if (payload.format === 'xls') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.xls');
        } else if (payload.format === 'doc') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.doc');
        }
      });
  }

  reportPo(payload) {
    return this.httpClient
      .post('/report/po', payload, {
        responseType: 'arraybuffer',
      })
      .subscribe((res) => {
        if (payload.format === 'pdf') {
          const blob = new Blob([res], { type: 'application/pdf' });
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL);
          // saveAs(blob, new Date().getTime() + '.pdf');
        } else if (payload.format === 'xls') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.xls');
        } else if (payload.format === 'doc') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.doc');
        }
      });
  }

  reportMr(payload) {
    return this.httpClient
      .post('/report/mr', payload, {
        responseType: 'arraybuffer',
      })
      .subscribe((res) => {
        if (payload.format === 'pdf') {
          const blob = new Blob([res], { type: 'application/pdf' });
          const fileURL = URL.createObjectURL(blob);
          window.open(fileURL);
          // saveAs(blob, new Date().getTime() + '.pdf');
        } else if (payload.format === 'xls') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.xls');
        } else if (payload.format === 'doc') {
          const blob = new Blob([res], { type: 'application/octet-stream' });
          saveAs(blob, new Date().getTime() + '.doc');
        }
      });
  }

  reportDuplicatePayment(request: any) {
    return this.httpClient
      .post(`${environment.apiUrl}` + '/paymentReport/duplicatePayment', request)
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
