import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReportComponent } from '@shared/pages/report/report.component';

const routes: Routes = [
  {
    path: '',
    component: ReportComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReportRoutingModule {}
