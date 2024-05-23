import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogComponent } from '@shared/pages/log/log.component';

const routes: Routes = [
  {
    path: '',
    component: LogComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LogRoutingModule {}
