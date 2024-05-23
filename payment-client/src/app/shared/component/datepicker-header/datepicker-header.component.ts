import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Inject,
  OnDestroy,
} from '@angular/core';
import { MatCalendar } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MatDateFormats } from '@angular/material/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-datepicker-header',
  templateUrl: './datepicker-header.component.html',
  styleUrls: ['./datepicker-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DatepickerHeaderComponent<D> implements OnDestroy {
  isYearSelectOpen = false;
  isMonthSelectOpen = false;
  listYear = [];
  periodYearLabel = '';
  periodMonthLabel = '';
  yearSelect;
  listMonth = [
    { id: 1, name: 'ม.ค.' },
    { id: 2, name: 'ก.พ.' },
    { id: 3, name: 'มี.ค.' },
    { id: 4, name: 'เม.ย.' },
    { id: 5, name: 'พ.ค.' },
    { id: 6, name: 'มิ.ย.' },
    { id: 7, name: 'ก.ค.' },
    { id: 8, name: 'ส.ค.' },
    { id: 9, name: 'ก.ย.' },
    { id: 10, name: 'ต.ค.' },
    { id: 11, name: 'พ.ย.' },
    { id: 12, name: 'ธ.ค.' },
  ];
  private _destroyed = new Subject<void>();

  constructor(
    private _calendar: MatCalendar<D>,
    private _dateAdapter: DateAdapter<D>,
    @Inject(MAT_DATE_FORMATS) private _dateFormats: MatDateFormats,
    cdr: ChangeDetectorRef
  ) {
    _calendar.stateChanges.pipe(takeUntil(this._destroyed)).subscribe(() => cdr.markForCheck());

    this.yearSelect = this._dateAdapter.getYear(this._calendar.activeDate) + 543;
  }

  get periodLabel() {
    return this._dateAdapter
      .format(this._calendar.activeDate, this._dateFormats.display.monthYearLabel)
      .toLocaleUpperCase();
  }

  ngOnDestroy() {
    this._destroyed.next();
    this._destroyed.complete();
  }

  previousClicked(mode: 'month' | 'year') {
    if (this.isYearSelectOpen) {
      this.loadYearByClick(this.listYear[0], 'prev');
    } else {
      this._calendar.activeDate =
        mode === 'month'
          ? this._dateAdapter.addCalendarMonths(this._calendar.activeDate, -1)
          : this._dateAdapter.addCalendarYears(this._calendar.activeDate, -1);
    }
  }

  nextClicked(mode: 'month' | 'year') {
    if (this.isYearSelectOpen) {
      this.loadYearByClick(this.listYear[this.listYear.length - 1], 'next');
    } else {
      this._calendar.activeDate =
        mode === 'month'
          ? this._dateAdapter.addCalendarMonths(this._calendar.activeDate, 1)
          : this._dateAdapter.addCalendarYears(this._calendar.activeDate, 1);
    }
  }

  selectYear(year) {
    if (year) {
      const currentYear = this._dateAdapter.getYear(this._calendar.activeDate) + 543;
      this._calendar.activeDate = this._dateAdapter.addCalendarYears(
        this._calendar.activeDate,
        year - currentYear
      );
      this.isMonthSelectOpen = true;
      this.isYearSelectOpen = false;
      this.periodMonthLabel = 'พ.ศ. ' + year;
    }
  }

  selectMonth(month) {
    if (month) {
      const currentMonth = this._dateAdapter.getMonth(this._calendar.activeDate) + 1;
      this._calendar.activeDate = this._dateAdapter.addCalendarMonths(
        this._calendar.activeDate,
        month - currentMonth
      );
      this.isMonthSelectOpen = false;
      this.isYearSelectOpen = false;
    }
  }

  periodConsitionClicked(mode: 'month' | 'year') {
    if (mode === 'year') {
      const currentYear = this._dateAdapter.getYear(this._calendar.activeDate) + 543;
      this.getYearList(currentYear);
      this.isYearSelectOpen = !this.isYearSelectOpen;
    } else {
      this.isYearSelectOpen = false;
      this.isMonthSelectOpen = false;
    }
  }

  getYearList(currentYear) {
    this.listYear = [];
    for (let i = currentYear; i > currentYear - 5; i--) {
      this.listYear.push(i);
    }
    this.listYear.reverse();
    this.listYear.pop();
    for (let i = currentYear; i < currentYear + 12; i++) {
      this.listYear.push(i);
    }
    this.periodYearLabel = this.listYear[0] + ' - ' + this.listYear[this.listYear.length - 1];
  }

  loadYearByClick(firstYear, type) {
    this.listYear = [];
    if (type === 'prev') {
      for (let i = firstYear; i > firstYear - 20; i--) {
        this.listYear.push(i);
      }
      this.listYear.reverse();
      this.listYear.pop();
    } else {
      for (let i = firstYear; i < firstYear + 20; i++) {
        this.listYear.push(i);
      }
    }
    this.periodYearLabel = this.listYear[0] + ' - ' + this.listYear[this.listYear.length - 1];
  }
}
