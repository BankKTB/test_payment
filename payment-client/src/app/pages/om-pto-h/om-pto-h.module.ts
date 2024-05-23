import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmPtoHRoutingModule } from './om-pto-h-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { OmPtoHComponent } from '@pages/om-pto-h/om-pto-h.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [OmPtoHComponent],
  imports: [SharedModule, CommonModule, OmPtoHRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class OmPtoHModule {}
