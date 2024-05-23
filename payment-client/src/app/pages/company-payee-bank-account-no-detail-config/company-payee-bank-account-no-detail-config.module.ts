import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayeeBankAccountNoDetailConfigRoutingModule } from './company-payee-bank-account-no-detail-config-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayeeBankAccountNoDetailConfigComponent } from '@pages/company-payee-bank-account-no-detail-config/company-payee-bank-account-no-detail-config.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayeeBankAccountNoDetailConfigComponent],
  imports: [SharedModule, CommonModule, CompanyPayeeBankAccountNoDetailConfigRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayeeBankAccountNoDetailConfigModule {}
