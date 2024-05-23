import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PagesModule } from '@pages/pages.module';
import { SharedModule } from '@shared/shared.module';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '@core/core.module';
import { CurrencyPipe } from '@shared/pipe/currency.pipe';
import { MAT_DATE_LOCALE, MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
import { InitFromIamComponent } from '@pages/init-from-iam/init-from-iam.component';

@NgModule({
  declarations: [AppComponent, InitFromIamComponent],
  imports: [
    PagesModule,
    CoreModule,
    SharedModule,
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
  ],
  exports: [],
  providers: [
    FormBuilder,
    CurrencyPipe,
    Constant,
    Utils,
    { provide: MAT_DATE_LOCALE, useValue: 'th-TH' },
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: { duration: 3000, verticalPosition: 'top' },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
