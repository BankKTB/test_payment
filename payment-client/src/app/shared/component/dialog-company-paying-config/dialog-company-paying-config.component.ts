import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS, MatSnackBar } from '@angular/material';
import Decimal from 'decimal.js';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { CompanyPayingService } from '@core/services/company-paying/company-paying.service';

export interface DialogData {
  item: {
    id: any;
    companyCode: any;
    companyName: any;
  };
}

@Component({
  selector: 'app-dialog-company-paying-config',
  templateUrl: './dialog-company-paying-config.component.html',
  styleUrls: ['./dialog-company-paying-config.component.scss'],
})
export class DialogCompanyPayingConfigComponent implements OnInit {
  companyPayingConfigForm: FormGroup;

  companyPayingIdControl: FormControl;
  minimumAmountForPayControl: FormControl;
  panelExpanded = true;
  id: number;

  constructor(
    private dialogRef: MatDialogRef<DialogCompanyPayingConfigComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private companyPayingService: CompanyPayingService,
    private _snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.createcompanyPayingConfigFormControl();
    this.createcompanyPayingConfigFormGroup();
    this.defaultcompanyPayingConfigForm();
    this.searchById();
  }

  createcompanyPayingConfigFormControl() {
    this.companyPayingIdControl = this.formBuilder.control('');
    this.minimumAmountForPayControl = this.formBuilder.control('');
  }

  createcompanyPayingConfigFormGroup() {
    this.companyPayingConfigForm = this.formBuilder.group({
      companyPayingId: this.companyPayingIdControl,
      minimumAmountForPay: this.minimumAmountForPayControl,
    });
  }

  defaultcompanyPayingConfigForm() {
    this.companyPayingConfigForm.patchValue({
      companyPayingId: Number,
      minimumAmountForPay: new Decimal(0),
    });
  }

  searchById() {
    this.id = this.data.item.id;
    this.companyPayingService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayingConfigForm.patchValue({
            companyPayingId: data.companyPayingId,
            minimumAmountForPay: data.minimumAmountForPay,
          });
        }
      } else if (result.status === 404) {
      }
    });
  }

  clickSave(value) {
    const payload = {
      companyPayingId: this.id,
      minimumAmountForPay: new Decimal(value.minimumAmountForPay).toFixed(2),
      id: this.id,
    };
    this.companyPayingService.update(payload, this.id).then((result) => {
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
      value: this.companyPayingConfigForm.value,
    });
  }

  openSnackBarSaveSuccess() {
    this._snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
      panelClass: '_success',
    });
  }
}
