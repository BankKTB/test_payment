import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentReverseRoutingModule } from './payment-reverse-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { PaymentReverseComponent } from '@pages/payment-reverse/payment-reverse.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [PaymentReverseComponent],
  imports: [SharedModule, CommonModule, PaymentReverseRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class PaymentReverseModule {}
