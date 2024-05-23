import { Directive, ElementRef, HostListener, Input, Self, Optional } from '@angular/core';
import { NgControl } from '@angular/forms';
import { Constant } from '@shared/utils/constant';

const NAVIGATION_KEYS = [
  'ArrowDown',
  'ArrowUp',
  'ArrowLeft',
  'ArrowRight',
  'Down',
  'Up',
  'Left',
  'Right',
  'Enter',
  'Esc',
  'Escape',
  'Backspace',
];

const CONTROL_KEYS = ['a', 'c', 'v', 'x'];

@Directive({
  selector: '[appDateInput]',
})
export class InputDateDirective {
  @Input() _minDate;
  @Input() _formControlName;
  private INVALID_PATTERN_INPUT = /^[\d.]+$/;
  private INVALID_PATTERN = /^(0?[1-9]|[12][0-9]|3[01])[\/\.](0?[1-9]|1[012])[\/\.]\d{4}$/;
  private STRIP_INVALID_PATTERN = new RegExp(this.INVALID_PATTERN, 'g');
  private STRIP_INVALID_PATTERN_INPUT = new RegExp(this.INVALID_PATTERN_INPUT, 'g');
  constructor(
    private element: ElementRef,
    @Self() @Optional() private ngControl: NgControl,
    private constant: Constant
  ) {
    // this.element.nativeElement.setAttribute('maxLength', this.max);
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    const key = event.key;
    if (NAVIGATION_KEYS.includes(key) || this.isControlKey(event)) {
      return;
    }
    const value = this.element.nativeElement.value;
    if (value.length > 9 || (key && !String(key).match(this.STRIP_INVALID_PATTERN_INPUT))) {
      event.preventDefault();
      event.stopPropagation();
    }
  }

  @HostListener('blur')
  onBlur() {
    const value = this.element.nativeElement.value;
    if (!!this.ngControl) {
      if (value.length > 0 && value.length < 9) {
        this.ngControl.control.setErrors({ wrongDateInput: true });
      }
      if (value.length < 1) {
        this.ngControl.control.setErrors({ wrongDateInput: null });
        this.ngControl.control.updateValueAndValidity();
      }

      if (this.ngControl.value < this._minDate) {
        this.ngControl.control.setErrors({ wrongDateInput: null });
        this.ngControl.control.updateValueAndValidity();
      }
    }
  }

  @HostListener('focus', ['$event.target'])
  onFocus(target: any) {
    const dateArr = target.value.split(' ');
    if (dateArr.length > 2) {
      let day: string = dateArr[0].toString();
      let monthObj = this.constant.MONTH_FULL_TH.find((e) => e.name === dateArr[1].trim());
      let month: string;
      if (monthObj) {
        month = monthObj.month;
      }
      month = +month < 10 ? '0' + month : month;
      let year = dateArr[2];
      this.element.nativeElement.value = `${day}.${month}.${year}`;
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    event.preventDefault();
    event.stopPropagation();

    const data = event.clipboardData.getData('text/plain').replace(this.STRIP_INVALID_PATTERN, '');

    document.execCommand('insertText', false, data);
  }

  @HostListener('drop', ['$event'])
  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();

    const data = event.dataTransfer.getData('text').replace(this.STRIP_INVALID_PATTERN, '');
    this.element.nativeElement.focus();
    document.execCommand('insertText', false, data);
  }

  private isControlKey(event: KeyboardEvent) {
    const isControl = event.ctrlKey || event.metaKey;
    return isControl && CONTROL_KEYS.includes(event.key);
  }
}
