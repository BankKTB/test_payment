import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { DialogRunProcessComponent } from '@shared/component/dialog-run-process/dialog-run-process.component';
import { MatDialog, MatSnackBar } from '@angular/material';
import { PaymentAliasService } from '@core/services/payment-alias/payment-alias.service';
import { PaymentService } from '@core/services/payment/payment.service';
import { Utils } from '@shared/utils/utils';
import { DialogShowProposalDocumentComponent } from '@shared/component/dialog-show-proposal-document/dialog-show-proposal-document.component';
import { SchedulerService } from '@core/services/scheduler/scheduler.service';
import { LocalStorageService } from '@core/services';
import { DialogReversePaymentAllComponent } from '@shared/component/dialog-reverse-payment-all/dialog-reverse-payment-all.component';
import {GenerateFileService} from '@core/services/generate-file/generate-file.service';

@Component({
  selector: 'app-tab-status',
  templateUrl: './tab-status.component.html',
  styleUrls: ['./tab-status.component.scss'],
})
export class TabStatusComponent implements OnInit {
  @Output() actionFromStatus = new EventEmitter<any>();
  // Open panal
  panelExpanded = true;

  parameterStatus = null;
  proposalStatus = null;
  proposalJobStatus = null;
  runStatus = null;
  runJobStatus = null;
  listSearchPaymentDetail: any = null; // data from search
  listShowMessage = [];

  proposalTotalDocument = 0;

  userProfile = null;
  WebInfo = null;

  hasAuthorizeReverse = false;

  constructor(
    private paymentAliasService: PaymentAliasService,
    private paymentService: PaymentService,
    private router: Router,
    private dialog: MatDialog,
    private utils: Utils,
    private schedulerService: SchedulerService,
    private snackBar: MatSnackBar,
    private localStorageService: LocalStorageService,
    private generateFileService: GenerateFileService
  ) {
    if (this.localStorageService.getUserMatrix()) {
      const matrix = this.localStorageService.getUserMatrix();
      this.hasAuthorizeReverse = matrix.privilege.screen.some((m) => {
        return (
          m.pageWeb === 'PM05' &&
          m.permission.find((p) => {
            return p.cancel;
          })
        );
      });
    }
  }

  ngOnInit() {}

  showStatus(have, object) {
    this.listSearchPaymentDetail = object;

    this.listShowMessage = [];

    if (have) {
      this.parameterStatus = object.parameterStatus;
      this.proposalStatus = object.proposalStatus;
      this.runStatus = object.runStatus;
      this.proposalJobStatus = object.proposalJobStatus;
      this.runJobStatus = object.runJobStatus;

      if (this.parameterStatus) {
        if (this.parameterStatus === 'S') {
          this.listShowMessage.push('บันทึกพารามิเตอร์แล้ว');
        } else {
          this.listShowMessage.push('ยังไม่มีบันทึกพารามิเตอร์');
        }
      }

      if (
        this.proposalJobStatus &&
        (this.proposalJobStatus === 'W' || this.proposalJobStatus === 'P')
      ) {
        const proposalScheduleDate = new Date(this.listSearchPaymentDetail.proposalScheduleDate);

        const dateStringShow = this.utils.format(proposalScheduleDate, 'input').toString();
        const timeString = `${proposalScheduleDate
          .getHours()
          .toString()
          .padStart(2, '0')}:${proposalScheduleDate
          .getMinutes()
          .toString()
          .padStart(2, '0')}:${proposalScheduleDate.getSeconds().toString().padStart(2, '0')}`;

        this.listShowMessage.push('ปล่อยข้อเสนอ วันที่ ' + dateStringShow + ' เวลา ' + timeString);
      }
      if (this.proposalJobStatus && this.proposalJobStatus === 'I') {
        const proposalScheduleDate = new Date(this.listSearchPaymentDetail.proposalScheduleDate);

        const dateStringShow = this.utils.format(proposalScheduleDate, 'input').toString();
        const timeString = `${proposalScheduleDate
          .getHours()
          .toString()
          .padStart(2, '0')}:${proposalScheduleDate
          .getMinutes()
          .toString()
          .padStart(2, '0')}:${proposalScheduleDate.getSeconds().toString().padStart(2, '0')}`;

        this.listShowMessage.push('ปล่อยข้อเสนอ วันที่ ' + dateStringShow + ' เวลา ' + timeString);
        this.listShowMessage.push('ปล่อยข้อเสนอทันทีโดยผู้ใช้งาน');
      }
      if (this.proposalStatus && this.proposalStatus === 'S') {
        this.listShowMessage.push('สร้างข้อเสนอสำเร็จ');
        this.listShowMessage.push(
          ' ใบสร้างข้อเสนอ ' +
            this.listSearchPaymentDetail.proposalTotalDocument +
            ' ถูกสร้าง ' +
            this.listSearchPaymentDetail.proposalSuccessDocument +
            ' สมบูรณ์แล้ว '
        );
        this.proposalTotalDocument = this.listSearchPaymentDetail.proposalTotalDocument;
      } else if (
        this.proposalStatus &&
        (this.proposalStatus === 'P' || this.proposalStatus === 'WP')
      ) {
        this.listShowMessage.push(' กำลังสร้างข้อเสนอ  ');
        this.proposalTotalDocument = this.listSearchPaymentDetail.proposalTotalDocument;
      }
      if (this.proposalJobStatus && this.proposalJobStatus === 'P') {
        this.listShowMessage.push('ปล่อยข้อเสนอถูกยกเลิกโดยผู้ใช้งาน');
      }
      if (
        this.proposalStatus &&
        this.proposalStatus !== 'S' &&
        this.proposalStatus !== 'P' &&
        this.proposalStatus !== 'W' &&
        this.proposalStatus !== 'WP'
      ) {
        this.listShowMessage.push('สร้างข้อเสนอไม่สำเร็จ');
      }

      // if (this.runJobStatus && this.runJobStatus === 'W') || this.runJobStatus === 'E') {
      // if (this.runJobStatus && this.runJobStatus !== 'A' && this.runJobStatus !== 'E') {
      if (this.runJobStatus && this.runJobStatus === 'W') {
        const runScheduleDate = new Date(this.listSearchPaymentDetail.runScheduleDate);

        const dateStringShow = this.utils.format(runScheduleDate, 'input').toString();
        const timeString = `${runScheduleDate
          .getHours()
          .toString()
          .padStart(2, '0')}:${runScheduleDate
          .getMinutes()
          .toString()
          .padStart(2, '0')}:${runScheduleDate.getSeconds().toString().padStart(2, '0')}`;

        this.listShowMessage.push(
          'ปล่อยการชำระเงิน วันที่' + dateStringShow + ' เวลา ' + timeString
        );
      }
      if (
        this.proposalStatus &&
        this.proposalStatus === 'S' &&
        this.runJobStatus &&
        this.runJobStatus === 'E'
      ) {
        this.listShowMessage.push('ยกเลิกการปล่อยชำระเงิน');
      }
      if (this.runJobStatus && this.runJobStatus === 'A') {
        this.listShowMessage.push('ปล่อยชำระเงินหลังจากข้อเสนอสำเร็จแล้ว');
      }
      if (this.runStatus && this.runStatus === 'S') {
        this.listShowMessage.push('ประมวลผลชำระเงินสำเร็จ');

        this.listShowMessage.push(
          ' ใบสั่งผ่านรายการ ' +
            this.listSearchPaymentDetail.runTotalDocument +
            ' ถูกสร้าง ' +
            this.listSearchPaymentDetail.runSuccessDocument +
            ' สมบูรณ์แล้ว ' +
            this.listSearchPaymentDetail.runRepairDocument +
            ' ต้องซ่อม ' +
            this.listSearchPaymentDetail.runErrorDocument +
            ' ไม่สมบูรณ์ '
        );
        this.listShowMessage.push(
          ' ประมวลผลตอบกลับการรันเอกสารจำนวน ' +
            this.listSearchPaymentDetail.idemCreatePaymentReply +
            ' รายการ  '
        );
      } else if (this.runStatus && this.runStatus === 'R') {
        this.listShowMessage.push(
          ' กำลังกลับรายการเอกสารจ่ายทั้งหมด ' +
            this.listSearchPaymentDetail.runTotalDocument +
            ' รายการ และ ถูกกลับรายการแล้ว ' +
            this.listSearchPaymentDetail.runReverseDocument
        );
        this.listShowMessage.push(
          ' ประมวลผลตอบกลับการกลับรายการเอกสารจ่าย ' +
            this.listSearchPaymentDetail.idemReversePaymentReply +
            ' รายการ  '
        );
      }
      if (this.runStatus && this.runStatus === 'P') {
        this.listShowMessage.push('กำลังประมวลผลชำระเงิน');
      }
      if (
        this.runStatus &&
        this.runStatus !== 'S' &&
        this.runStatus !== 'P' &&
        this.runStatus !== 'R'
      ) {
        this.listShowMessage.push('ประมวลผลชำระเงินไม่สำเร็จ');
      }
    } else {
      this.listShowMessage.push('ยังไม่มีบันทึกพารามิเตอร์');
      this.parameterStatus = null;
      this.proposalStatus = null;
      this.runStatus = null;
    }
  }

  createProposal() {
    const dialogRef = this.dialog.open(DialogRunProcessComponent, {});

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        // if (result.value.adjustNow) {
        //   this.paymentService
        //     .createProposal(this.listSearchPaymentDetail.id, this.localStorageService.getWebInfo())
        //     .then((data) => {
        //       if (data && data.error && data.error.status === 423) {
        //         this.snackBar.open(data.error.message, '', {
        //           panelClass: '_warning',
        //         });
        //         this.actionFromStatus.emit();
        //       } else {
        //         this.actionFromStatus.emit();
        //       }
        //     });
        // } else {
        const dateSelect = result.value.dateRun as Date;
        const timeSelect = result.value.timeRun as Date;
        const scheduleDate = new Date();
        scheduleDate.setDate(dateSelect.getDate());
        scheduleDate.setMonth(dateSelect.getMonth());
        scheduleDate.setFullYear(dateSelect.getFullYear());
        scheduleDate.setHours(timeSelect.getHours());
        scheduleDate.setMinutes(timeSelect.getMinutes());
        scheduleDate.setSeconds(timeSelect.getSeconds());

        const payload = {
          paymentAliasId: this.listSearchPaymentDetail.id,
          triggerAtInMillis: scheduleDate.getTime(),
          paymentType: 0,
        };

        const payloadUpdate = {
          id: this.listSearchPaymentDetail.id,
          paymentDate: this.listSearchPaymentDetail.paymentDate,
          paymentName: this.listSearchPaymentDetail.paymentName,
          proposalScheduleDate: scheduleDate,
          paymentAliasId: this.listSearchPaymentDetail.id,
        };

        if (result.value.adjustNow) {
          this.schedulerService.createJobRunNow(payload).then((data) => {
            this.actionFromStatus.emit();
          });
        } else {
          this.paymentAliasService
            .updateSchedule(payloadUpdate, this.listSearchPaymentDetail.id, 0)
            .then(() => {
              this.actionFromStatus.emit();
            })
            .then(() => {
              this.schedulerService.createJob(payload).then((data) => {
                this.actionFromStatus.emit();
              });
            });
        }
        // }
      }
    });
  }

  viewProposalDocument() {
    const dialogRef = this.dialog.open(DialogShowProposalDocumentComponent, {
      width: '1200px',
      data: {
        paymentAlias: this.listSearchPaymentDetail,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.actionFromStatus.emit();
      if (result) {
      }
    });
  }

  viewLogProposal() {
    const url = './log' + '?id=' + this.listSearchPaymentDetail.id + '&type=0';
    window.open(url, '_blank');
  }

  viewReportProposal() {
    const url = './display' + '?id=' + this.listSearchPaymentDetail.id + '&type=0';
    window.open(url, '_blank');
  }

  deleteProposal() {
    if (this.listSearchPaymentDetail) {
      if (this.listSearchPaymentDetail.id) {
        const data = {
          id: this.listSearchPaymentDetail.id,
          paymentDate: this.listSearchPaymentDetail.paymentDate,
          paymentName: this.listSearchPaymentDetail.paymentName,
          proposalStatus: 'DELETE',
        };

        const payload = {
          id: this.listSearchPaymentDetail.proposalTriggersId,
        };
        console.log(payload);
        this.paymentAliasService
          .deleteParameter(data, this.listSearchPaymentDetail.id)
          .then((result) => {
            this.actionFromStatus.emit();
          })
          .then(() => {
            this.schedulerService.deleteJob(payload).then((data) => {
              console.log(data);
              this.actionFromStatus.emit();
            });
          })
          .then(() => {
            console.log('before delete proposal log');
            this.schedulerService
              .deleteJobLog(this.listSearchPaymentDetail.proposalTriggersId)
              .then((data) => {
                console.log(data);
                this.actionFromStatus.emit();
              });
          });
      }
    }
  }

  createRunNew() {
    this.paymentService
      .real(this.listSearchPaymentDetail.id, this.localStorageService.getWebInfo())
      .then((data) => {
        if (data && data.error && data.error.status === 423) {
          this.snackBar.open(data.error.message, '', {
            panelClass: '_warning',
          });
          this.actionFromStatus.emit();
        } else {
          this.actionFromStatus.emit();
        }
      });
  }

  createRun() {
    const dialogRef = this.dialog.open(DialogRunProcessComponent, {});

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        // if (result.value.adjustNow) {
        //   this.paymentService
        //     .real(this.listSearchPaymentDetail.id, this.localStorageService.getWebInfo())
        //     .then((data) => {
        //       if (data && data.error && data.error.status === 423) {
        //         this.snackBar.open(data.error.message, '', {
        //           panelClass: '_warning',
        //         });
        //         this.actionFromStatus.emit();
        //       } else {
        //         this.actionFromStatus.emit();
        //       }
        //     });
        // } else {
        const dateSelect = result.value.dateRun as Date;
        const timeSelect = result.value.timeRun as Date;
        const scheduleDate = new Date();
        scheduleDate.setDate(dateSelect.getDate());
        scheduleDate.setMonth(dateSelect.getMonth());
        scheduleDate.setFullYear(dateSelect.getFullYear());
        scheduleDate.setHours(timeSelect.getHours());
        scheduleDate.setMinutes(timeSelect.getMinutes());
        scheduleDate.setSeconds(timeSelect.getSeconds());

        const payload = {
          paymentAliasId: this.listSearchPaymentDetail.id,
          triggerAtInMillis: scheduleDate.getTime(),
          paymentType: 1,
        };

        const payloadUpdate = {
          id: this.listSearchPaymentDetail.id,
          paymentDate: this.listSearchPaymentDetail.paymentDate,
          paymentName: this.listSearchPaymentDetail.paymentName,
          runScheduleDate: scheduleDate,
          paymentAliasId: this.listSearchPaymentDetail.id,
        };

        if (result.value.adjustNow) {
          this.schedulerService.createJobRunNow(payload).then((data) => {
            this.actionFromStatus.emit();
          });
        } else {
          this.paymentAliasService
            .updateSchedule(payloadUpdate, this.listSearchPaymentDetail.id, 1)
            .then(() => {
              this.actionFromStatus.emit();
            })
            .then(() => {
              this.schedulerService.createJob(payload).then((data) => {
                this.actionFromStatus.emit();
              });
            });
        }
        // }
      }
    });
  }

  refreshStatus() {
    this.actionFromStatus.emit();
  }

  reversePaymentAll() {
    if (this.listSearchPaymentDetail) {
      console.log(this.listSearchPaymentDetail);
      if (this.listSearchPaymentDetail.id) {
        const date = new Date(this.listSearchPaymentDetail.paymentDate);
        const dayGenerateFileDate = date.getDate();
        const monthGenerateFileDate = date.getMonth() + 1;
        const yearGenerateFileDate = date.getFullYear();
        const generateFileDate = this.utils.parseDate(
          dayGenerateFileDate,
          monthGenerateFileDate,
          yearGenerateFileDate
        );
        const generateFileName = this.listSearchPaymentDetail.paymentName;
        this.generateFileService.search(generateFileDate, generateFileName).then((data) => {
          console.log('data : ', data);
          if (data && data.data && data.data.generateFileRunStatus === 'S') {
            this.snackBar.open('ไม่สามารถกลับรายการจ่ายได้เนื่องจากมีการสร้างไฟล์ชำระเงินสำเร็จแล้ว', '', {
              panelClass: '_warning',
            });
          } else {
            if (this.listSearchPaymentDetail.paymentTriggersId) {
              this.schedulerService
                .deleteJobLog(this.listSearchPaymentDetail.paymentTriggersId)
                .then((data) => {
                  console.log(data);
                  this.actionFromStatus.emit();
                });
            }
            const dialogRef = this.dialog.open(DialogReversePaymentAllComponent, {
              disableClose: true,
              width: 'auto',
              data: {
                paymentAlias: this.listSearchPaymentDetail,
              },
            });
            dialogRef.afterClosed().subscribe((result) => {
              if (result && result.event === 'save') {
                this.actionFromStatus.emit();
              }
            });
          }
        });
      }
    }
  }

  deleteParameter() {
    if (this.listSearchPaymentDetail) {
      if (this.listSearchPaymentDetail.id) {
        this.paymentAliasService.delete(this.listSearchPaymentDetail.id).then((result) => {
          this.actionFromStatus.emit();
        });
      }
    }
  }

  viewLogRun() {
    const url = './log' + '?id=' + this.listSearchPaymentDetail.id + '&type=1';
    window.open(url, '_blank');
  }

  viewReportRun() {
    const url = './display' + '?id=' + this.listSearchPaymentDetail.id + '&type=1';
    window.open(url, '_blank');
  }

  createProposalNew() {
    this.paymentService
      .createProposal(this.listSearchPaymentDetail.id, this.localStorageService.getWebInfo())
      .then((data) => {
        if (data && data.error && data.error.status === 423) {
          this.snackBar.open(data.error.message, '', {
            panelClass: '_warning',
          });
          this.actionFromStatus.emit();
        } else {
          this.actionFromStatus.emit();
        }
      });
  }
}
