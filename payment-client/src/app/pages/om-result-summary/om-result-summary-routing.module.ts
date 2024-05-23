import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OmPtoHComponent } from '@pages/om-pto-h/om-pto-h.component';
import { OmResultComponent } from '@pages/om-result/om-result.component';
import { OmResultEComponent } from '@pages/om-result-e/om-result-e.component';
import { OmResultSummaryComponent } from '@pages/om-result-summary/om-result-summary.component';

const routes: Routes = [
  {
    path: '',
    component: OmResultSummaryComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OmResultSummaryRoutingModule {}
