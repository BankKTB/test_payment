import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReactiveFormsModule } from '@angular/forms';
import { NgxPaginationModule } from 'ngx-pagination';

import { DetailSngRoutingModule } from './detail-sng-routing.module';
import { DetailSngComponent } from './detail-sng.component';
// shared
import { SharedModule } from '@shared/shared.module';
import { MatTabsModule } from '@angular/material/tabs';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatExpansionModule } from '@angular/material/expansion';

@NgModule({
  declarations: [DetailSngComponent],
  imports: [
    SharedModule,
    CommonModule,
    DetailSngRoutingModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatExpansionModule,
    ReactiveFormsModule,
    NgxPaginationModule,
  ],
})
export class DetailSngModule {}
