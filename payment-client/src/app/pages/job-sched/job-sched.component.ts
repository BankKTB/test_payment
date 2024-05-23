import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSort } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LoaderService, LocalStorageService, SidebarService } from '@core/services';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { UserProfile } from '@core/models/user-profile';
import { Router } from '@angular/router';

export class jobSchedModel {
  select: string;
  jobName: string;
  ln: string;
  jobCreatedBy: string;
  status: string;
  startDate: string;
  startTime: string;
  endTime: string;
  duration: string;
  delay: string;
  executingServer: string;
}

@Component({
  selector: 'app-job-sched',
  templateUrl: './job-sched.component.html',
  styleUrls: ['./job-sched.component.scss'],
})
export class JobSchedComponent implements OnInit {
  dataSource = new MatTableDataSource([]);

  isShowSearchData = true;
  displayedColumns: string[] = [
    'select',
    'jobName',
    'ln',
    'jobCreatedBy',
    'status',
    'startDate',
    'startTime',
    'endTime',
    'duration',
    'delay',
    'executingServer',
  ];

  dataMock: jobSchedModel[] = [
    {
      select: '',
      jobName: '',
      ln: '',
      jobCreatedBy: '',
      status: '',
      startDate: '',
      startTime: '',
      endTime: '',
      duration: '',
      delay: '',
      executingServer: '',
    },
  ];

  listCompanyCode = [{ companyCodeFrom: null, companyCodeTo: null, optionExclude: false }];
  listDocType = [{ docTypeFrom: null, docTypeTo: null, optionExclude: false }];
  listPaymentMethod = [{ paymentMethodFrom: null, paymentMethodTo: null, optionExclude: false }];

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
    private localStorageService: LocalStorageService,
    private router: Router
  ) {
    this.createFormGroup();
  }

  ngOnInit() {
    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('omCgd');
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());
    this.userProfile = this.localStorageService.getUserProfile();
    // this.dataSource.sort = this.sort;
    this.dataSource = new MatTableDataSource(this.dataMock);
  }

  createFormGroup() {
    this.jobFormCreate = this.formBuilder.group({
      jobName: this.formBuilder.control(''), // Job Name
      userName: this.formBuilder.control(''), // User name
      jobStatusSched: this.formBuilder.control(''), // Sched
      jobStatusReleased: this.formBuilder.control(''), // Released
      jobStatusReady: this.formBuilder.control(''), // Ready
      jobStatusActive: this.formBuilder.control(''), // Active
      jobStatusFinished: this.formBuilder.control(''), // Finsished
      jobStatusCanceled: this.formBuilder.control(''), // Canceled
      jobStartDateFrom: this.formBuilder.control(''), // Date From
      jobStartDateTo: this.formBuilder.control(''), // Date to
      jobStartTimeFrom: this.formBuilder.control(''), // Time start
      jobStartTimeTo: this.formBuilder.control(''), // Time end
      jobStartAfterEvent: this.formBuilder.control(''), // after event
      programNameAbap: this.formBuilder.control(''), // ABAP program
    });
  }

  // for clear input
  clearInput(inputForm) {
    this.jobFormCreate.controls[inputForm].setValue('');
  }

  add() {
    this.router.navigate(['jobSched/detail']);
  }
}
