import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { MatSnackBar } from '@angular/material/snack-bar';
import Decimal from 'decimal.js';
import { SwiftFeeService } from '@core/services/swift-fee/swift-fee.service';

export interface DialogData {
  item: {
    id: any;
    companyCode: any;
    companyName: any;
  };
}

@Component({
  selector: 'app-dialog-edit-swift-fee',
  templateUrl: './dialog-edit-swift-fee.component.html',
  styleUrls: ['./dialog-edit-swift-fee.component.scss'],
})
export class DialogEditSwiftFeeComponent implements OnInit {
  swiftFeeConfigForm: FormGroup;

  swiftFeeIdControl: FormControl;

  bankKeyControl: FormControl;
  baseAmountControl: FormControl;
  baseFeeControl: FormControl;
  maxFeeControl: FormControl;
  varAmountControl: FormControl;
  varFeeControl: FormControl;
  smartControl: FormControl;

  id: number;

  isSaveSuccess = false;

  constructor(
    private dialogRef: MatDialogRef<DialogEditSwiftFeeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private swiftFeeService: SwiftFeeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.createSwiftFeeConfigFormControl();
    this.createSwiftFeeConfigFormGroup();
    this.defaultSwiftFeeConfigForm();
    this.searchById();
  }

  createSwiftFeeConfigFormControl() {
    this.swiftFeeIdControl = this.formBuilder.control('');

    this.bankKeyControl = this.formBuilder.control('');
    this.baseAmountControl = this.formBuilder.control('');
    this.baseFeeControl = this.formBuilder.control('');
    this.maxFeeControl = this.formBuilder.control('');
    this.varAmountControl = this.formBuilder.control('');
    this.varFeeControl = this.formBuilder.control('');
    this.smartControl = this.formBuilder.control('');
  }

  createSwiftFeeConfigFormGroup() {
    this.swiftFeeConfigForm = this.formBuilder.group({
      swiftFeeId: this.swiftFeeIdControl,
      bankKey: this.bankKeyControl,
      baseAmount: this.baseAmountControl,
      baseFee: this.baseFeeControl,
      maxFee: this.maxFeeControl,
      varAmount: this.varAmountControl,
      varFee: this.varFeeControl,
      smart: this.smartControl,
    });
  }

  defaultSwiftFeeConfigForm() {
    this.swiftFeeConfigForm.patchValue({
      swiftFeeId: Number,
      bankKey: '',
      baseAmount: new Decimal(0),
      baseFee: new Decimal(0),
      maxFee: new Decimal(0),
      varAmount: new Decimal(0),
      varFee: new Decimal(0),
      smart: false,
    });
  }

  searchById() {
    this.id = this.data.item.id;
    this.swiftFeeService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.swiftFeeConfigForm.patchValue({
            swiftFeeId: data.swiftFeeId,
            bankKey: data.bankKey,
            baseAmount: data.baseAmount,
            baseFee: data.baseFee,
            maxFee: data.maxFee,
            varAmount: data.varAmount,
            varFee: data.varFee,
            smart: data.smart,
          });
        }
      } else if (result.status === 404) {
      }
    });
  }

  clickSave(value) {
    const form = this.swiftFeeConfigForm.value;

    if (!value.bankKey) {
      this.snackBar.open('กรุณากรอก bank key ', '', {
        panelClass: '_warning',
      });
      return;
    } else {
      if (value.bankKey.length !== 3) {
        this.snackBar.open('กรุณากรอก bank key ให้ครบ 3 หลัก ', '', {
          panelClass: '_warning',
        });
        return;
      }
    }

    const payload = {
      bankKey: form.bankKey,
      baseAmount: form.baseAmount,
      baseFee: form.baseFee,
      maxFee: form.maxFee,
      varAmount: form.varAmount,
      varFee: form.varFee,
      smart: form.smart,

      id: this.id,
    };
    this.swiftFeeService.update(payload, this.id).then((result) => {
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.searchById();
          this.openSnackBarSaveSuccess();

          this.isSaveSuccess = true;
          this.dialogRef.close({
            event: true,
            saveSuccess: this.isSaveSuccess,
          });
        }
      } else if (result.status === 403) {
        this.snackBar.open('ข้อมูลนี้มีอยู่แล้ว', '', {
          panelClass: '_warning',
        });
        this.isSaveSuccess = false;
      } else if (result.status === 404) {
        this.isSaveSuccess = false;
      }
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }

  openSnackBarSaveSuccess() {
    this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
      panelClass: '_success',
    });
  }
}
