import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReportOmComponent } from '@shared/pages/report-om/report-om.component';

const routes: Routes = [
  {
    path: '',
    component: ReportOmComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReportOmRoutingModule {}
