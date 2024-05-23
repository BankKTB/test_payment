import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateComponent } from '@pages/batch-job/create/create.component';
import { BatchJobComponent } from '@pages/batch-job/batch-job.component';
import { SearchComponent } from '@pages/batch-job/search/search.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'search',
    component: BatchJobComponent,
  },
  {
    path: 'search',
    component: SearchComponent,
  },
  {
    path: 'create',
    component: CreateComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BatchJobRoutingModule {}
