import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GenerateFileReportRoutingModule } from './generate-file-report-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { GenerateFileReportComponent } from '@pages/generate-file-report/generate-file-report.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [GenerateFileReportComponent],
  imports: [SharedModule, CommonModule, GenerateFileReportRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class GenerateFileReportModule {}
