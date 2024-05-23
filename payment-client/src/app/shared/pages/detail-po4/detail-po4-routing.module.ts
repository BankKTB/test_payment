import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DetailPo4Component } from '@shared/pages/detail-po4/detail-po4.component';

const routes: Routes = [
  {
    path: '',
    component: DetailPo4Component,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DetailPo4RoutingModule {}
