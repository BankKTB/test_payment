import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { FiService } from '@core/services/fi/fi.service';
import { DialogOmSearchDetailComponent } from '@shared/component/dialog-om-search-detail/dialog-om-search-detail.component';
import { interval } from 'rxjs';
import { LocalStorageService } from '@core/services';
import { DialogShowErrorOmComponent } from '@shared/component/dialog-show-error-om/dialog-show-error-om.component';

export interface DialogData {
  items: any;
  listValidate: any;
}

@Component({
  selector: 'app-dialog-om-change-payment-block',
  templateUrl: './dialog-om-change-payment-block.component.html',
  styleUrls: ['./dialog-om-change-payment-block.component.scss'],
})
export class DialogOmChangePaymentBlockComponent implements OnInit, OnDestroy {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  subscription = null;
  header = null;
  displayedColumns: string[] = [
    'choose',
    'date',
    'time',
    'compCode',
    'accDocNo',
    'reverseOriginalDocumentNo',
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
  s;
  unblockDate = new Date();
  uuid: string;
  summaryResult: any = {
    process: 0,
    success: 0,
    total: 0,
    fail: 0,
  };

  constructor(
    public dialogRef: MatDialogRef<DialogOmChangePaymentBlockComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentBlockService: PaymentBlockService,
    private fiService: FiService,
    private localStorageService: LocalStorageService
  ) {
    this.dialogRef.disableClose = true;
    // dialogRef.backdropClick().subscribe(() => {
    //   this.dialogRef.close({ event: 'save' });
    // });
  }

  ngOnInit() {
    console.log('vali');
    // console.log(this.data);
    // console.log(this.paginator);
    // this.dataSource = new MatTableDataSource(this.data.items);
    // this.dataSource.sort = this.sort;
    // this.dataSource.paginator = this.paginator;
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  sendDocumentUpdateToIdem() {
    console.log(this.dataSource.data);
    this.paymentBlockService.changePaymentBlock(this.data.items).then((result) => {
      if (result.status === 200) {
        this.uuid = result.message;
        this.pullChangePaymentBlock(this.uuid);
        this.subscription = interval(20000).subscribe(() => {
          this.pullChangePaymentBlock(this.uuid);
        });
      }
    });
  }

  pullChangePaymentBlock(uuid: string) {
    console.log(this.dataSource.data);
    this.paymentBlockService.pullChangePaymentBlockGET(uuid).then((result) => {
      console.log(result);
      if (result.status === 200) {
        this.dataSource = new MatTableDataSource(result.data);
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
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      }
    });
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'save' });
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
      webInfo: this.localStorageService.getWebInfo(),
    };
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

  viewDetailError(element) {
    const payload = {
      companyCode: element.companyCode,
      documentNo: element.originalDocumentNo,
      fiscalYear: element.originalFiscalYear,
    };
    this.paymentBlockService.findLogDetail(payload).then((data) => {
      console.log(data);
      const response = data as any;
      if (response.status === 200) {
        const dialogRef = this.dialog.open(DialogShowErrorOmComponent, {
          width: '80vw',
          data: {
            value: response.data,
          },
        });
      }
    });
  }
}
