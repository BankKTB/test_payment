import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmResultERoutingModule } from './om-result-e-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { OmPtoHComponent } from '@pages/om-pto-h/om-pto-h.component';
import { OmResultComponent } from '@pages/om-result/om-result.component';
import { OmResultEComponent } from '@pages/om-result-e/om-result-e.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [OmResultEComponent],
  imports: [SharedModule, CommonModule, OmResultERoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class OmResultEModule {}
