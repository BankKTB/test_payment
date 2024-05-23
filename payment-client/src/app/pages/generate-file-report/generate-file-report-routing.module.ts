import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { GenerateFileReportComponent } from '@pages/generate-file-report/generate-file-report.component';
import { AuthGuardService } from '@core/services';

const routes: Routes = [
  {
    path: '',
    component: GenerateFileReportComponent,
    canActivate: [AuthGuardService],
    data: { auth: 'CF02' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenerateFileReportRoutingModule {}
