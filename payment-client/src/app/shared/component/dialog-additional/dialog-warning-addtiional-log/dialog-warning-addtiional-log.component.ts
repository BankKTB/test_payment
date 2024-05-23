import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

export interface DialogData {}

@Component({
  selector: 'app-dialog-warning-addtiional-log',
  templateUrl: './dialog-warning-addtiional-log.component.html',
  styleUrls: ['./dialog-warning-addtiional-log.component.scss'],
})
export class DialogWarningAddtiionalLogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogWarningAddtiionalLogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      this.dialogRef.close({
        event: true,
        value: 'Cancel',
      });
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  ngOnInit() {}

  clickSave() {
    this.dialogRef.close({
      event: true,
      value: 'Save',
    });
  }

  clickUnSave() {
    this.dialogRef.close({
      event: true,
      value: 'UnSave',
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }
}
