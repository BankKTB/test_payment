import { Component, Inject, OnInit } from '@angular/core';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RegenAuthenticationService } from '@core/services/authorize/regen/regen-authentication.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface DialogData {
  textConfirm: '';
}

@Component({
  selector: 'app-dialog-authentication-confirm',
  templateUrl: './dialog-authentication-confirm.component.html',
  styleUrls: ['./dialog-authentication-confirm.component.scss'],
})
export class DialogAuthenticationConfirmComponent implements OnInit {
  authenticateForm: FormGroup;
  listValidate = [];
  constructor(
    public dialogRef: MatDialogRef<DialogAuthenticationConfirmComponent>,
    private formBuilder: FormBuilder,
    private regenAuthenticationService: RegenAuthenticationService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.dialogRef.disableClose = true;
    dialogRef.backdropClick().subscribe(() => {
      this.closeDialog();
    });

    this.authenticateForm = this.formBuilder.group({
      username: this.formBuilder.control('', [Validators.required]), // วันที่สั่งโอน [GOVERNMENT ]
      password: this.formBuilder.control('', [Validators.required]), // วันที่สั่งโอน [GOVERNMENT ]
    });
  }

  closeDialog(): void {
    this.dialogRef.close({
      event: false,
      value: 'cancel',
    });
  }

  ngOnInit() {}

  clickConfirm() {
    const { username, password } = this.authenticateForm.value;
    const payload = {
      username,
      password,
    };
    if (this.authenticateForm.valid) {
      this.listValidate = [];
      this.regenAuthenticationService.authenticationRegen(payload).then((e) => {
        if (e.status === 200) {
          this.dialogRef.close({
            event: true,
            value: 'success',
          });
        } else if (e.status === 403) {
          this.snackBar.open('กรอกชื่อผู้ใช้ หรือรหัสผิด', '', {
            panelClass: '_warning',
          });
        } else {
          this.snackBar.open('เกิดข้อผิดพลาดบางอย่าง', '', {
            panelClass: '_warning',
          });
          // this.dialogRef.close({
          //   event: false,
          //   value: 'fail',
          // });
        }
      });
    } else {
      this.listValidate = []
      if (!this.authenticateForm.controls.username.valid) {
        this.listValidate.push('กรุณาระบุชื่อผู้ใข้')
      }
      if (!this.authenticateForm.controls.password.valid) {
        this.listValidate.push('กรุณาระบุรหัสผ่าน')
      }
    }
  }

  clickCancel() {
    this.dialogRef.close({
      event: false,
      value: 'fail',
    });
  }
}
