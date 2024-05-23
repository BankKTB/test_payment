import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberSequence',
})
export class NumberSequencePipe implements PipeTransform {
  transform(value: any, size?: number): string {
    if (value === null || value === undefined) {
      return '';
    }
    return this.pad(value, size);
  }

  pad(num: number, size: number): string {
    let s = num + '';
    while (s.length < size) {
      s = '0' + s;
    }
    return s;
  }
}
