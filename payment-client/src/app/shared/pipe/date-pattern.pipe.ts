import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'datePattern',
})
export class DatePatternPipe implements PipeTransform {
  transform(date: any, format?: string): string {
    if (date === null || date === undefined) {
      return ' - ';
    }
    let inputDate = new Date(date);
    if (Array.isArray(date)) {
      inputDate = new Date(
        date[0],
        date[1],
        date[2],
        !!date[3] && date[3],
        !!date[4] && date[4],
        !!date[5] && date[5],
        !!date[6] && date[6]
      );
    }

    const fullYear = inputDate.getFullYear() + 543;
    const day = +inputDate.getDate() < 10 ? '0' + inputDate.getDate() : inputDate.getDate();
    const month =
      +(inputDate.getMonth() + 1) < 10
        ? '0' + (inputDate.getMonth() + 1)
        : inputDate.getMonth() + 1;
    let returnDate: string;
    if (format === 'full') {
      returnDate = day + '.' + month + '.' + fullYear;
    }
    return returnDate;
  }
}
