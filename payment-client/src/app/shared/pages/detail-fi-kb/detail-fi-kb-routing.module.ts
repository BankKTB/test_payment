import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DetailFiKbComponent } from './detail-fi-kb.component';

const routes: Routes = [
  {
    path: '',
    component: DetailFiKbComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DetailFiKbRoutingModule {}
