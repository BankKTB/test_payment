import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
export interface DialogData {
  referenceDoc: string;
  type: string;
  listValidate: any;
  textError: string;
}
@Component({
  selector: 'app-dialog-result',
  templateUrl: './dialog-result.component.html',
  styleUrls: ['./dialog-result.component.scss'],
})
export class DialogResultComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogResultComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  closeDialog(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
    console.log(this.data.listValidate);
  }

  clickConfirm() {
    this.dialogRef.close({
      event: true,
      value: 'CONFIRM',
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
