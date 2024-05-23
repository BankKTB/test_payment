import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DialogConfirmComponent } from '@shared/component/dialog-confirm/dialog-confirm.component';
import { Constant } from '@shared/utils/constant';
import { log } from 'util';

@Component({
  selector: 'delete-button',
  templateUrl: './delete-button.component.html',
  styles: [':host { display: contents; }'],
})
export class DeleteButtonComponent implements OnInit {
  @Input() _isDisabled: boolean;
  @Input() _stringConfirm: string;
  @Output() _onDelete: EventEmitter<any> = new EventEmitter<any>();
  constructor(private dialog: MatDialog, private constant: Constant) {}

  ngOnInit(): void {
  }

  onDelete() {
    const dialogRef = this.dialog.open(DialogConfirmComponent, {
      width: '40vw',
      panelClass: 'dialog-confirm',
      data: {
        isShowConfirm: true,
        title: 'ลบข้อมูล',
        textConfirm: this._stringConfirm,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.value === 'save') {
          this._onDelete.emit();
        }
      }
    });
  }
}
