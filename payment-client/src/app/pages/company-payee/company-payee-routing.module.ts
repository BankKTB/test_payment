import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayeeComponent } from '@pages/company-payee/company-payee.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayeeComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayeeRoutingModule {}
