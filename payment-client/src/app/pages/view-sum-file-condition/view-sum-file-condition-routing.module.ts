import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SwiftFeeComponent } from '@pages/swift-fee/swift-fee.component';
import { SumFileConditionComponent } from '@pages/sum-file-condition/sum-file-condition.component';
import { ViewSumFileConditionComponent } from '@pages/view-sum-file-condition/view-sum-file-condition.component';

const routes: Routes = [
  {
    path: '',
    component: ViewSumFileConditionComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ViewSumFileConditionRoutingModule {}
