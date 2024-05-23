import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OmPtoHComponent } from '@pages/om-pto-h/om-pto-h.component';
import { OmResultComponent } from '@pages/om-result/om-result.component';

const routes: Routes = [
  {
    path: '',
    component: OmResultComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OmResultRoutingModule {}
