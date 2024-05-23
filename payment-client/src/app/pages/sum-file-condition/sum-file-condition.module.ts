import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SumFileConditionRoutingModule } from './sum-file-condition-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { SumFileConditionComponent } from '@pages/sum-file-condition/sum-file-condition.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [SumFileConditionComponent],
  imports: [SharedModule, CommonModule, SumFileConditionRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class SumFileConditionModule {}
