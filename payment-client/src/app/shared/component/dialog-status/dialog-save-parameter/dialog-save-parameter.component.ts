import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

export interface DialogData {}

@Component({
  selector: 'app-dialog-save-parameter',
  templateUrl: './dialog-save-parameter.component.html',
  styleUrls: ['./dialog-save-parameter.component.scss'],
})
export class DialogSaveParameterComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogSaveParameterComponent>,
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
