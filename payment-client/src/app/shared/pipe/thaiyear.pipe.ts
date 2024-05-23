import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'thaiyear',
})
export class ThaiyearPipe implements PipeTransform {
  transform(year: string): number {
    if(year) {
      return Number(year) + 543;
    }
  }
}
