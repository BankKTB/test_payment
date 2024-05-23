import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportRoutingModule } from './report-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { ReportComponent } from '@shared/pages/report/report.component';

@NgModule({
  declarations: [ReportComponent],
  imports: [SharedModule, CommonModule, ReportRoutingModule],
})
export class ReportModule {}
