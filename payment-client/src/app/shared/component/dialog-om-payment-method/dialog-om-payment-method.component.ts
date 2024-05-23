import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listPaymentMethod: [{ from: null; to: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-om-payment-method',
  templateUrl: './dialog-om-payment-method.component.html',
  styleUrls: ['./dialog-om-payment-method.component.scss'],
})
export class DialogOmPaymentMethodComponent implements OnInit {
  @ViewChildren('paymentMethodFrom') paymentMethodFrom: QueryList<ElementRef>;
  @ViewChildren('paymentMethodTo') paymentMethodTo: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listPaymentMethod = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmPaymentMethodComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentService: PaymentService,
    private loaderService: LoaderService
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      // if (this.isSaveSuccess && this.resultSave.accDocNo) {
      //   this.dialogRef.close({event: 'savesucess'});
      // } else {
      //   this.dialogRef.close({event: 'close'});
      // }
    });
  }

  ngOnInit() {
    console.log(this.data.listPaymentMethod);
    if (this.data.listPaymentMethod[0].from) {
      this.listPaymentMethod = this.data.listPaymentMethod;
    }
  }

  addInputCompany() {
    this.listPaymentMethod.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setPaymentMethod(index) {
    const paymentMethodFrom = this.paymentMethodFrom.toArray()[index].nativeElement.value;
    const paymentMethodTo = this.paymentMethodTo.toArray()[index].nativeElement.value;
    if (paymentMethodFrom) {
      this.listPaymentMethod[index].from = paymentMethodFrom;
    } else {
      this.listPaymentMethod[index].from = '';
    }
    if (paymentMethodTo) {
      this.listPaymentMethod[index].to = paymentMethodTo;
    } else {
      this.listPaymentMethod[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listPaymentMethod[index].optionExclude = optionExclude;
    } else {
      this.listPaymentMethod[index].optionExclude = false;
    }
  }

  deleteInputPaymentMethod(index) {
    if (this.listPaymentMethod.length > 1) {
      this.listPaymentMethod.splice(index, 1);
    }
  }

  onNoClick() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  copyFromExcel(event, index, type) {
    // console.log(event);

    const value = event.data;

    // console.log('value' + value);

    if (value.length > 10) {
      // console.log('index' + index);

      console.log(value.split('\n'));
      const list = value.split('\n');

      if (list.length > this.listPaymentMethod.length) {
        const need = list.length - this.listPaymentMethod.length;
        for (let i = 0; i < need; i++) {
          this.listPaymentMethod.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listPaymentMethod);
      if (type === 'paymentMethodFrom') {
        for (let i = index; i < list.length; i++) {
          this.listPaymentMethod[i].from = list[i];
        }
      } else if (type === 'paymentMethodTo') {
        for (let i = index; i < list.length; i++) {
          this.listPaymentMethod[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchPaymentMethod(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'paymentMethodFrom') {
          this.listPaymentMethod[index].from = result.value;
        } else if (type === 'paymentMethodTo') {
          this.listPaymentMethod[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listPaymentMethod,
    });
  }
}
