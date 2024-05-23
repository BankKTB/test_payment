import { Pipe, PipeTransform } from '@angular/core';
import { Constant } from '@shared/utils/constant';

@Pipe({
  name: 'reasonReverse',
})
export class ReasonReversePipe implements PipeTransform {
  constructor(private constant: Constant) {}

  transform(reason: string) {
    const object = this.constant.LIST_REASON_BACK_LIST.filter((item) => item.id === reason);
    if (object.length > 0) {
      return object[0].name;
    } else {
      return reason;
    }
  }
}
