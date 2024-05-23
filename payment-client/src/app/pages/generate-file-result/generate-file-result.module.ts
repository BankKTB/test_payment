import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GenerateFileResultRoutingModule } from './generate-file-result-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { GenerateFileResultComponent } from '@pages/generate-file-result/generate-file-result.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [GenerateFileResultComponent],
  imports: [SharedModule, CommonModule, GenerateFileResultRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class GenerateFileResultModule {}
