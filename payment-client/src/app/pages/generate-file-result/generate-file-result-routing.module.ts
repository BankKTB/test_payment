import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { GenerateFileResultComponent } from '@pages/generate-file-result/generate-file-result.component';

const routes: Routes = [
  {
    path: ':logId',
    component: GenerateFileResultComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenerateFileResultRoutingModule {}
