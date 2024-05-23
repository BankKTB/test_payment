import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayingBankConfigComponent } from '@pages/company-paying-bank/company-paying-bank-config.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayingBankConfigComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayingBankConfigRoutingModule {}
