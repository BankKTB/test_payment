import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PaymentReverseComponent } from '@pages/payment-reverse/payment-reverse.component';

const routes: Routes = [
  {
    path: '',
    component: PaymentReverseComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PaymentReverseRoutingModule {}
