import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timePattern',
})
export class TimePatternPipe implements PipeTransform {
  transform(date: any, format?: string): string {
    if (date === null || date === undefined) {
      return ' - ';
    }
    let convertDate = new Date(date);
    if (Array.isArray(date)) {
      convertDate = new Date(
        date[0],
        date[1],
        date[2],
        !!date[3] && date[3],
        !!date[4] && date[4],
        !!date[5] && date[5],
        !!date[6] && date[6]
      );
    }
    if (convertDate instanceof Date && !isNaN(convertDate.valueOf())) {
      const hour = ('0' + convertDate.getHours()).slice(-2);
      const minute = ('0' + convertDate.getMinutes()).slice(-2);
      const second = ('0' + convertDate.getSeconds()).slice(-2);
      return `${hour}:${minute}:${second}`;
    }
    return 'Invalid Value';
  }
}
