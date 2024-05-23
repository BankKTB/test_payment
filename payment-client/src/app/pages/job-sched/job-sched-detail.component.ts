import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSort } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LoaderService, LocalStorageService, SidebarService } from '@core/services';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { UserProfile } from '@core/models/user-profile';
import { DialogEditJobSchedComponent } from '@shared/component/dialog-edit-job-sched/dialog-edit-job-sched.component';
import { INPUT_DATE_FORMATS, InputDateAdapter } from '@shared/utils/input-date-adapter';

@Component({
  selector: 'app-job-sched-detail',
  templateUrl: './job-sched-detail.component.html',
  styleUrls: ['./job-sched-detail.component.scss'],
})
export class JobSchedDetailComponent implements OnInit {
  dataSource = new MatTableDataSource([]);

  isShowSearchData = true;

  panelExpandedJobStatus = true;
  panelExpandedJobStart = true;
  panelExpandedJobStep = true;

  isOpenCollapseDetail = true; // เปิดปิด collpase

  isSubmitedForm = false;
  listValidate = [];
  role = 'USER_CGD';

  isDisablePayMethod = false;

  userProfile: UserProfile = null;

  jobFormCreate: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    private constant: Constant,
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private loaderService: LoaderService,
    private localStorageService: LocalStorageService
  ) {
    this.createFormGroup();
  }

  ngOnInit() {
    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('omCgd');
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());
    this.userProfile = this.localStorageService.getUserProfile();
  }

  createFormGroup() {
    this.jobFormCreate = this.formBuilder.group({
      general: this.formBuilder.control(''), // Job Name
      jobName: this.formBuilder.control(''), // Job Name
      jobClass: this.formBuilder.control(''), // Job class
      jobStatus: this.formBuilder.control(''), // Status
      jobExecTarget: this.formBuilder.control(''), // Exec. Target
      jobStartDate: this.formBuilder.control(''), // Start date
      jobStartTime: this.formBuilder.control(''), // Start time
      jobNoStartAfterDate: this.formBuilder.control(''), // No Start After Date
      jobNoStartAfterTime: this.formBuilder.control(''), // No Start After Time
      jobStep: this.formBuilder.control(''), // Job steps
    });
  }

  // for clear input
  clearInput(inputForm) {
    this.jobFormCreate.controls[inputForm].setValue('');
  }

  edit() {
    const dialogRef = this.dialog.open(DialogEditJobSchedComponent, {
      width: '70vw',
      data: {},
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (result.event === 'display') {
          window.scrollTo(0, 0);
        } else if (result.event === 'savesucess') {
        }
      }
    });
  }
}
