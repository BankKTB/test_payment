import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmRoutingModule } from './om-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { OmComponent } from '@pages/om/om.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [OmComponent],
  imports: [SharedModule, CommonModule, OmRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class OmModule {}