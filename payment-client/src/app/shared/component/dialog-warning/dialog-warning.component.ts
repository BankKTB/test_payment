import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
export interface DialogData {
  textWarning: string;
}
@Component({
  selector: 'app-dialog-warning',
  templateUrl: './dialog-warning.component.html',
  styleUrls: ['./dialog-warning.component.scss'],
})
export class DialogWarningComponent implements OnInit {
  text = '';

  constructor(
    public dialogRef: MatDialogRef<DialogWarningComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย

      this.dialogRef.close({ event: true });
    });
    dialogRef.disableClose = true;
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    this.text = this.data.textWarning;
  }

  onNoClick(): void {
    this.dialogRef.close({ event: true });
  }
}
