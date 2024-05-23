import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayeeHouseBankKeyDetailConfigComponent } from '@pages/company-payee-house-bank-key-detail-config/company-payee-house-bank-key-detail-config.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayeeHouseBankKeyDetailConfigComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayeeHouseBankKeyDetailConfigRoutingModule {}
