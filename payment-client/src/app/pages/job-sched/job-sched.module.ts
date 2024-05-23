import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { JobSchedRoutingModule } from './job-sched-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { JobSchedComponent } from '@pages/job-sched/job-sched.component';
import { JobSchedDetailComponent } from '@pages/job-sched/job-sched-detail.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [JobSchedComponent, JobSchedDetailComponent],
  imports: [SharedModule, CommonModule, JobSchedRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class JobSchedModule {}
