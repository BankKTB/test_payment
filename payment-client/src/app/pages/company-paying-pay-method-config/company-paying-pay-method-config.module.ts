import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayingPayMethodConfigRoutingModule } from './company-paying-pay-method-config-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayingPayMethodConfigComponent } from '@pages/company-paying-pay-method-config/company-paying-pay-method-config.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayingPayMethodConfigComponent],
  imports: [SharedModule, CommonModule, CompanyPayingPayMethodConfigRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayingPayMethodConfigModule {}
