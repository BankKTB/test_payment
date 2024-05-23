import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LoaderService } from '@core/services';
import { DialogDetailDocumentColumnTableComponent } from '@shared/component/dialog-detail-document-column-table/dialog-detail-document-column-table.component';

import { MatSort } from '@angular/material/sort';

export interface DialogData {
  document: any;
}

@Component({
  selector: 'app-dialog-om-log',
  templateUrl: './dialog-om-log.component.html',
  styleUrls: ['./dialog-om-log.component.scss'],
})
export class DialogOmLogComponent implements OnInit {
  resultSave = null;

  isSaveSuccess = false;

  listDocument = [];
  header = null;
  document;

  p = 1;

  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    public dialogRef: MatDialogRef<DialogOmLogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentBlockService: PaymentBlockService,
    private loaderService: LoaderService
  ) {
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
    this.document = this.data.document;
    this.searchPaymentBlockLogDetail(this.data.document);
    // this.findPage();
    // this.save(0, this.data.head, this.data.item);
  }

  searchPaymentBlockLogDetail(document) {
    console.log(document);
    // this.loaderService.loadingToggleStatus(true);
    this.paymentBlockService
      .searchPaymentBlockLogDetail(document.accDocNo + document.compCode + document.fiscalYear)
      .then((data) => {
        console.log(data);
        // this.loaderService.loadingToggleStatus(false);
        const response = data as any;
        const result = response.data;

        if (result) {
          this.listDocument = result;
        }
      });
  }

  onNoClick(): void {
    this.dialogRef.close({});
  }
}
