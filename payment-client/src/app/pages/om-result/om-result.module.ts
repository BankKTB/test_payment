import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OmResultRoutingModule } from './om-result-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';

import { OmPtoHComponent } from '@pages/om-pto-h/om-pto-h.component';
import { OmResultComponent } from '@pages/om-result/om-result.component';

@NgModule({
  declarations: [OmResultComponent],
  imports: [SharedModule, CommonModule, OmResultRoutingModule],
})
export class OmResultModule {}
