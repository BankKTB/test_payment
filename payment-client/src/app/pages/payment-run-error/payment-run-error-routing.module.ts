import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProposalDetailComponent } from '@pages/proposal-detail/proposal-detail.component';
import { PaymentRunErrorComponent } from '@pages/payment-run-error/payment-run-error.component';

const routes: Routes = [
  {
    path: '',
    component: PaymentRunErrorComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PaymentRunErrorRoutingModule {}
