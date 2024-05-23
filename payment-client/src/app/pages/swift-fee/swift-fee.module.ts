import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SwiftFeeRoutingModule } from './swift-fee-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { SmartFeeComponent } from '@pages/smart-fee/smart-fee.component';
import { SwiftFeeComponent } from '@pages/swift-fee/swift-fee.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [SwiftFeeComponent],
  imports: [SharedModule, CommonModule, SwiftFeeRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class SwiftFeeModule {}
