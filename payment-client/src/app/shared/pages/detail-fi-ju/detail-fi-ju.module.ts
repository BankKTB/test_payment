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

import { DetailFiJuRoutingModule } from './detail-fi-ju-routing.module';
import { DetailFiJuComponent } from './detail-fi-ju.component';
// shared
import { SharedModule } from '@shared/shared.module';

@NgModule({
  declarations: [DetailFiJuComponent],
  imports: [
    SharedModule,
    CommonModule,
    DetailFiJuRoutingModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatExpansionModule,
    ReactiveFormsModule,
    NgxPaginationModule,
  ],
})
export class DetailFiJuModule {}
