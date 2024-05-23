import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GenerateFilePacRoutingModule } from './generate-file-pac-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';
import { GenerateFilePacComponent } from '@pages/generate-file-pac/generate-file-pac.component';

@NgModule({
  declarations: [GenerateFilePacComponent],
  imports: [SharedModule, CommonModule, GenerateFilePacRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class GenerateFilePacModule {}
