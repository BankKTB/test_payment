import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProposalDetailComponent } from '@pages/proposal-detail/proposal-detail.component';

const routes: Routes = [
  {
    path: '',
    component: ProposalDetailComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProposalDetailRoutingModule {}
