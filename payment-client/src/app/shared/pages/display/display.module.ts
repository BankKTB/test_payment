import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DisplayRoutingModule } from './display-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { DisplayComponent } from '@shared/pages/display/display.component';

@NgModule({
  declarations: [DisplayComponent],
  imports: [SharedModule, CommonModule, DisplayRoutingModule],
})
export class DisplayModule {}
