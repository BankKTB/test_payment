import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SumFileConditionService } from '@core/services/sum-file-condition/sum-file-condition.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { LocalStorageService } from '@core/services';
import { UserProfile } from '@core/models/user-profile';

export interface DialogData {
  copyValue: any;
}

@Component({
  selector: 'app-dialog-save-sum-file-condition',
  templateUrl: './dialog-save-sum-file-condition.component.html',
  styleUrls: ['./dialog-save-sum-file-condition.component.scss'],
})
export class DialogSaveSumFileConditionComponent implements OnInit {
  sumFileConditionConfigForm: FormGroup;

  sumFileConditionIdControl: FormControl;
  vendorFromControl: FormControl;
  vendorToControl: FormControl;
  paymentMethodControl: FormControl;

  validFromControl: FormControl;
  validToControl: FormControl;

  activeControl: FormControl;

  id: number;

  isSaveSuccess = false;

  userProfile: UserProfile = null;

  constructor(
    private dialogRef: MatDialogRef<DialogSaveSumFileConditionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private sumFileConditionService: SumFileConditionService,
    private snackBar: MatSnackBar,
    private localStorageService: LocalStorageService
  ) {}

  ngOnInit() {
    this.userProfile = this.localStorageService.getUserProfile();
    this.createSumFileConditionConfigFormControl();
    this.createSumFileConditionConfigFormGroup();

    if (this.data.copyValue) {
      this.copySumFileConditionConfigForm();
    } else {
      this.defaultSumFileConditionConfigForm();
    }
  }

  createSumFileConditionConfigFormControl() {
    this.sumFileConditionIdControl = this.formBuilder.control('');

    this.vendorFromControl = this.formBuilder.control('');
    this.vendorToControl = this.formBuilder.control('');
    this.paymentMethodControl = this.formBuilder.control('');

    this.activeControl = this.formBuilder.control('');
  }

  createSumFileConditionConfigFormGroup() {
    this.sumFileConditionConfigForm = this.formBuilder.group({
      sumFileConditionId: this.sumFileConditionIdControl,

      vendorFrom: this.vendorFromControl,
      vendorTo: this.vendorToControl,
      paymentMethod: this.paymentMethodControl,

      validFrom: this.validFromControl,
      validTo: this.validToControl,
      active: this.activeControl,
    });
  }

  defaultSumFileConditionConfigForm() {
    this.sumFileConditionConfigForm.patchValue({
      sumFileConditionId: Number,

      vendorFrom: '',
      vendorTo: '',
      paymentMethod: '',

      validFrom: '',
      validTo: '',
      active: false,
    });
  }

  copySumFileConditionConfigForm() {
    this.sumFileConditionConfigForm.patchValue({
      sumFileConditionId: Number,

      vendorFrom: this.data.copyValue.vendorFrom,
      vendorTo: this.data.copyValue.vendorTo,
      paymentMethod: this.data.copyValue.paymentMethod,

      validFrom: new Date(this.data.copyValue.validFrom),
      validTo: new Date(this.data.copyValue.validTo),
      active: this.data.copyValue.active === true ? '' : 'X',
    });
  }

  clickSave(value) {
    if (!value.paymentMethod) {
      this.snackBar.open('กรุณากรอก วิธีชำระเงิน ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.vendorFrom) {
      this.snackBar.open('กรุณากรอก ผู้ขายจาก ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.vendorTo) {
      this.snackBar.open('กรุณากรอก ผู้ขายถึง ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.validFrom) {
      this.snackBar.open('กรุณากรอก มีผลจาก ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.validTo) {
      this.snackBar.open('กรุณากรอก มีผลถึง ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (value.active && value.active !== 'X') {
      this.snackBar.open('กรุณากรอก กรอก X หรือ ค่าว่างเท่านั้น ', '', {
        panelClass: '_warning',
      });
      return;
    }

    const validFrom = new Date();
    validFrom.setDate(value.validFrom.getDate());
    validFrom.setMonth(value.validFrom.getMonth());
    validFrom.setFullYear(value.validFrom.getFullYear());

    const validTo = new Date();
    validTo.setDate(value.validTo.getDate());
    validTo.setMonth(value.validTo.getMonth());
    validTo.setFullYear(value.validTo.getFullYear());

    const payload = {
      vendorFrom: value.vendorFrom,
      vendorTo: value.vendorTo,
      paymentMethod: value.paymentMethod,
      validFrom,
      validTo,
      created: new Date(),
      createdBy: this.userProfile.userdata.username,
      updated: new Date(),
      updatedBy: this.userProfile.userdata.username,
      active: value.active === 'X' ? false : true,
    };
    console.log(payload);
    this.sumFileConditionService.create(payload).then((result) => {
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

  clearInput(inputForm) {
    this.sumFileConditionConfigForm.controls[inputForm].setValue('');
  }
}
