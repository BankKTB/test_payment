import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayeeHouseBankKeyDetailConfigRoutingModule } from './company-payee-house-bank-key-detail-config-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayeeHouseBankKeyDetailConfigComponent } from '@pages/company-payee-house-bank-key-detail-config/company-payee-house-bank-key-detail-config.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayeeHouseBankKeyDetailConfigComponent],
  imports: [SharedModule, CommonModule, CompanyPayeeHouseBankKeyDetailConfigRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayeeHouseBankKeyDetailConfigModule {}
