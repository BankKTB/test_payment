import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyPayingComponent } from '@pages/company-paying/company-paying.component';

const routes: Routes = [
  {
    path: '',
    component: CompanyPayingComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CompanyPayingRoutingModule {}
