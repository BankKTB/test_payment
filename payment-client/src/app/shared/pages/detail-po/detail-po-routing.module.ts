import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DetailPoComponent } from '@shared/pages/detail-po/detail-po.component';

const routes: Routes = [
  {
    path: '',
    component: DetailPoComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DetailPoRoutingModule {}
