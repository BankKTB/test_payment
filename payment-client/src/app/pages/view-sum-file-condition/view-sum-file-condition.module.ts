import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ViewSumFileConditionRoutingModule } from './view-sum-file-condition-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { SumFileConditionComponent } from '@pages/sum-file-condition/sum-file-condition.component';
import { ViewSumFileConditionComponent } from '@pages/view-sum-file-condition/view-sum-file-condition.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [ViewSumFileConditionComponent],
  imports: [SharedModule, CommonModule, ViewSumFileConditionRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ViewSumFileConditionModule {}
