import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReturnComponent } from '@pages/return/return.component';
import { AuthGuardService } from '@core/services';

const routes: Routes = [
  {
    path: '',
    component: ReturnComponent,
    canActivate: [AuthGuardService],
    data: { auth: 'KJ03' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ReturnRoutingModule {}
