import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ViewSwiftFeeRoutingModule } from './view-swift-fee-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { SmartFeeComponent } from '@pages/smart-fee/smart-fee.component';
import { SwiftFeeComponent } from '@pages/swift-fee/swift-fee.component';
import { ViewSwiftFeeComponent } from '@pages/view-swift-fee/view-swift-fee.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [ViewSwiftFeeComponent],
  imports: [SharedModule, CommonModule, ViewSwiftFeeRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ViewSwiftFeeModule {}
