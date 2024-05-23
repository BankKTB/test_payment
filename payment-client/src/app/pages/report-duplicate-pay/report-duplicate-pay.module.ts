import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportDuplicatePayComponent } from '@pages/report-duplicate-pay/report-duplicate-pay.component';
import { ReportDuplicatePayRoutingModule } from '@pages/report-duplicate-pay/report-duplicate-pay-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [ReportDuplicatePayComponent],
  imports: [SharedModule, CommonModule, ReportDuplicatePayRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ReportDuplicatePayModule {}
