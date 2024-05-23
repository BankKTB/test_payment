import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayingBankConfigRoutingModule } from './company-paying-bank-config-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayingBankConfigComponent } from '@pages/company-paying-bank/company-paying-bank-config.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayingBankConfigComponent],
  imports: [SharedModule, CommonModule, CompanyPayingBankConfigRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayingBankConfigModule {}
