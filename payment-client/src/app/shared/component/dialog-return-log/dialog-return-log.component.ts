import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { PaymentService } from '@core/services/payment/payment.service';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { interval } from 'rxjs';

export interface DialogData {
  textAlert: string;
  listLog: any;
  logType: string;
}

@Component({
  selector: 'app-dialog-return-log',
  templateUrl: './dialog-return-log.component.html',
  styleUrls: ['./dialog-return-log.component.scss'],
})
export class DialogReturnLogComponent implements OnInit {
  @ViewChild('paginatorStep1FileError', { static: false, read: MatPaginator })
  paginatorStep1FileError: MatPaginator;
  @ViewChild('paginatorStep1Success', { static: false, read: MatPaginator })
  paginatorStep1Success: MatPaginator;
  @ViewChild('paginatorStep1NotSuccess', { static: false, read: MatPaginator })
  paginatorStep1NotSuccess: MatPaginator;
  @ViewChild('paginatorStep4', { static: false, read: MatPaginator })
  paginatorStep4: MatPaginator;
  displayedColumnsFileErrors: string[] = ['fileName', 'error'];
  displayedColumnsUpdateStatus: string[] = [
    'refRunning',
    'refLine',
    'paymentDate',
    'paymentName',
    'accountNoFrom',
    'accountNoTo',
    'fileType',
    'fileName',
    'transferDate',
    'vendor',
    'amount',
    'bankFee',
    'fileStatus',
  ];
  displayedColumnsReversePayment: string[] = [
    'documentNo',
    'compCode',
    'fiscalYear',
    'paymentDate',
    'returnStatus',
  ];
  displayedColumnsReverseInvoice: string[] = [
    'documentNo',
    'compCode',
    'fiscalYear',
    'returnStatus',
  ];
  displayedColumnsPull: string[] = [
    'choose',
    'companyCode',
    'documentNo',
    'fiscalYear',
    'reverseCompanyCode',
    'reverseDocumentNo',
    'reverseFiscalYear',
    'status',
  ];

  displayedColumnsPullStep4: string[] = [
    'choose',
    'originalCompanyCode',
    'originalDocumentNo',
    'originalFiscalYear',
    'poDocumentNo',
    'originalIdemStatus',
    'reverseOriginalCompanyCode',
    'reverseOriginalDocumentNo',
    'reverseOriginalFiscalYear',
  ];

  summaryResult: any = {
    process: 0,
    success: 0,
    total: 0,
    fail: 0,
  };
  dataSourceFileError = new MatTableDataSource([]);
  dataSourceUpdateStatusSuccess = new MatTableDataSource([]);
  dataSourceUpdateStatusNotSuccess = new MatTableDataSource([]);
  dataSourceReversePayment = new MatTableDataSource([]);
  dataSourceReverseInvoice = new MatTableDataSource([]);

  dataSourcePull = new MatTableDataSource([]);
  @ViewChild(MatSort, { static: true }) sortPull: MatSort;
  @ViewChild('paginatorPull', { static: true }) paginatorPull: MatPaginator;
  subscription = null;

  constructor(
    public dialogRef: MatDialogRef<DialogReturnLogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private paymentService: PaymentService
  ) {
    dialogRef.backdropClick().subscribe(() => {
      this.dialogRef.close({
        event: true,
        value: 'Cancel',
      });
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
    });
  }

  ngOnInit() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    if (this.data.logType === 'STATUS_STEP1') {
      const listCanUpdate = [];
      const listCanNotUpdate = [];
      this.data.listLog.details.forEach((log) => {
        if (log.logType === 'CAN_UPDATE') {
          listCanUpdate.push(log);
        } else {
          listCanNotUpdate.push(log);
        }
      });
      this.dataSourceFileError = new MatTableDataSource(this.data.listLog.errors);
      this.dataSourceUpdateStatusSuccess = new MatTableDataSource(listCanUpdate);
      this.dataSourceUpdateStatusNotSuccess = new MatTableDataSource(listCanNotUpdate);
      setTimeout(() => {
        this.dataSourceFileError.paginator = this.paginatorStep1FileError;
        this.dataSourceUpdateStatusSuccess.paginator = this.paginatorStep1Success;
        this.dataSourceUpdateStatusNotSuccess.paginator = this.paginatorStep1NotSuccess;
      }, 500);
    } else if (this.data.logType === 'STATUS') {
      const listCanUpdate = [];
      const listCanNotUpdate = [];
      this.data.listLog.forEach((log) => {
        if (log.logType === 'CAN_UPDATE') {
          listCanUpdate.push(log);
        } else {
          listCanNotUpdate.push(log);
        }
      });
      this.dataSourceUpdateStatusSuccess = new MatTableDataSource(listCanUpdate);
      this.dataSourceUpdateStatusNotSuccess = new MatTableDataSource(listCanNotUpdate);
    } else if (this.data.logType === 'PAYMENT_REVERSE') {
      this.pullMass();
      // this.dataSourceReversePayment = new MatTableDataSource(this.data.listLog);
    } else if (this.data.logType === 'PAYMENT_INVOICE') {
      this.pullStep4Mass();
      // this.dataSourceReverseInvoice = new MatTableDataSource(this.data.listLog);
    } else if (this.data.logType === 'NOT_FOUND_COMPLETE') {
      // this.dataSourceReverseInvoice = new MatTableDataSource(this.data.listLog);
    }
  }

  pullMass() {
    this.pullMassReverseDocument(this.data.listLog);
    this.subscription = interval(20000).subscribe(() => {
      this.pullMassReverseDocument(this.data.listLog);
    });
  }

  refresh() {
    this.pullMassReverseDocument(this.data.listLog);
  }

  pullMassReverseDocument(object) {
    this.paymentService.pullMassReverseDocument(object).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSourcePull = new MatTableDataSource(data);
          this.dataSourcePull.sort = this.sortPull;
          this.dataSourcePull.paginator = this.paginatorPull;
          if (result.data.length > 0) {
            this.summaryResult = {
              ...this.summaryResult,
              process: result.data[0].process || '0',
              success: result.data[0].success || '0',
              total: result.data[0].total || '0',
              fail: result.data[0].fail || '0',
            };
            if (result.data[0].process === 0) {
              if (this.subscription) {
                this.subscription.unsubscribe();
              }
            }
          }
          console.log(this.sortPull);
          console.log(this.paginatorPull);
          console.log(this.dataSourcePull);
        }
      } else if (result.status === 404) {
      }
    });
  }

  viewDocument(element) {
    console.log(this.data);
    if (this.dataSourcePull.data.length > 0) {
      const url =
        './detail-fi-kb?compCode=' +
        element.companyCode +
        '&docNo=' +
        element.documentNo +
        '&docYear=' +
        element.fiscalYear;
      window.open(url, 'name', 'width=1200,height=700');
    } else {
      const url =
        './detail-fi-kb?compCode=' +
        element.compCode +
        '&docNo=' +
        element.accountDocNo +
        '&docYear=' +
        element.fiscalYear;
      window.open(url, 'name', 'width=1200,height=700');
    }
  }

  viewDocumentPaymentReverse(element, payment) {
    if (this.dataSourcePull.data.length > 0) {
      if (payment) {
        const url =
          './detail-fi-kb?compCode=' +
          element.paymentCompanyCode +
          '&docNo=' +
          element.paymentDocumentNo +
          '&docYear=' +
          element.paymentFiscalYear;
        window.open(url, 'name', 'width=1200,height=700');
      } else {
        const url =
          './detail-fi-kb?compCode=' +
          element.originalCompanyCode +
          '&docNo=' +
          element.originalDocumentNo +
          '&docYear=' +
          element.originalFiscalYear;
        window.open(url, 'name', 'width=1200,height=700');
      }
    } else {
      const url =
        './detail-fi-kb?compCode=' +
        element.compCode +
        '&docNo=' +
        element.accountDocNo +
        '&docYear=' +
        element.fiscalYear;
      window.open(url, 'name', 'width=1200,height=700');
    }
  }

  closeDialog(): void {
    this.dialogRef.close();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  clickConfirm() {
    this.dialogRef.close({
      event: true,
      value: 'CONFIRM',
    });
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  clickUnSave() {
    this.dialogRef.close({
      event: true,
      value: 'UnSave',
    });
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  mappingLogType(logType) {
    if (logType === 'CAN_UPDATE') {
      return 'กำลังทำการกลับรายการ';
    } else {
      return 'ไม่สามารถกลับรายการได้';
    }
  }

  isShowMultipleLineLog(element: any): boolean {
    return element.autoStep3;
  }

  pullStep4Mass() {
    this.pullMassStep4ReverseDocument(this.data.listLog);
    this.subscription = interval(20000).subscribe(() => {
      this.pullMassStep4ReverseDocument(this.data.listLog);
    });
  }

  refreshStep4() {
    this.pullMassStep4ReverseDocument(this.data.listLog);
  }

  pullMassStep4ReverseDocument(object) {
    this.paymentService.pullMassStep4ReverseDocument(object).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSourcePull = new MatTableDataSource(data);
          this.dataSourcePull.sort = this.sortPull;
          this.dataSourcePull.paginator = this.paginatorStep4;
          if (result.data.length > 0) {
            this.summaryResult = {
              ...this.summaryResult,
              process: result.data[0].process || '0',
              success: result.data[0].success || '0',
              total: result.data[0].total || '0',
              fail: result.data[0].fail || '0',
            };
            if (result.data[0].process === 0) {
              if (this.subscription) {
                this.subscription.unsubscribe();
              }
            }
          }
        }
      } else if (result.status === 404) {
      }
    });
  }
}
