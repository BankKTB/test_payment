import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import Decimal from 'decimal.js';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SmartFeeService } from '@core/services/smartFee/smart-fee.service';

export interface DialogData {
  item: {
    id: any;
    companyCode: any;
    companyName: any;
  };
}

@Component({
  selector: 'app-dialog-edit-smart-fee',
  templateUrl: './dialog-edit-smart-fee.component.html',
  styleUrls: ['./dialog-edit-smart-fee.component.scss'],
})
export class DialogEditSmartFeeComponent implements OnInit {
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
    private dialogRef: MatDialogRef<DialogEditSmartFeeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private smartFeeService: SmartFeeService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.createSmartFeeConfigFormControl();
    this.createSmartFeeConfigFormGroup();
    this.defaultSmartFeeConfigForm();
    this.searchById();
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

  searchById() {
    this.id = this.data.item.id;
    this.smartFeeService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.smartFeeConfigForm.patchValue({
            smartFeeId: data.smartFeeId,
            amountMax: data.amountMax,
            amountMin: data.amountMin,
            glAccount: data.glAccount,
            bankFee: data.bankFee,
            botFee: data.botFee,
            samedayBankFee: data.samedayBankFee,
            samedayBotFee: data.samedayBotFee,
          });
        }
      } else if (result.status === 404) {
      }
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
    const form = this.smartFeeConfigForm.value;
    const payload = {
      // amountMax: form.amountMax,
      // amountMin: form.amountMin,
      // glAccount: form.glAccount,
      bankFee: form.bankFee,
      botFee: form.botFee,
      samedayBankFee: form.samedayBankFee,
      samedayBotFee: form.samedayBotFee,
      id: this.id,
    };
    this.smartFeeService.update(payload, this.id).then((result) => {
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
