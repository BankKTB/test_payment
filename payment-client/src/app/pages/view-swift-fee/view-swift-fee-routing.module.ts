import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewSwiftFeeComponent } from '@pages/view-swift-fee/view-swift-fee.component';

const routes: Routes = [
  {
    path: '',
    component: ViewSwiftFeeComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ViewSwiftFeeRoutingModule {}
