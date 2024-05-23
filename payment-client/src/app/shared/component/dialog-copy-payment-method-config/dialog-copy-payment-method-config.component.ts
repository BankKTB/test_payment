import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogSearchParameterComponent } from '@shared/component/dialog-search-parameter/dialog-search-parameter.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { CompanyPayingPayMethodConfigService } from '@core/services/company-paying-pay-method-config/company-paying-pay-method-config.service';

export interface DialogData {
  config: {
    companyPayingId: '';
    minimumAmount: '';
    maximumAmount: '';
    allowedSinglePayment: '';
    allowedPartnerAnotherCountry: '';
    allowedCurrencyAnotherCountry: '';
    allowedBankAnotherCountry: '';
  };
}

@Component({
  selector: 'app-dialog-copy-payment-method-config',
  templateUrl: './dialog-copy-payment-method-config.component.html',
  styleUrls: ['./dialog-copy-payment-method-config.component.scss'],
})
export class DialogCopyPaymentMethodConfigComponent implements OnInit {
  copyPaymentMethodConfigForm: FormGroup;

  payMethodControl: FormControl; // รหัสหน่วยงาน
  payMethodNameControl: FormControl; // ชื่อหน่วยงาน

  constructor(
    private dialogRef: MatDialogRef<DialogSearchParameterComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayingPayMethodConfigService: CompanyPayingPayMethodConfigService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      this.dialogRef.close({ event: false });
    });
  }

  ngOnInit() {
    this.createCopyPayMethodFormControl();
    this.createCopyPayMethodFormGroup();
  }

  createCopyPayMethodFormControl() {
    this.payMethodControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.payMethodNameControl = this.formBuilder.control(''); // ชื่อหน่วยงาน
  }

  createCopyPayMethodFormGroup() {
    this.copyPaymentMethodConfigForm = this.formBuilder.group({
      payMethod: this.payMethodControl, // รหัสหน่วยงาน
      payMethodName: this.payMethodNameControl, // ชื่อหน่วยงาน
    });
  }

  clickSave() {
    const value = this.copyPaymentMethodConfigForm.value;

    if (!value.payMethod) {
      this.snackBar.open('กรุณากรอก วิธีชำระเงิน', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      companyPayingId: this.data.config.companyPayingId,
      payMethod: value.payMethod,
      payMethodName: value.payMethodName,
      minimumAmount: this.data.config.minimumAmount,
      maximumAmount: this.data.config.maximumAmount,
      allowedSinglePayment: this.data.config.allowedSinglePayment,
      allowedPartnerAnotherCountry: this.data.config.allowedPartnerAnotherCountry,
      allowedCurrencyAnotherCountry: this.data.config.allowedCurrencyAnotherCountry,
      allowedBankAnotherCountry: this.data.config.allowedBankAnotherCountry,
    };
    console.log(payload);
    this.companyPayingPayMethodConfigService.create(payload).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
            panelClass: '_success',
          });
          this.dialogRef.close({
            event: true,
            status: 'Success',
            value: '',
          });
        }
      } else if (result.status === 403) {
        this.snackBar.open('วิธีชำระเงินนี้มีอยู่แล้ว', '', {
          panelClass: '_error',
        });
      }
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      status: 'Cancel',
      value: '',
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.copyPaymentMethodConfigForm.patchValue({
          payMethod: result.value,
          payMethodName: result.name,
        });
      }
    });
  }
}
