import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SwiftFeeComponent } from '@pages/swift-fee/swift-fee.component';

const routes: Routes = [
  {
    path: '',
    component: SwiftFeeComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SwiftFeeRoutingModule {}
