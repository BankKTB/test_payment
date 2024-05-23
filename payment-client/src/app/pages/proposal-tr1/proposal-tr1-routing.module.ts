import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProposalTr1Component } from '@pages/proposal-tr1/proposal-tr1.component';

const routes: Routes = [
  {
    path: '',
    component: ProposalTr1Component,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProposalTr1RoutingModule {}
