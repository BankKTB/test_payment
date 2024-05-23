import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GenerateDocumentJuRoutingModule } from './generate-document-ju-routing.module';
import { SharedModule } from '@shared/shared.module';
import { GenerateDocumentJuComponent } from '@pages/generate-document-ju/generate-document-ju.component';
import { SearchCriteriaComponent } from './search-criteria/search-criteria.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';
@NgModule({
  declarations: [GenerateDocumentJuComponent, SearchCriteriaComponent],
  imports: [SharedModule, CommonModule, GenerateDocumentJuRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class GenerateDocumentJuModule {}
