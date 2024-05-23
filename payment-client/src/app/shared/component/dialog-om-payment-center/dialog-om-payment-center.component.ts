import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listDisbursementCode: [{ from: null; to: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-om-payment-center',
  templateUrl: './dialog-om-payment-center.component.html',
  styleUrls: ['./dialog-om-payment-center.component.scss'],
})
export class DialogOmPaymentCenterComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listDisbursementCode = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmPaymentCenterComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog
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
    console.log(this.data.listDisbursementCode);
    if (this.data.listDisbursementCode[0].from) {
      this.listDisbursementCode = this.data.listDisbursementCode;
    }
  }

  addInputCompany() {
    this.listDisbursementCode.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setDisbursementCode(index) {
    const disbursementCodeFrom = this.from.toArray()[index].nativeElement.value;
    const disbursementCodeTo = this.to.toArray()[index].nativeElement.value;
    if (disbursementCodeFrom) {
      this.listDisbursementCode[index].from = disbursementCodeFrom;
    } else {
      this.listDisbursementCode[index].from = '';
    }
    if (disbursementCodeTo) {
      this.listDisbursementCode[index].to = disbursementCodeTo;
    } else {
      this.listDisbursementCode[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listDisbursementCode[index].optionExclude = optionExclude;
    } else {
      this.listDisbursementCode[index].optionExclude = false;
    }
  }

  deleteInputDisbursementCode(index) {
    if (this.listDisbursementCode.length > 1) {
      this.listDisbursementCode.splice(index, 1);
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

      if (list.length > this.listDisbursementCode.length) {
        const need = list.length - this.listDisbursementCode.length;
        for (let i = 0; i < need; i++) {
          this.listDisbursementCode.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listDisbursementCode);
      if (type === 'disbursementCodeFrom') {
        for (let i = index; i < list.length; i++) {
          this.listDisbursementCode[i].from = list[i];
        }
      } else if (type === 'disbursementCodeTo') {
        for (let i = index; i < list.length; i++) {
          this.listDisbursementCode[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchDisbursementCode(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'disbursementCodeFrom') {
          this.listDisbursementCode[index].from = result.value;
        } else if (type === 'disbursementCodeTo') {
          this.listDisbursementCode[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listDisbursementCode,
    });
  }
}
