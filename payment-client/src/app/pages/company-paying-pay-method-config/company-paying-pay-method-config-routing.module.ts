import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayingPayMethodConfigComponent } from '@pages/company-paying-pay-method-config/company-paying-pay-method-config.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayingPayMethodConfigComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayingPayMethodConfigRoutingModule {}
