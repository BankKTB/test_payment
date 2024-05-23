import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { BatchJobService } from '@core/services/batch-job/batch-job.service';
import { SearchBatchRequest } from '@core/models/batch-job/search-batch-request';
import { SearchScheduleResponse } from '@core/models/batch-job/search-schedule-response';
import { DialogConfirmComponent } from '@shared/component/dialog-confirm/dialog-confirm.component';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSort } from '@angular/material/sort';
import { SidebarService } from '@core/services';
import { DialogSearchJobParameterComponent } from '@shared/component/dialog-search-job-parameter/dialog-search-job-parameter.component';

export interface selectOption {
  value: string;
  text: string;
  formControlName: string;
}

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements AfterViewInit {
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  searchBatchForm: FormGroup;
  jobStatuses: selectOption[] = [
    { value: 'scheduled', text: 'Scheduled', formControlName: 'jobStatusScheduled' },
    { value: 'chained', text: 'Chained', formControlName: 'jobStatusChained' },
    { value: 'running', text: 'Running', formControlName: 'jobStatusRunning' },
    { value: 'finished', text: 'Finished', formControlName: 'jobStatusFinished' },
    { value: 'cancelled', text: 'Cancelled', formControlName: 'jobStatusCancelled' },
    { value: 'inActive', text: 'Inactive', formControlName: 'jobStatusInactive' },
  ];
  displayedColumns: string[] = [
    'jobType',
    'jobName',
    'paymentDate',
    'paymentName',
    'proposal',
    'createdBy',
    // 'status',
    'state',
    'jobDate',
    'runTime',
    'startDate',
    'startTime',
    'afterEvent',
    'finishedDate',
    'finishedTime',
    'duration',
    'action',
  ];
  dataSource = new MatTableDataSource<SearchScheduleResponse>();
  listValidate = [];

  constructor(
    private batchJobService: BatchJobService,
    private dialog: MatDialog,
    private sidebarService: SidebarService,
    private formBuilder: FormBuilder
  ) {
    this.createFormGroup();
  }

  ngOnInit() {
    this.sidebarService.updatePageType('payment');
    this.sidebarService.updateNowPage('batch-job-search');
  }

  createFormGroup() {
    this.searchBatchForm = this.formBuilder.group({
      jobDate: this.formBuilder.control(new Date()),
      paymentName: this.formBuilder.control(''),
      paymentDate: this.formBuilder.control(''),
      jobStatusScheduled: this.formBuilder.control(true),
      jobStatusChained: this.formBuilder.control(true),
      jobStatusRunning: this.formBuilder.control(true),
      jobStatusFinished: this.formBuilder.control(true),
      jobStatusCancelled: this.formBuilder.control(true),
      jobStatusInactive: this.formBuilder.control(true),
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getJobStatus() {
    const state = [];
    Object.keys(this.searchBatchForm.controls).forEach((key) => {
      if (key.match(/jobStatus/)) {
        if (this.searchBatchForm.controls[key].value) {
          state.push(key.replace('jobStatus', '').toUpperCase());
        }
      }
    });
    return state;
  }

  loadBatchJob() {
    this.listValidate = [];
    this.dataSource = new MatTableDataSource<SearchScheduleResponse>();
    const { paymentName, paymentDate, jobDate } = this.searchBatchForm.getRawValue();
    const request: SearchBatchRequest = {
      state: this.getJobStatus(),
      status: '',
      paymentDate,
      paymentName,
      jobDate,
    };

    this.batchJobService.searchAllWithCondition(request).then((e) => {
      if (e.status === 200) {
        this.dataSource = new MatTableDataSource<SearchScheduleResponse>(e.data);
        if (this.dataSource.data) {
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        }
      } else if (e.status === 404) {
        this.listValidate.push('ไม่พบข้อมูล');
      }
    });
  }

  classJobStatus(jobStatus: string) {
    switch (jobStatus) {
      case 'SCHEDULED':
        return 'text-processing';
      case 'CHAINED':
        return 'text-processing';
      case 'WAITING':
        return 'text-processing';
      case 'RUNNING':
        return 'text-blue';
      case 'FINISHED':
        return 'text-green';
      case 'COMPLETED':
        return 'text-green';
      case 'CANCELLED':
        return 'text-red';
      case 'INACTIVE':
        return 'text-black';
      default:
        return '';
    }
  }

  clickStartJob(scheduleItem: any) {
    const dialogRef = this.openDialogConfirm('ต้องการ Run Job?');
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.batchJobService.startJob(scheduleItem.id).then((e) => {
          setTimeout(() => {
            this.batchJobService.startJob(scheduleItem.id).then((e) => {
              this.loadBatchJob();
            });
          }, 3000); // 3000 milliseconds = 3 seconds
        });
      }
    });
  }

  clickStopJob(scheduleItem: any) {
    const dialogRef = this.openDialogConfirm('ต้องการหยุดการทำงาน?');
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.batchJobService.stopJob(scheduleItem.id).then((e) => {
          this.loadBatchJob();
        });
      }
    });
  }

  clickDeleteJob(scheduleItem: any) {
    const dialogRef = this.openDialogConfirm('ต้องการลบ?');
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.batchJobService.deleteJob(scheduleItem.id).then((e) => {
          this.loadBatchJob();
        });
      }
    });
  }

  openDialogConfirm(textConfirm: string) {
    return this.dialog.open(DialogConfirmComponent, {
      data: {
        textConfirm,
      },
    });
  }

  viewLogPayment(item: SearchScheduleResponse) {
    const url = `./payment?paymentDate=${item.paymentAlias.paymentDate}&paymentName=${item.paymentAlias.paymentName}`;
    window.open(url, '_blank');
  }

  classClickAble(item): string {
    if (item && item.length > 0) {
      if (item.trim() !== '-') {
        return 'pointer';
      }
    }
    return '';
  }

  openDialogSearchParameterComponent(): void {
    const dialog = this.dialog.open(DialogSearchJobParameterComponent, {});
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.searchBatchForm.patchValue({
          paymentName: result.paymentName, // การกำหนด
          paymentDate: new Date(result.paymentDate),
        });
      }
    });
  }
}
