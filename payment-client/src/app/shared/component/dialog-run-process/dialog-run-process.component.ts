import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogSearchParameterComponent } from '../dialog-search-parameter/dialog-search-parameter.component';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';

@Component({
  selector: 'app-dialog-run-process',
  templateUrl: './dialog-run-process.component.html',
  styleUrls: ['./dialog-run-process.component.scss'],
})
export class DialogRunProcessComponent implements OnInit {
  runProposalForm: FormGroup;

  dateRunControl: FormControl; // วันประมวลผล
  timeRunControl: FormControl; // เวลาประมวล
  adjustNowControl: FormControl; // ทันที

  listValidate = [];

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
    // this.defaultCopyParameterForm();
  }

  createCopyParameterFormControl() {
    this.dateRunControl = this.formBuilder.control(new Date()); // วันที่ประมวลผล
    this.timeRunControl = this.formBuilder.control(new Date()); // เวลาประมวล
    this.adjustNowControl = this.formBuilder.control(''); // ทันที
  }

  createCopyParameterFormGroup() {
    this.runProposalForm = this.formBuilder.group({
      dateRun: this.dateRunControl, // วันที่ประมวลผล
      timeRun: this.timeRunControl, // เวลาประมวล
      adjustNow: this.adjustNowControl, // ทันที
    });
  }

  defaultCopyParameterForm() {
    this.runProposalForm.patchValue({
      dateRun: '', // วันที่ประมวลผล
      timeRun: new Date(), // เวลาประมวล
      adjustNow: false, // ทันที
    });
  }

  clickSave() {
    this.listValidate = [];
    const nowDate = new Date();

    const form = this.runProposalForm.value;

    const dateSelect = form.dateRun as Date;
    const timeSelect = form.timeRun as Date;
    const scheduleDate = new Date();
    scheduleDate.setDate(dateSelect.getDate());
    scheduleDate.setMonth(dateSelect.getMonth());
    scheduleDate.setFullYear(dateSelect.getFullYear());
    scheduleDate.setHours(timeSelect.getHours());
    scheduleDate.setMinutes(timeSelect.getMinutes());
    scheduleDate.setSeconds(timeSelect.getSeconds());

    if (form.adjustNow) {
      this.dialogRef.close({
        event: true,
        status: 'Save',
        value: this.runProposalForm.value,
      });
    } else {
      if (scheduleDate >= nowDate) {
        this.dialogRef.close({
          event: true,
          status: 'Save',
          value: this.runProposalForm.value,
        });
      } else {
        this.listValidate.push('* กรุณาเลือกเวลาที่มากกว่าปัจจุบัน');
      }
    }
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      status: 'Cancel',
      value: '',
    });
  }

  minFilter = (d: Date): boolean => {
    const from = new Date();
    from.setDate(from.getDate() - 1);
    if (d < from) {
      return false;
    }
    return true;
  };
}
