import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayeeHouseBankKeyConfigRoutingModule } from './company-payee-house-bank-key-config-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayeeHouseBankKeyConfigComponent } from '@pages/company-payee-house-bank-key-config/company-payee-house-bank-key-config.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayeeHouseBankKeyConfigComponent],
  imports: [SharedModule, CommonModule, CompanyPayeeHouseBankKeyConfigRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayeeHouseBankKeyConfigModule {}
