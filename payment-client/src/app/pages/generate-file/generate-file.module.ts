import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GenerateFileRoutingModule } from './generate-file-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { GenerateFileComponent } from '@pages/generate-file/generate-file.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [GenerateFileComponent],
  imports: [SharedModule, CommonModule, GenerateFileRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class GenerateFileModule {}
