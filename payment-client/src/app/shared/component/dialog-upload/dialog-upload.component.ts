import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as XLSX from 'xlsx';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { PaymentService } from '@core/services/payment/payment.service';
import { LocalStorageService } from '@core/services';
import { Utils } from '@shared/utils/utils';
import { interval } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface DialogData {
  type: any;
}

@Component({
  selector: 'app-dialog-upload',
  templateUrl: './dialog-upload.component.html',
  styleUrls: ['./dialog-upload.component.scss'],
})
export class DialogUploadComponent implements OnInit {
  arrayBuffer: any;
  file: File;
  fileName = 'Choose file';
  chooseFile = true;
  listDocument = [];
  listValidate = [];
  uuid = '';

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  dataSourcePull = new MatTableDataSource([]);
  @ViewChild(MatSort, { static: true }) sortPull: MatSort;
  @ViewChild('paginatorPull', { static: true }) paginatorPull: MatPaginator;
  displayedColumns: string[] = [
    'choose',
    'compCode',
    'accountDocNo',
    'fiscalYear',
    'compCodeAgency',
    'accountDocNoAgency',
    'fiscalYearAgency',
    'reasonNo',
    'revDateAcct',
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
  confirm = false;

  subscription = null;

  summaryResult: any = {
    process: 0,
    success: 0,
    total: 0,
    fail: 0,
  };

  constructor(
    public dialogRef: MatDialogRef<DialogUploadComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private paymentService: PaymentService,
    private localStorageService: LocalStorageService,
    private utils: Utils,
    private snackBar: MatSnackBar,
  ) {
  }

  ngOnInit() {
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
      if (this.data.type === 'payment' && itemList.length > 50) {
        this.listValidate.push(' ไม่สามารถทำรายการมากกว่า 50 รายการ ');
        return;
      }
      for (let i = 0; i < itemList.length; i++) {
        console.log(itemList[i]);
        const compCode = itemList[i].รหัสหน่วยงาน.toString().trim();
        const accountDocNo = itemList[i].เลขที่เอกสารกลับรายการ.toString().trim();
        const fiscalYear = itemList[i].ปีงบประมาณ.toString().trim();
        const reasonNo = itemList[i].เหตุผลการกลับรายการ.toString().trim();
        const revDateAcct = itemList[i].วันที่ผ่านรายการ.toString().trim();
        const period = Number(itemList[i].งวด.toString().trim());
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
          const dateSub = revDateAcct.split('-');
          const checkDate =
            Number(dateSub[2] - 543).toString() + '-' + dateSub[1] + '-' + dateSub[0];
          if (new Date(checkDate).toString() === 'Invalid Date') {
            console.log('abcd');
            this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
            break;
          } else if (Number(dateSub[2]) < 2500) {
            this.listValidate.push(' ข้อมูลไฟล์ไม่ตรงตาม format ');
            break;
          } else {
            if (this.data.type === 'payment' && accountDocNo.toString().startsWith('4')) {
              const item = {
                compCodeAgency: compCode !== '99999' ? compCode.toString() : '',
                accountDocNoAgency: compCode !== '99999' ? accountDocNo.toString() : '',
                fiscalYearAgency:
                  compCode !== '99999' ? this.utils.convertYearToAD(fiscalYear) : '',
                compCode: compCode === '99999' ? compCode.toString() : '',
                accountDocNo: compCode === '99999' ? accountDocNo.toString() : '',
                fiscalYear: compCode === '99999' ? this.utils.convertYearToAD(fiscalYear) : '',
                reasonNo: !reasonNo.toString().startsWith('0') ? '0'.concat(reasonNo) : reasonNo,
                revDateAcct:
                  Number(dateSub[2] - 543).toString() + '-' + dateSub[1] + '-' + dateSub[0],
                webInfo: this.localStorageService.getWebInfo(),
                period,
              };

              this.listDocument.push(item);
            } else if (this.data.type !== 'payment' && accountDocNo.toString().startsWith('3')) {
              const item = {
                compCode,
                accountDocNo,
                fiscalYear: this.utils.convertYearToAD(fiscalYear),
                reasonNo: !reasonNo.toString().startsWith('0') ? '0'.concat(reasonNo) : reasonNo,
                revDateAcct:
                  Number(dateSub[2] - 543).toString() + '-' + dateSub[1] + '-' + dateSub[0],
                webInfo: this.localStorageService.getWebInfo(),
                period,
              };
              this.listDocument.push(item);
            }
          }
        }

        if (this.data.type === 'payment') {
          this.displayedColumns = [
            'choose',
            'compCode',
            'accountDocNo',
            'fiscalYear',
            'compCodeAgency',
            'accountDocNoAgency',
            'fiscalYearAgency',
            'reasonNo',
            'revDateAcct',
          ];
        } else {
          this.displayedColumns = [
            'choose',
            'compCode',
            'accountDocNo',
            'fiscalYear',
            'reasonNo',
            'revDateAcct',
          ];
        }
      }
      console.log(this.listDocument);
      this.checkOriginalDocument(this.listDocument);
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
    this.uuid = '';
    console.log(this.listDocument);
    this.confirm = true;
    this.paymentService.massReverseDocument(this.listDocument).then((response) => {
      console.log('pullReversePaymentDocument', response);
      if (response.status === 200) {
        this.confirm = true;
        this.dataSource = new MatTableDataSource(response.data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        this.pullNewMassReverseDocument(response.message);
        this.uuid = response.message;
        this.subscription = interval(20000).subscribe(() => {
          this.pullNewMassReverseDocument(response.message);
        });
      }
    });
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
          console.log(this.sortPull);
          console.log(this.paginatorPull);
          console.log(this.dataSourcePull);
        }
      } else if (result.status === 404) {
      }
    });
  }

  pullNewMassReverseDocument(uuid) {
    this.paymentService.pullNewMassReverseDocument(uuid).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          if (this.data.type === 'payment') {
            this.displayedColumnsPull = [
              'choose',
              'companyCode',
              'documentNo',
              'fiscalYear',
              'companyCodeAgency',
              'documentNoAgency',
              'fiscalYearAgency',
              'reverseCompanyCode',
              'reverseDocumentNo',
              'reverseFiscalYear',
              'status',
            ];
          } else {
            this.displayedColumnsPull = [
              'choose',
              'companyCode',
              'documentNo',
              'fiscalYear',
              'reverseCompanyCode',
              'reverseDocumentNo',
              'reverseFiscalYear',
              'status',
            ];
          }
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
          this.dataSourcePull = new MatTableDataSource(data);
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

  groupBy(objectArray, property) {
    return objectArray.reduce((acc, obj) => {
      let key = obj[property];
      if (!acc[key]) {
        acc[key] = [];
      }
      acc[key].push(obj);
      return acc;
    }, {});
  }

  checkOriginalDocument(listDocument) {
    // console.log(this.groupBy(listDocument.filter(item => item.compCodeAgency), 'compCodeAgency'));
    const listGroup = this.groupBy(
      listDocument.filter((item) => item.compCodeAgency),
      'compCodeAgency',
    );
    console.log(listGroup);

    for (const [key, value] of Object.entries(listGroup)) {
      const items = value as any;
      const listItem = [];
      items.forEach((item) => {
        const data = {
          compCode: item.compCodeAgency,
          fiscalYear: item.fiscalYearAgency,
          docNo: item.accountDocNoAgency,
        };
        listItem.push(data);
      });
      const payload = {
        webInfo: this.localStorageService.getWebInfo(),
        lines: listItem,
      };
      this.paymentService.checkOriginalDocument(payload).then((result) => {
        console.log('payload');
        console.log(result);
        if (result.status === 200) {
          if (result.data.lines.length > 0) {
            const listOriginal = result.data.lines;
            console.log(listOriginal);
            for (const objectResponse of listOriginal) {
              console.log(objectResponse);
              console.log(this.listDocument);
              const document = this.listDocument.find(
                (object) =>
                  object.compCodeAgency === objectResponse.compCode &&
                  object.fiscalYearAgency === objectResponse.fiscalYear &&
                  object.accountDocNoAgency === objectResponse.docNo,
              );
              console.log(document);
              if (document) {
                document.compCode = objectResponse.oriCompCode;
                document.fiscalYear = objectResponse.oriFiscalYear;
                document.accountDocNo = objectResponse.oriDocNo;
              }
            }
          }
        } else if (result.status === 422) {
        }
      });
    }
  }
}
