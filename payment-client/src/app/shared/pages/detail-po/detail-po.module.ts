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
import { DetailPoComponent } from '@shared/pages/detail-po/detail-po.component';
import { DetailPoRoutingModule } from '@shared/pages/detail-po/detail-po-routing.module';

@NgModule({
  declarations: [DetailPoComponent],
  imports: [
    SharedModule,
    CommonModule,
    DetailPoRoutingModule,
    MatTabsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatExpansionModule,
    ReactiveFormsModule,
    NgxPaginationModule,
  ],
})
export class DetailPoModule {}
