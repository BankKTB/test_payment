import { Component, Inject, OnInit } from '@angular/core';
import { DialogData } from '@shared/component/dialog-status/dialog-save-parameter/dialog-save-parameter.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog-confirm-delete-master',
  templateUrl: './dialog-confirm-delete-master.component.html',
  styleUrls: ['./dialog-confirm-delete-master.component.scss'],
})
export class DialogConfirmDeleteMasterComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogConfirmDeleteMasterComponent>,
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
