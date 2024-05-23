import { Directive, ElementRef, HostListener, Input } from '@angular/core';
// noinspection DuplicatedCode
const input = Input('numericType');

@Directive({
  selector: '[appEnglishOnly]',
})
export class EnglishOnlyDirective {
  @input numericType: string;
  inputElement: HTMLInputElement;
  private regex = {
    english: new RegExp(/^[A-Za-z0-9_:,.\s]+$/),
  };

  constructor(private el: ElementRef) {
    this.inputElement = el.nativeElement;
  }

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

    this.checkEnglishOnly(event);
  }

  @HostListener('keyup', ['$event'])
  onKeyUp(e: KeyboardEvent) {
    this.checkEnglishOnly(e);
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const pastedInput: string = event.clipboardData.getData('text/plain');
    const pasted = !String(pastedInput).match(this.regex.english);
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
    const pasted = !String(textData).match(this.regex.english);
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

  checkEnglishOnly(event) {
    const current: string = this.el.nativeElement.value;
    const next: string = current.concat(event.key);
    if (next && !String(next).match(this.regex.english)) {
      event.preventDefault();
    }
  }
}
