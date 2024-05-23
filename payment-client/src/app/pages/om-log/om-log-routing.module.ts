import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OmLogComponent } from '@pages/om-log/om-log.component';

const routes: Routes = [
  {
    path: '',
    component: OmLogComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class OmLogRoutingModule {}
