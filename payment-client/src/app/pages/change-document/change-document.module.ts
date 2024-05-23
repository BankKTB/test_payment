import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// shared
import { SharedModule } from '@shared/shared.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';
import { ChangeDocumentComponent } from '@pages/change-document/change-document.component';
import { ChangeDocumentRoutingModule } from '@pages/change-document/change-document-routing.module';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  declarations: [ChangeDocumentComponent],
  imports: [SharedModule, CommonModule, ChangeDocumentRoutingModule, NgxPaginationModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ChangeDocumentModule {}
