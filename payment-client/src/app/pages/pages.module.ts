import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@shared/shared.module';

import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from 'app/app-routing.module';
import { ProposalListComponent } from './proposal-list/proposal-list.component';

@NgModule({
  declarations: [ProposalListComponent],
  imports: [
    CommonModule,
    SharedModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
  ],
})
export class PagesModule {}
