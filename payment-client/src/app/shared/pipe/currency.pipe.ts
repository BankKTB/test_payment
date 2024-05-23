import { Pipe, PipeTransform } from '@angular/core';

const padding = '000000';

@Pipe({
  name: 'mycurrency',
})
export class CurrencyPipe implements PipeTransform {
  private readonly prefix: string;
  private readonly decimalSeparator: string;
  private readonly thousandsSeparator: string;
  private readonly suffix: string;

  constructor() {
    this.prefix = '';
    this.suffix = '';
    this.decimalSeparator = '.';
    this.thousandsSeparator = ',';
  }

  transform(value: string, fractionSize: number = 0): string {
    if (parseFloat(value) % 1 !== 0) {
      fractionSize = 2;
    }
    let [integer, fraction = ''] = (parseFloat(value).toString() || '').toString().split('.');

    fraction =
      fractionSize > 0
        ? this.decimalSeparator + (fraction + padding).substring(0, fractionSize)
        : '';
    integer = integer.replace('/B(?=(d{3})+(?!d))/g', this.thousandsSeparator);
    if (isNaN(parseFloat(integer))) {
      integer = '0';
    }
    return this.prefix + integer + fraction + this.suffix;
  }

  parse(value: string, fractionSize: number = 2): string {
    let [integer, fraction = ''] = (value || '')
      .replace(this.prefix, '')
      .replace(this.suffix, '')
      .split(this.decimalSeparator);

    integer = integer.replace(new RegExp(',', 'g'), '');

    fraction =
      parseInt(fraction, 10) > 0 && fractionSize > 0
        ? this.decimalSeparator + (fraction + padding).substring(0, fractionSize)
        : '';

    return integer + fraction;
  }
}
