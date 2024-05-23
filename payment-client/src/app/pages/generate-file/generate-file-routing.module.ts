import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GenerateFileComponent } from '@pages/generate-file/generate-file.component';

const routes: Routes = [
  {
    path: '',
    component: GenerateFileComponent,
    // canActivate: [AuthGuardService],
    // data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenerateFileRoutingModule {}
