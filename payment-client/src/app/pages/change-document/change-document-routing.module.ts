import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChangeDocumentComponent } from '@pages/change-document/change-document.component';
import { AuthGuardService } from '@core/services';

const routes: Routes = [
  {
    path: '',
    component: ChangeDocumentComponent,
    canActivate: [AuthGuardService],
    data: { auth: 'CHLITM' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ChangeDocumentRoutingModule {}
