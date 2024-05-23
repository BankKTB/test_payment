import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DetailSngComponent } from './detail-sng.component';

const routes: Routes = [
  {
    path: '',
    component: DetailSngComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DetailSngRoutingModule {
}
