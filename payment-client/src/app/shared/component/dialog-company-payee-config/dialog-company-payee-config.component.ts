import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS, MatSnackBar } from '@angular/material';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';

export interface DialogData {
  item: {
    id: any;
    companyCode: any;
    companyName: any;
  };
}

@Component({
  selector: 'app-dialog-company-payee-config',
  templateUrl: './dialog-company-payee-config.component.html',
  styleUrls: ['./dialog-company-payee-config.component.scss'],
})
export class DialogCompanyPayeeConfigComponent implements OnInit {
  companyPayeeConfigForm: FormGroup;

  companyPayeeCodeControl: FormControl;
  companyPayingCodeControl: FormControl;
  amountDueDateControl: FormControl;
  paymentMethodControl: FormControl;
  id: number;

  // Open panal
  panelExpandedControlInfo = true;
  panelExpandedMoney = true;
  panelExpandedSeller = true;

  constructor(
    private dialogRef: MatDialogRef<DialogCompanyPayeeConfigComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private companyPayeeService: CompanyPayeeService,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.createcompanyPayeeConfigFormControl();
    this.createcompanyPayeeConfigFormGroup();
    this.defaultcompanyPayeeConfigForm();
    this.searchById();
  }

  createcompanyPayeeConfigFormControl() {
    this.companyPayeeCodeControl = this.formBuilder.control('');
    this.companyPayingCodeControl = this.formBuilder.control('');
    this.amountDueDateControl = this.formBuilder.control('');
    this.paymentMethodControl = this.formBuilder.control('');
  }

  createcompanyPayeeConfigFormGroup() {
    this.companyPayeeConfigForm = this.formBuilder.group({
      companyPayeeCode: this.companyPayeeCodeControl,
      companyPayingCode: this.companyPayingCodeControl,
      amountDueDate: this.amountDueDateControl,
      paymentMethod: this.paymentMethodControl,
    });
  }

  defaultcompanyPayeeConfigForm() {
    this.companyPayeeConfigForm.patchValue({
      companyPayeeCode: '',
      companyPayingCode: '',
      amountDueDate: '',
      paymentMethod: '',
    });
  }

  searchById() {
    this.id = this.data.item.id;
    this.companyPayeeService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayeeConfigForm.patchValue({
            companyPayeeCode: data.companyPayeeCode,
            companyPayingCode: data.companyPayingCode,
            amountDueDate: data.amountDueDate,
            paymentMethod: data.paymentMethod,
          });
        }
      } else if (result.status === 404) {
      }
    });
  }

  clickSave(value) {
    const payload = {
      amountDueDate: Number(value.amountDueDate),
      companyPayeeCode: value.companyPayeeCode,
      companyPayeeId: this.id,
      companyPayingCode: value.companyPayingCode,
      id: this.id,
      paymentMethod: value.paymentMethod,
    };
    this.companyPayeeService.update(payload, this.id).then((result) => {
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
      value: this.companyPayeeConfigForm.value,
    });
  }

  openSnackBarSaveSuccess() {
    this._snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
      panelClass: '_success',
    });
  }
}
