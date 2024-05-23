import { Component, Inject, OnInit } from '@angular/core';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface DialogData {
  textConfirm: '';
  title: '';
}

@Component({
  selector: 'app-dialog-confirm',
  templateUrl: './dialog-confirm.component.html',
  styleUrls: ['./dialog-confirm.component.scss'],
})
export class DialogConfirmComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogConfirmComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.dialogRef.disableClose = true;
    dialogRef.backdropClick().subscribe(() => {
      this.closeDialog();
    });
  }

  closeDialog(): void {
    this.dialogRef.close({
      event: false,
      value: 'cancel',
    });
  }

  ngOnInit() {}

  clickConfirm() {
    this.dialogRef.close({
      event: true,
      value: 'save',
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: false,
      value: 'cancel',
    });
  }
}
