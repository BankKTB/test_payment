import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LogRoutingModule } from './log-routing.module';
// shared
import { SharedModule } from '@shared/shared.module';
import { LogComponent } from '@shared/pages/log/log.component';

@NgModule({
  declarations: [LogComponent],
  imports: [SharedModule, CommonModule, LogRoutingModule],
})
export class LogModule {}
