import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmCgdRoutingModule } from './om-cgd-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { OmCgdComponent } from '@pages/om-cgd/om-cgd.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [OmCgdComponent],
  imports: [SharedModule, CommonModule, OmCgdRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class OmCgdModule {}
