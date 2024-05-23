import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS, MatSnackBar } from '@angular/material';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { PayMethodConfigService } from '@core/services/pay-method-config/pay-method-config.service';
import { DialogPayMethodCurrencyConfigComponent } from '@shared/component/dialog-pay-method-currency-config/dialog-pay-method-currency-config.component';

export interface DialogData {
  item: {
    id: any;
    country: any;
    countryNameEN: any;
    payMethod: any;
    payMethodName: any;
  };
}

@Component({
  selector: 'app-dialog-pay-method-config',
  templateUrl: './dialog-pay-method-config.component.html',
  styleUrls: ['./dialog-pay-method-config.component.scss'],
})
export class DialogPayMethodConfigComponent implements OnInit {
  payMethodConfigForm: FormGroup;

  countryControl: FormControl;
  payMethodControl: FormControl;
  payMethodNameControl: FormControl;

  allowedPersonnelPaymentControl: FormControl;
  bankDetailControl: FormControl;
  documentTypeForPaymentControl: FormControl;
  payByControl: FormControl;
  allowedPersonnelPaymentBoolean = false;
  bankDetailBoolean = false;
  payByBoolean = false;

  panelExpandedPayMethod = true;
  panelExpandedMainRequest = true;
  panelExpandedBankInfo = true;

  id: number;
  listDoctype = [{ valueCode: 'PM' }];

  constructor(
    private dialogRef: MatDialogRef<DialogPayMethodConfigComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private payMethodConfigService: PayMethodConfigService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.createpayMethodConfigFormControl();
    this.createpayMethodConfigFormGroup();
    this.defaultpayMethodConfigForm();
    this.searchById();
  }

  createpayMethodConfigFormControl() {
    this.countryControl = this.formBuilder.control('');
    this.payMethodControl = this.formBuilder.control('');
    this.payMethodNameControl = this.formBuilder.control('');

    this.allowedPersonnelPaymentControl = this.formBuilder.control('');
    this.bankDetailControl = this.formBuilder.control('');
    this.documentTypeForPaymentControl = this.formBuilder.control('');
    this.payByControl = this.formBuilder.control('');
  }

  createpayMethodConfigFormGroup() {
    this.payMethodConfigForm = this.formBuilder.group({
      country: this.countryControl,
      payMethod: this.payMethodControl,
      payMethodName: this.payMethodNameControl,

      allowedPersonnelPayment: this.allowedPersonnelPaymentControl,
      bankDetail: this.bankDetailControl,
      documentTypeForPayment: this.documentTypeForPaymentControl,
      payBy: this.payByControl,
    });
  }

  defaultpayMethodConfigForm() {
    this.payMethodConfigForm.patchValue({
      allowedPersonnelPayment: '',
      bankDetail: '',
      documentTypeForPayment: '',
      payBy: '',
    });
  }

  searchById() {
    this.id = this.data.item.id;
    this.payMethodConfigService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.payMethodConfigForm.patchValue({
            allowedPersonnelPayment: data.allowedPersonnelPayment,
            bankDetail: data.bankDetail,
            country: data.country,
            documentTypeForPayment: data.documentTypeForPayment ? data.documentTypeForPayment : '',
            id: data.id,
            payBy: data.payBy,
            payMethod: data.payMethod,
            payMethodName: data.payMethodName,
          });
          this.allowedPersonnelPaymentBoolean = data.allowedPersonnelPayment;
          this.bankDetailBoolean = data.bankDetail;
          this.payByBoolean = data.payBy;
        }
      } else if (result.status === 404) {
      }
    });
  }

  clickSave(value) {
    console.log(value);
    const payload = {
      allowedPersonnelPayment: value.allowedPersonnelPayment,
      bankDetail: value.bankDetail,
      country: '',
      documentTypeForPayment: value.documentTypeForPayment ? value.documentTypeForPayment : '',
      id: this.id,
      payBy: value.payBy ? value.payBy : '',
      payMethod: '',
      payMethodName: '',
    };
    this.payMethodConfigService.update(payload, this.id).then((result) => {
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.searchById();
          this.openSnackBarSaveSuccess();
        }
      } else if (result.status === 404) {
      }
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }

  clickConfirm() {
    this.dialogRef.close({
      event: true,
      value: this.payMethodConfigForm.value,
    });
  }

  openSnackBarSaveSuccess() {
    this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
      panelClass: '_success',
    });
  }

  openDialogPayMethodCurrencyConfig() {
    const dialogRef = this.dialog.open(DialogPayMethodCurrencyConfigComponent, {
      width: '50vw',
      data: {
        item: this.data.item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }
}
