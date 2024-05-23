import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Constant } from '@shared/utils/constant';

// recieve data from parent
export interface DialogData {
  prepaidValue: any;
  checkStatus: boolean;
}
@Component({
  selector: 'app-dialog-po-advance-payment',
  templateUrl: './dialog-po-advance-payment.component.html',
  styleUrls: ['./dialog-po-advance-payment.component.scss'],
})
export class DialogPoAdvancePaymentComponent implements OnInit {
  dialogPrepaidForm: FormGroup;
  prepaidValueControl: FormControl; // จำนวนเงินจ่ายล่วงหน้า

  prepaidValue;
  isDisabledFromSearch = false;
  idSubmitedForm = false;

  constructor(
    public dialogRef: MatDialogRef<DialogPoAdvancePaymentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private formBuilder: FormBuilder
  ) {
    dialogRef.disableClose = true;
  }

  ngOnInit() {
    window.scrollTo(0, 0);
    this.createFormControl();
    this.createFormGroup();

    this.isDisabledFromSearch = this.data.checkStatus;
    if (this.isDisabledFromSearch) {
      this.dialogPrepaidForm.disable();
    } else {
      this.dialogPrepaidForm.enable();
    }
    this.prepaidValue = this.data.prepaidValue;
    if (this.prepaidValue !== null) {
      this.setInputFormEditTax(this.prepaidValue);
    }
  }

  createFormControl() {
    this.prepaidValueControl = this.formBuilder.control(''); // จำนวนเงินจ่ายล่วงหน้า
  }

  createFormGroup() {
    this.dialogPrepaidForm = this.formBuilder.group({
      prepaidValue: this.prepaidValueControl,
    });
  }

  setInputFormEditTax(data) {
    this.dialogPrepaidForm.patchValue({
      prepaidValue: data.prepaidValue,
    });
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'cancel' });
  }

  onClickSave() {
    this.prepaidValue = null;
    const form = this.dialogPrepaidForm.value;
    const prepaid = {
      prepaidValue: form.prepaidValue,
    };

    if (prepaid.prepaidValue) {
      this.prepaidValue = prepaid;
    } else {
      this.prepaidValue = null;
    }

    this.dialogRef.close({
      event: 'save',
      prepaidValue: this.prepaidValue,
    });
  }
}
