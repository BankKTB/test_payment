import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { FiService } from '@core/services/fi/fi.service';
import { LoaderService } from '@core/services';
import { PaymentService } from '@core/services/payment/payment.service';
import { DialogOmSearchDetailComponent } from '@shared/component/dialog-om-search-detail/dialog-om-search-detail.component';
export interface DialogData {
  payload: any;
}

@Component({
  selector: 'app-dialog-bank-account-detail',
  templateUrl: './dialog-bank-account-detail.component.html',
  styleUrls: ['./dialog-bank-account-detail.component.scss'],
})
export class DialogBankAccountDetailComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  constructor(
    public dialogRef: MatDialogRef<DialogBankAccountDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant
  ) {
    this.dialogRef.disableClose = true;
  }

  ngOnInit() {}

  onNoClick(): void {
    this.dialogRef.close({ event: 'close' });
  }

  clickCancel(): void {
    this.dialogRef.close({ event: 'close' });
  }
}
