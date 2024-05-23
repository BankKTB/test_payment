import { Pipe, PipeTransform } from '@angular/core';
import { Decimal } from 'decimal.js';

@Pipe({
  name: 'bigdecimal',
})
export class CustomDecimalPipe implements PipeTransform {
  transform(n: any): any {
    // console.log(n);
    if (n) {
      return new Decimal(n).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    } else {
      // return ''
      return new Decimal(0).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }
  }
}
