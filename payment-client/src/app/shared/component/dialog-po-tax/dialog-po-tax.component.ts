import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';

export interface DialogData {
  page: any;
  headerTable: [];
  typeTax: any;
  checkStatus: boolean;
}
@Component({
  selector: 'app-dialog-po-tax',
  templateUrl: './dialog-po-tax.component.html',
  styleUrls: ['./dialog-po-tax.component.scss'],
})
export class DialogPoTaxComponent implements OnInit {
  dialogTaxForm: FormGroup;

  typeTaxControl: FormControl; // ประเภท Tax
  valueTaxControl: FormControl; // จำนวน
  typeTax;
  isDisabledFromSearch = false;
  idSubmitedForm = false;

  constructor(
    public dialogRef: MatDialogRef<DialogPoTaxComponent>,
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

    this.typeTax = this.data.typeTax;
    this.isDisabledFromSearch = this.data.checkStatus;
    if (this.isDisabledFromSearch) {
      this.dialogTaxForm.disable();
    } else {
      this.dialogTaxForm.enable();
    }
    if (this.typeTax !== null) {
      this.setInputFormEditTax(this.typeTax);
    }
  }

  createFormControl() {
    this.typeTaxControl = this.formBuilder.control('ZVAT'); // ประเภท Tax
    this.valueTaxControl = this.formBuilder.control(''); // จำนวน
  }

  createFormGroup() {
    this.dialogTaxForm = this.formBuilder.group({
      typeTax: this.typeTaxControl,
      valueTax: this.valueTaxControl,
    });
  }

  setInputFormEditTax(data) {
    this.dialogTaxForm.patchValue({
      typeTax: data.typeTax,
      valueTax: data.valueTax,
    });
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'cancel' });
  }

  onClickSave() {
    this.typeTax = null;
    const form = this.dialogTaxForm.value;
    const tax = {
      typeTax: form.typeTax,
      valueTax: form.valueTax,
    };

    if (tax.valueTax) {
      this.typeTax = tax;
    } else {
      this.typeTax = null;
    }

    this.dialogRef.close({
      event: 'save',
      typeTax: this.typeTax,
    });
  }
}
