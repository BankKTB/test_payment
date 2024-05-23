import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HolidayComponent } from '@pages/holiday/holiday.component';
import { SearchComponent } from '@pages/holiday/search/search.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'search',
    component: HolidayComponent,
  },
  {
    path: 'search',
    component: SearchComponent,
  },
  {
    path: 'create',
    component: SearchComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HolidayRoutingModule {}
