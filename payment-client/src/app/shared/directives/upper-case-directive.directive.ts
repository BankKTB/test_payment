import { Directive, ElementRef, HostListener, Input } from '@angular/core';

const input = Input('numericType');

@Directive({
  selector: '[appUpperCaseDirective]',
})
export class UpperCaseDirectiveDirective {
  @input numericType: string;

  inputElement: HTMLInputElement;

  constructor(private el: ElementRef) {
    this.inputElement = el.nativeElement;
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent) {
    this.updateInput(event);
  }

  @HostListener('keyup', ['$event'])
  onKeyUp(e: KeyboardEvent) {
    this.updateInput(e);
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const pastedInput: string = event.clipboardData.getData('text/plain');
    this.updateInput(pastedInput);
  }

  @HostListener('drop', ['$event'])
  onDrop(event: DragEvent) {
    const textData = event.dataTransfer.getData('text');
    this.inputElement.focus();
    this.updateInput(textData);
  }

  updateInput(event) {
    const current: string = this.el.nativeElement.value;
    this.el.nativeElement.value = current.toUpperCase();
  }
}
