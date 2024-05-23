import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReactiveFormsModule } from '@angular/forms';
import {
  MatDatepickerModule,
  MatExpansionModule,
  MatNativeDateModule,
  MatTabsModule,
} from '@angular/material';
import { NgxPaginationModule } from 'ngx-pagination';
// shared
import { SharedModule } from '@shared/shared.module';
import { DetailPo4Component } from '@shared/pages/detail-po4/detail-po4.component';
import { DetailPo4RoutingModule } from '@shared/pages/detail-po4/detail-po4-routing.module';

@NgModule({
  declarations: [DetailPo4Component],
  imports: [
    SharedModule,
    CommonModule,
    DetailPo4RoutingModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatExpansionModule,
    ReactiveFormsModule,
    NgxPaginationModule,
  ],
})
export class DetailPo4Module {}
