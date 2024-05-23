import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaymentComponent } from '@pages/payment/payment.component';
import { AuthGuardService } from '@core/services';

const routes: Routes = [
  {
    path: '',
    component: PaymentComponent,
    canActivate: [AuthGuardService],
    data: { auth: 'PM01' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PaymentRoutingModule {}
