import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GenerateDocumentJuComponent } from '@pages/generate-document-ju/generate-document-ju.component';
import { SearchCriteriaComponent } from '@pages/generate-document-ju/search-criteria/search-criteria.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'search',
    component: GenerateDocumentJuComponent,
  },
  {
    path: 'search',
    component: SearchCriteriaComponent,
    data: { auth: 'JU01' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class GenerateDocumentJuRoutingModule {}
