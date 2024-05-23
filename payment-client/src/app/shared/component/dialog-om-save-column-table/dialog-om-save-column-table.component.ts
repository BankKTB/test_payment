import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface DialogData {
  role: any;
  createBy: any;
  value: any;
  type: any;
  dataColumn: any;
}

@Component({
  selector: 'app-dialog-om-save-column-table',
  templateUrl: './dialog-om-save-column-table.component.html',
  styleUrls: ['./dialog-om-save-column-table.component.scss'],
})
export class DialogOmSaveColumnTableComponent implements OnInit {
  saveColumnForm: FormGroup;

  nameControl: FormControl;
  userOnlyControl: FormControl;

  constructor(
    private dialogRef: MatDialogRef<DialogOmSaveColumnTableComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private paymentBlockService: PaymentBlockService,
    private utils: Utils,
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
    this.createFormControl();
    this.createFormGroup();
  }

  createFormControl() {
    console.log(this.data);
    if (this.data.type === 'edit') {
      this.nameControl = this.formBuilder.control(this.data.dataColumn.name);
      this.userOnlyControl = this.formBuilder.control(this.data.dataColumn.isUserOnly);
    } else {
      this.nameControl = this.formBuilder.control('');
      this.userOnlyControl = this.formBuilder.control(true);
    }
  }

  createFormGroup() {
    this.saveColumnForm = this.formBuilder.group({
      name: this.nameControl,
      userOnly: this.userOnlyControl,
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      status: 'Cancel',
      value: '',
    });
  }

  clickSave() {
    const value = this.saveColumnForm.value;
    console.log(value, value.userOnly);
    if (!value.name) {
      this.snackBar.open('กรุณา กรอก ชื่อ', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      name: value.name,
      userOnly: value.userOnly,
      createBy: this.data.createBy,
      role: this.data.role,
      id: null,
      createDate: '',

      jsonText: JSON.stringify(this.data.value),
    };
    if (this.data.type === 'save') {
      this.paymentBlockService.createColumn(payload).then((result) => {
        if (result.status === 201) {
          const data = result.data;
          if (data) {
            this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
              panelClass: '_success',
            });
            this.dialogRef.close({
              event: true,
              status: 'Success',
              value: data,
            });
          }
        } else if (result.status === 403) {
          this.snackBar.open('ชื่อโครงร่างนี้มีอยู่ในระบบแล้ว', '', {
            panelClass: '_error',
          });
        }
      });
    } else if (this.data.type === 'edit') {
      payload.id = this.data.dataColumn.id;
      payload.createDate = this.data.dataColumn.createDate;
      this.paymentBlockService.updateColumn(payload).then((result) => {
        if (result.status === 201) {
          const data = result.data;
          if (data) {
            this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
              panelClass: '_success',
            });
            this.dialogRef.close({
              event: true,
              status: 'Success',
              value: data,
            });
          }
        } else if (result.status === 403) {
          this.snackBar.open('ชื่อโครงร่างนี้มีอยู่ในระบบแล้ว', '', {
            panelClass: '_error',
          });
        }
      });
    }
  }
}
