import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogSearchParameterComponent } from '@shared/component/dialog-search-parameter/dialog-search-parameter.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { PayMethodConfigService } from '@core/services/pay-method-config/pay-method-config.service';

export interface DialogData {
  config: {
    id: '';
    payBy: '';
    allowedPersonnelPayment: '';
    bankDetail: '';
    documentTypeForPayment: '';
  };
}

@Component({
  selector: 'app-dialog-copy-payment-method-country-config',
  templateUrl: './dialog-copy-payment-method-country-config.component.html',
  styleUrls: ['./dialog-copy-payment-method-country-config.component.scss'],
})
export class DialogCopyPaymentMethodCountryConfigComponent implements OnInit {
  copyPaymentMethodCountryConfigForm: FormGroup;

  countryControl: FormControl; // ประเทศ
  countryNameEnControl: FormControl; // ประเทศ
  payMethodControl: FormControl; // รหัสหน่วยงาน
  payMethodNameControl: FormControl; // ชื่อหน่วยงาน

  constructor(
    private dialogRef: MatDialogRef<DialogSearchParameterComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private payMethodConfigService: PayMethodConfigService,
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
    this.countryControl = this.formBuilder.control(''); // ประเทศ
    this.countryNameEnControl = this.formBuilder.control(''); // ประเทศ
    this.payMethodControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.payMethodNameControl = this.formBuilder.control(''); // ชื่อหน่วยงาน
  }

  createCopyPayMethodFormGroup() {
    this.copyPaymentMethodCountryConfigForm = this.formBuilder.group({
      country: this.countryControl, // ประเทศ
      countryNameEn: this.countryNameEnControl,
      payMethod: this.payMethodControl, // รหัสหน่วยงาน
      payMethodName: this.payMethodNameControl, // ชื่อหน่วยงาน
    });
  }

  clickSave() {
    const value = this.copyPaymentMethodCountryConfigForm.value;

    if (!value.country) {
      this.snackBar.open('กรุณาเลือกประเทศ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.payMethod) {
      this.snackBar.open('กรุณาเลือกวิธีชำระเงิน', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      payMethod: value.payMethod,
      payMethodName: value.payMethodName,
      country: value.country,
      countryNameEn: value.countryNameEn,
      copyId: this.data.config.id,
      payBy: this.data.config.payBy,
      allowedPersonnelPayment: this.data.config.allowedPersonnelPayment,
      bankDetail: this.data.config.bankDetail,
      documentTypeForPayment: this.data.config.documentTypeForPayment,
    };
    console.log(payload);
    this.payMethodConfigService.copy(payload).then((result) => {
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
        if (type === 'country') {
          this.copyPaymentMethodCountryConfigForm.patchValue({
            country: result.value,
          });
        } else if (type === 'paymentMethod') {
          this.copyPaymentMethodCountryConfigForm.patchValue({
            payMethod: result.value,
            payMethodName: result.name,
          });
        }
      }
    });
  }
}
