import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SwiftFeeService } from '@core/services/swift-fee/swift-fee.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import Decimal from 'decimal.js';

export interface DialogData {
  copyValue: any;
}

@Component({
  selector: 'app-dialog-save-swift-fee',
  templateUrl: './dialog-save-swift-fee.component.html',
  styleUrls: ['./dialog-save-swift-fee.component.scss'],
})
export class DialogSaveSwiftFeeComponent implements OnInit {
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
    private dialogRef: MatDialogRef<DialogSaveSwiftFeeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private swiftFeeService: SwiftFeeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.createSwiftFeeConfigFormControl();
    this.createSwiftFeeConfigFormGroup();
    if (this.data.copyValue) {
      this.copySwiftFeeConfigForm();
    } else {
      this.defaultSwiftFeeConfigForm();
    }
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

  copySwiftFeeConfigForm() {
    this.swiftFeeConfigForm.patchValue({
      swiftFeeId: Number,
      bankKey: this.data.copyValue.bankKey,
      baseAmount: this.data.copyValue.baseAmount,
      baseFee: this.data.copyValue.baseFee,
      maxFee: this.data.copyValue.maxFee,
      varAmount: this.data.copyValue.varAmount,
      varFee: this.data.copyValue.varFee,
      smart: this.data.copyValue.smart,
    });
  }

  clickSave(value) {
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
      bankKey: value.bankKey,
      baseAmount: value.baseAmount,
      baseFee: value.baseFee,
      maxFee: value.maxFee,
      varAmount: value.varAmount,
      varFee: value.varFee,
      smart: value.smart,
    };
    this.swiftFeeService.create(payload).then((result) => {
      if (result.status === 201) {
        const data = result.data;
        if (data) {
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
