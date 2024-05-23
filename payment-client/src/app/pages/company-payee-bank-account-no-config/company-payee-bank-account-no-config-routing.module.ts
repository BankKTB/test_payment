import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayeeBankAccountNoConfigComponent } from '@pages/company-payee-bank-account-no-config/company-payee-bank-account-no-config.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayeeBankAccountNoConfigComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayeeBankAccountNoConfigRoutingModule {}
