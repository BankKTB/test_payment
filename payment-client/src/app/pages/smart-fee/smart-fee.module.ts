import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SmartFeeRoutingModule } from './smart-fee-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { SmartFeeComponent } from '@pages/smart-fee/smart-fee.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [SmartFeeComponent],
  imports: [SharedModule, CommonModule, SmartFeeRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class SmartFeeModule {}
