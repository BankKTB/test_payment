import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

export interface DialogData {}

@Component({
  selector: 'app-dialog-search-vendor',
  templateUrl: './dialog-search-vendor.component.html',
  styleUrls: ['./dialog-search-vendor.component.scss'],
})
export class DialogSearchVendorComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DialogSearchVendorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    dialogRef.disableClose = false;
  }

  closeDialog(): void {
    const dialogRef = this.dialogRef.close();
  }

  ngOnInit() {}
}
