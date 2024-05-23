import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
// shared
import { SharedModule } from '@shared/shared.module';
import { PayMethodComponent } from '@pages/pay-method/pay-method.component';
import { PayMethodRoutingModule } from '@pages/pay-method/pay-method-routing.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [PayMethodComponent],
  imports: [SharedModule, CommonModule, PayMethodRoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class PayMethodModule {}
