import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmLogRoutingModule } from './om-log-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { OmLogComponent } from '@pages/om-log/om-log.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [OmLogComponent],
  imports: [SharedModule, CommonModule, OmLogRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class OmLogModule {}
