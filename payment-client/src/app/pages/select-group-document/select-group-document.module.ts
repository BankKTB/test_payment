import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SelectGroupDocumentRoutingModule } from './select-group-document-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';
import { SelectGroupDocumentComponent } from '@pages/select-group-document/select-group-document.component';

@NgModule({
  declarations: [SelectGroupDocumentComponent],
  imports: [SharedModule, CommonModule, SelectGroupDocumentRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class SelectGroupDocumentModule {}
