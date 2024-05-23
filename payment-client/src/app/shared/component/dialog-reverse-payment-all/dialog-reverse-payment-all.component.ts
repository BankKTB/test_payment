import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { PaymentAliasService } from '@core/services/payment-alias/payment-alias.service';
import { PaymentService } from '@core/services/payment/payment.service';
import { LocalStorageService } from '@core/services';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Utils } from '@shared/utils/utils';
import { interval } from 'rxjs';

export interface DialogData {
  paymentAlias: any;
}

@Component({
  selector: 'app-dialog-reverse-payment-all',
  templateUrl: './dialog-reverse-payment-all.component.html',
  styleUrls: ['./dialog-reverse-payment-all.component.scss'],
})
export class DialogReversePaymentAllComponent implements OnInit, OnDestroy {
  reverseForm: FormGroup;

  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  header = null;
  displayedColumns: string[] = [
    'choose',
    'paymentCompanyCode',
    'paymentDocumentNo',
    'paymentFiscalYear',
    'reversePaymentCompanyCode',
    'reversePaymentDocumentNo',
    'reversePaymentFiscalYear',
  ];

  isConfirm = false;

  subscription = null;
  listValidate = [];
  summaryResult: any = {
    process: 0,
    success: 0,
    total: 0,
    fail: 0,
  };

  constructor(
    public dialogRef: MatDialogRef<DialogReversePaymentAllComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private paymentAliasService: PaymentAliasService,
    private paymentService: PaymentService,
    private localStorageService: LocalStorageService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    public utils: Utils
  ) {
    this.dialogRef.disableClose = true;
    dialogRef.backdropClick().subscribe(() => {
      if (this.isConfirm) {
        if (this.data.paymentAlias.id) {
          this.paymentAliasService.deletePaymentDocumentAll(this.data.paymentAlias.id).then(() => {
            this.dialogRef.close({ event: 'save' });
          });
        }
      } else {
        this.dialogRef.close({ event: 'close' });
      }
    });
  }

  ngOnInit() {
    this.createFormGroup();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  createFormGroup() {
    this.reverseForm = this.formBuilder.group({
      paymentDate: this.formBuilder.control(''), // วันที่ประมวลผล
      paymentName: this.formBuilder.control(''), // การกำหนด
      reasonReverse: this.formBuilder.control('', [Validators.required]), // รายละเอียดวันที่ปรับปรุง
      reverseDate: this.formBuilder.control(''),
    });
    this.reverseForm.controls.paymentDate.disable();
    this.reverseForm.controls.paymentName.disable();
    this.reverseForm.controls.reverseDate.disable();
    this.setInput();
  }

  setInput() {
    console.log(this.data.paymentAlias.jsonText);
    console.log(JSON.parse(this.data.paymentAlias.jsonText));
    const object = JSON.parse(this.data.paymentAlias.jsonText);
    const parameter = object.parameter;
    this.reverseForm.patchValue({
      paymentDate: new Date(this.data.paymentAlias.paymentDate), // วันที่ประมวลผล
      paymentName: this.data.paymentAlias.paymentName, // การกำหนด
      reasonReverse: '',
      reverseDate: new Date(parameter.postDate), // วันที่ประมวลผล
    });
  }

  pullPaymentDocument() {
    this.paymentService
      .pullReversePaymentDocument(this.data.paymentAlias.id, this.localStorageService.getWebInfo())
      .then((response) => {
        console.log('pullReversePaymentDocument', response);
        if (response.status === 200) {
          this.dataSource = new MatTableDataSource(response.data.data);
          this.summaryResult = {
            ...this.summaryResult,
            process: response.data.process || '0',
            success: response.data.success || '0',
            total: response.data.total || '0',
            fail: response.data.fail || '0',
          };
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        }
      });
  }

  onNoClick(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    if (this.isConfirm) {
      if (this.data.paymentAlias.id) {
        this.paymentAliasService.deletePaymentDocumentAll(this.data.paymentAlias.id).then(() => {
          this.dialogRef.close({ event: 'save' });
        });
      }
    } else {
      this.dialogRef.close({ event: 'close' });
    }
  }

  clickConfirm(): void {
    this.listValidate = [];
    this.validateForm();
    if (this.listValidate.length <= 0) {
      const form = this.reverseForm.getRawValue();

      let reverseDate = '';
      if (form.reverseDate) {
        const dayReverseDate = form.reverseDate.getDate();
        const monthReverseDate = form.reverseDate.getMonth() + 1;
        const yearReverseDate = form.reverseDate.getFullYear();
        reverseDate = this.utils.parseDate(dayReverseDate, monthReverseDate, yearReverseDate);
      }

      const payload = {
        webInfo: this.localStorageService.getWebInfo(),
        paymentAliasId: this.data.paymentAlias.id,
        reasonReverse: form.reasonReverse,
        reverseDate,
      };

      this.paymentService.reversePaymentAll(payload).then((response) => {
        console.log('reversePaymentAll', response);
        if (response.status === 200) {
          this.isConfirm = true;
          this.dataSource = new MatTableDataSource(response.data);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;

          if (this.subscription) {
            this.subscription.unsubscribe();
          }
          this.subscription = interval(10000).subscribe(() => {
            this.pullPaymentDocument();
          });
        } else if (response.status === 403) {
          this.isConfirm = false;
          this.snackBar.open('กรุณาเปิดงวดที่ต้องการ', '', {
            panelClass: '_warning',
          });
        }
      });
    }
  }

  clickCancel(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.dialogRef.close({ event: 'close' });
  }

  viewDocument(element) {
    console.log(this.data);
    const url =
      './detail-fi-kb?compCode=' +
      element.paymentCompanyCode +
      '&docNo=' +
      element.paymentDocumentNo +
      '&docYear=' +
      element.paymentFiscalYear;
    window.open(url, 'name', 'width=1200,height=700');
  }

  onChangeReason(event) {
    const object = JSON.parse(this.data.paymentAlias.jsonText);
    const parameter = object.parameter;
    if (event === '06' || event === '07') {
      this.reverseForm.controls.reverseDate.enable();
    } else {
      if (this.data.paymentAlias) {
        this.reverseForm.controls.reverseDate.disable();
        this.reverseForm.patchValue({
          reverseDate: new Date(parameter.postDate ? new Date(parameter.postDate) : ''),
        });
      } else {
        this.reverseForm.patchValue({
          reverseDate: new Date(),
        });
      }
    }
  }

  validateForm() {
    Object.keys(this.reverseForm.controls).forEach((key) => {
      const controlErrors = this.reverseForm.get(key).errors;
      if (controlErrors) {
        console.log(key);
        if (key === 'reasonReverse') {
          this.listValidate.push('กรุณาระบุ เหตุผลการกลับรายการ');
        }
      }
    });
  }
}
