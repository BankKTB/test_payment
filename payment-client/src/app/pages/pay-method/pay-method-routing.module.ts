import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PayMethodComponent } from '@pages/pay-method/pay-method.component';

const routes: Routes = [
  {
    path: '',
    component: PayMethodComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PayMethodRoutingModule {}
