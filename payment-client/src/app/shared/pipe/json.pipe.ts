import { Pipe, PipeTransform } from '@angular/core';
import * as vkbeautify from 'vkbeautify';

@Pipe({
  name: 'json_beautify',
})
export class JsonPipe implements PipeTransform {
  transform(value: string): string {
    return vkbeautify.json(value);
  }
}
