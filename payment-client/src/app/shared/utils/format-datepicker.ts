import { NativeDateAdapter } from '@angular/material';
import { MatDateFormats } from '@angular/material/core';
// can be use constant ask p'BEN
const monthTh = {
  1: 'ม.ค.',
  2: 'ก.พ.',
  3: 'มี.ค.',
  4: 'เม.ย.',
  5: 'พ.ค.',
  6: 'มิ.ย.',
  7: 'ก.ค.',
  8: 'ส.ค.',
  9: 'ก.ย.',
  10: 'ต.ค.',
  11: 'พ.ย.',
  12: 'ธ.ค.',
};
const monthFullTh = {
  1: 'มกราคม',
  2: 'กุมภาพันธ์',
  3: 'มีนาคม',
  4: 'เมษายน',
  5: 'พฤษภาคม',
  6: 'มิถุนายน',
  7: 'กรกฎาคม',
  8: 'สิงหาคม',
  9: 'กันยายน',
  10: 'ตุลาคม',
  11: 'พฤศจิกายน',
  12: 'ธันวาคม',
};
const dayFull = {
  Mon: 'จ.',
  Tue: 'อ.',
  Wed: 'พ.',
  Thu: 'พฤ.',
  Fri: 'ศ.',
  Sat: 'ส.',
  Sun: 'อา.',
};

export class AppDateAdapter extends NativeDateAdapter {
  format(date: Date, displayFormat: Object): string {
    if (displayFormat === 'input') {
      let day: string = date.getDate().toString();
      day = +day < 10 ? '0' + day : day;
      let month: string = monthFullTh[date.getMonth() + 1].toString();
      month = +month < 10 ? '0' + month : month;
      let year;
      year = date.getFullYear() + 543;
      return `${day} ${month} ${year}`;
    }
    const dayFullName = dayFull[date.toDateString().split(' ')[0]];
    const day: string = date.getDate() ? date.getDate().toString() : '01';
    const month: string = monthTh[date.getMonth() + 1]
      ? monthTh[date.getMonth() + 1].toString()
      : monthTh[1];
    const year = date.getFullYear() + 543;
    return `${dayFullName} ${day} ${month} ${year}`;
  }

  // format(date: Date, displayFormat: Object): string {
  //   if (displayFormat === 'input') {
  //     let day: string = date.getDate().toString();
  //     day = +day < 10 ? '0' + day : day;
  //     let month: string = monthFullTh[date.getMonth() + 1].toString();
  //     month = +month < 10 ? '0' + month : month;
  //     const year = date.getFullYear() + 543;
  //     return `${day} ${month} ${year}`;
  //   }
  //   const dayFullName = dayFull[date.toDateString().split(' ')[0]];
  //   const day: string = date.getDate() ? date.getDate().toString() : '01';
  //   const month: string = monthTh[date.getMonth() + 1] ? monthTh[date.getMonth() + 1].toString() : monthTh[1];
  //   const year = date.getFullYear() + 543;
  //   return `${dayFullName} ${day} ${month} ${year}`;
  // }
}

export const APP_DATE_FORMATS: MatDateFormats = {
  parse: {
    dateInput: { month: 'short', year: 'numeric', day: 'numeric' },
  },
  display: {
    dateInput: 'input',
    monthYearLabel: { year: 'numeric', month: 'numeric' },
    dateA11yLabel: { year: 'numeric', month: 'long', day: 'numeric' },
    monthYearA11yLabel: { year: 'numeric', month: 'long' },
  },
};
