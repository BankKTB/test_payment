import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { Constant } from '@shared/utils/constant';
import { MasterService } from '@core/services';

export interface DialogData {
  paymentMethod: string;
}

@Component({
  selector: 'app-dialog-search-payment-method',
  templateUrl: './dialog-search-payment-method.component.html',
  styleUrls: ['./dialog-search-payment-method.component.scss'],
})
export class DialogSearchPaymentMethodComponent implements OnInit {
  @ViewChildren('checkBoxPaymentMethod') checkBoxPaymentMethod: QueryList<ElementRef>;

  paymentSelected = '';
  dataSource = new MatTableDataSource([]);

  constructor(
    private dialogRef: MatDialogRef<DialogSearchPaymentMethodComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private masterService: MasterService
  ) {
    dialogRef.disableClose = false;
    dialogRef.backdropClick().subscribe(() => {
      this.dialogRef.close({ event: false });
    });
  }

  ngOnInit() {
    if (this.data.paymentMethod) {
      if (this.constant.PAYMENT_METHOD.length > 0) {
        this.constant.PAYMENT_METHOD.forEach((item) => {
          item.selected = false;
        });
        this.paymentSelected = this.data.paymentMethod;
        this.checkPaymentMethodSelecet();
      } else {
        this.searchPaymentMethod().then(() => {
          this.constant.PAYMENT_METHOD.forEach((item) => {
            item.selected = false;
          });
          this.paymentSelected = this.data.paymentMethod;
          this.checkPaymentMethodSelecet();
        });
      }
    }
  }

  async searchPaymentMethod() {
    await this.masterService.findPaymentMethodCodeWithParam('**').then((value) => {
      this.constant.PAYMENT_METHOD = value.data;
      this.constant.PAYMENT_METHOD.forEach((item) => {
        this.constant.PAYMENT_METHOD_SEARCH.push(item.valueCode);
      });
    });
  }

  checkPaymentMethodSelecet() {
    if (this.paymentSelected.length > 0 && this.paymentSelected) {
      const paymentAll = this.paymentSelected.split('');
      paymentAll.forEach((payment) => {
        let checkPayment = this.constant.PAYMENT_METHOD.find(
          (item) => item.valueCode === payment.toUpperCase()
        );
        checkPayment.selected = true;
      });
    }
  }

  closeDialog(): void {
    this.dialogRef.close({
      event: false,
    });
  }

  confirmSelect() {
    this.paymentSelected = '';
    this.checkBoxPaymentMethod.toArray().forEach((item) => {
      const data = item as any;
      if (data._checked) {
        this.paymentSelected += data.value;
      }
    });
    this.dialogRef.close({
      event: true,
      value: this.paymentSelected,
    });
  }
}
