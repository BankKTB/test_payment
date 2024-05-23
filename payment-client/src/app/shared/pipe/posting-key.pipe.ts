import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'posting',
})
export class PostingKeyPipe implements PipeTransform {
  transform(key): string {
    let posting = '';
    if (key === '11') {
      posting = 'เครดิต';
    } else if (key === '12') {
      posting = 'เครดิต';
    } else if (key === '13') {
      posting = 'เครดิต';
    } else if (key === '14') {
      posting = 'เครดิต';
    } else if (key === '15') {
      posting = 'เครดิต';
    } else if (key === '16') {
      posting = 'เครดิต';
    } else if (key === '17') {
      posting = 'เครดิต';
    } else if (key === '18') {
      posting = 'เครดิต';
    } else if (key === '19') {
      posting = 'เครดิต';
    } else if (key === '31') {
      posting = 'เครดิต';
    } else if (key === '32') {
      posting = 'เครดิต';
    } else if (key === '34') {
      posting = 'เครดิต';
    } else if (key === '35') {
      posting = 'เครดิต';
    } else if (key === '36') {
      posting = 'เครดิต';
    } else if (key === '37') {
      posting = 'เครดิต';
    } else if (key === '38') {
      posting = 'เครดิต';
    } else if (key === '39') {
      posting = 'เครดิต';
    } else if (key === '50') {
      posting = 'เครดิต';
    } else if (key === '75') {
      posting = 'เครดิต';
    } else if (key === '96') {
      posting = 'เครดิต';
    } else if (key === '91') {
      posting = 'เครดิต';
    } else if (key === '01') {
      posting = 'เดบิต';
    } else if (key === '02') {
      posting = 'เดบิต';
    } else if (key === '03') {
      posting = 'เดบิต';
    } else if (key === '04') {
      posting = 'เดบิต';
    } else if (key === '05') {
      posting = 'เดบิต';
    } else if (key === '06') {
      posting = 'เดบิต';
    } else if (key === '07') {
      posting = 'เดบิต';
    } else if (key === '08') {
      posting = 'เดบิต';
    } else if (key === '09') {
      posting = 'เดบิต';
    } else if (key === '21') {
      posting = 'เดบิต';
    } else if (key === '22') {
      posting = 'เดบิต';
    } else if (key === '24') {
      posting = 'เดบิต';
    } else if (key === '25') {
      posting = 'เดบิต';
    } else if (key === '26') {
      posting = 'เดบิต';
    } else if (key === '27') {
      posting = 'เดบิต';
    } else if (key === '28') {
      posting = 'เดบิต';
    } else if (key === '29') {
      posting = 'เดบิต';
    } else if (key === '40') {
      posting = 'เดบิต';
    } else if (key === '70') {
      posting = 'เดบิต';
    } else if (key === '86') {
      posting = 'เดบิต';
    } else if (key === '81') {
      posting = 'เดบิต';
    }
    return posting;
  }
}
