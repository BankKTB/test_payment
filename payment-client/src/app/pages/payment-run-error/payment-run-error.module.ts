import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentRunErrorRoutingModule } from './payment-run-error-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { ProposalDetailComponent } from '@pages/proposal-detail/proposal-detail.component';
import { PaymentRunErrorComponent } from '@pages/payment-run-error/payment-run-error.component';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [PaymentRunErrorComponent],
  imports: [SharedModule, CommonModule, PaymentRunErrorRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class PaymentRunErrorModule {}
