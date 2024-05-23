import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { MatSnackBar } from '@angular/material/snack-bar';
import { SumFileConditionService } from '@core/services/sum-file-condition/sum-file-condition.service';
import { UserProfile } from '@core/models/user-profile';
import { LocalStorageService } from '@core/services';

export interface DialogData {
  item: {
    id: any;
    companyCode: any;
    companyName: any;
  };
}

@Component({
  selector: 'app-dialog-edit-sum-file-condition',
  templateUrl: './dialog-edit-sum-file-condition.component.html',
  styleUrls: ['./dialog-edit-sum-file-condition.component.scss'],
})
export class DialogEditSumFileConditionComponent implements OnInit {
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

  isReadonly = false;

  userProfile: UserProfile = null;

  constructor(
    private dialogRef: MatDialogRef<DialogEditSumFileConditionComponent>,
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
    this.defaultSumFileConditionConfigForm();
    this.searchById();
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

  searchById() {
    this.id = this.data.item.id;
    this.sumFileConditionService.searchByID(this.id).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        console.log(data);
        if (data) {
          this.sumFileConditionConfigForm.patchValue({
            sumFileConditionId: data.sumFileConditionId,
            vendorFrom: data.vendorFrom,
            vendorTo: data.vendorTo,
            paymentMethod: data.paymentMethod,
            validFrom: new Date(data.validFrom),
            validTo: new Date(data.validTo),
            active: data.active ? ' ' : 'X',
          });
          this.isReadonly = true;
        }
      } else if (result.status === 404) {
      }
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
      id: this.id,
      validFrom,
      validTo,
      updated: new Date(),
      updatedBy: this.userProfile.userdata.username,
      active: value.active === ' ' ? true : false,
    };
    console.log(payload);
    this.sumFileConditionService.update(payload, this.id).then((result) => {
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

  clearInput(inputForm) {
    this.sumFileConditionConfigForm.controls[inputForm].setValue('');
  }
}
