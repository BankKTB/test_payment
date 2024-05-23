import { Component, Inject, OnInit } from '@angular/core';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface DialogData {
  statusActive: '';
}
@Component({
  selector: 'app-dialog-confirm-status-usability',
  templateUrl: './dialog-confirm-status-usability.component.html',
  styleUrls: ['./dialog-confirm-status-usability.component.scss'],
})
export class DialogConfirmStatusUsabilityComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogConfirmStatusUsabilityComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    // dialogRef.backdropClick().subscribe(() => {
    //   console.log('close dialog');
    //   // ส่ง event param ตาม ปกติเลย
    //   this.dialogRef.close({
    //     event: true,
    //     value: 'Cancel',
    //   });
    // });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  ngOnInit() {}

  clickDelete() {
    this.dialogRef.close({
      event: true,
      value: 'Delete',
    });
  }

  clickUnDelete() {
    this.dialogRef.close({
      event: true,
      value: 'UnDelete',
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }
}
