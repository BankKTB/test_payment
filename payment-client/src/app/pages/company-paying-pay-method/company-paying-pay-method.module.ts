import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayingPayMethodRoutingModule } from './company-paying-pay-method-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayingPayMethodComponent } from '@pages/company-paying-pay-method/company-paying-pay-method.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayingPayMethodComponent],
  imports: [SharedModule, CommonModule, CompanyPayingPayMethodRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayingPayMethodModule {}
