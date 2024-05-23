import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayingPayMethodComponent } from '@pages/company-paying-pay-method/company-paying-pay-method.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayingPayMethodComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayingPayMethodRoutingModule {}
