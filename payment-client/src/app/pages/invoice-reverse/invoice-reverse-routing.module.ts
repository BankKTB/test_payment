import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InvoiceReverseComponent } from '@pages/invoice-reverse/invoice-reverse.component';

const routes: Routes = [
  {
    path: '',
    component: InvoiceReverseComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InvoiceReverseRoutingModule {}
