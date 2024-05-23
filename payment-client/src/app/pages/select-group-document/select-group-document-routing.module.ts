import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SelectGroupDocumentComponent } from '@pages/select-group-document/select-group-document.component';
import { AuthGuardService } from '@core/services';

const routes: Routes = [
  {
    path: '',
    component: SelectGroupDocumentComponent,
    canActivate: [AuthGuardService],
    data: { auth: 'MENU' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SelectGroupDocumentRoutingModule {}
