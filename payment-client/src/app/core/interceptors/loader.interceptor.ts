import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { LoaderService } from '@core/services/loader/loader.service';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class LoaderInterceptor implements HttpInterceptor {
  private requests: HttpRequest<any>[] = [];

  constructor(private loaderService: LoaderService) {}

  removeRequest(req: HttpRequest<any>) {
    const i = this.requests.indexOf(req);
    if (i >= 0) {
      this.requests.splice(i, 1);
    }
    this.loaderService.loadingToggleStatus(this.requests.length > 0);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.requests.push(req);
    this.loaderService.loadingToggleStatus(true);
    return Observable.create((observer) => {
      const subscription = next.handle(req).subscribe(
        (event) => {
          if (event instanceof HttpResponse) {
            this.removeRequest(req);
            observer.next(event);
          }
        },
        (err) => {
          this.removeRequest(req);
          observer.error(err);
        },
        () => {
          this.removeRequest(req);
          observer.complete();
        }
      );
      return () => {
        this.removeRequest(req);
        // this.loaderService.loadingToggleStatus(false);
        subscription.unsubscribe();
      };
    });
  }
}
