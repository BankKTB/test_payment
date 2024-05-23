import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProposalTr1Component } from '@pages/proposal-tr1/proposal-tr1.component';
import { ProposalTr1RoutingModule } from '@pages/proposal-tr1/proposal-tr1-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@NgModule({
  declarations: [ProposalTr1Component],
  imports: [SharedModule, CommonModule, ProposalTr1RoutingModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class ProposalTr1Module {}
