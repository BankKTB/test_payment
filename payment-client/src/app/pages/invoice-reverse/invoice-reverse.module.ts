import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InvoiceReverseRoutingModule } from './invoice-reverse-routing.module';
import { SharedModule } from '@shared/shared.module';
import { InvoiceReverseComponent } from '@pages/invoice-reverse/invoice-reverse.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [InvoiceReverseComponent],
  imports: [SharedModule, CommonModule, InvoiceReverseRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class InvoiceReverseModule {}
