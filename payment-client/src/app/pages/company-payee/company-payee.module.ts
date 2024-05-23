import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayeeRoutingModule } from './company-payee-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayeeComponent } from '@pages/company-payee/company-payee.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayeeComponent],
  imports: [SharedModule, CommonModule, CompanyPayeeRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayeeModule {}
