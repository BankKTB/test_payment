import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { GenerateFileService } from '@core/services/generate-file/generate-file.service';
import { CreateBatchJobRequest } from '@core/models/batch-job/create-batch-job-request';
import { SearchBatchRequest } from '@core/models/batch-job/search-batch-request';
import { BatchJobService } from '@core/services/batch-job/batch-job.service';
import { SidebarService } from '@core/services';
import { DialogBatchTransactionPaymentNameComponent } from '@shared/component/dialog-batch-transaction-payment-name/dialog-batch-transaction-payment-name.component';
import { ThaidatePipe } from '@shared/pipe/thaidate.pipe';
import { Utils } from '@shared/utils/utils';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent implements OnInit {
  readonly JOB_TYPE = new Map([
    ['payment', { name: 'payment', value: 'PAYMENT' }],
    ['RETURN', { name: 'return', value: 'RETURN' }],
  ]);
  readonly JOB_TYPE_PAYMENT = 'PAYMENT';
  readonly JOB_TYPE_RETURN = 'RETURN';
  createBatchForm: FormGroup;
  listValidate = [];
  isCanSubmit: boolean = false;
  pipe: ThaidatePipe;
  batchJobId: string = '';

  constructor(
    private dialog: MatDialog,
    private utils: Utils,
    private sidebarService: SidebarService,
    private formBuilder: FormBuilder,
    private batchJobService: BatchJobService,
    private generateFileService: GenerateFileService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {
    this.createFormGroup();
    this.route.queryParams.subscribe((params) => {
      if (Object.keys(params).length) {
        this.batchJobId = params.id;
      }
    });
  }

  ngOnInit() {
    this.sidebarService.updatePageType('payment');
    this.sidebarService.updateNowPage('batch-job-create');
  }

  createFormGroup() {
    this.createBatchForm = this.formBuilder.group({
      jobType: this.formBuilder.control(this.JOB_TYPE_PAYMENT), //
      paymentName: this.formBuilder.control(''),
      paymentDate: this.formBuilder.control(new Date()),
      paymentType: this.formBuilder.control({ value: '0', disabled: true }),
      scheduleType: this.formBuilder.control({ value: true, disabled: true }),
      jobDate: this.formBuilder.control({ value: new Date(), disabled: true }),
      jobTime: this.formBuilder.control({ value: new Date(), disabled: true }, []),
      isAfterEvent: this.formBuilder.control({ value: false, disabled: true }),
      afterEvent: this.formBuilder.control({ value: '', disabled: true }),
      paymentAliasId: this.formBuilder.control(''),
      proposalJobStatus: this.formBuilder.control(''),
      proposalStatus: this.formBuilder.control(''),
      runJobStatus: this.formBuilder.control(''),
      runStatus: this.formBuilder.control(''),
      paymentNameAfterEvent: this.formBuilder.control(''),

      // JOB TYPE RETURN
      jobReturnDate: this.formBuilder.control(new Date()),
      jobReturnTime: this.formBuilder.control(new Date()),
    });

    this.createBatchForm.controls.paymentAliasId.valueChanges.subscribe((value) => {
      this.createBatchForm.patchValue({
        paymentType: '0',
        scheduleType: true,
      });
      if (value) {
        this.createBatchForm.controls.jobDate.enable();
        this.createBatchForm.controls.jobTime.enable();
      } else {
        this.createBatchForm.controls.scheduleType.disable();
        this.createBatchForm.controls.jobDate.disable();
        this.createBatchForm.controls.jobTime.disable();

        if (this.createBatchForm.controls.paymentType.value === '0') {
          this.createBatchForm.patchValue({
            scheduleType: true,
          });
          this.createBatchForm.controls.scheduleType.disable();
        } else {
          this.createBatchForm.controls.scheduleType.enable();
        }
      }
    });

    this.createBatchForm.controls.paymentType.valueChanges.subscribe((value) => {
      this.isCanSubmit = true;
      const { proposalStatus, proposalJobStatus, runJobStatus } = this.createBatchForm.value;

      if (runJobStatus === 'W' || runJobStatus === 'S') {
        this.isCanSubmit = false;
        this.createBatchForm.controls.scheduleType.disable();
        this.createBatchForm.controls.jobDate.disable();
        this.createBatchForm.controls.jobTime.disable();
      } else {
        if (value === '1') {
          this.createBatchForm.controls.isAfterEvent.enable();
          if (proposalStatus !== 'S' && proposalStatus !== 'P' && proposalJobStatus === 'W') {
            this.createBatchForm.controls.scheduleType.enable();
          } else {
            this.createBatchForm.patchValue({
              scheduleType: true,
            });
            this.createBatchForm.controls.scheduleType.disable();
          }
        } else {
          this.createBatchForm.controls.isAfterEvent.disable();
          this.createBatchForm.patchValue({
            scheduleType: true,
          });
          if (proposalJobStatus === 'W') {
            this.isCanSubmit = false;
          }
        }
      }
    });
  }

  validateForm() {
    const jobDateTime: Date = this.createBatchForm.controls.jobTime.value;
    const currentTime: Date = new Date();
    const scheduleType = this.createBatchForm.controls.scheduleType.value;
    const paymentNameAfterEvent = this.createBatchForm.controls.paymentNameAfterEvent.value;
    if (scheduleType) {
      if (jobDateTime.getTime() < currentTime.getTime()) {
        this.listValidate.push('Job time ไม่สามารถน้อยกว่า ปัจจุบัน');
        return false;
      }
    } else {
      if (!paymentNameAfterEvent) {
        this.listValidate.push('ไม่พบข้อมูล payment after');
        return false;
      }
    }
    return true;
  }

  validateFormReturn() {
    const jobReturnTime: Date = this.createBatchForm.controls.jobReturnTime.value;
    const currentTime: Date = new Date();
    if (jobReturnTime.getTime() < currentTime.getTime()) {
      this.listValidate.push('Job time ไม่สามารถน้อยกว่า ปัจจุบัน');
      return false;
    }

    return true;
  }

  onSubmit() {
    this.listValidate = [];
    const {
      jobType: { value },
    } = this.createBatchForm.controls;
    if (value === this.JOB_TYPE_PAYMENT) {
      this.runJobPayment();
    } else {
      this.runJobReturn();
    }
  }

  runJobPayment() {
    this.validateForm();
    if (this.listValidate.length <= 0) {
      const { paymentAliasId, jobDate, jobTime } = this.createBatchForm.value;
      const jobScheduleDate = new Date(jobDate);
      jobScheduleDate.setHours(jobTime.getHours(), jobTime.getMinutes(), jobTime.getSeconds());
      const request: CreateBatchJobRequest = {
        triggerAtInMillis: jobScheduleDate.getTime(),
        paymentAliasId,
        paymentType: this.createBatchForm.controls.paymentType.value,
        parentId: this.createBatchForm.controls.afterEvent.value,
        jobType: this.JOB_TYPE_PAYMENT,
      };
      this.batchJobService.addJob(request).then((r) => {
        if (r === null) {
          this.successCreateJob();
          return;
        }
        if (r.status === 200) {
          this.successCreateJob();
        } else {
          this.snackBar.open('ดำนเนินการไม่สำเร็จ', '', {
            panelClass: '_warning',
          });
        }
      });
    }
  }

  runJobReturn() {
    this.validateFormReturn();
    console.log(this.listValidate.length);
    if (this.listValidate.length <= 0) {
      const { jobReturnDate, jobReturnTime } = this.createBatchForm.value;
      const jobScheduleDate = new Date(jobReturnDate);
      jobScheduleDate.setHours(
        jobReturnTime.getHours(),
        jobReturnTime.getMinutes(),
        jobReturnTime.getSeconds()
      );
      const request: CreateBatchJobRequest = {
        triggerAtInMillis: jobScheduleDate.getTime(),
        paymentType: this.createBatchForm.controls.paymentType.value,
        parentId: this.createBatchForm.controls.afterEvent.value,
        jobType: this.JOB_TYPE_RETURN,
      };
      this.batchJobService.addJob(request).then((r) => {
        if (r === null) {
          this.successCreateJob();
          return;
        }
        if (r.status === 200) {
          this.successCreateJob();
        } else {
          this.snackBar.open('ดำนเนินการไม่สำเร็จ', '', {
            panelClass: '_warning',
          });
        }
      });
    }
  }

  defaultFormControl() {
    this.createBatchForm.reset();
    this.createBatchForm.patchValue({
      paymentDate: new Date(),
      paymentType: '0',
      scheduleType: true,
      jobDate: new Date(),
      jobTime: new Date(),
      isAfterEvent: false,
      jobType: this.JOB_TYPE_PAYMENT,
    });
  }

  successCreateJob() {
    this.defaultFormControl();
    this.snackBar.open('สร้าง job สำเร็จ', '', {
      panelClass: '_success',
    });
  }

  onChangePaymentData() {
    const { paymentDate, paymentName } = this.createBatchForm.value;
    const date = new Date(paymentDate);
    const convertPaymentDate = this.utils.parseDate(
      date.getDate(),
      date.getMonth() + 1,
      date.getFullYear()
    );
    if (convertPaymentDate && paymentName) {
      // this.batchJobService.findCreateJobByCondition(this.checkStarAndPercent(textSearch));
      this.batchJobService
        .findCreateJobByDateAndName(convertPaymentDate, paymentName)
        .then((result) => {
          if (result.status === 200) {
            const { paymentAliasId } = result.data;
            this.createBatchForm.patchValue({
              paymentAliasId: paymentAliasId,
            });
            this.isCanSubmit = true;
          } else if (result.status === 404) {
            this.snackBar.open('ไม่พบการกำหนดนี้', '', {
              panelClass: '_warning',
            });
            this.createBatchForm.patchValue({
              paymentAliasId: '',
            });
            this.isCanSubmit = false;
          }
        });
    }
  }

  openDialogPaymentNameSelector(): void {
    const dialogRef = this.dialog.open(DialogBatchTransactionPaymentNameComponent, {
      data: {
        title: 'รายการการชำระเงินอัตโนมัติ : ภาพรวม',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.isCanSubmit = true;
        const {
          paymentDate,
          paymentName,
          id,
          proposalJobStatus,
          proposalStatus,
          runJobStatus,
          runStatus,
        } = result.value;
        this.createBatchForm.patchValue({
          paymentName: paymentName,
          paymentDate: new Date(paymentDate),
          paymentAliasId: id,
          proposalJobStatus,
          proposalStatus,
          runJobStatus,
          runStatus,
        });
        // proposalJobStatus ตั้ง job proposal  [W waiting S Success R running]
        // proposalStatus proposal ถุกรันเสดยัง [S success P processing]
        // runJobStatus ตั้ง job ดำนเนิการชำระเงิน  [W waiting S Success R running]
        // runStatus  ดำนเนิการชำระเงิน เสดอ่ะยัง [S success P processing]
        if (proposalJobStatus !== null || proposalStatus === 'S' || proposalStatus === 'P') {
          if (proposalStatus === 'S' || proposalStatus === 'P' || proposalJobStatus === 'W') {
            this.createBatchForm.patchValue({
              paymentType: '1',
            });
            this.createBatchForm.controls.paymentType.disable();
          } else {
            this.createBatchForm.controls.paymentType.enable();
          }
        } else {
          this.createBatchForm.patchValue({
            paymentType: '0',
          });
          this.createBatchForm.controls.paymentType.disable();
        }
      }
    });
  }

  changeIsAfterEvent() {
    const {
      scheduleType,
      paymentDate,
      paymentName,
      _proposalJobStatus,
      _proposalStatus,
    } = this.createBatchForm.value;

    if (!scheduleType) {
      const request: SearchBatchRequest = {
        jobDate: null,
        paymentDate: paymentDate,
        paymentName: paymentName,
        state: ['SCHEDULED'],
      };
      this.batchJobService.searchAllWithCondition(request).then((e) => {
        if (e.status === 200) {
          if (e.data.length > 0) {
            const {
              id,
              paymentAlias: { paymentName, paymentDate },
              paymentType,
            } = e.data[0];
            this.pipe = new ThaidatePipe();
            this.createBatchForm.patchValue({
              afterEvent: id,
              paymentNameAfterEvent: `${this.pipe.transform(
                new Date(paymentDate),
                ''
              )} - ${paymentName} - ${paymentType === 0 ? 'X' : ''}`,
            });
          } else {
            this.createBatchForm.patchValue({
              scheduleType: true,
            });
            this.snackBar.open('ไม่พบข้อมูล job ข้อเสนอของ การกำหนดนี้', '', {
              panelClass: '_warning',
            });
          }
        }
      });
    }
  }

  onChangeJobType() {
    if (this.createBatchForm.controls.jobType.value === this.JOB_TYPE_RETURN) {
      this.isCanSubmit = true;
    }
  }
}
