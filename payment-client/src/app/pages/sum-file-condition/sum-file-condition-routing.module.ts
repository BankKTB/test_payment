import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SwiftFeeComponent } from '@pages/swift-fee/swift-fee.component';
import { SumFileConditionComponent } from '@pages/sum-file-condition/sum-file-condition.component';

const routes: Routes = [
  {
    path: '',
    component: SumFileConditionComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SumFileConditionRoutingModule {}
