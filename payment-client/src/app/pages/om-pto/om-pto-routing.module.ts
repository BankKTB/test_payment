import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OmPtoComponent } from '@pages/om-pto/om-pto.component';

const routes: Routes = [
  {
    path: '',
    component: OmPtoComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OmPtoRoutingModule {}
