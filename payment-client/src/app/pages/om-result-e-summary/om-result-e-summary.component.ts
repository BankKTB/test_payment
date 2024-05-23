import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LoaderService, LocalStorageService, SidebarService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { UserProfile } from '@core/models/user-profile';

@Component({
  selector: 'app-om-result-e-summary',
  templateUrl: './om-result-e-summary.component.html',
  styleUrls: ['./om-result-e-summary.component.scss'],
})
export class OmResultESummaryComponent implements OnInit {
  button = 'Submit';

  panelExpanded = true;
  panelExpanded1 = true;

  omForm: FormGroup;
  departmentCodeFromControl: FormControl; // รหัสหน่วยงาน
  departmentCodeToControl: FormControl; // รหัสหน่วยงาน

  accDocNoFromControl: FormControl;
  accDocNoToControl: FormControl;

  yearAccountFromControl: FormControl; // ปีบัญชี
  yearAccountToControl: FormControl; // ปีบัญชี

  unblockDateControl: FormControl;

  statusFromControl: FormControl;
  statusToControl: FormControl;

  provinceCodeFromControl: FormControl; // รหัสจังหวัด
  provinceCodeToControl: FormControl; // รหัสจังหวัด

  disbursementCodeFromControl: FormControl; // รหัสหน่วยเบิกจ่าย
  disbursementCodeToControl: FormControl; // รหัสหน่วยเบิกจ่าย

  isOpenCollapseDetail = true; // เปิดปิด collpase

  isSubmitedForm = false;
  listValidate = [];
  listSearchValidate = [];

  role = 'USER_CGD';

  isDisableStatus = true;

  currentDate = new Date();

  report = null;
  userProfile: UserProfile = null;
  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    private constant: Constant,
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private loaderService: LoaderService,
    private localStorageService: LocalStorageService
  ) {}

  ngOnInit() {
    this.currentDate.setHours(0);
    this.currentDate.setMinutes(0);
    this.currentDate.setSeconds(0);
    this.currentDate.setMilliseconds(0);

    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('om-result-e-summary');
    this.userProfile = this.localStorageService.getUserProfile();
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());

    this.createFormControl();
    this.createFormGroup();
  }

  createFormControl() {
    this.departmentCodeFromControl = this.formBuilder.control('', [Validators.required]); // รหัสหน่วยงาน
    this.departmentCodeToControl = this.formBuilder.control(''); // รหัสหน่วยงาน

    this.accDocNoFromControl = this.formBuilder.control('');
    this.accDocNoToControl = this.formBuilder.control('');

    this.yearAccountFromControl = this.formBuilder.control(this.utils.fiscYear, [
      Validators.required,
    ]); // ปีบัญชี
    this.yearAccountToControl = this.formBuilder.control(this.utils.fiscYear); // ปีบัญชี
    this.unblockDateControl = this.formBuilder.control('', [Validators.required]); // วันที่อนุมัติ
    this.statusFromControl = this.formBuilder.control('B'); // เปลี่ยนสถานะจาก
    this.statusToControl = this.formBuilder.control('E'); // สถานะใหม่

    this.provinceCodeFromControl = this.formBuilder.control(''); // รหัสจังหวัด
    this.provinceCodeToControl = this.formBuilder.control(''); // รหัสจังหวัด

    this.disbursementCodeFromControl = this.formBuilder.control(''); // รหัสหน่วยเบิกจ่าย
    this.disbursementCodeToControl = this.formBuilder.control(''); // รหัสหน่วยเบิกจ่าย
  }

  createFormGroup() {
    this.omForm = this.formBuilder.group({
      departmentCodeFrom: this.departmentCodeFromControl, // รหัสหน่วยงาน
      departmentCodeTo: this.departmentCodeToControl, // รหัสหน่วยงาน

      accDocNoFrom: this.accDocNoFromControl,
      accDocNoTo: this.accDocNoToControl,

      provinceCodeFrom: this.provinceCodeFromControl, // รหัสจังหวัด
      provinceCodeTo: this.provinceCodeToControl, // รหัสจังหวัด
      yearAccountFrom: this.yearAccountFromControl, // ปีบัญชี
      yearAccountTo: this.yearAccountToControl, // ปีบัญชี

      unblockDate: this.unblockDateControl, // วันที่อนุมัติ
      statusFrom: this.statusFromControl, // เปลี่ยนสถานะจาก
      statusTo: this.statusToControl, // สถานะใหม่
      disbursementCodeFrom: this.disbursementCodeFromControl, // รหัสหน่วยเบิกจ่าย
      disbursementCodeTo: this.disbursementCodeToControl, // รหัสหน่วยเบิกจ่าย
    });
  }

  onSearch() {
    const form = this.omForm.value;

    this.listSearchValidate = [];
    this.report = null;

    if (!form.departmentCodeFrom) {
      this.listSearchValidate.push('กรุณา กรอก รหัสหน่วยงาน');
    }
    if (!form.unblockDate) {
      this.listSearchValidate.push('กรุณา เลือก วันที่อนุมัติ');
    }

    this.isSubmitedForm = true;
    if (this.listSearchValidate.length <= 0) {
      this.isSubmitedForm = false;

      const payload = {
        compCodeFrom: '',
        compCodeTo: '',
        accDocNoFrom: '',
        accDocNoTo: '',
        fiscalYearFrom: '',
        fiscalYearTo: '',

        statusFrom: '',
        statusTo: '',
        unblockDate: '',

        fiAreaFrom: '',
        fiAreaTo: '',
        paymentCenterFrom: '',
        paymentCenterTo: '',
      };

      payload.compCodeFrom = form.departmentCodeFrom;
      payload.compCodeTo = form.departmentCodeTo;
      payload.accDocNoFrom = form.accDocNoFrom;
      payload.accDocNoTo = form.accDocNoTo;
      payload.fiscalYearFrom = this.utils.convertYearToAD(form.yearAccountFrom);
      payload.fiscalYearTo = this.utils.convertYearToAD(form.yearAccountTo);

      let unblockDate = '';
      if (form.unblockDate) {
        const day = form.unblockDate.getDate();
        const month = form.unblockDate.getMonth() + 1;
        const year = form.unblockDate.getFullYear();
        unblockDate = this.utils.parseDate(day, month, year);
      }

      payload.unblockDate = unblockDate;

      payload.statusFrom = form.statusFrom;
      payload.statusTo = form.statusTo;

      payload.fiAreaFrom = form.provinceCodeFrom;
      payload.fiAreaTo = form.provinceCodeTo;

      payload.paymentCenterFrom = form.disbursementCodeFrom;
      payload.paymentCenterTo = form.disbursementCodeTo;

      console.log(payload);

      this.search(payload);
    }
  }

  search(payload) {
    this.paymentBlockService.report(payload).then((result) => {
      this.report = result.data;

      console.log(result);
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.omForm.patchValue({ [result.type]: result.value });
        if (type === 'departmentCodeFrom') {
        } else if (type === 'departmentCodeTo') {
        } else if (type === 'provinceCodeFrom') {
        } else if (type === 'provinceCodeTo') {
        } else if (type === 'vendorTaxIdFrom') {
        } else if (type === 'vendorTaxIdTo') {
        } else if (type === 'disbursementCodeFrom') {
        } else if (type === 'disbursementCodeTo') {
        } else if (type === 'payMethodFrom') {
        } else if (type === 'payMethodTo') {
        } else if (type === 'specialTypeFrom') {
        } else if (type === 'specialTypeTo') {
        }
      }
    });
  }

  // for clear input
  clearInput(inputForm) {
    this.omForm.controls[inputForm].setValue('');
  }

  viewDocument(item) {
    const url =
      './detail-fi-kb?compCode=' +
      item.companyCode +
      '&docNo=' +
      item.accDocNo +
      '&docYear=' +
      item.fiscalYear;
    window.open(url, 'name', 'width=1200,height=700');
  }
}
