import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OmComponent } from '@pages/om/om.component';

const routes: Routes = [
  {
    path: '',
    component: OmComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OmRoutingModule {}
