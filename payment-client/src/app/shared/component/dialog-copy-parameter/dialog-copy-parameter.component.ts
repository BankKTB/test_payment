import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogSearchParameterComponent } from '../dialog-search-parameter/dialog-search-parameter.component';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { DatepickerHeaderComponent } from '@shared/component/datepicker-header/datepicker-header.component';

@Component({
  selector: 'app-dialog-copy-parameter',
  templateUrl: './dialog-copy-parameter.component.html',
  styleUrls: ['./dialog-copy-parameter.component.scss'],
})
export class DialogCopyParameterComponent implements OnInit {
  copyParameterForm: FormGroup;

  paymentDateControl: FormControl; // วันที่ประมวลผล
  paymentNameControl: FormControl; // การกำหนด
  adjustDateControl: FormControl; // รายละเอียดวันที่ปรับปรุง
  public datePickerHeader = DatepickerHeaderComponent;

  constructor(
    private dialogRef: MatDialogRef<DialogSearchParameterComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      this.dialogRef.close({ event: false });
    });
  }

  ngOnInit() {
    this.createCopyParameterFormControl();
    this.createCopyParameterFormGroup();
    this.defaultCopyParameterForm();
  }

  createCopyParameterFormControl() {
    this.paymentDateControl = this.formBuilder.control(''); // วันที่ประมวลผล
    this.paymentNameControl = this.formBuilder.control(''); // การกำหนด
    this.adjustDateControl = this.formBuilder.control(''); // รายละเอียดวันที่ปรับปรุง
  }

  createCopyParameterFormGroup() {
    this.copyParameterForm = this.formBuilder.group({
      paymentDate: this.paymentDateControl, // วันที่ประมวลผล
      paymentName: this.paymentNameControl, // การกำหนด
      adjustDate: this.adjustDateControl, // รายละเอียดวันที่ปรับปรุง
    });
  }

  defaultCopyParameterForm() {
    this.copyParameterForm.patchValue({
      paymentDate: '', // วันที่ประมวลผล
      paymentName: '', // การกำหนด
      adjustDate: true, // รายละเอียดวันที่ปรับปรุง
    });
  }

  openDialogSearchParameterComponent(): void {
    const dialog = this.dialog.open(DialogSearchParameterComponent, {});

    dialog.afterClosed().subscribe((result) => {
      console.log('The dialog was closed');
      if (result && result.event) {
        this.copyParameterForm.patchValue({
          paymentDate: new Date(result.paymentDate), // วันที่ประมวลผล
          paymentName: result.paymentName, // การกำหนด
        });
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'Save',
      value: this.copyParameterForm.value,
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      status: 'Cancel',
      value: '',
    });
  }
}
