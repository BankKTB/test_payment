import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { Constant } from '@shared/utils/constant';
import { ActivatedRoute } from '@angular/router';
import { Utils } from '@shared/utils/utils';
import Decimal from 'decimal.js';
export interface DialogData {
  taxFee: any;
}

@Component({
  selector: 'app-dialog-tax-fee',
  templateUrl: './dialog-tax-fee.component.html',
  styleUrls: ['./dialog-tax-fee.component.scss'],
})
export class DialogTaxFeeComponent implements OnInit {
  dialogTaxFeeForm: FormGroup;
  isLoading = false;
  typeTaxControl: FormControl; // ประเภท Tax
  taxBaseCalculateControl: FormControl; // ฐานการคำนวณ Tax
  valueTaxControl: FormControl; // จำนวน
  typeFeeControl: FormControl; // ประเภท ค่าปรับ
  feeBaseCalculateControl: FormControl; // ฐานการคำนวณ ค่าปรับ
  valueFeeControl: FormControl; // จำนวน ค่าปรับ

  taxFee;

  constructor(
    public dialogRef: MatDialogRef<DialogTaxFeeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private formBuilder: FormBuilder,
    public utils: Utils,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    console.log(this.data);

    window.scrollTo(0, 0);
    this.createFormControl();
    this.createFormGroup();

    if (this.data.taxFee !== null) {
      this.setInputFormEditTax(this.data.taxFee);
    }
    this.dialogTaxFeeForm.disable();
  }

  createFormControl() {
    this.typeTaxControl = this.formBuilder.control(''); // ประเภท Tax
    this.taxBaseCalculateControl = this.formBuilder.control(''); // ฐานการคำนวณ Tax
    this.valueTaxControl = this.formBuilder.control(''); // จำนวน

    this.typeFeeControl = this.formBuilder.control(''); // ประเภท ค่าปรับ
    this.feeBaseCalculateControl = this.formBuilder.control(''); // ฐานการคำนวณ ค่าปรับ
    this.valueFeeControl = this.formBuilder.control(''); // จำนวน ค่าปรับ
  }

  createFormGroup() {
    this.dialogTaxFeeForm = this.formBuilder.group({
      typeTax: this.typeTaxControl, // ประเภท Tax
      taxBaseCalculate: this.taxBaseCalculateControl, // ฐานการคำนวณ Tax
      valueTax: this.valueTaxControl, // จำนวน
      typeFee: this.typeFeeControl, // ประเภท ค่าปรับ
      feeBaseCalculate: this.feeBaseCalculateControl, // ฐานการคำนวณ ค่าปรับ
      valueFee: this.valueFeeControl, // จำนวน ค่าปรับ
    });
  }

  setInputFormEditTax(data) {
    console.log('ssss', data);

    if (data.typeTax === '11') {
      data.codeTax = 'A2';
    } else if (data.typeTax === '10') {
      data.codeTax = 'A1';
    } else if (data.typeTax === '12') {
      data.codeTax = 'A3';
    } else if (data.typeTax === '13') {
      data.codeTax = 'A4';
    }

    if (data.typeFee === '01') {
      data.codeFee = 'B1';
    } else if (data.typeFee === '02') {
      data.codeFee = 'B2';
    }
    this.dialogTaxFeeForm.patchValue({
      typeTax: data.codeTax ? data.typeTax : '', // ประเภท Tax
      taxBaseCalculate: data.codeTax ? data.taxBaseCalculate : '', // ฐานการคำนวณ Tax
      valueTax: data.codeTax ? data.valueTax : '', // จำนวน
      typeFee: data.codeFee ? data.typeFee : '', // ประเภท ค่าปรับ
      feeBaseCalculate: data.codeFee ? data.feeBaseCalculate : '', // ฐานการคำนวณ ค่าปรับ
      valueFee: data.codeFee ? data.valueFee : '', // จำนวน ค่าปรับ
    });
  }

  onNoClick(): void {
    this.dialogRef.close({ event: 'cancel' });
  }
}
