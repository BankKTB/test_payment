import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CompanyPayingRoutingModule } from './company-paying-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { CompanyPayingComponent } from '@pages/company-paying/company-paying.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [CompanyPayingComponent],
  imports: [SharedModule, CommonModule, CompanyPayingRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class CompanyPayingModule {}
