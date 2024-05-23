import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSort } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Observable } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { LoaderService, LocalStorageService, MasterService, SidebarService } from '@core/services';
import { DialogReversePaymentComponent } from '@shared/component/dialog-reverse-payment/dialog-reverse-payment.component';
import { UserProfile } from '@core/models/user-profile';
import { WebInfo } from '@core/models/web-info';
import { FiService } from '@core/services/fi/fi.service';
import { DialogUploadComponent } from '@shared/component/dialog-upload/dialog-upload.component';

@Component({
  selector: 'app-invoice-reverse',
  templateUrl: './invoice-reverse.component.html',
  styleUrls: ['./invoice-reverse.component.scss'],
})
export class InvoiceReverseComponent implements OnInit {
  @ViewChildren('reasonInput') reasonInput: QueryList<ElementRef>;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  reversePaymentDocForm: FormGroup;
  departmentCodeFromControl: FormControl; // รหัสหน่วยงาน
  documentNoControl: FormControl; // เลขที่ใบขอกลับรายการ
  fiscalYearControl: FormControl; // ปีงบประมาณ
  reasonReverseControl: FormControl; // เหตุผลการกลับรายการ
  dateAcctControl: FormControl; //  วันที่ผ่านรายการ
  fiscPeriodControl: FormControl; //  วันที่ผ่านรายการ
  departmentCodeFromBindingName = '';
  webInfo: WebInfo;
  userProfile: UserProfile = null;

  dataSource = new MatTableDataSource([]);
  selectionApprove = new SelectionModel<any>(true, []);
  selectionNotApprove = new SelectionModel<any>(true, []);

  isShowSearchData = true;

  filteredOptionsPaymentFrom: Observable<string[]>;
  filteredOptionsPaymentTo: Observable<string[]>;

  filteredOptionsSpecialFrom: Observable<string[]>;
  filteredOptionsSpecialTo: Observable<string[]>;

  filteredOptionsDocTypeFrom: Observable<string[]>;
  filteredOptionsDocTypeTo: Observable<string[]>;

  button = 'Submit';

  isOpenCollapseDetail = true; // เปิดปิด collpase

  isSubmitedForm = false;
  listValidate = [];
  listSearchValidate = [];
  listMessageResponse = [];
  role = 'USER_CGD';

  isDisablePayMethod = false;

  currentDate = new Date();

  tempDate = new Date();

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    public constant: Constant,
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private loaderService: LoaderService,
    private localStorageService: LocalStorageService,
    private masterService: MasterService,
    private fiService: FiService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('reverse');
    this.sidebarService.updateNowPage('reverse-invoice');
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());
    this.utils.fiscPeriod = this.utils.bcCalculateFiscPeriod(new Date()); // defaul
    this.userProfile = this.localStorageService.getUserProfile();
    this.webInfo = this.localStorageService.getWebInfo();

    this.createFormControl();
    this.createFormGroup();
    //
    // if (this.role === 'USER_PTO') {
    //   this.defaultOmFormForArea();
    // } else if (this.role === 'USER_CGD') {
    //   this.defaultOmFormForCGD();
    // } else if (this.role === 'USER_PTO_H') {
    //   this.defaultOmFormForAreaTreatMent();
    // }
    //
    // this.dataSource.sort = this.sort;
    // this.dataSource.paginator = this.paginator;
  }

  getDisplayedColumns() {
    // this.columnTable.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    // return this.columnTable.filter((cd) => cd.showColumn).map((cd) => cd.key);
  }

  createFormControl() {
    this.departmentCodeFromControl = this.formBuilder.control('', [Validators.required]); // รหัสหน่วยงาน
    this.documentNoControl = this.formBuilder.control('', [Validators.required]); // เลขที่ใบขอกลับรายการ
    this.fiscalYearControl = this.formBuilder.control(this.utils.fiscYear, [Validators.required]); // ปีงบประมาณ
    this.reasonReverseControl = this.formBuilder.control('', [Validators.required]); // เหตุผลการกลับรายการ
    this.dateAcctControl = this.formBuilder.control(new Date(), [Validators.required]); // วันที่ผ่านรายการ
    this.fiscPeriodControl = this.formBuilder.control(this.utils.fiscPeriod, [Validators.required]); // วันที่ผ่านรายการ

  }

  createFormGroup() {
    this.reversePaymentDocForm = this.formBuilder.group({
      departmentCodeFrom: this.departmentCodeFromControl, // รหัสหน่วยงาน
      documentNo: this.documentNoControl, // เลขที่ใบขอกลับรายการ
      fiscalYear: this.fiscalYearControl, // ปีงบประมาณ
      reasonReverse: this.reasonReverseControl, // เหตุผลการกลับรายการ
      dateAcct: this.dateAcctControl, // วันที่ผ่านรายการ
      fiscPeriod: this.fiscPeriodControl,
    });
  }

  openDetailFi() {
    const form = this.reversePaymentDocForm.getRawValue();
    const url =
      './detail-fi-kb?compCode=' +
      form.departmentCodeFrom +
      '&docNo=' +
      form.documentNo +
      '&docYear=' +
      (form.fiscalYear - 543);
    // window.open(url, 'name', 'width=1200,height=700');
    window.open(url, '_blank');
  }

  validateForm() {
    Object.keys(this.reversePaymentDocForm.controls).forEach((key) => {
      const controlErrors = this.reversePaymentDocForm.get(key).errors;
      if (controlErrors) {
        console.log(key);
        if (key === 'departmentCodeFrom') {
          this.listValidate.push('กรุณาระบุ รหัสหน่วยงาน');
        }
        if (key === 'documentNo') {
          this.listValidate.push('กรุณาระบุ เลขที่ใบขอกลับรายการ');
        }
        if (key === 'reasonReverse') {
          this.listValidate.push('กรุณาระบุ เหตุผลการกลับรายการ');
        }
        if (key === 'dateAcct') {
          this.listValidate.push('กรุณาระบุ วันผ่านรายการ');
        }
      }
    });
  }

  onPreSave() {
    const listItem = [];
    this.listValidate = [];
    this.validateForm();
    const form = this.reversePaymentDocForm.getRawValue();
    const date = new Date(form.dateAcct);
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    const reasonObj = this.constant.LIST_REASON_BACK_LIST.find((o) => o.id === form.reasonReverse);
    const objRequest = {
      accountDocNo: form.documentNo,
      compCode: form.departmentCodeFrom,
      fiscalYear: (form.fiscalYear - 543).toString(),
      accountDocNoDisplay: form.documentNo,
      compCodeDisplay: form.departmentCodeFrom,
      fiscalYearDisplay: (form.fiscalYear - 543).toString(),
      flag: 0,
      reason: '',
      reasonNo: form.reasonReverse,
      revDateAcct: this.utils.parseDate(day, month, year),
      webInfo: this.webInfo,
      period:Number( form.fiscPeriod)
    };
    this.isSubmitedForm = true;
    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      const dialogRef = this.dialog.open(DialogReversePaymentComponent, {
        disableClose: true,
        width: '50%',
        data: {
          objRequest,
          reverseType: 'INVOICE',
        },
      });
      dialogRef.afterClosed().subscribe((result) => {
        if (result && result.event === 'save') {
        }
      });
    }
  }

  clearInput(inputForm) {
    this.reversePaymentDocForm.controls[inputForm].setValue('');
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.reversePaymentDocForm.patchValue({ [result.type]: result.value });
        if (type === 'departmentCodeFrom') {
          this.departmentCodeFromBindingName = result.name;
        }
      }
    });
  }

  onBlurSearch(type) {
    const form = this.reversePaymentDocForm.value;
    if (type === 'departmentCodeFrom') {
      if (form.departmentCodeFrom) {
        this.masterService.findOneCompanyCodeWithParam(form.departmentCodeFrom).then((data) => {
          const response = data as any;
          if (response.status === 200) {
            if (response.data) {
              this.reversePaymentDocForm.patchValue({
                departmentCodeFrom: response.data.valueCode,
              });
              this.departmentCodeFromBindingName = response.data.name;
            }
          } else if (response.status === 413) {
            this.departmentCodeFromBindingName = '';
          } else if (response.status === 404) {
            this.departmentCodeFromBindingName = '';
          } else if (response.status === 500) {
            this.departmentCodeFromBindingName = '';
          }
        });
      }
    }
  }

  searchDetailDocument() {
    const form = this.reversePaymentDocForm.getRawValue();
    console.log(form);
    if (
      form.documentNo &&
      form.documentNo.length === 10 &&
      form.departmentCodeFrom &&
      form.departmentCodeFrom.length === 5 &&
      form.fiscalYear &&
      form.fiscalYear.toString().length === 4
    ) {
      const payload = {
        compCode: form.departmentCodeFrom,
        accountDocNo: form.documentNo,
        fiscalYear: this.utils.convertYearToAD(form.fiscalYear),
        webInfo: this.localStorageService.getWebInfo(),
      };
      console.log(payload);
      this.loaderService.loadingToggleStatus(true);
      this.fiService.searchDetail(payload).then((result) => {
        if (result.status === 200) {
          const header = result.data.header;
          this.tempDate = header.dateAcct;
          this.utils.fiscPeriod = this.utils.bcCalculateFiscPeriod( new Date(header.dateAcct));
          if (this.utils.fiscPeriod.length > 0) {
            this.reversePaymentDocForm.patchValue({
              fiscPeriod: this.utils.fiscPeriod[0],
              dateAcct: new Date(header.dateAcct),
            });
          } else {
            this.reversePaymentDocForm.patchValue({
              fiscPeriod: this.utils.fiscPeriod,
              dateAcct: new Date(header.dateAcct),
            });
          }
          if (form.reasonReverse === '06' || form.reasonReverse === '07') {
            this.reversePaymentDocForm.controls.dateAcct.enable();
          } else {
            this.reversePaymentDocForm.controls.dateAcct.disable();
          }
        }
        console.log(result);
      });
    }
  }

  onChangeReason(event) {
    if (event === '06' || event === '07') {
      this.reversePaymentDocForm.controls.dateAcct.enable();
    } else {

      this.utils.fiscPeriod = this.utils.bcCalculateFiscPeriod(this.tempDate ? new Date( this.tempDate) : new Date());

      if (this.utils.fiscPeriod.length > 0) {
        this.reversePaymentDocForm.patchValue({
          fiscPeriod: this.utils.fiscPeriod[0],
        });
      } else {
        this.reversePaymentDocForm.patchValue({
          fiscPeriod: this.utils.fiscPeriod,
        });
      }

      if (this.tempDate) {
        this.reversePaymentDocForm.controls.dateAcct.disable();
        this.reversePaymentDocForm.patchValue({
          dateAcct: new Date(this.tempDate ? this.tempDate : ''),
        });
      } else {
        this.reversePaymentDocForm.patchValue({
          dateAcct: new Date(),
        });
      }
    }
  }

  openDialogUploadFile() {
    const dialogRef = this.dialog.open(DialogUploadComponent, {
      disableClose: true,
      width: 'auto',
      data: {
        type: 'invoice',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event === 'save') {
      }
    });
  }
  onChangePostDate(event) {
    const form = this.reversePaymentDocForm.getRawValue();
    const date = event.value;
    this.utils.fiscYear = this.utils.CalculateFiscYear(date);
    this.utils.fiscPeriod = this.utils.bcCalculateFiscPeriod(date);
    if (this.utils.fiscPeriod.length > 0) {
      this.reversePaymentDocForm.patchValue({
        fiscPeriod: this.utils.fiscPeriod[0],
      });
    } else {
      this.reversePaymentDocForm.patchValue({
        fiscPeriod: this.utils.fiscPeriod,
      });
    }
  }
}
