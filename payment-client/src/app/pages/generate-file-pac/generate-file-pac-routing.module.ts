import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GenerateFilePacComponent } from '@pages/generate-file-pac/generate-file-pac.component';

const routes: Routes = [
  {
    path: '',
    component: GenerateFilePacComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenerateFilePacRoutingModule {}
