import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LoaderService } from '@core/services';
import { PaymentRunErrorService } from '@core/services/payment-run-error/payment-run-error.service';

export interface DialogData {
  value: any;
}

@Component({
  selector: 'app-dialog-payment-run-error-detail',
  templateUrl: './dialog-payment-run-error-detail.component.html',
  styleUrls: ['./dialog-payment-run-error-detail.component.scss'],
})
export class DialogPaymentRunErrorDetailComponent implements OnInit {
  resultSave = null;

  isSaveSuccess = false;

  listDocument = [];
  header = null;
  document;

  p = 1;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    public dialogRef: MatDialogRef<DialogPaymentRunErrorDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentBlockService: PaymentBlockService,
    private loaderService: LoaderService,
    private paymentRunErrorService: PaymentRunErrorService
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
  }

  onNoClick(): void {
    this.dialogRef.close({});
  }
}
