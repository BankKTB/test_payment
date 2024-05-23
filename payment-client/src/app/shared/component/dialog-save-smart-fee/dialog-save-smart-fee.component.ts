import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SmartFeeService } from '@core/services/smartFee/smart-fee.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import Decimal from 'decimal.js';

export interface DialogData {
  copyValue: any;
}

@Component({
  selector: 'app-dialog-save-smart-fee',
  templateUrl: './dialog-save-smart-fee.component.html',
  styleUrls: ['./dialog-save-smart-fee.component.scss'],
})
export class DialogSaveSmartFeeComponent implements OnInit {
  smartFeeConfigForm: FormGroup;

  smartFeeIdControl: FormControl;
  amountMaxControl: FormControl;
  amountMinControl: FormControl;
  glAccountControl: FormControl;
  bankFeeControl: FormControl;
  botFeeControl: FormControl;
  samedayBankFeeControl: FormControl;
  samedayBotFeeControl: FormControl;

  id: number;

  isSaveSuccess = false;

  constructor(
    private dialogRef: MatDialogRef<DialogSaveSmartFeeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private smartFeeService: SmartFeeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    console.log(this.data.copyValue);
    this.createSmartFeeConfigFormControl();
    this.createSmartFeeConfigFormGroup();

    if (this.data.copyValue) {
      this.copySmartFeeConfigForm();
    } else {
      this.defaultSmartFeeConfigForm();
    }

    // this.searchById();
  }

  createSmartFeeConfigFormControl() {
    this.smartFeeIdControl = this.formBuilder.control('');
    this.amountMaxControl = this.formBuilder.control('');
    this.amountMinControl = this.formBuilder.control('');
    this.glAccountControl = this.formBuilder.control('');
    this.bankFeeControl = this.formBuilder.control('');
    this.botFeeControl = this.formBuilder.control('');
    this.samedayBankFeeControl = this.formBuilder.control('');
    this.samedayBotFeeControl = this.formBuilder.control('');
  }

  createSmartFeeConfigFormGroup() {
    this.smartFeeConfigForm = this.formBuilder.group({
      smartFeeId: this.smartFeeIdControl,
      amountMax: this.amountMaxControl,
      amountMin: this.amountMinControl,
      glAccount: this.glAccountControl,
      bankFee: this.bankFeeControl,
      botFee: this.botFeeControl,
      samedayBankFee: this.samedayBankFeeControl,
      samedayBotFee: this.samedayBotFeeControl,
    });
  }

  defaultSmartFeeConfigForm() {
    this.smartFeeConfigForm.patchValue({
      smartFeeId: Number,
      amountMax: new Decimal(0),
      amountMin: new Decimal(0),
      glAccount: '',
      bankFee: new Decimal(0),
      botFee: new Decimal(0),
      samedayBankFee: new Decimal(0),
      samedayBotFee: new Decimal(0),
    });
  }

  copySmartFeeConfigForm() {
    this.smartFeeConfigForm.patchValue({
      smartFeeId: Number,
      amountMax: this.data.copyValue.amountMax,
      amountMin: this.data.copyValue.amountMin,
      glAccount: this.data.copyValue.glAccount,
      bankFee: this.data.copyValue.bankFee,
      botFee: this.data.copyValue.botFee,
      samedayBankFee: this.data.copyValue.samedayBankFee,
      samedayBotFee: this.data.copyValue.samedayBotFee,
    });
  }

  clickSave(value) {
    if (!value.glAccount) {
      this.snackBar.open('กรุณากรอก G/L ', '', {
        panelClass: '_warning',
      });
      return;
    } else {
      if (value.glAccount.length !== 10) {
        this.snackBar.open('รหัส G/L ไม่ครบ 10 หลัก', '', {
          panelClass: '_warning',
        });
        return;
      }
    }

    const payload = {
      amountMax: value.amountMax,
      amountMin: value.amountMin,
      glAccount: value.glAccount,
      bankFee: value.bankFee,
      botFee: value.botFee,
      samedayBankFee: value.samedayBankFee,
      samedayBotFee: value.samedayBotFee,
    };
    this.smartFeeService.create(payload).then((result) => {
      console.log(result);
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
