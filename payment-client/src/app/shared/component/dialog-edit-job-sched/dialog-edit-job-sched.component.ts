import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { Utils } from '@shared/utils/utils';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';

export interface DialogData {
  role: '';
  createBy: '';
  value: '';
}

@Component({
  selector: 'dialog-edit-job-sched',
  templateUrl: './dialog-edit-job-sched.component.html',
  styleUrls: ['./dialog-edit-job-sched.component.scss'],
})
export class DialogEditJobSchedComponent implements OnInit {
  editJobSchedForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<DialogEditJobSchedComponent>,
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
    this.createFormGroup();
  }

  createFormGroup() {
    this.editJobSchedForm = this.formBuilder.group({
      schedStartDate: this.formBuilder.control(''),
      schedStartTime: this.formBuilder.control(''),
      schedNoStartAfterDate: this.formBuilder.control(''),
      schedNoStartAfterTime: this.formBuilder.control(''),
      PeriodicJob: this.formBuilder.control(''),
    });
  }

  // for clear input
  clearInput(inputForm) {
    this.editJobSchedForm.controls[inputForm].setValue('');
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }
}
