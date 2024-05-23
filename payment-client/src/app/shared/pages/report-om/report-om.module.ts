import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// shared
import { SharedModule } from '@shared/shared.module';
import { ReportOmComponent } from '@shared/pages/report-om/report-om.component';
import { ReportOmRoutingModule } from '@shared/pages/report-om/report-om-routing.module';

@NgModule({
  declarations: [ReportOmComponent],
  imports: [SharedModule, CommonModule, ReportOmRoutingModule],
})
export class ReportOmModule {}
