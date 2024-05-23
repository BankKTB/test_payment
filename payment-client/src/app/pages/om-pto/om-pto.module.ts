import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmPtoRoutingModule } from './om-pto-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { OmPtoComponent } from '@pages/om-pto/om-pto.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [OmPtoComponent],
  imports: [SharedModule, CommonModule, OmPtoRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class OmPtoModule {}