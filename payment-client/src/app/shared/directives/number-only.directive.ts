import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appNumberOnly]',
})
export class NumberOnlyDirective {
  @Input() decimal? = false;
  inputElement: HTMLInputElement;
  private regex = new RegExp(/^\d+$/);
  private decimalCounter = 0;

  constructor(private el: ElementRef) {
    this.inputElement = el.nativeElement;
  }

  // noinspection DuplicatedCode,DuplicatedCode
  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    if (
      // Allow: Delete, Backspace, Tab, Escape, Enter
      [46, 8, 9, 27, 13].indexOf(event.keyCode) !== -1 ||
      (event.keyCode === 65 && event.ctrlKey === true) || // Allow: Ctrl+A
      (event.keyCode === 67 && event.ctrlKey === true) || // Allow: Ctrl+C
      (event.keyCode === 86 && event.ctrlKey === true) || // Allow: Ctrl+V
      (event.keyCode === 88 && event.ctrlKey === true) || // Allow: Ctrl+X
      (event.keyCode === 65 && event.metaKey === true) || // Cmd+A (Mac)
      (event.keyCode === 67 && event.metaKey === true) || // Cmd+C (Mac)
      (event.keyCode === 86 && event.metaKey === true) || // Cmd+V (Mac)
      (event.keyCode === 88 && event.metaKey === true) || // Cmd+X (Mac)
      (event.keyCode >= 35 && event.keyCode <= 39) // Home, End, Left, Right
    ) {
      return; // let it happen, don't do anything
    }

    const current: string = this.el.nativeElement.value;
    const next: string = current.concat(event.key);
    if (next && !String(next).match(this.regex)) {
      event.preventDefault();
    }
  }

  @HostListener('keyup')
  onKeyUp() {
    if (!this.decimal) {
      return;
    } else {
      this.decimalCounter = this.el.nativeElement.value.split('.').length - 1;
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const pastedInput: string = event.clipboardData.getData('text/plain');
    let pasted = false;
    if (!this.decimal) {
      pasted = document.execCommand('insertText', false, pastedInput.replace(/[^0-9]/g, ''));
    } else if (this.isValidDecimal(pastedInput)) {
      pasted = document.execCommand('insertText', false, pastedInput.replace(/[^0-9.]/g, ''));
    }
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

  @HostListener('drop', ['$event'])
  onDrop(event: DragEvent) {
    const textData = event.dataTransfer.getData('text');
    this.inputElement.focus();

    let pasted = false;
    if (this.decimal) {
      pasted = document.execCommand('insertText', false, textData.replace(/[^0-9]/g, ''));
    } else if (this.isValidDecimal(textData)) {
      pasted = document.execCommand('insertText', false, textData.replace(/[^0-9.]/g, ''));
    }
    if (pasted) {
      event.preventDefault();
    } else {
      if (navigator.clipboard) {
        navigator.clipboard.writeText(textData).then(() => {
          document.execCommand('paste');
        });
      }
    }
  }

  isValidDecimal(str: string): boolean {
    return str.split('.').length <= 2;
  }
}
