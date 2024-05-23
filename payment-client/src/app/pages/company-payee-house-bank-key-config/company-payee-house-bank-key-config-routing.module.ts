import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayeeHouseBankKeyConfigComponent } from '@pages/company-payee-house-bank-key-config/company-payee-house-bank-key-config.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayeeHouseBankKeyConfigComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayeeHouseBankKeyConfigRoutingModule {}
