import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-base-text-area',
  templateUrl: './base-text-area.component.html',
  styleUrls: ['./base-text-area.component.scss'],
})
export class BaseTextAreaComponent implements OnInit {
  @Input() maxLength: number = 200;
  @Input() value: any;
  @Input() isDisabled: boolean = false;
  @Output() onChangeValue = new EventEmitter<any>();
  remainingText: string = '1000';
  currentValue: string = '';
  constructor() {
    this.remainingText = this.renderRemainText((this.value && this.value.length) || 0);
  }

  ngOnInit() {}

  onKeyUpValue(e) {
    this.remainingText = this.renderRemainText(e.target.value.length);
  }

  onBlurValue(e) {
    this.onChangeValue.emit(e);
  }

  renderRemainText(remainLength: number) {
    return `${remainLength}/${(this.maxLength && this.maxLength.toString()) || 0}`;
  }
}
