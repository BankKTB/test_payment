import { Pipe, PipeTransform } from '@angular/core';
import { Constant } from '@shared/utils/constant';

@Pipe({
  name: 'detailPaymentBlock',
})
export class DetailPaymentBlockPipe implements PipeTransform {
  constructor(private constant: Constant) {}
  transform(reason: string) {
    const object = this.constant.LIST_PAYMENT_BLOCK.filter((item) => item.id === reason);
    if (object.length > 0) {
      return object[0].name;
    } else {
      return reason;
    }
  }
}
