import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogSearchParameterComponent } from '@shared/component/dialog-search-parameter/dialog-search-parameter.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';

export interface DialogData {
  config: {
    companyPayingCode: '';
    amountDueDate: '';
    paymentMethod: '';
  };
}

@Component({
  selector: 'app-dialog-copy-company-payee',
  templateUrl: './dialog-copy-company-payee.component.html',
  styleUrls: ['./dialog-copy-company-payee.component.scss'],
})
export class DialogCopyCompanyPayeeComponent implements OnInit {
  copyCompanyForm: FormGroup;

  companyCodeControl: FormControl; // รหัสหน่วยงาน
  companyNameControl: FormControl; // ชื่อหน่วยงาน

  constructor(
    private dialogRef: MatDialogRef<DialogSearchParameterComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayeeService: CompanyPayeeService,
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
    this.createcompanyPayeeFormControl();
    this.createcompanyPayeeFormGroup();
  }

  createcompanyPayeeFormControl() {
    this.companyCodeControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.companyNameControl = this.formBuilder.control(''); // ชื่อหน่วยงาน
  }

  createcompanyPayeeFormGroup() {
    this.copyCompanyForm = this.formBuilder.group({
      companyCode: this.companyCodeControl, // รหัสหน่วยงาน
      companyName: this.companyNameControl, // ชื่อหน่วยงาน
    });
  }

  clickSave() {
    const value = this.copyCompanyForm.value;

    if (!value.companyCode) {
      this.snackBar.open('กรุณาเลือก รหัสบริษัท', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.companyName) {
      this.snackBar.open('กรุณาเลือก ชื่อบริษัท', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      companyCode: value.companyCode,
      companyName: value.companyName,
      companyPayeeCode: value.companyCode,
      companyPayingCode: this.data.config.companyPayingCode,
      amountDueDate: this.data.config.amountDueDate,
      paymentMethod: this.data.config.paymentMethod,
    };
    this.companyPayeeService.create(payload).then((result) => {
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
        this.snackBar.open('หน่วยงานนี้มีข้อมูลแล้ว', '', {
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
        this.copyCompanyForm.patchValue({
          companyCode: result.value,
          companyName: result.name,
        });
      }
    });
  }
}
