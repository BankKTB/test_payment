import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PaymentService } from '@core/services/payment/payment.service';
import { LocalStorageService } from '@core/services';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as XLSX from 'xlsx';
import { interval } from 'rxjs';

export interface DialogData {}

@Component({
  selector: 'app-dialog-upload-change-document',
  templateUrl: './dialog-upload-change-document.component.html',
  styleUrls: ['./dialog-upload-change-document.component.scss'],
})
export class DialogUploadChangeDocumentComponent implements OnInit {
  arrayBuffer: any;
  file: File;
  fileName = 'Choose file';
  chooseFile = true;
  listDocument = [];
  listValidate = [];
  uuid = null;

  summaryResult: any = {
    process: 0,
    success: 0,
    total: 0,
    fail: 0,
  };

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  dataSourcePull = new MatTableDataSource([]);
  @ViewChild(MatSort, { static: true }) sortPull: MatSort;
  @ViewChild('paginatorPull', { static: true }) paginatorPull: MatPaginator;
  displayedColumns: string[] = ['choose', 'compCode', 'accountDocNo', 'fiscalYear', 'paymentBlock'];
  displayedColumnsPull: string[] = [
    'choose',
    'companyCode',
    'documentNo',
    'paymentBlock',
    'status',
  ];
  confirm = false;

  subscription = null;

  constructor(
    public dialogRef: MatDialogRef<DialogUploadChangeDocumentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private paymentService: PaymentService,
    private localStorageService: LocalStorageService,
    private utils: Utils,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    console.log(this.sort);
    console.log(this.paginator);

    console.log(new Date('2021-13-01'));
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onSelectedFile(event) {
    console.log(event.target.files);
    if (event.target.files.length > 0) {
      this.file = event.target.files[0];
      this.fileName = this.file.name;
    }
  }

  upload() {
    this.chooseFile = false;
    this.listValidate = [];
    const dialogRef = this.dialogRef;
    // this.ValidateTax = [];
    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      this.arrayBuffer = fileReader.result;
      const data = new Uint8Array(this.arrayBuffer);
      const arr = [];
      for (let i = 0; i !== data.length; ++i) {
        arr[i] = String.fromCharCode(data[i]);
      }
      const bstr = arr.join('');
      const workbook = XLSX.read(bstr, { type: 'binary' });
      const worksheet = workbook.Sheets[workbook.SheetNames[0]];
      const worksheet2 = workbook.Sheets[workbook.SheetNames[2]];

      console.log('worksheet1=', worksheet);
      console.log('worksheet2=', worksheet2);
      console.log('worksheet1 sheet_to_json', XLSX.utils.sheet_to_json(worksheet, { raw: true }));
      console.log('worksheet2 sheet_to_json', XLSX.utils.sheet_to_json(worksheet2, { raw: true }));

      const itemList = XLSX.utils.sheet_to_json(worksheet, { raw: true }) as any[];
      console.log(itemList.length);
      for (let i = 0; i < itemList.length; i++) {
        console.log(itemList[i]);
        const compCode = itemList[i].รหัสหน่วยงาน;
        const accountDocNo = itemList[i].เลขที่เอกสาร;
        const fiscalYear = itemList[i].ปีบัญชี;
        const paymentBlock = itemList[i].PaymentBlock ? itemList[i].PaymentBlock : ' ';
        if (compCode && compCode.toString().length !== 5) {
          this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
          break;
        } else if (accountDocNo && accountDocNo.toString().length !== 10) {
          this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
          break;
        } else if (fiscalYear && fiscalYear.toString().length !== 4) {
          this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
          break;
        } else {
          // const dateSub = dateBaseLine.split('-');
          // const checkDate =
          //   Number(dateSub[2] - 543).toString() + '-' + dateSub[1] + '-' + dateSub[0];
          // if (new Date(checkDate).toString() === 'Invalid Date') {
          //   this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
          //   break;
          // } else if (Number(dateSub[2]) < 2500) {
          //   this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
          //   break;
          // } else {
          const item = {
            compCode,
            accountDocNo,
            fiscalYear: this.utils.convertYearToAD(fiscalYear),
            paymentBlock,
            // revDateAcct:
            //   Number(dateSub[2] - 543).toString() + '-' + dateSub[1] + '-' + dateSub[0],
            webInfo: this.localStorageService.getWebInfo(),
          };
          this.listDocument.push(item);

          // }
        }
      }
      console.log(this.listDocument);
      if (this.listValidate.length <= 0) {
        this.dataSource = new MatTableDataSource(this.listDocument);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      } else {
        this.dialogRef.close({ event: 'close' });
        if (this.subscription) {
          this.subscription.unsubscribe();
        }
        this.snackBar.open('ข้อมูลไฟล์ไม่ตรงตาม format', '', {
          panelClass: '_error',
        });
      }
      // dialogRef.close({
      //   dataList1: XLSX.utils.sheet_to_json(worksheet, { raw: true }),
      //   dataList2: XLSX.utils.sheet_to_json(worksheet2, { raw: true }),
      //   worksheet,
      // });
    };
    fileReader.readAsArrayBuffer(this.file);
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'cancel' });
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  clickCancel(): void {
    this.dialogRef.close({ event: 'close' });
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  clickConfirm(): void {
    console.log(this.listDocument);
    this.confirm = true;
    this.uuid = null;
    this.paymentService.massChangeDocument(this.listDocument).then((response) => {
      console.log('pullReversePaymentDocument', response);
      if (response.status === 200) {
        this.confirm = true;
        this.dataSource = new MatTableDataSource(response.data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.uuid = response.message;
        this.pullNewMassChangeDocument(response.message);
        this.subscription = interval(20000).subscribe(() => {
          this.pullNewMassChangeDocument(response.message);
        });
      }
    });
  }

  pullMassChangeDocument(object) {
    this.paymentService.pullMassChangeDocument(object).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSourcePull = new MatTableDataSource(data);
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
          this.dataSourcePull.sort = this.sortPull;
          this.dataSourcePull.paginator = this.paginatorPull;
          console.log(this.sortPull);
          console.log(this.paginatorPull);
          console.log(this.dataSourcePull);
        }
      } else if (result.status === 404) {
      }
    });
  }
  pullNewMassChangeDocument(uuid) {
    this.paymentService.pullNewMassChangeDocument(uuid).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSourcePull = new MatTableDataSource(data);
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
          this.dataSourcePull.sort = this.sortPull;
          this.dataSourcePull.paginator = this.paginatorPull;
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
}
