import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReportDuplicatePayComponent } from '@pages/report-duplicate-pay/report-duplicate-pay.component';

const routes: Routes = [
  {
    path: '',
    component: ReportDuplicatePayComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReportDuplicatePayRoutingModule {}
