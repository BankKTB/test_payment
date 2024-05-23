import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OmCgdComponent } from '@pages/om-cgd/om-cgd.component';

const routes: Routes = [
  {
    path: '',
    component: OmCgdComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OmCgdRoutingModule {}
