import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayeeBankAccountNoConfigRoutingModule } from './company-payee-bank-account-no-config-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayeeBankAccountNoConfigComponent } from '@pages/company-payee-bank-account-no-config/company-payee-bank-account-no-config.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayeeBankAccountNoConfigComponent],
  imports: [SharedModule, CommonModule, CompanyPayeeBankAccountNoConfigRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayeeBankAccountNoConfigModule {}
