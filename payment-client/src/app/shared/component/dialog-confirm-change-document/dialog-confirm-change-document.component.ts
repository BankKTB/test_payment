import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { FiService } from '@core/services/fi/fi.service';
import { DialogOmSearchDetailComponent } from '@shared/component/dialog-om-search-detail/dialog-om-search-detail.component';
import { LoaderService } from '@core/services';
import { PaymentService } from '@core/services/payment/payment.service';

export interface DialogData {
  payload: any;
  oldValueTemp: any;
}

@Component({
  selector: 'app-dialog-confirm-change-document',
  templateUrl: './dialog-confirm-change-document.component.html',
  styleUrls: ['./dialog-confirm-change-document.component.scss'],
})
export class DialogConfirmChangeDocumentComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  header = null;
  displayedColumns: string[] = [
    'choose',
    'date',
    'time',
    'compCode',
    'accDocNo',
    'docType',
    'fiscalYear',
    'line',
    'valueOld',
    'valueNew',
    'reason',
    'userPost',
    'userName',
    'status',
  ];

  isConfirm = false;

  listValidate = [];
  isClose = false;

  constructor(
    public dialogRef: MatDialogRef<DialogConfirmChangeDocumentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentBlockService: PaymentBlockService,
    private fiService: FiService,
    private loaderService: LoaderService,
    private paymentService: PaymentService
  ) {
    this.dialogRef.disableClose = true;
    console.log(this.data);
    // dialogRef.backdropClick().subscribe(() => {
    //   this.dialogRef.close({ event: 'save' });
    // });
  }

  ngOnInit() {}

  sendDocumentUpdateToIdem() {
    this.loaderService.loadingToggleStatus(true);
    this.fiService.updateLineVendor(this.data.payload).then((result) => {
      if (result.status === 200) {
        this.isClose = false;
        this.dialogRef.close({ event: 'save' });
        // this.changeDocument();
      } else if (result.status === 422) {
        console.log(result);
        this.isClose = true;
        this.listValidate = result.error.data.errors;
      }
    });
  }

  changeDocument() {
    this.paymentService.changeDocument(this.data.payload).then((result) => {
      console.log('changeDocument');
      console.log(result);
      if (result.status === 200) {
        this.dialogRef.close({ event: 'save' });
      } else if (result.status === 422) {
      }
    });
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'close' });
  }

  clickConfirm(): void {
    this.isConfirm = true;
    this.sendDocumentUpdateToIdem();
  }

  clickCancel(): void {
    this.dialogRef.close({ event: 'close' });
  }

  chooseDataSearchDetail(element) {
    const payload = {
      compCode: element.companyCode,
      docNo: element.originalDocumentNo,
      fiscalYear: element.originalFiscalYear,
      webInfo: element.webInfo,
    };
    console.log(payload);
    const dialogRef = this.dialog.open(DialogOmSearchDetailComponent, {
      width: '80vw',
      data: {
        payload,
      },
    });
    // this.fiService.paymentBlockDetail(payload).then((data) => {
    //   console.log(data);
    //   const response = data as any;
    //   if (response.status === 200) {
    //     const dialogRef = this.dialog.open(DialogOmSearchDetailComponent, {
    //       width: '80vw',
    //       data: {
    //         item: response.data,
    //         payload
    //       },
    //     });
    //     // this.listResult = ;
    //   }
    // });
  }

  isUpdateValueByOldTemp(objKey: string): boolean {
    return this.data.payload[objKey] !== this.data.oldValueTemp[objKey];
  }
}
