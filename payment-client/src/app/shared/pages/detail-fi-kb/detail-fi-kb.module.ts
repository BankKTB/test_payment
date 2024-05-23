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

import { DetailFiKbRoutingModule } from './detail-fi-kb-routing.module';
import { DetailFiKbComponent } from './detail-fi-kb.component';
// shared
import { SharedModule } from '@shared/shared.module';

@NgModule({
  declarations: [DetailFiKbComponent],
  imports: [
    SharedModule,
    CommonModule,
    DetailFiKbRoutingModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatExpansionModule,
    ReactiveFormsModule,
    NgxPaginationModule,
  ],
})
export class DetailFiKbModule {}
