import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@shared/shared.module';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';
import { SearchComponent } from '@pages/holiday/search/search.component';
import { HolidayRoutingModule } from '@pages/holiday/holiday-routing.module';
import { HolidayComponent } from '@pages/holiday/holiday.component';
import { MatTabsModule } from '@angular/material/tabs';

@NgModule({
  declarations: [SearchComponent, HolidayComponent],
  imports: [CommonModule, SharedModule, HolidayRoutingModule, MatTabsModule],
  providers: [
    { provide: DateAdapter, useClass: InputDateAdapter },
    { provide: MAT_DATE_FORMATS, useValue: INPUT_DATE_FORMATS },
  ],
})
export class HolidayModule {}
