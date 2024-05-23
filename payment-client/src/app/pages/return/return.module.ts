import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReturnRoutingModule } from './return-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { ReturnComponent } from '@pages/return/return.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [ReturnComponent],
  imports: [SharedModule, CommonModule, ReturnRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ReturnModule {}
