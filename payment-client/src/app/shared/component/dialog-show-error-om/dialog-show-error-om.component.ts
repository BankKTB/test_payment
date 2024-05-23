import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
export interface DialogData {
  value: any;
}
@Component({
  selector: 'app-dialog-show-error-om',
  templateUrl: './dialog-show-error-om.component.html',
  styleUrls: ['./dialog-show-error-om.component.scss'],
})
export class DialogShowErrorOmComponent implements OnInit {
  resultSave = null;

  isSaveSuccess = false;

  listDocument = [];
  header = null;
  document;

  p = 1;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    public dialogRef: MatDialogRef<DialogShowErrorOmComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.dialogRef.disableClose = true;
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      if (this.isSaveSuccess && this.resultSave.accDocNo) {
        this.dialogRef.close({ event: 'savesucess' });
      } else {
        this.dialogRef.close({ event: 'close' });
      }
    });
  }

  ngOnInit() {
    this.listDocument = this.data.value;
    console.log(this.listDocument);
  }

  onNoClick(): void {
    this.dialogRef.close({});
  }
}
