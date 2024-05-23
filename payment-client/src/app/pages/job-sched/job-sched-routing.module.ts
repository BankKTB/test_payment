import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { JobSchedComponent } from '@pages/job-sched/job-sched.component';
import { JobSchedDetailComponent } from '@pages/job-sched/job-sched-detail.component';

const routes: Routes = [
  {
    path: '',
    component: JobSchedComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
  {
    path: 'detail',
    component: JobSchedDetailComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class JobSchedRoutingModule {}
