import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateComponent } from '@pages/batch-job/create/create.component';
import { BatchJobComponent } from '@pages/batch-job/batch-job.component';
import { SharedModule } from '@shared/shared.module';
import { BatchJobRoutingModule } from './batch-job-routing.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [CreateComponent, BatchJobComponent, SearchComponent],
  imports: [CommonModule, SharedModule, BatchJobRoutingModule, MatSlideToggleModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class BatchJobModule {}
