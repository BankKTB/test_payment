import { Directive, ElementRef, forwardRef, HostListener, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import Decimal from 'decimal.js';

export const CURRENCY_INPUT_MASK_DIRECTIVE_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => CurrencyDirective),
  multi: true,
};

@Directive({
  selector: '[appCurrency]',
  providers: [CURRENCY_INPUT_MASK_DIRECTIVE_VALUE_ACCESSOR, DecimalPipe],
})
export class CurrencyDirective implements ControlValueAccessor, OnInit {
  @Input() decimal? = false;
  private el: HTMLInputElement;
  private onModelChange;
  private onModelTouched;
  private lastNumVal: Decimal;
  private lastStrVal: string;
  private DECIMAL_MARK = '.';

  constructor(private elementRef: ElementRef) {
    this.el = elementRef.nativeElement;
  }

  private static transformWithPipe(str: string): string {
    // return this.decimalPipe.transform(str, '1.2-2');
    return str.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }

  private static getUnmaskedValue(value: string): string {
    return value.replace(/[^-\d\\.]/g, '');
  }

  private static convertStrToDecimal(str: string): Decimal {
    // return this.isNumeric(str) ? Math.abs(parseFloat(str)) : null;
    return CurrencyDirective.isNumeric(str) ? new Decimal(str) : null;
  }

  private static convertDecimalToStr(n: Decimal): string {
    return CurrencyDirective.isNumeric(n) ? new Decimal(n).toFixed(2) + '' : '';
  }

  private static isNumeric(n: any): boolean {
    return !isNaN(parseFloat(n)) && isFinite(n);
  }

  ngOnInit() {
    // console.log(this.elementRef.nativeElement);
    // this.el = this.elementRef.nativeElement;
  }

  @HostListener('focus')
  handleFocus() {
    const strVal: string = this.getInputValue();
    const unmaskedStr: string = CurrencyDirective.getUnmaskedValue(strVal);
    this.updateInputValue(unmaskedStr);
  }

  @HostListener('cut')
  handleCut() {
    setTimeout(() => {
      this.inputUpdated();
    }, 0);
  }

  @HostListener('keypress', ['$event'])
  handleKeypress(event: any) {
    // Restrict characters
    const newChar: string = String.fromCharCode(event.which);
    const allowedChars: RegExp = /^[\d.]+$/;
    if (!allowedChars.test(newChar)) {
      event.preventDefault();
      return;
    }
    // Handle decimal mark input
    const currentValue: string = event.target.value;
    const separatorIdx: number = currentValue.indexOf(this.DECIMAL_MARK);
    const hasFractionalPart: boolean = separatorIdx >= 0;
    if (!hasFractionalPart || newChar !== this.DECIMAL_MARK) {
      return;
    }
    const isOutsideSelection = !this.isIdxBetweenSelection(separatorIdx);
    if (isOutsideSelection) {
      const positionAfterMark = separatorIdx + 1;
      this.setCursorPosition(positionAfterMark);
      event.preventDefault();
      return;
    }
  }

  @HostListener('input')
  handleInput() {
    this.inputUpdated();
  }

  // @HostListener('paste', ['$event'])
  // handlePaste(event: any) {
  //   setTimeout(() => {
  //     this.inputUpdated();
  //   }, 1);
  // }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const pastedInput: string = event.clipboardData.getData('text/plain');
    let pasted: boolean;
    // if (this.decimal) {
    //   pasted = document.execCommand('insertText', false, pastedInput.replace(/[^0-9]/g, ''));
    // } else if (this.isValidDecimal(pastedInput)) {
    pasted = document.execCommand('insertText', false, pastedInput.replace(/[^0-9.]/g, ''));
    // }
    if (pasted) {
      event.preventDefault();
    } else {
      if (navigator.clipboard) {
        navigator.clipboard.writeText(pastedInput).then(() => {
          document.execCommand('paste');
        });
      }
    }
  }

  @HostListener('blur', ['$event'])
  handleBlur(event: any) {
    const strVal: string = this.getInputValue();
    const numVal: Decimal = CurrencyDirective.convertStrToDecimal(strVal);
    this.maskInput(numVal);
    this.onModelTouched.apply(event);
  }

  registerOnChange(callbackFunction): void {
    this.onModelChange = callbackFunction;
  }

  registerOnTouched(callbackFunction): void {
    this.onModelTouched = callbackFunction;
  }

  setDisabledState(value: boolean): void {
    this.el.disabled = value;
  }

  writeValue(numValue: Decimal): void {
    this.maskInput(numValue);
  }

  private maskInput(numVal: Decimal): void {
    if (!CurrencyDirective.isNumeric(numVal)) {
      this.updateInputValue('');
      return;
    }
    const strVal: string = CurrencyDirective.convertDecimalToStr(numVal);
    const newVal: string = CurrencyDirective.transformWithPipe(strVal);

    numVal = new Decimal(numVal);
    if (numVal.comparedTo(1000000000000000) >= 0) {
      this.updateInputValue(new Decimal('999999999999999.99').toFixed(2));
    } else {
      this.updateInputValue(newVal);
    }
  }

  private inputUpdated() {
    this.restrictDecimalValue();
    const strVal: string = this.getInputValue();
    const unmaskedVal: string = CurrencyDirective.getUnmaskedValue(strVal);
    const numVal: Decimal = CurrencyDirective.convertStrToDecimal(unmaskedVal);
    if (numVal) {
      if (numVal.comparedTo(1000000000000000) >= 0) {
        this.updateInputValue(this.lastStrVal, true);
        return;
      }
    }

    if (numVal !== this.lastNumVal) {
      this.lastNumVal = numVal;
      this.lastStrVal = strVal;
      this.onModelChange(numVal);
      this.lastNumVal = undefined;
    }
  }

  private restrictDecimalValue(): void {
    const strVal: string = this.getInputValue();
    const dotIdx: number = strVal.indexOf(this.DECIMAL_MARK);
    const hasFractionalPart: boolean = dotIdx >= 0;
    if (hasFractionalPart) {
      const fractionalPart: string = strVal.substring(dotIdx + 1);
      if (fractionalPart.length > 2) {
        const choppedVal: string = strVal.substring(0, dotIdx + 3);
        this.updateInputValue(choppedVal, true);
        return;
      }
    }
  }

  private updateInputValue(value: string, savePosition = false) {
    if (savePosition) {
      this.saveCursorPosition();
    }

    if (value !== undefined && this.el.value !== undefined) {
      this.el.value = value;
    } else {
      this.el.value = '';
    }
  }

  private getInputValue(): string {
    return this.el.value;
  }

  private saveCursorPosition() {
    const position: number = this.el.selectionStart;
    setTimeout(() => {
      this.setCursorPosition(position);
    }, 1);
  }

  private setCursorPosition(position: number) {
    this.el.selectionStart = position;
    this.el.selectionEnd = position;
  }

  private isIdxBetweenSelection(idx: number) {
    if (this.el.selectionStart === this.el.selectionEnd) {
      return false;
    }
    return idx >= this.el.selectionStart && idx < this.el.selectionEnd;
  }
}
