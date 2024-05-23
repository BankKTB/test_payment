import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PaymentService } from '@core/services/payment/payment.service';

export interface DialogData {
  objRequest: any;
  reverseType: string;
}

@Component({
  selector: 'app-dialog-reverse-payment',
  templateUrl: './dialog-reverse-payment.component.html',
  styleUrls: ['./dialog-reverse-payment.component.scss'],
})
export class DialogReversePaymentComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<DialogReversePaymentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private paymentService: PaymentService,
    private snackBar: MatSnackBar,
  ) {
  }

  ngOnInit() {
    console.log(this.data.objRequest);
    if (this.data.objRequest) {
      // this.onSave();
      this.checkOriginalDocument();
    }
  }

  onSave(flag = 0) {
    this.data.objRequest.flag = flag;
    this.data.objRequest.observe = 'response';
    if (this.data.reverseType === 'PAYMENT') {
      this.postReversePayment(flag);
    } else {
      this.postReverseInvoice(flag);
    }
  }

  postReversePayment(flag) {
    console.log(this.data.objRequest)
    this.paymentService.reversePayment(this.data.objRequest).then((result) => {
      console.log(result);
      if (result.status === 200) {
        if (flag === 1) {
          this.dialogRef.close({
            event: true,
            value: 'Cancel',
          });
          this.snackBar.open('กลับรายการสำเร็จ', '', {
            panelClass: '_success',
          });
        }
        this.viewDocument();
      } else if (result.status === 422) {
        this.snackBar.open(result.error.message, '', {
          panelClass: '_warning',
        });
      }
      // if (result.status === 201) {
      //   const data = result.data;
      // if (data) {
      // this.openSnackBarSaveSuccess();
      // this.isSaveSuccess = true;
      // this.dialogRef.close({
      //   event: true,
      //   saveSuccess: this.isSaveSuccess,
      // });
      // }
      // }
      // else if (result.status === 403) {
      // this.snackBar.open('ข้อมูลนี้มีอยู่แล้ว', '', {
      //   panelClass: '_warning',
      // });
      // this.isSaveSuccess = false;
      // }
    });
  }

  postReverseInvoice(flag) {
    this.paymentService.reverseInvoice(this.data.objRequest).then((result) => {
      console.log(result);
      if (result.status === 200) {
        if (flag === 1) {
          this.dialogRef.close({
            event: true,
            value: 'Cancel',
          });
          this.snackBar.open('กลับรายการสำเร็จ', '', {
            panelClass: '_success',
          });
        }
        this.viewDocument();
      } else if (result.status === 422) {
        // const text =
        //   'ไม่สามารถกลับรายการได้ เพราะ ' +
        //   'เลขที่เอกสารขอเบิก ' +
        //   object.accountDocNo +
        //   ' ' +
        //   object.compCode +
        //   ' ' +
        //   object.fiscalYear +
        //   ' มีการสร้างข้อเสนอค้างไว้';
        this.snackBar.open(result.error.message, '', {
          panelClass: '_warning',
        });
      }
      // if (result.status === 201) {
      //   const data = result.data;
      // if (data) {
      // this.openSnackBarSaveSuccess();
      // this.isSaveSuccess = true;
      // this.dialogRef.close({
      //   event: true,
      //   saveSuccess: this.isSaveSuccess,
      // });
      // }
      // }
      // else if (result.status === 403) {
      // this.snackBar.open('ข้อมูลนี้มีอยู่แล้ว', '', {
      //   panelClass: '_warning',
      // });
      // this.isSaveSuccess = false;
      // }
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }

  viewDocument() {
    console.log(this.data);
    const url =
      './detail-fi-kb?compCode=' +
      this.data.objRequest.compCodeDisplay +
      '&docNo=' +
      this.data.objRequest.accountDocNoDisplay +
      '&docYear=' +
      this.data.objRequest.fiscalYearDisplay +
      '&autoRefresh=' +
      true
    // window.open(url, 'name', 'width=1200,height=700');
    window.open(url, '_blank');
  }

  checkOriginalDocument() {


    const payload = {
      webInfo: this.data.objRequest.webInfo,
      lines: [{
        compCode: this.data.objRequest.compCode,
        fiscalYear: this.data.objRequest.fiscalYear,
        docNo: this.data.objRequest.accountDocNo,
      }],
    };

    this.paymentService.checkOriginalDocument(payload).then((result) => {
      console.log(result);
      if (result.status === 200) {

        if (result.data.lines.length > 0) {

          this.data.objRequest.compCode = result.data.lines[0].oriCompCode;
          this.data.objRequest.fiscalYear = result.data.lines[0].oriFiscalYear;
          this.data.objRequest.accountDocNo = result.data.lines[0].oriDocNo;

        }

      } else if (result.status === 422) {
        this.snackBar.open(result.error.message, '', {
          panelClass: '_warning',
        });
      }

    });
  }
}
