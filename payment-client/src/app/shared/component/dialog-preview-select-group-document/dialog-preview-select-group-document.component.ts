import { Component, Inject, OnInit } from '@angular/core';
import { UserProfile } from '@core/models/user-profile';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Utils } from '@shared/utils/utils';
import Decimal from 'decimal.js';
import { SelectGroupDocumentService } from '@core/services/select-group-document/select-group-document.service';

export interface DialogData {
  object: any;
  request: any;
}

@Component({
  selector: 'app-dialog-preview-select-group-document',
  templateUrl: './dialog-preview-select-group-document.component.html',
  styleUrls: ['./dialog-preview-select-group-document.component.scss'],
})
export class DialogPreviewSelectGroupDocumentComponent implements OnInit {
  button = 'Submit';

  listPreviewDocument = null;
  listSuccessDocument = null;
  listUnSuccessDocument = null;
  sumTotalAmount = new Decimal(0);
  userProfile: UserProfile = null;

  isConfirm = false;

  constructor(
    public dialogRef: MatDialogRef<DialogPreviewSelectGroupDocumentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private dialog: MatDialog,
    public utils: Utils,
    public selectGroupDocumentService: SelectGroupDocumentService
  ) {
    this.dialogRef.disableClose = true;
  }

  ngOnInit() {
    console.log(this.data);
    this.listPreviewDocument = this.data.object.unBlockDocuments;
    this.sumTotalAmount = this.data.object.sumAmount;
  }

  viewDocument(item) {
    const url =
      './detail-fi-kb?compCode=' +
      item.companyCode +
      '&docNo=' +
      item.accDocNo +
      '&docYear=' +
      item.fiscalYear;
    window.open(url, 'name', 'width=1200,height=700');
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'close' });
  }

  clickConfirm(): void {
    this.isConfirm = true;
    this.selectGroupDocumentService.create(this.data.request).then((response) => {
      if (response.status === 201) {
        this.listSuccessDocument = response.data.success;
        this.listUnSuccessDocument = response.data.unSuccess;
      }
    });
  }

  clickCancel(): void {
    this.dialogRef.close({ event: 'close' });
  }
}
