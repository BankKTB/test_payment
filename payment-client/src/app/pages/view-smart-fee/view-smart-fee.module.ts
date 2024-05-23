import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ViewSmartFeeRoutingModule } from './view-smart-fee-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { SmartFeeComponent } from '@pages/smart-fee/smart-fee.component';
import { ViewSmartFeeComponent } from '@pages/view-smart-fee/view-smart-fee.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [ViewSmartFeeComponent],
  imports: [SharedModule, CommonModule, ViewSmartFeeRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ViewSmartFeeModule {}
