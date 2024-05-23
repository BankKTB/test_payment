import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SmartFeeComponent } from '@pages/smart-fee/smart-fee.component';

const routes: Routes = [
  {
    path: '',
    component: SmartFeeComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SmartFeeRoutingModule {}
