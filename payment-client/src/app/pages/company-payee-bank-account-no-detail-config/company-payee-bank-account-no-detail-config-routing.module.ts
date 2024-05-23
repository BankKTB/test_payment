import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayeeBankAccountNoDetailConfigComponent } from '@pages/company-payee-bank-account-no-detail-config/company-payee-bank-account-no-detail-config.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayeeBankAccountNoDetailConfigComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayeeBankAccountNoDetailConfigRoutingModule {}
