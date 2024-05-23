import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import {
  AuthGuardService,
  AuthorizeService,
  LoaderService,
  LocalStorageService,
  MasterService,
  ParentPathService,
  SidebarService,
  WebInfoService,
} from '@core/services';
import { BrService } from '@core/services/br/br.service';
import { ReportService } from '@core/services/report/report-service';
import { LoaderInterceptor } from '@core/interceptors/loader.interceptor';
import { JwtInterceptor } from '@core/interceptors/jwt.interceptor';
import { JwtErrorInterceptor } from '@core/interceptors/jwt.error.interceptor';

export const httpInterceptorProviders = [
  // { provide: HTTP_INTERCEPTORS, useClass: CacheInterceptor, multi: true },
  // { provide: HTTP_INTERCEPTORS, useClass: HttpCacheService, multi: true }
];

@NgModule({
  declarations: [],
  imports: [CommonModule, HttpClientModule],
  providers: [
    LocalStorageService,
    WebInfoService,
    SidebarService,
    AuthorizeService,
    AuthGuardService,
    LoaderService,
    MasterService,
    BrService,
    ReportService,
    ParentPathService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoaderInterceptor,
      multi: true,
    },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: JwtErrorInterceptor, multi: true },
  ],
})
export class CoreModule {}
