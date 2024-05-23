import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DetailFiJuComponent } from './detail-fi-ju.component';

const routes: Routes = [
  {
    path: '',
    component: DetailFiJuComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DetailFiJuRoutingModule {}
